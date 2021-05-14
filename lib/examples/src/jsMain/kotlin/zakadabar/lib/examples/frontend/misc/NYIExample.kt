/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.misc

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.misc.NYI
import zakadabar.stack.frontend.builtin.note.secondaryNote

class NYIExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + secondaryNote("NYI Example", zke {
            + column {
                + NYI("this will be the header")
                + row {
                    + NYI("first item")
                    + NYI("second item")
                    + NYI("third item")
                }
                + NYI("this will be the footer")
            }
        })

    }

}