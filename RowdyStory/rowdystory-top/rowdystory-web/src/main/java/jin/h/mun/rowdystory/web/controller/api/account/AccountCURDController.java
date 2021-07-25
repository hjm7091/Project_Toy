package jin.h.mun.rowdystory.web.controller.api.account;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
import jin.h.mun.rowdystory.service.account.rowdy.AccountService;
import jin.h.mun.rowdystory.web.controller.view.account.Account.AccountMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RequestMapping( AccountAPI.BASE )
@RestController
public class AccountCURDController {

    private final AccountService accountService;

    @PostMapping( produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<UserDTO> register( @RequestBody UserRegisterRequest userRegisterRequest ) throws Exception {
        UserDTO userDTO = accountService.register( userRegisterRequest );
        return ResponseEntity.created( URI.create( AccountMapping.LOGIN ) ).body( userDTO );
    }

    @GetMapping( produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<List<UserDTO>> getAll() throws Exception {
        return ResponseEntity.ok( accountService.getAll() );
    }

    @GetMapping( value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<UserDTO> getById( @PathVariable( "id" ) Long id ) throws Exception {
        Optional<UserDTO> userOpt = accountService.get( id );
        return userOpt.map( ResponseEntity::ok ).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @PutMapping( value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<UserDTO> updateById( @PathVariable( "id" ) Long id,
                                              @RequestBody UserUpdateRequest userUpdateRequest ) throws Exception {
        Optional<UserDTO> userOpt = accountService.update( id, userUpdateRequest );
        return userOpt.map( ResponseEntity::ok ).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping( value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<Boolean> deleteById( @PathVariable( "id" ) Long id ) throws Exception {
        boolean result = accountService.delete( id );
        return ResponseEntity.ok( result );
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<String> handle( Exception e ) {
        String error = String.format( "class : %s, message : %s", e.getClass().getSimpleName(), e.getMessage() );
        return new ResponseEntity<>( error, HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
