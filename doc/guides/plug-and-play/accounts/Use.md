# Use: Accounts

## Authentication

The authentication status is associated with a session. Each session has a unique ID generated by Ktor and sent to the
frontend in a cookie, named `ZKL_SESSION`.

* As soon as the frontend connects, the backend creates a session.
* The session has an `executor`, the entity who owns the session.
* Before logging in, the session executor is the user named `anonymous`.
* All routes are inside Ktor's `authenticate`.

The actual login means that we assign a new user to the session.

The clients execute [LoginAction](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/LoginAction.kt)
and [LogoutAction](/lib/accounts/src/commonMain/kotlin/zakadabar/lib/accounts/data/LogoutAction.kt) 
to perform login and logout.

```kotlin
var actionStatus = LoginAction("demo", Secret("wrong")).execute()

if (actionStatus.success) {
    println("logged in")
} else {
    println("failed to login")
}

session = SessionBo.read(EntityId("current")) // returns with the roles of the logged in user
```

## Basic Authentication (JVM client)

To use basic authentication from a client, assign an authenticating HTTP client
to `CommBase.client`. This method authenticates the client with an `Authentication`
HTTP header. No session is created and no login is required.

```kotlin
CommBase.client = httpClientWithAuth("so", "so")
AccountPrivateBo.all()
```

## Use on the Frontend

Use the `executor` global value. It is an instance of [ZkExecutor](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkExecutor.kt):

```kotlin
class ZkExecutor(
    val account: AccountPublicDto,
    val anonymous: Boolean,
    val roles: List<String>
)
```

The browser frontend usually fetches the session data in `main.kt` by calling `sessionManager.init`.

This call sets the `executor` property of `ZkApplication`. For details see: [Use](./Use.md).

```kotlin
 with(application) {
    sessionManager.init()
    // ...
}
```

## Helper Methods on JavaScript Frontend

[application](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt):

```kotlin
if (hasRole("role-name")) {
    TODO()
}
```

[ZkElement](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkElement.kt):

```kotlin
ifNotAnonymous {
    TODO()
}

ifAnonymous {
    TODO()
}

withRole("role-name") {
    TODO()
}

withoutRole("role-name") {
    TODO()
}
```

## Get Account of the User on the Backend

For CRUD, queries, actions and BLOBs the called business logic method receives
an [Executor](/core/core-core/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/Executor.kt) instance.

```kotlin
open class Executor internal constructor(

    val accountId: Long,
    val roleIds: List<RecordId<RoleDto>>,
    private val roleNames: List<String>

) : Principal {

    fun hasRole(roleName: String): Boolean

    fun hasRole(roleId: RecordId<RoleDto>): Boolean

    fun hasOneOfRoles(roleNames: Array<out String>): Boolean

}
```

## Business Logic Authorization

See [Authorizer](../../backend/Authorizer.md) for more information about authorization
in business logic modules.

## Foreign Keys (Exposed)

To have foreign keys referencing accounts or roles, use AccountPrivateBo and RoleBo
in your BO definition. These will generate the following code:

```kotlin
    internal val account = reference("account", AccountPrivateExposedTableCommon)
    internal val role = reference("role", RoleExposedTableCommon)
```

To set the entity id of an `AccountPriveteBo` field from an `AccountPublicBo` field:

```kotlin
account = EntityId(publicBo.id)
```

## Manual Authorization Checks

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Authorizer Are Preferred">

While these methods are supported, it is better to use the [Authorizer](../../backend/Authorizer.md)
of business logics. The reason is that the authorizer is a single point of authorization
which can be analyzed programmatically. In the future we plan to implement features based
on the authorizers like "Endpoint Security Report" or configuration of authorizers from
the interface (which role is needed for example).

</div>

[authorize.kt](/core/core-core/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/authorize.kt)

```kotlin
authorize(executor, "role-name") // throws Unauthorized when doesn't have the role

authorize(executor, 1L) // throws Unauthorized when doesn't have the role with the given role id

authorize(executor, "role-name-1", "role-name-2") // throws Unauthorized when doesn't have at lease one of the roles

authorize(executor) { // throws Unauthorized when the check returns with false
    return (executor.accountId % 2 == 0)
}

authorize(true) // throws Unauthorized when the parameter is false, use this to enable public access
```

## Get Account in Custom Backends

The backend provides an extension function to Ktor's `ApplicationCall` that returns with
an [Executor](/core/core-core/src/jvmMain/kotlin/zakadabar/stack/backend/authorize/Executor.kt) instance.

This instance has an `accountId` field which stores the id of the account that executes the code.

```kotlin
override fun onInstallRoutes(route: Route) {
    with(route) {
        get("ping") {
            val executor = call.executor()
            authorize(executor, "pinger")
            call.respond("pong")
        }
    }
}
```

## Public Account Information

To provide generic account data, implement a backend
for [AccountPublicBo](/core/core-core/src/commonMain/kotlin/zakadabar/stack/data/builtin/account/AccountPublicBo.kt).

This BO is meant to provide public account information for displaying names, avatars etc.

**Be careful with sensitive data, such as e-mail addresses!**