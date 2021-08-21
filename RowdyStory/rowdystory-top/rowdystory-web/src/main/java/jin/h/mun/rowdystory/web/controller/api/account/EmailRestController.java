package jin.h.mun.rowdystory.web.controller.api.account;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.UpdateRequest;
import jin.h.mun.rowdystory.service.account.rowdy.AccountService;
import jin.h.mun.rowdystory.web.controller.api.common.ExceptionHandler;
import jin.h.mun.rowdystory.web.resolver.session.SessionDefine;
import jin.h.mun.rowdystory.web.resolver.session.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Slf4j
@RequestMapping( AccountAPI.EMAIL )
@RestController
public class EmailRestController extends ExceptionHandler {

    private final AccountService accountService;

    private final HttpSession httpSession;

    @GetMapping( consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<Boolean> checkDuplicate( String email ) {
        return new ResponseEntity<>( accountService.checkDuplicate( email ), HttpStatus.OK );
    }

    @PutMapping( consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<UserDTO> changeEmail( @SessionUser UserDTO userDTO, @RequestBody UpdateRequest updateRequest ) {
        UserDTO changedUserDTO = accountService.changeEmail( userDTO.getEmail(), updateRequest.getEmail() );
        httpSession.setAttribute( SessionDefine.USER.getName(), changedUserDTO );
        return new ResponseEntity<>( changedUserDTO, HttpStatus.OK );
    }
}
