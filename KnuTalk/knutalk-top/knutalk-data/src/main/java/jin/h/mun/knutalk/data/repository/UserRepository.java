package jin.h.mun.knutalk.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.domain.account.enums.SocialType;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail( String email );
	
	Optional<User> findByEmailAndSocialType( String email, SocialType socialType );
}
