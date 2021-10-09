package jin.h.mun.rowdystory.web.controller.view.attribute;

import jin.h.mun.rowdystory.web.controller.api.account.AccountAPI;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AttributeKeyAndValueForApi {

    //account
    ACCOUNT_API( "account_api", AccountAPI.BASE ),



    ;

    public final String key;

    public final String value;
}
