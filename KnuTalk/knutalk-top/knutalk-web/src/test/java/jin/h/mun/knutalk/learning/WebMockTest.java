package jin.h.mun.knutalk.learning;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jin.h.mun.knutalk.web.config.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import jin.h.mun.knutalk.learning.controller.GreetingController;
import jin.h.mun.knutalk.learning.service.GreetingService;

@RunWith( SpringRunner.class )
@WebMvcTest( controllers = GreetingController.class,
	excludeFilters = {
		@ComponentScan.Filter( type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class )
	}
)
public class WebMockTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private GreetingService greetingService;
	
	@Test
	@WithMockUser( "USER" )
	public void greetingShouldReturnMessageFromService() throws Exception {
		when( greetingService.greet() ).thenReturn( "Hello, Mock." );
		
		mockMvc.perform( get("/learning/greeting") )
				.andDo( print() )
				.andExpect( status().isOk() )
				.andExpect( content().string( containsString( "Hello, Mock." ) ) );
	}
	
}
