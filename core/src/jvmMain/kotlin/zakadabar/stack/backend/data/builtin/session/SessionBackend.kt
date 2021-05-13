/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.session

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.resources.setting
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.backend.ktor.session.StackSession
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.account.*
import zakadabar.stack.data.builtin.misc.ServerDescriptionDto
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

/**
 * Session read (user account, roles), login and logout.
 */
object SessionBackend : RecordBackend<SessionDto>() {

    private val serverDescription by setting<ServerDescriptionDto>("zakadabar.server.description")

    override val dtoClass = SessionDto::class

    override var logActions = false // do not log passwords

    override fun onModuleLoad() {
        + SessionTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.action(LoginAction::class, ::action)
        route.action(LogoutAction::class, ::action)
    }

    /**
     * Read session data. Does not use record id as session is identified by Ktor.
     */
    override fun read(call: ApplicationCall, executor: Executor, recordId: RecordId<SessionDto>) = transaction {
        val session = call.sessions.get<StackSession>() ?: throw IllegalStateException()

        if (session.account == Server.anonymous.id) {
            SessionDto(
                id = EmptyRecordId(),
                account = Server.anonymous,
                anonymous = true,
                roles = emptyList(),
                serverDescription = serverDescription
            )
        } else {
            val (account, principalId) = Server.findAccountById(session.account)
            val (_, roleNames) = PrincipalBackend.roles(principalId)
            SessionDto(
                id = EmptyRecordId(),
                account = account,
                anonymous = false,
                roles = roleNames,
                serverDescription = serverDescription
            )
        }
    }

    private fun action(call: ApplicationCall, executor: Executor, action: LoginAction): ActionStatusDto {

        val result = try {
            authenticate(executor.accountId, action.accountName, action.password.value, throwLocked = true) ?: return ActionStatusDto(false)
        } catch (ex: PrincipalBackend.AccountLockedException) {
            return ActionStatusDto(false, "locked")
        }

        val (account, principalId) = result

        val (roleIds, roleNames) = PrincipalBackend.roles(principalId)

        if (StackRoles.siteMember !in roleNames) return ActionStatusDto(false)

        call.sessions.set(StackSession(LongRecordId(account.id.toLong()), roleIds, roleNames))

        return ActionStatusDto(true)
    }

    fun authenticate(executorAccountId: RecordId<AccountPrivateDto>, accountName: String, password: String, throwLocked: Boolean = false): Pair<AccountPublicDto, RecordId<PrincipalDto>>? {
        val (account, principalId) = try {

            val (account, principalId) = Server.findAccountByName(accountName)

            PrincipalBackend.authenticate(principalId, password)

            account to principalId

        } catch (ex: PrincipalBackend.InvalidCredentials) {
            logger.warn("${executorAccountId}: /login result=fail name=${accountName}")
            return null
        } catch (ex: NoSuchElementException) {
            logger.warn("${executorAccountId}: /login result=fail name=${accountName}")
            return null
        } catch (ex: PrincipalBackend.AccountLockedException) {
            logger.warn("${executorAccountId}: /login result=locked name=${accountName}")
            if (throwLocked) throw ex
            return null
        } catch (ex: PrincipalBackend.AccountExpiredException) {
            logger.warn("${executorAccountId}: /login result=expired name=${accountName}")
            return null
        } catch (ex: PrincipalBackend.AccountNotValidatedException) {
            logger.warn("${executorAccountId}: /login result=not-validated name=${accountName}")
            return null
        } catch (ex: Exception) {
            logger.error("${executorAccountId}: /login result=error name=${accountName}", ex)
            throw ex
        }

        logger.info("${executorAccountId}: /login result=success new-account=${account.id} name=${accountName}")
        return account to principalId
    }

    @Suppress("UNUSED_PARAMETER") // action is needed here because of route mapping
    private fun action(call: ApplicationCall, executor: Executor, action: LogoutAction): ActionStatusDto {

        val old = call.sessions.get<StackSession>() !!

        logger.info("${executor.accountId}: /logout old=${old.account} new=${Server.anonymous.id}")

        call.sessions.set(StackSession(LongRecordId(Server.anonymous.id.toLong()), emptyList(), emptyList()))

        return ActionStatusDto(success = true)
    }
}