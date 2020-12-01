/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.builtin.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.builtin.desktop.navigator.EntityNavigator
import zakadabar.stack.frontend.builtin.util.Slider
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.util.PublicApi

/**
 * A desktop center component that show a navigator and views/pages.
 */
@PublicApi
open class DesktopCenter(
    private val navigationInstance: ZkElement? = EntityNavigator()
) : ZkElement() {

    private var mainInstance: ZkElement? = null

    override fun init(): ZkElement {
        super.init()

        className = desktopClasses.center

        val slider = if (navigationInstance == null) {
            null
        } else {
            Slider(this, navigationInstance, minRemaining = 200.0).withClass(coreClasses.verticalSlider)
        }

        mainInstance = ZkElement()

        this += navigationInstance
        this += slider
        this += mainInstance

        return this
    }

    open fun onNavigation() {

//        val state = Navigation.state
//
//        val eventRevision = revision.incrementAndGet()
//
//        launch {
//            val viewState = state.viewState ?: return@launch
//
//            // entity load took too long, the user clicked to somewhere else
//            if (eventRevision != revision.value) return@launch
//
//            this -= mainInstance
//
//            mainInstance = getMainInstance(viewState)
//
//            this += mainInstance
//        }

    }

//    open fun getMainInstance(viewState: NavState.ViewState): ComplexElement? {
//        if (viewState.localId == null) return null
//
//        @Suppress("UNCHECKED_CAST")
//        val dtoFrontend = dtoFrontends[viewState.dataType] ?: return null
//
//        return when (viewState.viewName) {
//            Navigation.CREATE -> dtoFrontend.createView()
//            Navigation.READ -> dtoFrontend.readView()
//            Navigation.UPDATE -> dtoFrontend.updateView()
//            Navigation.DELETE -> dtoFrontend.deleteView()
//            Navigation.ALL -> dtoFrontend.allView()
//            else -> null
//        }
//    }

    fun switchMain(newMain: ZkElement) {
        this -= mainInstance
        mainInstance = newMain
        this += mainInstance
    }
}