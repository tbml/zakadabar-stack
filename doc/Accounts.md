# Accounts

The stack differentiates between accounts and principals. An account a business related entity for storing business
related data such as names, contact information, etc. A principal is a security related entity for authentication and
authorization.

The stack contains handling of principals out-of-the-box, including:

* basic crud
* password handling
* login / logout
* authorization
* roles

The stack **does not** contain handling of accounts, because the information stored is specific for each system.

The [demo](../demo/demo) project shows how to:

* create [private](../demo/demo/src/jvmMain/kotlin/zakadabar/demo/backend/account/AccountPrivateBackend.kt)
  and [public](../demo/demo/src/jvmMain/kotlin/zakadabar/demo/backend/account/AccountPublicBackend.kt) account backends
* create [account settings](../demo/demo/src/jsMain/kotlin/zakadabar/demo/frontend/pages/account/Form.kt) for the user
* create [account list](../demo/demo/src/jsMain/kotlin/zakadabar/demo/frontend/pages/account/Table.kt) for
  administrators
* perform [login](../demo/demo/src/jsMain/kotlin/zakadabar/demo/frontend/pages/misc/Login.kt)
  and [logout](../demo/demo/src/jsMain/kotlin/zakadabar/demo/frontend/SideBar.kt)

## Authentication

* as soon as the frontend connects, the backend creates a session
* the session has an `executor`: the entity who owns the session
* before logging in, the session executor is the user named "anonymous"
* all routes are inside Ktor's `authenticate`

The actual login means that we assign a new user to the session.

The clients execute [LoginAction](../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/LoginAction.kt) and
[LogoutAction](../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/LogoutAction.kt) to perform login and logout.

```kotlin
var actionStatus = LoginAction("demo", Secret("wrong")).execute()

if (actionStatus.success) {
    println("logged in")
} else {
    println("failed to login")
}

session = SessionDto.read(0L) // this will return with the roles of the logged in user
```

Working examples:

* [web client](../demo/demo/src/jsMain/kotlin/zakadabar/demo/frontend/pages/misc/Login.kt)
* [jvm client](../demo/demo-jvm-client/src/jvmMain/kotlin/zakadabar/demo/frontend/Main.kt)

## Get User Information on the Frontend

Frontend user information is in the `executor` property
of [ZkApplication](../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt).

The `executor` property stores
a [ZkExecutor](../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkExecutor.kt) instance:

```kotlin
class ZkExecutor(
    val account: AccountPublicDto,
    val anonymous: Boolean,
    val roles: List<String>
)
```

Typically [main.kt](../demo/demo/src/jsMain/kotlin/main.kt) fetches the current account during bootstrap.

Login and logout changes the executor, see [Authentication](Authentication.md) for more information.

## Helper Methods on JavaScript Frontend

[ZkApplication](../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt)

```kotlin
if (hasRole("role-name")) {
    // do something
}
```

[ZkElement](../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkElement.kt)

```kotlin
ifNotAnonymous {
    // do something
}

ifAnonymous {
    // do something
}

withRole("role-name") {
    // do something
}

withoutRole("role-name") {
    // do something
}
```

## Get Account of the User on the Backend

For CRUD, queries, actions and BLOBs the called backend method receives
an [Executor](../core/src/jvmMain/kotlin/zakadabar/stack/util/Executor.kt)
instance.

```kotlin
open class Executor internal constructor(
    val accountId: Long,
    private val roles: List<String>
) 
```

## Helper Methods on Backend

[authorize.kt](../core/src/jvmMain/kotlin/zakadabar/stack/backend/authorize.kt)

```kotlin
authorize(executor, "role-name") // throws Unauthorized when doesn't have the role

authorize(executor, "role-name-1", "role-name-2") // throws Unauthorized when doesn't have at lease one of the roles

authorize(executor) { // throws Unauthorized when the check returns with false
    return (executor.accountId % 2 == 0)
}
```

## Get Account in Custom Backends

The backend provides an extension function to Ktor's `ApplicationCall` that returns with
an [Executor](../core/src/jvmMain/kotlin/zakadabar/stack/util/Executor.kt) instance.

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
for [AccountPublicDto](../core/src/commonMain/kotlin/zakadabar/stack/data/builtin/AccountPublicDto.kt).

This DTO is meant to provide public account information for displaying names, avatars etc.

Working example:

* [AccountPublicBackend.kt](../demo/demo/src/jvmMain/kotlin/zakadabar/demo/backend/account/AccountPublicBackend.kt)