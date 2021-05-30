package jin.h.mun.rowdystory.web.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping( path = { HomeURL.ROOT, HomeURL.HOME } )
    public String home() {
        return HomeURL.HOME;
    }

}
