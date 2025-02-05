/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.peristence

import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.lib.schedule.data.Subscription

class SubscriptionTable : ExposedPaTable<Subscription>(
    tableName = "schedule_subscription"
) {

    val nodeUrl = text("node_address")
    val nodeId = uuid("node_id")
    val actionNamespace = text("action_namespace").nullable()
    val actionType = text("action_type").nullable()

}