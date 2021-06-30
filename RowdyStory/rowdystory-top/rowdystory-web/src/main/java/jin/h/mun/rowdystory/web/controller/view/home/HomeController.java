package jin.h.mun.rowdystory.web.controller.view.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping( path = { HomeView.ROOT, HomeView.HOME } )
    public String home() {
        return HomeView.HOME;
    }

}
