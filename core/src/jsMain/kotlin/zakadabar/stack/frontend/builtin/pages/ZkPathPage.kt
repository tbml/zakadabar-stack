/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.frontend.application.ZkAppLayout
import zakadabar.stack.frontend.application.application

/**
 * A page that provides convenience methods for serving hierarchic URLs.
 *
 * Use case: serving markdown files from a directory.
 *
 * General frontend URL format is `/$viewName/<path>` where `path` may have more
 * segments, like: `/ContentPage/guides/backend/CustomBackend.md`.
 *
 * @property  path  the part after the view name, contains [name], does not contain leading '/'
 * @property  name  last part of the path, typically the file name
 */
open class ZkPathPage(
    layout: ZkAppLayout? = null,
    cssClass: String? = null
) : ZkPage(layout, cssClass) {

    open val name
        get() = application.routing.navState.segments.last()

    open val path
        get() = application.routing.navState.segments.drop(2).joinToString("/")

}