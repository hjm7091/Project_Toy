package jin.h.mun.rowdystory.service.account.rowdy;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.RegisterRequest;
import jin.h.mun.rowdystory.dto.account.api.UpdateRequest;
import jin.h.mun.rowdystory.exception.account.AccountPreviousPasswordUnmatchedException;
import jin.h.mun.rowdystory.exception.account.ErrorMessage;
import jin.h.mun.rowdystory.exception.account.SocialAccountUnmodifiableException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional( readOnly = true )
@RequiredArgsConstructor
@Service
public class AccountService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll() throws Exception {
        return userRepository.findAll()
                .stream().map( User::toDTO )
                .collect( Collectors.toList() );
    }

    public Optional<UserDTO> get( Long id ) throws Exception {
        return userRepository.findById( id ).map( User::toDTO );
    }

    public boolean checkDuplicate( String email ) {
        return userRepository.findByEmail( email ).isPresent();
    }

    @Transactional
    public UserDTO changeEmail( String from, String to ) {
        User user = userRepository.findByEmail( from ).orElseThrow( () -> new IllegalStateException( "impossible case..." ) );
        if ( user.isSocialUser() ) {
            throw new SocialAccountUnmodifiableException( ErrorMessage.SOCIAL_ACCOUNT_UNMODIFIABLE.getMessage() );
        }
        user.changeEmail( to );
        return user.toDTO();
    }

    @Transactional
    public void changePassword( String email, String from, String to ) {
        User user = userRepository.findByEmail( email ).orElseThrow( () -> new IllegalStateException( "impossible case..." ) );
        if ( user.isSocialUser() ) {
            throw new SocialAccountUnmodifiableException( ErrorMessage.SOCIAL_ACCOUNT_UNMODIFIABLE.getMessage() );
        }
        if ( !passwordEncoder.matches( from, user.getPassword() ) ) {
            throw new AccountPreviousPasswordUnmatchedException( ErrorMessage.ACCOUNT_PREVIOUS_PASSWORD_UNMATCHED.getMessage() );
        }
        user.changePassword( to );
    }

    @Transactional
    public UserDTO register( RegisterRequest registerRequest ) throws Exception {
        return userRepository.save( new User( registerRequest ) ).toDTO();
    }

    @Transactional
    public Optional<UserDTO> updateById( Long id, UpdateRequest updateRequest ) throws Exception {
        Optional<User> userOpt = userRepository.findById( id );
        if ( userOpt.isPresent() ) {
            UserDTO userDTO = userOpt.get().update( updateRequest ).toDTO();
            return Optional.of( userDTO );
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<UserDTO> updateByEmail( String email, UpdateRequest updateRequest ) {
        Optional<User> userOpt = userRepository.findByEmail( email );
        if ( userOpt.isPresent() ) {
            UserDTO userDTO = userOpt.get().update( updateRequest ).toDTO();
            return Optional.of( userDTO );
        }
        return Optional.empty();
    }

    @Transactional
    public boolean delete( Long id ) throws Exception {
        Optional<User> userOpt = userRepository.findById( id );
        if ( userOpt.isPresent() ) {
            userRepository.delete( userOpt.get() );
            return true;
        }
        return false;
    }
}
