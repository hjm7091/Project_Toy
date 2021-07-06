package jin.h.mun.rowdystory.web.controller.view.home;

import jin.h.mun.rowdystory.web.controller.attributes.home.HomeAttributes;
import jin.h.mun.rowdystory.web.controller.view.home.HomeResolver.HomeMapping;
import jin.h.mun.rowdystory.web.controller.view.home.HomeResolver.HomeView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends HomeAttributes {

    @GetMapping( path = { HomeMapping.ROOT, HomeMapping.HOME } )
    public String home() {
        return HomeView.HOME;
    }

}
