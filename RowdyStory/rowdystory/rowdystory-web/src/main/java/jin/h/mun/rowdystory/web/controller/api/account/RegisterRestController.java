package jin.h.mun.rowdystory.web.controller.api.account;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.api.RegisterRequest;
import jin.h.mun.rowdystory.service.account.rowdy.CommonCRUDService;
import jin.h.mun.rowdystory.web.controller.api.common.ExceptionHandler;
import jin.h.mun.rowdystory.web.controller.view.account.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping( AccountAPI.REGISTER )
public class RegisterRestController extends ExceptionHandler {

    private final CommonCRUDService commonCRUDService;

    @PostMapping( produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<UserDTO> register( @RequestBody RegisterRequest registerRequest ) throws Exception {
        UserDTO userDTO = commonCRUDService.register( registerRequest );
        return ResponseEntity.created( URI.create( Account.AccountMapping.LOGIN ) ).body( userDTO );
    }

}
