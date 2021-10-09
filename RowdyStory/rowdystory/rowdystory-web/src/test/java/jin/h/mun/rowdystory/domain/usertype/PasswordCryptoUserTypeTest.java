package jin.h.mun.rowdystory.domain.usertype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordCryptoUserTypeTest {

    @Test
    @DisplayName( "JPA 에 의해 호출되지 않는 메서드 직접 호출하여 값 확인" )
    public void checkMethodReturn() {
        //given
        PasswordCryptoUserType instance = PasswordCryptoUserType.getINSTANCE();
        Object object = new Object();

        //when
        Class<?> returnedClass = instance.returnedClass();
        int hashCode = instance.hashCode( object );
        Serializable disassemble = instance.disassemble( object );
        Object assemble = instance.assemble( null, object );
        Object replace = instance.replace( object, null, null );

        //then
        assertThat( returnedClass ).isEqualTo( String.class );
        assertThat( hashCode ).isEqualTo( object.hashCode() );
        assertThat( disassemble ).isNull();
        assertThat( assemble ).isNull();
        assertThat( replace ).isEqualTo( object );
    }

}