package jin.h.mun.rowdystory.crypto.sha256;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SHA256UtilTest {

    @Test
    @DisplayName( "sha256 암호화 테스트" )
    public void encrypt() throws Exception {
        String firstTrying = SHA256Util.encrypt( "test" );
        String secondTrying = SHA256Util.encrypt( "test" );
        assertThat( firstTrying ).isEqualTo( secondTrying );
    }

}