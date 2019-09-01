package org.mjstudio.gfree.domain.common

interface Msg {
    val msg: String
}

class CustomMsg(override val msg: String) : Msg

enum class GeneralMsg(override val msg: String) : Msg {
    SEND_EMAIL("send email"),
    EXIT("Press the back button to exit the G.free"),
}

enum class NegativeMsg(override val msg: String, val throwable: Throwable? = null) : Msg {
    FAIL("fail"),
    SERVER_DISCONNECTED("disconnected"),

    //region AUTH
    EMAIL_NOT_EXIST("account doesn't exist. please sign up"),
    PASSWORD_INCORRECT("password is wrong"),
    PASSWORD_NOT_EQUAL("password equality check fail"),
    EMAIL_EMPTY("email is empty"),
    EMAIL_GIST_FORMAT("only GIST email can sign up"),
    PASSWORD_LENGTH6("password length must longer than 6"),
    EMAIL_NEED_VERIFICATION("email is not verified"),
    SIGN_IN_FAIL("fail"),
    VERIFICATION_FAIL("not verificated"),

    FAIL_SEND_VERIFICATION_EMAIL("sending account verification email is failed"),
    FAIL_SEND_RESET_PASSWORD_EMAIL("sending password reset email is failed"),
    FIREBASE_ACCOUNT_CREATE_FAIL("account can't be created. already exist?"),
    SERVER_ACCOUNT_CREATE_FAIL("check network connection. \nor server disconnected?"),
    //endregion

    NETWORK("network is disconnected"),
    CLASS_DUPLICATE("class times are duplicated!"),

    CLASS_REGISTER_FAIL("cannot register class"),
    CLASS_UNREGISTER_FAIL("cannot unregister class"),

    CLASS_LIST_GET_FAIL("cannot list datas"),
    CLASS_DATA_GET_FAIL("cannot get class data"),
    CLASS_USER_GET_FAIL("cannot get users data in class"),
    CLASS_REVIEW_GET_FAIL("cannot get reviews in class"),

    PROFILE_INIT_FAIL("cannot get profile datas"),
    PROFILE_CHANGE_FAIL("cannot update profile info"),

    NOTI_LIST_FAIL("fail to get notification list"),

    POST_FAIL_MAKE_REQUEST("fail to make edit request"),

    EDIT_LIST_FAIL("fail to get edit request list"),

    ;
}

enum class PositiveMsg(override val msg: String) : Msg {
    SUCCESS("success"),
    SERVER_CONNECTED("connected"),

    //region AUTH
    SEND_VERIFICATION_EMAIL("verification email is sended. It takes quite a while."),
    SEND_PASSWORD_REST_EMAIL("password reset email is sended. It takes quite a while."),
    ACCOUNT_CREATED("account is successfully created!"),

    SIGN_IN_COMPLETE("success"),
    VERIFICATION_COMPLETE("verified"),

    //endregion

    CLASS_REGISTER_SUCCESS("class register success"),
    CLASS_UNREGISTER_SUCCESS("class unregister success"),
}