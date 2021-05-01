package jin.h.mun.knutalk.learning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jin.h.mun.knutalk.learning.service.GreetingService;

@Controller
@RequestMapping( "/learning" )
public class GreetingController {

	private final GreetingService greetingService;

	public GreetingController( GreetingService greetingService ) {
		this.greetingService = greetingService;
	}
	
	@RequestMapping( "/greeting" )
	@ResponseBody
	public String greeting() {
		return greetingService.greet();
	}
	
}
