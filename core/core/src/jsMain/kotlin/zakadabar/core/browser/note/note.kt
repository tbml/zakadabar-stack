/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.core.browser.note

import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.ZkFlavour

fun notePrimary(title: String, text: String) = ZkNote(ZkFlavour.Primary, title, text)
fun noteSecondary(title: String, text: String) = ZkNote(ZkFlavour.Secondary, title, text)
fun noteSuccess(title: String, text: String) = ZkNote(ZkFlavour.Success, title, text)
fun noteWarning(title: String, text: String) = ZkNote(ZkFlavour.Warning, title, text)
fun noteDanger(title: String, text: String) = ZkNote(ZkFlavour.Danger, title, text)
fun noteInfo(title: String, text: String) = ZkNote(ZkFlavour.Info, title, text)

fun notePrimary(title: String, content: ZkElement) = ZkNote(ZkFlavour.Secondary, title, content)
fun noteSecondary(title: String, content: ZkElement) = ZkNote(ZkFlavour.Secondary, title, content)
fun noteSuccess(title: String, content: ZkElement) = ZkNote(ZkFlavour.Success, title, content)
fun noteWarning(title: String, content: ZkElement) = ZkNote(ZkFlavour.Warning, title, content)
fun noteDanger(title: String, content: ZkElement) = ZkNote(ZkFlavour.Danger, title, content)
fun noteInfo(title: String, content: ZkElement) = ZkNote(ZkFlavour.Info, title, content)
