package jin.h.mun.rowdystory.web.controller.view.account;

public class Account {

    public static class AccountView {
        public static final String PREFIX = "account";
        public static final String LOGIN = PREFIX + "/login";
        public static final String PASSWORD = PREFIX + "/password";
        public static final String REGISTER = PREFIX + "/register";
        public static final String INFO = PREFIX + "/info";
    }

    public static class AccountMapping {
        public static final String PREFIX = "/account";
        public static final String LOGIN = PREFIX + "/login";
        public static final String LOGIN_FAIL = PREFIX + "/loginFail";
        public static final String LOGOUT = PREFIX + "/logout";
        public static final String PASSWORD = PREFIX + "/password";
        public static final String REGISTER = PREFIX + "/register";
        public static final String INFO = PREFIX + "/info";
    }

}
