package jin.h.mun.rowdystory.web.controller.api.account;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.UpdateRequest;
import jin.h.mun.rowdystory.service.account.rowdy.AccountService;
import jin.h.mun.rowdystory.web.controller.api.common.ExceptionHandler;
import jin.h.mun.rowdystory.web.resolver.session.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RequestMapping( AccountAPI.BASE )
@RestController
public class CRUDForAdminRestController extends ExceptionHandler {

    private final AccountService accountService;

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
                                              @RequestBody UpdateRequest updateRequest ) throws Exception {
        Optional<UserDTO> userOpt = accountService.updateById( id, updateRequest );
        return userOpt.map( ResponseEntity::ok ).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @PutMapping( consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<UserDTO> updateByEmail( @SessionUser UserDTO userDTO,
                                                  @RequestBody UpdateRequest updateRequest ) {
        Optional<UserDTO> userOpt = accountService.updateByEmail( userDTO.getEmail(), updateRequest );
        return userOpt.map( ResponseEntity::ok ).orElseGet( () -> ResponseEntity.notFound().build() );
    }

    @DeleteMapping( value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<Boolean> deleteById( @PathVariable( "id" ) Long id ) throws Exception {
        boolean result = accountService.delete( id );
        return ResponseEntity.ok( result );
    }
}
