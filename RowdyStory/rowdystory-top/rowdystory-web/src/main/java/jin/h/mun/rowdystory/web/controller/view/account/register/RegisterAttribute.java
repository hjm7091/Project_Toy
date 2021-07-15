package jin.h.mun.rowdystory.web.controller.view.account.register;

import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import jin.h.mun.rowdystory.web.controller.view.common.HeaderAttribute;

import java.util.function.Supplier;

import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForApi.ACCOUNT_API;
import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForView.REGISTER_OBJECT;

public class RegisterAttribute extends ModelAttribute {

    public RegisterAttribute() {
        viewName = AccountView.REGISTER;

        Supplier<HeaderAttribute> header = HeaderAttribute::new;
        attributes.putAll( header.get().getAttributes() );

        attributes.putIfAbsent( REGISTER_OBJECT.key, REGISTER_OBJECT.value );
        attributes.putIfAbsent( ACCOUNT_API.key, ACCOUNT_API.value );
    }
}
