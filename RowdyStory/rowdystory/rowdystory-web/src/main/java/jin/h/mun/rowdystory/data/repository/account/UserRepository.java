package jin.h.mun.rowdystory.data.repository.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail( String email );
	
	Optional<User> findByEmailAndSocialType( String email, SocialType socialType );

	List<User> findByUserName( String userName );
}
