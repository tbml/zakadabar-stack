/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // automatic detection does not work here because many strings are auto bound

package zakadabar.stack.resources

open class ZkBuiltinStrings : ZkStringStore() {

    // -------------------------------------------------------------------------
    // Name of the application
    // -------------------------------------------------------------------------

    open val applicationName by "Zakadabar"

    // -------------------------------------------------------------------------
    // General labels for buttons, general messages to the user
    // -------------------------------------------------------------------------

    open val actionFail by "Action execution error."
    open val actionSuccess by "Successful action execution."
    open val actions by "Actions"
    open val back by "Back"
    open val cancel by "cancel"
    open val cannotAttachMoreImage by "Image count maximum reached, cannot add more images."
    open val close by "close"
    open val confirmDelete by "Delete is irreversible. Are you sure?"
    open val confirmation by "confirmation"
    open val createFail by "Create failed."
    open val createSuccess by "Create success."
    open val delete by "Delete"
    open val deleteFail by "Delete failed."
    open val deleteSuccess by "Delete success."
    open val details by "Details"
    open val dropFilesHere by "Drop files here."
    open val edit by "Edit"
    open val execute by "execute"
    open val falseText by "False"
    open val id by "Id"
    open val invalidFieldsExplanation by "Cannot save the data yet as some values are invalid. They are marked by red color, please enter valid values and try save again."
    open val invalidFieldsToast by "Invalid fields, cannot save yet."
    open val invalidValue by "Invalid Value"
    open val loading by "Loading"
    open val missingRoute by "This page does not exists. Please go back or <a href=\"/\">go to the home page</a>."
    open val name by "Name"
    open val no by "no"
    open val notSaved by "Changes are not saved, by going back you'll lose them. Are you sure?"
    open val notSelected by "not selected"
    open val ok by "ok"
    open val processing by "... processing ..."
    open val programError by "Error during program execution, please notify the support about this."
    open val queryFail by "Query execution error."
    open val querySuccess by "Query Success."
    open val save by "Save"
    open val show by "show"
    open val trueText by "True"
    open val updateFail by "Update failed."
    open val updateSuccess by "Update success."
    open val yes by "yes"
    open val executeError by "Error while executing the function."

    // -------------------------------------------------------------------------
    // Values for the build-in administration UI
    // -------------------------------------------------------------------------

    open val account by "Account"
    open val accountStatus by "Account Status"
    open val accounts by "Accounts"
    open val administration by "Administration"
    open val basics by "Basics"
    open val fullName by "Full Name"
    open val locales by "Locales"
    open val login by "Login"
    open val loginFail by "Login failed."
    open val loginFailCount by "Failed logins"
    open val loginLocked by "Login has failed because the account is locked."
    open val logout by "Logout"
    open val newPassword by "New Password"
    open val newPasswordVerification by "Verification"
    open val oldPassword by "Old Password"
    open val organizationName by "Organization Name"
    open val passwordChange by "Password Change"
    open val passwordChangeExpOwn by "Please type in your old password and then the new password twice. The password has to be at least 8 characters long."
    open val passwordChangeExpSo by "Please type in the new password twice. The password has to be at least 8 characters long."
    open val passwordChangeFail by "Failed to change the password."
    open val passwordChangeInvalid by "Invalid fields, cannot change password yet."
    open val role by "Role"
    open val roles by "Roles"
    open val sessionRenew by "Your session has been expired. Please log in again to continue."
    open val sessionRenewError by "An error happened during session renewal. You have been logged out, please log in again to continue."
    open val setting by "Setting"
    open val settings by "Settings"
    open val translations by "Translations"
    open val authenticationNeeded by "Authentication needed to access this function. Please log in."
    open val forbiddenExplanation by "This function is unavailable on this account."

}

