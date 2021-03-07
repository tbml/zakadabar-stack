/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.frontend.resources

import zakadabar.stack.frontend.builtin.button.ZkButtonTheme
import zakadabar.stack.frontend.builtin.dock.ZkDockTheme
import zakadabar.stack.frontend.builtin.form.ZkFormTheme
import zakadabar.stack.frontend.builtin.icon.ZkIconTheme
import zakadabar.stack.frontend.builtin.layout.ZkLayoutTheme
import zakadabar.stack.frontend.builtin.misc.ZkFontTheme
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBarTheme
import zakadabar.stack.frontend.builtin.table.ZkTableTheme
import zakadabar.stack.frontend.builtin.toast.ZkToastTheme

interface ZkTheme {

    var button: ZkButtonTheme

    var dock: ZkDockTheme

    var font: ZkFontTheme

    var form: ZkFormTheme

    var icon: ZkIconTheme

    var layout: ZkLayoutTheme

    var sidebar: ZkSideBarTheme

    var table: ZkTableTheme

    var toast: ZkToastTheme

}