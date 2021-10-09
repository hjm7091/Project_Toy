package jin.h.mun.rowdystory.web.controller.view.account.password;

import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import jin.h.mun.rowdystory.web.controller.view.common.HeaderAttribute;

import java.util.function.Supplier;

import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForView.PASSWORD_OBJECT;
import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForView.PASSWORD_URL;

public class PasswordAttribute extends ModelAttribute {

    public PasswordAttribute() {
        viewName = AccountView.PASSWORD;

        Supplier<HeaderAttribute> header = HeaderAttribute::new;
        attributes.putAll( header.get().getAttributes() );
        attributes.putIfAbsent( PASSWORD_URL.key, PASSWORD_URL.value );
        attributes.putIfAbsent( PASSWORD_OBJECT.key, PASSWORD_OBJECT.value );
    }

}
