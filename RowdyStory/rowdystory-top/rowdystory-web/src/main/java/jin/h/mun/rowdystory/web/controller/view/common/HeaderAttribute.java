package jin.h.mun.rowdystory.web.controller.view.common;

import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;

import static jin.h.mun.rowdystory.web.controller.view.attribute.AttributeKeyAndValueForView.*;

public class HeaderAttribute extends ModelAttribute {

    public HeaderAttribute() {
        attributes.putIfAbsent( HOME_URL.key , HOME_URL.value );
        attributes.putIfAbsent( REGISTER_URL.key, REGISTER_URL.value );
        attributes.putIfAbsent( LOGIN_URL.key, LOGIN_URL.value );
    }
}
