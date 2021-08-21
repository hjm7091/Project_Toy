package jin.h.mun.rowdystory.web.controller.api.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler( Exception.class )
    public ResponseEntity<String> handle( Exception e ) {
        String error = String.format( "class : %s, message : %s", e.getClass().getSimpleName(), e.getMessage() );
        log.error( "error : {}", error );
        return new ResponseEntity<>( error, HttpStatus.INTERNAL_SERVER_ERROR );
    }

}
