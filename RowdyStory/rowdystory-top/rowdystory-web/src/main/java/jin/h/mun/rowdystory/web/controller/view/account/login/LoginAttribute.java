package jin.h.mun.rowdystory.web.controller.view.account.login;

import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import jin.h.mun.rowdystory.web.controller.view.common.HeaderAttribute;

import java.util.function.Supplier;

import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForView.LOGIN_OBJECT;
import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForView.PASSWORD_URL;

public class LoginAttribute extends ModelAttribute {

    public LoginAttribute() {
        viewName = AccountView.LOGIN;

        Supplier<HeaderAttribute> header = HeaderAttribute::new;
        attributes.putAll( header.get().getAttributes() );
        attributes.putIfAbsent( LOGIN_OBJECT.key, LOGIN_OBJECT.value );
        attributes.putIfAbsent( PASSWORD_URL.key, PASSWORD_URL.value );
    }
}
