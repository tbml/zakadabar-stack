/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.cookbook.common.comm.basics

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.data.schema.BoSchema
import zakadabar.stack.util.default

class CommBasics {

    suspend fun allExample() {
        ExampleBo.all().forEach { println(it.myField) }
        ExampleBo.allAsMap()
    }

    suspend fun createExamples() {

        default<ExampleBo>().create()
        default<ExampleBo> { myField = 12 }.create()
        ExampleBo(EntityId(), myField = 12, myField2 = 13).create()

        // create returns with the newly created BO, this is returned by the server
        // and contains the proper EntityId with a value

        default<ExampleBo>()
            .create()
            .apply { println(id) }

    }

    suspend fun readExamples() {
        ExampleBo.read(12)
        ExampleBo.read("12")
        ExampleBo.read(EntityId(12))
    }

    suspend fun updateExample() {
        ExampleBo.read(12).apply { myField = 14 }.update()
    }

    suspend fun deleteExample() {
        ExampleBo.delete(EntityId(12))
        ExampleBo.read(12).delete()
    }

    suspend fun queryExample() {
        ExampleQuery("", 12).execute().forEach { println(it) }
    }

    suspend fun actionExample() {
        ExampleAction("").execute()
    }

}

@Serializable
class ExampleBo(
    override var id: EntityId<ExampleBo>,
    var myField: Int,
    var myField2: Int
) : EntityBo<ExampleBo> {

    companion object : EntityBoCompanion<ExampleBo>("zkc-example-bo")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::myField
    }
}

@Serializable
class ExampleQuery(
    var param1: String,
    var param2: Int
) : QueryBo<List<String>> {

    companion object : QueryBoCompanion(ExampleBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(String.serializer()))

    override fun schema() = BoSchema {
        + ::param1
        + ::param2
    }
}

@Serializable
class ExampleAction(

    var name : String

) : ActionBo<Unit> {

    companion object : ActionBoCompanion(ExampleBo.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), Unit.serializer())

    override fun schema() = BoSchema {
        + ::name blank false min 1 max 30
    }

}