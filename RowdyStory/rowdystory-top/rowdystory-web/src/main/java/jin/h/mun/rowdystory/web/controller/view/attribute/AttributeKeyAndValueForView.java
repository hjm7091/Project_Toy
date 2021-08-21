package jin.h.mun.rowdystory.web.controller.view.attribute;

import jin.h.mun.rowdystory.dto.account.view.LoginForm;
import jin.h.mun.rowdystory.dto.account.view.PasswordForm;
import jin.h.mun.rowdystory.dto.account.api.RegisterRequest;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeMapping;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AttributeKeyAndValueForView {

    //home
    HOME_URL( "home_url", HomeMapping.HOME ),

    //register
    REGISTER_URL( "register_url", AccountMapping.REGISTER ),
    REGISTER_OBJECT( "register_object", new RegisterRequest() ),

    //login
    LOGIN_URL( "login_url", AccountMapping.LOGIN ),
    LOGIN_OBJECT( "login_object", new LoginForm() ),
    LOGIN_ERROR_MESSAGE( "login_error_message", null ),

    //password
    PASSWORD_URL( "password_url", AccountMapping.PASSWORD ),
    PASSWORD_OBJECT( "password_object", new PasswordForm() ),
    ;

    public final String key;

    public final Object value;
}
