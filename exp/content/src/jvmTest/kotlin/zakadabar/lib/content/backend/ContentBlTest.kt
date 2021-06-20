/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.lib.content.data.*
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.data.LocaleStatus
import zakadabar.stack.backend.server
import zakadabar.stack.backend.util.default
import kotlin.test.assertEquals

class ContentBlTest {

    companion object : TestCompanion() {

        lateinit var localeHu: LocaleBo
        lateinit var localeEn: LocaleBo
        lateinit var statusPublic: StatusBo

        @BeforeClass
        @JvmStatic
        fun setup() {
            super.setup("so", "so")

            runBlocking {
                localeHu = default<LocaleBo> {
                    name = "hu"
                    status = LocaleStatus.Public
                }.create()

                localeEn = default<LocaleBo> {
                    name = "en"
                    status = LocaleStatus.Public
                }.create()

                statusPublic = default<StatusBo> {
                    name = "public"
                }.create()
            }
        }

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testTitleConflict() = runBlocking {
        make("conflict")
        try {
            make("conflict")
        } catch (cre: ClientRequestException) {
            assertEquals(HttpStatusCode.Conflict, cre.response.status)
        }
        Unit
    }

    @Test
    fun testNavQuery() = runBlocking {
        transaction {
            ContentExposedTable.deleteAll()
        }

        val (master1, localizedHu1, _) = make("nav 1")
        val (master1c1, localizedHu1c1, _) = make("nav 1.1", master1)
        val (master1c1c1, localizedHu1c1c1, _) = make("nav 1.1.1", master1c1)
        val (master1c2, localizedHu1c2, _) = make("nav 1.2", master1)
        val (_, localizedHu1c2c1, _) = make("nav 1.2.1", master1c2)
        val (_, _, _) = make("nav 1.1.1.1", master1c1c1)
        val (_, _, _) = make("nav 1.1.1.2", master1c1c1)
        val (_, _, _) = make("nav 1.1.1.3", master1c1c1)

        val result = NavQuery(localeHu.name, localizedHu1.id).execute()

        assertEquals(2, result.size)
        assertNavEntry(result[0], localeHu, localizedHu1c1, 1, localizedHu1, localizedHu1c1)
        assertNavEntry(result[0].children[0], localeHu, localizedHu1c1c1, 3, localizedHu1, localizedHu1c1, localizedHu1c1c1)
        assertNavEntry(result[1], localeHu, localizedHu1c2, 1, localizedHu1, localizedHu1c2)
        assertNavEntry(result[1].children[0], localeHu, localizedHu1c2c1, 0, localizedHu1, localizedHu1c2, localizedHu1c2c1)

        val result2 = NavQuery(localeHu.name, null).execute()
        assertEquals(1, result2.size)
        assertNavEntry(result2[0], localeHu, localizedHu1, 2, localizedHu1)
    }

    private fun assertNavEntry(entry: NavEntry, locale: LocaleBo, bo: ContentBo, childrenCount: Int, vararg segments: ContentBo) {
        assertEquals(bo.id, entry.localizedId)
        assertEquals(childrenCount, entry.children.size)
        assertEquals(bo.title, entry.title)
        assertEquals("/${locale.name}/${segments.joinToString("/") { it.seoTitle }}", entry.seoPath)
    }

    @Test
    fun testByLocalizedPath() = runBlocking {

        // content 1 -----------------------------------------------------------

        val (master1, localizedHu1, localizedEn1) = make("1")

        assertResolve(localizedHu1, localeHu, localizedHu1)
        assertResolve(localizedEn1, localeEn, localizedEn1)

        // content 2 -----------------------------------------------------------

        val (master2, localizedHu2, localizedEn2) = make("2")

        assertResolve(localizedHu2, localeHu, localizedHu2)
        assertResolve(localizedEn2, localeEn, localizedEn2)

        // content 1.1 ---------------------------------------------------------

        val (_, localizedHu1c1, localizedEn1c1) = make("1.1", master1)

        assertResolve(localizedHu1c1, localeHu, localizedHu1, localizedHu1c1)
        assertResolve(localizedEn1c1, localeEn, localizedEn1, localizedEn1c1)

        // content 1.2 ---------------------------------------------------------

        val (_, localizedHu1c2, localizedEn1c2) = make("1.2", master1)

        assertResolve(localizedHu1c2, localeHu, localizedHu1, localizedHu1c2)
        assertResolve(localizedEn1c2, localeEn, localizedEn1, localizedEn1c2)

        // content 2.1 ---------------------------------------------------------

        val (master2c1, localizedHu2c1, localizedEn2c1) = make("2.1", master2)

        assertResolve(localizedHu2c1, localeHu, localizedHu2, localizedHu2c1)
        assertResolve(localizedEn2c1, localeEn, localizedEn2, localizedEn2c1)

        // content 2.1.1 -------------------------------------------------------

        val (_, localizedHu2c1c1, localizedEn2c1c1) = make("2.1.1", master2c1)

        assertResolve(localizedHu2c1c1, localeHu, localizedHu2, localizedHu2c1, localizedHu2c1c1)
        assertResolve(localizedEn2c1c1, localeEn, localizedEn2, localizedEn2c1, localizedEn2c1c1)

    }

    private suspend fun assertResolve(expected: ContentBo, locale: LocaleBo, vararg segments: ContentBo) {
        val resolved = BySeoPath("/${locale.name}/${segments.joinToString("/") { it.seoTitle }}").execute()
        assertEquals(expected.id, resolved.id)
    }

    private suspend fun make(postfix: String, pParent: ContentBo? = null): Triple<ContentBo, ContentBo, ContentBo> {

        if (pParent != null && ! pParent.folder) {
            pParent.folder = true
            pParent.update()
        }

        val masterContent = default<ContentBo> {
            modifiedAt = Clock.System.now()
            modifiedBy = session.account.id
            parent = pParent?.id
            status = statusPublic.id
            title = "title master $postfix"
        }.create()

        val localizedHu = default<ContentBo> {
            modifiedAt = Clock.System.now()
            modifiedBy = session.account.id
            status = statusPublic.id
            master = masterContent.id
            locale = localeHu.id
            title = "Cím Hu $postfix"
        }.create()

        val localizedEn = default<ContentBo> {
            modifiedAt = Clock.System.now()
            modifiedBy = session.account.id
            status = statusPublic.id
            master = masterContent.id
            locale = localeEn.id
            title = "Title En $postfix"
        }.create()

        return Triple(masterContent, localizedHu, localizedEn)
    }

    @Test
    fun testConvenience() = runBlocking {
        transaction {
            ContentExposedTable.deleteAll()
        }

        val (master1, localizedHu1, localizedEn1) = make("master 1")

        val contentBl = server.first<ContentBl>()

        transaction {
            assertEquals(localizedHu1.id, contentBl.localized(localeHu.id, master1.id).id)
            assertEquals(localizedEn1.id, contentBl.localized(localeEn.id, master1.id).id)

            assertEquals(
                master1.id to localizedHu1.id,
                contentBl.masterAndLocalized(localeHu.id, master1.id).let { it.first.id to it.second.id }
            )

            assertEquals(
                master1.id to localizedHu1.id,
                contentBl.masterAndLocalized(localeHu.id, localizedHu1.id).let { it.first.id to it.second.id }
            )

        }
    }

}