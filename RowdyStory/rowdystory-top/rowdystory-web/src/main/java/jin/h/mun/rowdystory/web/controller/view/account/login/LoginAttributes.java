package jin.h.mun.rowdystory.web.controller.view.account.login;

import jin.h.mun.rowdystory.dto.account.UserLoginRequest;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

public class LoginAttributes {

    public static final String LOGIN_REQUEST_OBJECT = "loginRequest";
    public static final String LOGIN_URI = "login";
    public static final String LOGIN_ERROR_STRING = "loginError";
    public static final String FIND_PASSWORD_URI = "findPassword";
    public static final String JOIN_URI = "join";

    @ModelAttribute( LOGIN_REQUEST_OBJECT )
    public final UserLoginRequest loginRequest() {
        return new UserLoginRequest();
    }

    @ModelAttribute( LOGIN_URI )
    public final String login() {
        return AccountMapping.LOGIN;
    }

    @ModelAttribute( FIND_PASSWORD_URI )
    public final String findPassword() { return AccountMapping.FIND_PASSWORD; }

    @ModelAttribute( JOIN_URI )
    public final String signUp() { return AccountMapping.JOIN; }
}
