package jin.h.mun.knutalk.web.learning;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jin.h.mun.knutalk.web.learning.controller.HomeController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {
	
	@Autowired
	private HomeController homeController;
	
	@Test
	public void contextLoads() {
		assertThat(homeController).isNotNull();
	}

}
