package jin.h.mun.rowdystory.service.account.rowdy;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
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

    @Transactional
    public UserDTO register( UserRegisterRequest userRegisterRequest ) throws Exception {
        userRegisterRequest.setPassword( passwordEncoder.encode( userRegisterRequest.getPassword() ) );
        return userRepository.save( new User( userRegisterRequest ) ).toDTO();
    }

    @Transactional
    public Optional<UserDTO> update( Long id, UserUpdateRequest userUpdateRequest ) throws Exception {
        Optional<User> userOpt = userRepository.findById( id );
        if ( userOpt.isPresent() ) {
            userUpdateRequest.setPassword( passwordEncoder.encode( userUpdateRequest.getPassword() ) );
            UserDTO userDTO = userOpt.get().update( userUpdateRequest ).toDTO();
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
