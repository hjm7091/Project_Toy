package jin.h.mun.knutalk.data.repository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan( "jin.h.mun.knutalk.domain" )
public class RepositoryTestApplication {
}
