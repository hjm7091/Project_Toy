package jin.h.mun.rowdystory.service.account.rowdy;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.exception.account.AlreadyRegisteredSocialException;
import jin.h.mun.rowdystory.exception.account.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class FormLoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername( String email ) throws UsernameNotFoundException {
        log.info( "{} 로그인 시도", email );

        Optional<User> userOpt = userRepository.findByEmail( email );

        // 이메일이 존재하지 않는 경우
        User user = userOpt.orElseThrow( () -> {
            String message = ErrorMessage.EMAIL_NOT_EXIST.getMessage();
            return new UsernameNotFoundException( message );
        } );

        // 소셜 로그인으로 등록된 계정인 경우
        if ( user.isSocialUser() ) {
            String message = ErrorMessage.EMAIL_AlREADY_REGISTERED_SOCIAL_ACCOUNT.getMessage();
            throw new AlreadyRegisteredSocialException( message );
        }

        Set<SimpleGrantedAuthority> authorities = Collections.singleton( new SimpleGrantedAuthority( user.getRoleType().getRoleName() ) );

        return new org.springframework.security.core.userdetails.User( email, user.getPassword(), authorities );
    }

}
