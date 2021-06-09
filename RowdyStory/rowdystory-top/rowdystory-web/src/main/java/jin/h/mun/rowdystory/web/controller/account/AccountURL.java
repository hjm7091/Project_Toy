package jin.h.mun.rowdystory.web.controller.account;

import jin.h.mun.rowdystory.web.controller.home.HomeURL;

public class AccountURL {

    public static final String LOGIN = "account/login";
    public static final String LOGIN_FAIL = "account/loginFail";
    public static final String LOGOUT = "logout";

    public static final String ROOT_LOGIN = HomeURL.ROOT + LOGIN;
    public static final String ROOT_LOGIN_FAIL = HomeURL.ROOT + LOGIN_FAIL;
    public static final String ROOT_LOGOUT = HomeURL.ROOT + LOGOUT;

}
