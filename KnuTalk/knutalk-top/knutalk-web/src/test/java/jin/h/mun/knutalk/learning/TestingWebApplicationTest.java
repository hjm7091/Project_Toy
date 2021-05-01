package jin.h.mun.knutalk.learning;

import jin.h.mun.knutalk.learning.controller.HomeController;
import jin.h.mun.knutalk.web.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest( controllers = HomeController.class,
	excludeFilters = {
		@ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class )
	}
)
public class TestingWebApplicationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithMockUser( roles = "USER" )
	public void greetingShouldReturnDefaultMessage() throws Exception {
		mockMvc.perform( get( "/learning/home" ) )
				.andDo( print() )
				.andExpect( status().isOk() )
				.andExpect( content().string( containsString( "Hello, World." ) ) );
	}
}
