/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
class ExampleBundle(

    override var id : EntityId<ExampleBundle>,
    var name : String,
    var content : ByteArray = ByteArray(0)

) : EntityBo<ExampleBundle> {

    companion object : EntityBoCompanion<ExampleBundle>("example-bundle")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name max 100
    }

}