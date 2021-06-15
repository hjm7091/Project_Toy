package jin.h.mun.rowdystory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RowdyStoryApplicationTest {

	@Autowired
	RowdyStoryApplication rowdyStoryApplication;
	
	@Test
	public void contextLoads() {
		assertThat( rowdyStoryApplication ).isNotNull();
	}

	@Test
	public void run() {
		RowdyStoryApplication.main( new String[]{} );
	}

}
