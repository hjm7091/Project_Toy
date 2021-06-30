package jin.h.mun.rowdystory.web.controller.api.account;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
import jin.h.mun.rowdystory.service.account.rowdy.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping( AccountAPI.BASE )
@RequiredArgsConstructor
@RestController
@Slf4j
public class AccountApiController {

    private final AccountService accountService;

    @PostMapping( produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<UserDTO> register( @RequestBody UserRegisterRequest userRegisterRequest ) throws Exception {
        UserDTO userDTO = accountService.register( userRegisterRequest );
        URI uri = linkTo( AccountApiController.class ).slash( userDTO.getId() ).toUri();
        return ResponseEntity.created( uri ).body( userDTO );
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
    public ResponseEntity<String> deleteById( @PathVariable( "id" ) Long id ) throws Exception {
        boolean result = accountService.delete( id );
        if ( result ) return ResponseEntity.ok( "delete success." );
        else return ResponseEntity.notFound().build();
    }
}
