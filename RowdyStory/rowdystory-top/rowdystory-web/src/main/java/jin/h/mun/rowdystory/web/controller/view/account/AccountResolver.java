package jin.h.mun.rowdystory.web.controller.view.account;

public class AccountResolver {

    public static class AccountView {
        public static final String PREFIX = "account";
        public static final String LOGIN = PREFIX + "/login";
        public static final String FIND_PASSWORD = PREFIX + "/findPassword";
        public static final String REGISTER = PREFIX + "/register";
    }

    public static class AccountMapping {
        public static final String PREFIX = "/account";
        public static final String LOGIN = PREFIX + "/login";
        public static final String LOGIN_FAIL = PREFIX + "/loginFail";
        public static final String LOGOUT = PREFIX + "/logout";
        public static final String FIND_PASSWORD = PREFIX + "/findPassword";
        public static final String REGISTER = PREFIX + "/register";
    }

}
