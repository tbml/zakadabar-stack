/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.query.anonymous

import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.QueryBusinessLogicBase
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.util.PublicApi

@PublicApi
class QueryBl : QueryBusinessLogicBase<Query, List<String>>(
    queryBoClass = Query::class
) {

    override val authorizer = object : Authorizer<BaseBo> {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            return
        }
    }

    override fun execute(executor: Executor, bo: Query): List<String> =
        listOf("Hello", "World")

}