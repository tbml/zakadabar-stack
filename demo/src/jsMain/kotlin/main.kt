/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch

/**
 * The main method of the web browser application. Stared by the bundled Javascript file
 * (the-place-that-cant-be-found.js) in this case.
 */
fun main() {

    // Launch starts something in the background. Think setTimeout(0, ...) in JavaScript.
    // This launch method starts the task in GlobalScope, and in case an exception is thrown
    // it tries to format it to be a bit more readable.

    launch {

        // add KClass names as data attributes to DOM elements, useful for debugging, not meant for production
        // See: https://github.com/spxbhuhb/zakadabar-stack/blob/master/doc/misc/Productivity.md#simpleelement-addkclass

        ZkElement.addKClass = true

        // Initialize the frontend. This method needs a running backend because it
        // fetches the account of the user who runs the frontend.
        // See: https://github.com/spxbhuhb/zakadabar-stack/blob/master/doc/cookbook/common/Accounts.md

        with(Application) {
            routing = Routing
            init()
        }

        FrontendContext.init()

        // Add an instance of Home to the document body

        //document.body?.appendChild(Home.init().element)

    }

}