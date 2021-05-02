package jin.h.mun.rowdystory.learning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class HttpRequestTest {
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@BeforeEach
	public void setUp() {
		mvc = MockMvcBuilders
				.webAppContextSetup( context )
				.apply( springSecurity() )
				.build();
	}

	@Test
	@WithMockUser( roles = "USER" )
	public void greetingShouldReturnDefaultMessage() throws Exception {
		mvc.perform( get( "/learning/home" ) )
		    .andExpect( status().isOk() )
		    .andExpect( content().string( "Hello, World." ) );

		mvc.perform( get( "/learning/greeting" ) )
		    .andExpect( status().isOk() )
		    .andExpect( content().string( "Hello, World." ) );
	}

}