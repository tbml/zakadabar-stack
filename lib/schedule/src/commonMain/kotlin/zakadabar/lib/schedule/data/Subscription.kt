/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.UUID

/**
 * Subscription for jobs of the given type. When subscribed, the dispatcher
 * may push a job to this subscription at any time.
 *
 * @param  id               Id of the subscription.
 * @param  nodeUrl          The root URL of the node that creates the subscription.
 * @param  nodeId           The ID of the node that creates this subscription.
 * @param  actionNamespace  Filters the jobs this subscription applies to.
 * @param  actionType       Filters the jobs this subscription appies to.
 */
@Serializable
class Subscription(

    override var id: EntityId<Subscription>,
    var nodeUrl: String,
    var nodeId : UUID,
    var actionNamespace : String?,
    var actionType : String?

) : EntityBo<Subscription> {

    companion object : EntityBoCompanion<Subscription>("zk-schedule-subscription")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::nodeUrl
        + ::nodeId
        + ::actionNamespace
        + ::actionType
    }

}