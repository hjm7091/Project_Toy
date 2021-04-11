package jin.h.mun.knutalk.data.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.domain.account.enums.SocialType;

public interface UserFindRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail( String email );
	
	Optional<User> findByEmailAndSocialType( String email, SocialType socialType );

	List<User> findByUserName( String userName );
}
