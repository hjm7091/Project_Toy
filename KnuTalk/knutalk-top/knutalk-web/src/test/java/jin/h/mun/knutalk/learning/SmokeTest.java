package jin.h.mun.knutalk.learning;

import jin.h.mun.knutalk.learning.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {
	
	@Autowired
	private HomeController homeController;
	
	@Test
	public void contextLoads() {
		assertThat( homeController ).isNotNull();
	}

}
