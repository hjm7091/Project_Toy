package jin.h.mun.rowdystory.web.controller.view.home;

import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import jin.h.mun.rowdystory.web.controller.view.common.HeaderAttribute;
import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeView;

import java.util.function.Supplier;

public class HomeAttribute extends ModelAttribute {

    public HomeAttribute() {
        viewName = HomeView.HOME;

        Supplier<HeaderAttribute> supplier = HeaderAttribute::new;
        attributes.putAll( supplier.get().getAttributes() );
    }
}
