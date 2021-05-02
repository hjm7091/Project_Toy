package jin.h.mun.rowdystory.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( "/learning" )
public class HomeController {

	@RequestMapping( "/home" )
	@ResponseBody
	public String greeting() {
		return "Hello, World.";
	}
	
}
