package jin.h.mun.rowdystory.service.account;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserLoginRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginErrorDistinctionService {

    private final UserRepository userRepository;

    @Transactional( readOnly = true )
    public DistinctionResult distinguish( UserLoginRequest userLoginRequest ) {
        Optional<User> userOpt = userRepository.findByEmail( userLoginRequest.getEmail() );

        if ( !userOpt.isPresent() ) {
            FieldError emailError = new FieldError( "userLoginRequest", "email", ErrorMessage.email );
            return DistinctionResult.builder().fieldError( emailError ).build();
        } else {
            User user = userOpt.get();

            if ( user.getSocialType() != null ) {
                FieldError socialError = new FieldError( "userLoginRequest", "social", ErrorMessage.social );
                return DistinctionResult.builder().fieldError( socialError ).build();
            }

            FieldError passwordError = new FieldError( "userLoginRequest", "password", ErrorMessage.password );
            return DistinctionResult.builder().fieldError( passwordError ).build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class DistinctionResult {

        private final FieldError fieldError;

    }

    public static class ErrorMessage {
        public static final String email = "해당 이메일에 대한 정보가 없습니다. 이메일 주소를 확인해주세요.";
        public static final String password = "비밀번호가 일치하지 않습니다. 비밀번호를 확인해주세요.";
        public static final String social = "해당 계정은 소셜 로그인으로 등록된 계정입니다. 소셜 로그인을 통해 로그인해주세요.";
    }
}
