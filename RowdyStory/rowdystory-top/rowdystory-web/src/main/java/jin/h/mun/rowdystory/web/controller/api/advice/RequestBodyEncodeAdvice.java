package jin.h.mun.rowdystory.web.controller.api.advice;

import jin.h.mun.rowdystory.dto.annotation.Encoding;
import jin.h.mun.rowdystory.service.util.ReflectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RequestBodyEncodeAdvice extends org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports( MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass ) {
        return true;
    }

    @Override
    public Object afterBodyRead( Object target, HttpInputMessage inputMessage, MethodParameter parameter,
                                 Type targetType, Class<? extends HttpMessageConverter<?>> converterType ) {
        encrypt( target );
        return target;
    }

    private void encrypt( Object target ) {
        Field[] fields = target.getClass().getDeclaredFields();
        for ( Field field : fields ) {
            if ( shouldBeEncrypted( field ) && field.getType() == String.class ) {
                String value = ( String ) ReflectionUtil.invokeGetterMethod( field.getName(), target );
                if ( value != null ) {
                    ReflectionUtil.changeField( field.getName(), target, passwordEncoder.encode( value ) );
                }
            }
        }
    }

    private boolean shouldBeEncrypted( Field field ) {
        return field.getAnnotation( Encoding.class ) != null;
    }

}
