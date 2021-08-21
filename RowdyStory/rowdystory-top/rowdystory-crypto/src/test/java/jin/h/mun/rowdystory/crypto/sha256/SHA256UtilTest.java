package jin.h.mun.rowdystory.crypto.sha256;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SHA256UtilTest {

    @Test
    @DisplayName( "sha256 암호화 테스트 (유효한 알고리즘)" )
    public void encryptUsingValidAlgorithm() {
        //given
        SHA256Util sha256Util = new SHA256Util( "SHA-256" );

        //when
        String firstTrying = sha256Util.encrypt( "test" );
        String secondTrying = sha256Util.encrypt( "test" );

        //then
        assertThat( firstTrying ).isEqualTo( secondTrying );
    }

    @Test
    @DisplayName( "sha256 암호화 테스트 (유효하지 않은 알고리즘)" )
    public void encryptUsingInValidAlgorithm() {
        //given
        SHA256Util sha256Util = new SHA256Util( "INVALID" );

        //when
        assertThrows( RuntimeException.class, () -> sha256Util.encrypt( "test" ) );
    }

}