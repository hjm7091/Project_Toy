package jin.h.mun.rowdystory.web.controller.api.advice;

import jin.h.mun.rowdystory.service.util.ReflectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Type;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RequestBodyFieldEncodeAdvice extends org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter {

    private final PasswordEncoder passwordEncoder;

    private static final String[] targetFieldNames = { "password" };

    @Override
    public boolean supports( MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass ) {
        return true;
    }

    @Override
    public Object afterBodyRead( Object target, HttpInputMessage inputMessage, MethodParameter parameter,
                                 Type targetType, Class<? extends HttpMessageConverter<?>> converterType ) {
        encode( target );
        return target;
    }

    private void encode( Object target ) {
        for ( String fieldName : targetFieldNames ) {
            if ( ReflectionUtil.checkFieldExistence( fieldName, target ) ) {
                Class<?> fieldType = ReflectionUtil.getFieldType( fieldName, target );
                if ( fieldType == String.class ) {
                    String value = ( String ) ReflectionUtil.invokeGetterMethod( fieldName, target );
                    if ( value != null ) {
                        ReflectionUtil.changeField( fieldName, target, passwordEncoder.encode( value ) );
                    }
                }
            }
        }
    }


}
