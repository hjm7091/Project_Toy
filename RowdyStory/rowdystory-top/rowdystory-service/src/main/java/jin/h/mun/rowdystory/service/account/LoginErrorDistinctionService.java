package jin.h.mun.rowdystory.service.account;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserLoginRequest;
import jin.h.mun.rowdystory.exception.account.ErrorMessage;
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
    public FieldError distinguish( UserLoginRequest userLoginRequest ) {
        Optional<User> userOpt = userRepository.findByEmail( userLoginRequest.getEmail() );

        if ( !userOpt.isPresent() ) {
            return new FieldError( "userLoginRequest", "email", ErrorMessage.EMAIL_NOT_EXIST.getMessage() );
        } else {
            User user = userOpt.get();

            if ( user.isSocialUser() ) {
                return new FieldError( "userLoginRequest", "social", ErrorMessage.EMAIL_AlREADY_REGISTERED_SOCIAL_ACCOUNT.getMessage() );
            }

            return new FieldError( "userLoginRequest", "password", ErrorMessage.PASSWORD_NOT_MATCH.getMessage() );
        }
    }

}
