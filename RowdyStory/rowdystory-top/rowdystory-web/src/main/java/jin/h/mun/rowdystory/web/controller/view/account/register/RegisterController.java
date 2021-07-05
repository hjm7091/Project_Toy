package jin.h.mun.rowdystory.web.controller.view.account.register;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.service.account.rowdy.AccountService;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountMapping;
import jin.h.mun.rowdystory.web.controller.view.account.AccountResolver.AccountView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RegisterController extends RegisterAttributes {

    private final AccountService accountService;

    @GetMapping( AccountMapping.REGISTER )
    public String register() {
        return AccountView.REGISTER;
    }

    @PostMapping( path = AccountMapping.REGISTER, consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<String> register( @RequestBody UserRegisterRequest userRegisterRequest ) throws Exception {
        UserDTO userDTO = accountService.register( userRegisterRequest );
        return new ResponseEntity<>( HttpStatus.OK.getReasonPhrase(), HttpStatus.OK );
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<String> handle( Exception e ) {
        String error = String.format( "class : %s, message : %s", e.getClass().getSimpleName(), e.getMessage() );
        return new ResponseEntity<>( error, HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
