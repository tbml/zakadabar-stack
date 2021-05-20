/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.crud

import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.newInstance
import kotlin.reflect.KClass

/**
 * Provides common functions used in most CRUD implementations.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
open class ZkInlineCrud<T : RecordDto<T>> : ZkElement(), ZkCrud<T> {

    lateinit var companion: RecordDtoCompanion<T>
    lateinit var dtoClass: KClass<T>
    lateinit var editorClass: KClass<out ZkCrudEditor<T>>
    lateinit var tableClass: KClass<out ZkTable<T>>

    var addLocalTitle = true

    lateinit var editorInstance: ZkCrudEditor<T>
    lateinit var tableInstance: ZkTable<T>

    override fun openAll() {

        clear()

        io {
            tableInstance = tableClass.newInstance()

            tableInstance.crud = this
            tableInstance.addLocalTitle = addLocalTitle
            tableInstance.setData(companion.comm.all())

            + tableInstance
        }
    }

    private fun newEditor(): ZkCrudEditor<T> {
        clear()

        style {
            height = "100%"
            overflowY = "auto"
        }

        editorInstance = editorClass.newInstance()

        with(editorInstance) {
            addLocalTitle = this@ZkInlineCrud.addLocalTitle
            openUpdate = { openUpdate(it.id) }
            onBack = { openAll() }
        }

        return editorInstance
    }

    override fun openCreate() {
        with(newEditor()) {
            dto = dtoClass.newInstance().apply { schema().setDefaults() }
            mode = ZkElementMode.Create
        }

        + (editorInstance as ZkElement)
    }

    override fun openRead(recordId: RecordId<T>) {
        io {
            with(newEditor()) {
                dto = companion.read(recordId)
                mode = ZkElementMode.Read
            }

            + (editorInstance as ZkElement)
        }
    }

    override fun openUpdate(recordId: RecordId<T>) {
        io {
            with(newEditor()) {
                dto = companion.read(recordId)
                mode = ZkElementMode.Update
            }

            + (editorInstance as ZkElement)
        }
    }

    override fun openDelete(recordId: RecordId<T>) {
        io {
            with(newEditor()) {
                dto = companion.read(recordId)
                mode = ZkElementMode.Delete
            }

            + (editorInstance as ZkElement)
        }
    }
}
