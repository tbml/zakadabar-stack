/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import kotlinx.browser.document
import org.w3c.dom.HTMLTableSectionElement
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.table.columns.*
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch
import kotlin.reflect.KProperty1

open class ZkTable<T> : ZkElement() {

    val columns = mutableListOf<ZkColumn<T>>()

    val preloads = mutableListOf<ZkTablePreload<*>>()

    private val tbody = document.createElement("tbody") as HTMLTableSectionElement

    override fun init() = build {
        + table(ZkTableStyles.table) {
            buildContext.style.cssText = gridTemplateColumns()
            + thead {
                columns.forEach { it.renderHeader(this) }
            }
            + tbody
        }
    }

    fun <RT : Any> preload(loader: suspend () -> RT) = ZkTablePreload(loader)

    fun setData(data: List<T>): ZkTable<T> {
        launch {
            preloads.forEach {
                it.job.join()
            }

            build {
                this.buildContext = tbody
                for ((index, row) in data.withIndex()) {
                    + tr {
                        for (column in columns) {
                            + td {
                                column.render(this, index, row)
                            }
                        }
                    }
                }
            }
        }

        return this
    }

    private fun gridTemplateColumns(): String {
        var s = "grid-template-columns:"
        for (column in columns) {
            s += " " + column.gridTemplate()
        }
        return "$s;"
    }

    operator fun KProperty1<T, RecordId<T>>.unaryPlus(): ZkRecordIdColumn<T> {
        val column = ZkRecordIdColumn(this@ZkTable, this)
        columns += column
        return column
    }

    fun KProperty1<T, RecordId<T>>.actions(crud: ZkCrud<T>): ZkActionsColumn<T> {
        return ZkActionsColumn(this@ZkTable, this, crud)
    }

    operator fun ZkActionsColumn<T>.unaryPlus(): ZkActionsColumn<T> {
        columns += this
        return this
    }

    operator fun KProperty1<T, String>.unaryPlus(): ZkStringColumn<T> {
        val column = ZkStringColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, Double>.unaryPlus(): ZkDoubleColumn<T> {
        val column = ZkDoubleColumn(this@ZkTable, this)
        columns += column
        return column
    }

    fun custom(builder: ZkCustomColumn<T>.() -> Unit = {}): ZkCustomColumn<T> {
        val column = ZkCustomColumn(this)
        column.builder()
        return column
    }

    operator fun ZkCustomColumn<T>.unaryPlus(): ZkCustomColumn<T> {
        columns += this
        return this
    }

}