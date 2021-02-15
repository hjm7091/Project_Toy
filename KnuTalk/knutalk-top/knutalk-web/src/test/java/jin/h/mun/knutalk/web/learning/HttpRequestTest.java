package jin.h.mun.knutalk.web.learning;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void greetingShouldReturnDefaultMessage() throws Exception {
		String url1 = "http://localhost:" + port + "/";
		assertThat(this.restTemplate.getForObject(url1, String.class)).contains("Hello, World.");
		
		String url2 = "http://localhost:" + port + "/greeting";
		assertThat(this.restTemplate.getForObject(url2, String.class)).contains("Hello, World.");
	}

}
