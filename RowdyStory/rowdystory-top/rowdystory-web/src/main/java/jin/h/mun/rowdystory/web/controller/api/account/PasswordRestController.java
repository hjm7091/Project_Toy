package jin.h.mun.rowdystory.web.controller.api.account;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.PasswordUpdateRequest;
import jin.h.mun.rowdystory.service.account.rowdy.AccountService;
import jin.h.mun.rowdystory.web.controller.api.common.ExceptionHandler;
import jin.h.mun.rowdystory.web.resolver.session.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RequestMapping( AccountAPI.PASSWORD )
@RestController
public class PasswordRestController extends ExceptionHandler {

    private final AccountService accountService;

    @PutMapping( consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<UserDTO> changePassword( @SessionUser UserDTO userDTO, @RequestBody PasswordUpdateRequest passwordUpdateRequest ) {
        accountService.changePassword( userDTO.getEmail(), passwordUpdateRequest.getBefore(), passwordUpdateRequest.getAfter() );
        return ResponseEntity.ok( userDTO );
    }
}
