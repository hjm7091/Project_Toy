package jin.h.mun.rowdystory.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping( path = { "/", "/home" } )
    public String home() {
        return "home";
    }

}
