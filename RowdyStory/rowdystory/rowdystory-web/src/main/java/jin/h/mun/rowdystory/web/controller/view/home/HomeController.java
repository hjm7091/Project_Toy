package jin.h.mun.rowdystory.web.controller.view.home;

import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeMapping;
import jin.h.mun.rowdystory.web.controller.view.home.Home.HomeView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping( path = { HomeMapping.ROOT, HomeMapping.HOME } )
    public String home() {
        return HomeView.HOME;
    }

}
