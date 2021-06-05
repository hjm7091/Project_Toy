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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if ( !userOpt.isPresent() ) {
            String message = ErrorMessage.EMAIL_NOT_EXIST.getMessage();
            log.error( message + " {}", email );
            throw new UsernameNotFoundException( message );
        }

        User user = userOpt.get();

        // 소셜 로그인으로 등록된 계정인 경우
        if ( user.isSocialUser() ) {
            String message = ErrorMessage.EMAIL_AlREADY_REGISTERED_SOCIAL_ACCOUNT.getMessage();
            log.error( message + " {}", email );
            throw new AlreadyRegisteredSocialException( message );
        }

        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        roles.add( new SimpleGrantedAuthority( user.getRoleType().getRoleName() ) );

        return new org.springframework.security.core.userdetails.User( email, user.getPassword(), roles );
    }

}
