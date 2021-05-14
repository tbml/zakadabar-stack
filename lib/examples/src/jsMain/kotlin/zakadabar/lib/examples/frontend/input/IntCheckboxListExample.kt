/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.input

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.secondaryButton
import zakadabar.stack.frontend.builtin.input.ZkCheckboxList
import zakadabar.stack.frontend.builtin.input.ZkCheckboxListItem
import zakadabar.stack.frontend.builtin.note.ZkNote
import zakadabar.stack.frontend.builtin.note.secondaryNote
import zakadabar.stack.frontend.builtin.note.successNote
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.util.marginRight

/**
 * This example shows how to create checkbox lists.
 */
class IntCheckboxListExample(
    element: HTMLElement
) : ZkElement(element) {

    private var intItems = listOf(
        ZkCheckboxListItem(1, "First Integer", true),
        ZkCheckboxListItem(2, "Second Integer", true),
        ZkCheckboxListItem(5, "Fifth Integer", false)
    )

    private val intCheckboxList = ZkCheckboxList(intItems)

    override fun onCreate() {
        super.onCreate()

        + column(zkPageStyles.content) {
            + successNote("Integers", intCheckboxList) marginBottom 20
            + secondaryButton(stringStore.execute, onClick = ::onExecute) marginBottom 20
            + secondaryNote("Output", "")
        }
    }

    private fun onExecute() {
        find<ZkNote>().first { it.flavour == ZkFlavour.Secondary }.content = zke {
            intCheckboxList.items.forEach {
                + div { + "${it.value} = ${it.selected}" } marginRight 20
            }
        }
    }

}