/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.session

/**
 * Thrown when the client presents a session cookie that is now known
 * by the server. Converted to HTTP Status code 440.
 */
class LoginTimeout : Exception()