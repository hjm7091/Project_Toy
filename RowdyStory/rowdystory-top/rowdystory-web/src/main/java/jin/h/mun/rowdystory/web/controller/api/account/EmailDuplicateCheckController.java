package jin.h.mun.rowdystory.web.controller.api.account;

import jin.h.mun.rowdystory.service.account.rowdy.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class EmailDuplicateCheckController {

    private final AccountService accountService;

    @GetMapping( path = AccountAPI.BASE, consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE )
    public ResponseEntity<Boolean> checkDuplicate( String email ) {
        return new ResponseEntity<>( accountService.checkDuplicate( email ), HttpStatus.OK );
    }

}
