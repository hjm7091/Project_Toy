package jin.h.mun.rowdystory.web.controller.view.account.info;

import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountView;
import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import jin.h.mun.rowdystory.web.controller.view.common.HeaderAttribute;

import java.util.function.Supplier;

public class InfoAttribute extends ModelAttribute {

    public InfoAttribute() {
        viewName = AccountView.INFO;

        Supplier<HeaderAttribute> header = HeaderAttribute::new;
        attributes.putAll( header.get().getAttributes() );
    }
}
