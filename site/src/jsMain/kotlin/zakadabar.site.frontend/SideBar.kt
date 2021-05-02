/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import kotlinx.browser.window
import zakadabar.demo.lib.frontend.crud.BuiltinCrud
import zakadabar.demo.lib.frontend.form.FormFieldsGenerated
import zakadabar.demo.lib.frontend.form.SyntheticForm
import zakadabar.demo.lib.frontend.input.CheckboxList
import zakadabar.demo.lib.frontend.layout.TabContainer
import zakadabar.demo.lib.frontend.modal.Modals
import zakadabar.demo.lib.frontend.pages.ArgPage
import zakadabar.demo.lib.frontend.query.QueryPage
import zakadabar.demo.lib.frontend.table.FetchedTable
import zakadabar.demo.lib.frontend.table.GeneratedTable
import zakadabar.demo.lib.frontend.toast.Toasts
import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.site.frontend.pages.misc.Content
import zakadabar.site.frontend.pages.misc.GetStarted
import zakadabar.site.frontend.pages.misc.Overview
import zakadabar.site.resources.Strings
import zakadabar.stack.StackRoles
import zakadabar.stack.data.builtin.account.LogoutAction
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.pages.account.accounts.Accounts
import zakadabar.stack.frontend.builtin.pages.account.login.Login
import zakadabar.stack.frontend.builtin.pages.resources.locales.Locales
import zakadabar.stack.frontend.builtin.pages.resources.settings.Settings
import zakadabar.stack.frontend.builtin.pages.resources.translations.Translations
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.fourRandomInt

object SideBar : ZkSideBar() {

    private lateinit var contents: List<ContentEntry>

    override fun onCreate() {
        super.onCreate()

        io {
            contents = ContentQuery().execute().sortedBy { it.name }

            + item(Strings.overview) { Overview.open() }

            + item(Strings.getStarted) { GetStarted.open() }

            contentGroup(Strings.documentation, "guides/")

            + group(Strings.examples) {
                + item("ArgPage") { ArgPage.open(ArgPage.Args(fourRandomInt()[0], "hello")) }
                + item("CheckboxList") { CheckboxList.open() }
                + item("Modals") { Modals.open() }
                + item("Crud") { BuiltinCrud.openAll() }
                + group("Form") {
                    + item("Fields") { FormFieldsGenerated.open() }
                    + item("Synthetic") { SyntheticForm.open() }
                }
                + item("Query") { QueryPage.open() }
                + item("TabContainer") { TabContainer.open() }
                + group("Table") {
                    + item("Generated") { GeneratedTable.open() }
                    + item("Fetched") { FetchedTable.open() }
                }
                + item("Toasts") { Toasts.open() }
                + item("Settings") { Settings.openAll() }
            }

            contentGroup("ChangeLog", "changelog/", true)

            contentGroup("Contribute", "contribute/")

            withOneOfRoles(StackRoles.securityOfficer, StackRoles.siteAdmin) {

                + group(Strings.administration) {

                    + item(Strings.settings) { Settings.openAll() }

                    withRole(StackRoles.siteAdmin) {
                        + item(Strings.locales) { Locales.openAll() }
                        + item(Strings.translations) { Translations.openAll() }
                    }

                    withRole(StackRoles.securityOfficer) {
                        + item(Strings.accounts) { Accounts.openAll() }
                        + item(Strings.roles) { Roles.openAll() }
                    }

                }
            }

            ifAnonymous {
                + item(Strings.login) { Login.open() }
            }

            ifNotAnonymous {
                + item(Strings.account) { Accounts.openUpdate(ZkApplication.executor.account.id) }
                + item(Strings.logout) {
                    io {
                        LogoutAction().execute()
                        window.location.href = "/"
                    }
                }
            }
        }

    }

    private fun contentGroup(title: String, path: String, sortByDate: Boolean = false) {
        var entries = contents.filter { it.path.startsWith(path) }

        if (sortByDate) entries = entries.sortedBy { it.lastModified }

        + group(title) {
            entries.forEach {
                + item(it.name) { Content.open(it) }
            }
        }
    }

}



