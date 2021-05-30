package jin.h.mun.rowdystory.service.account;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
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
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername( String email ) throws UsernameNotFoundException {
        log.info( "{} 로그인 시도", email );

        Optional<User> userOpt = userRepository.findByEmail( email );

        // 이메일이 존재하지 않는 경우
        if ( !userOpt.isPresent() ) {
            String message = "해당 이메일에 대한 정보가 없습니다. 이메일 주소를 확인해주세요.";
            log.error( message + " {}", email );
            throw new UsernameNotFoundException( message );
        }

        User user = userOpt.get();

        // 소셜 로그인으로 등록된 계정인 경우
        if ( user.getSocialType() != null ) {
            String message = "해당 계정은 소셜 로그인으로 등록된 계정입니다. 소셜 로그인을 통해 로그인해주세요.";
            log.error( message + " {}", email );
            throw new UsernameNotFoundException( message );
        }

        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        roles.add( new SimpleGrantedAuthority( user.getRoleType().getRoleName() ) );

        return new org.springframework.security.core.userdetails.User( email, user.getPassword(), roles );
    }

}
