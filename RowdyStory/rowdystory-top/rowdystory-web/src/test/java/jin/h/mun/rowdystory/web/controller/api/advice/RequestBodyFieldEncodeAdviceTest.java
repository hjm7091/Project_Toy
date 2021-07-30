package jin.h.mun.rowdystory.web.controller.api.advice;

import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBodyFieldEncodeAdviceTest {

    @Test
    @DisplayName( "객체에 password 필드가 없는 경우 아무일도 일어나지 않음." )
    public void nonExistPasswordField() {
        //given
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        RequestBodyFieldEncodeAdvice requestBodyFieldEncodeAdvice = new RequestBodyFieldEncodeAdvice( passwordEncoder );
        class TestClass { @Getter final String field = "field"; }

        //when
        TestClass testClass = ( TestClass ) requestBodyFieldEncodeAdvice.afterBodyRead( new TestClass(), null, null, null, null );

        //then
        assertThat( testClass.getField() ).isEqualTo( "field" );
    }

    @Test
    @DisplayName( "객체에 password 필드가 있지만 String 타입이 아닌 경우 아무일도 일어나지 않음." )
    public void existPasswordFieldButNotString() {
        //given
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        RequestBodyFieldEncodeAdvice requestBodyFieldEncodeAdvice = new RequestBodyFieldEncodeAdvice( passwordEncoder );
        class TestClass { @Getter final int password = 123; }

        //when
        TestClass testClass = ( TestClass ) requestBodyFieldEncodeAdvice.afterBodyRead( new TestClass(), null, null, null, null );

        //then
        assertThat( testClass.getPassword() ).isEqualTo( 123 );
    }

}