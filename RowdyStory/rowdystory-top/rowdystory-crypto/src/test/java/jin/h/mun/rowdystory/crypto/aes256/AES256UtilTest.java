package jin.h.mun.rowdystory.crypto.aes256;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AES256UtilTest {

    @Test
    @DisplayName( "암복호화 테스트" )
    public void encryptAndDecrypt() {
        //given
        String key = "key", msg = "msg";

        //when
        String encrypted = AES256Util.encrypt( msg, key );
        String decrypted = AES256Util.decrypt( encrypted, key );

        //then
        assertThat( decrypted ).isEqualTo( msg );
    }

    @Test
    @DisplayName( "salt 를 사용하므로 동일한 값을 암호화해도 암호화된 값이 동일하지 않음" )
    public void encryptTwice() {
        //given
        String key = "key";
        String msg = "msg";

        //when
        String encrypted1 = AES256Util.encrypt( msg, key );
        String encrypted2 = AES256Util.encrypt( msg, key );

        //then
        assertThat( encrypted1 ).isNotEqualTo( encrypted2 );
    }

    @Test
    @DisplayName( "복호화시 잘못된 키를 사용하면 에러 발생" )
    public void decryptWithWrongKey() {
        //given
        String key1 = "key1", key2 = "key2";
        String msg = "msg";

        //when
        String encrypted = AES256Util.encrypt( msg, key1 );
        String message = assertThrows( RuntimeException.class, () -> AES256Util.decrypt( encrypted, key2 ) ).getMessage();

        //then
        assertThat( message ).contains( "Given final block not properly padded" );
    }

    @Test
    @DisplayName( "미리 암호화된 값 복호화 테스트" )
    public void decryptPreEncryptedStr() {
        //given
        String encryptedStr1 = "qNgbzb3vMh0WlkjNIlxLjudADMV6O5yo7SJAtSvzSS1yjA3Rh9f5Uhw1GBuAa26Vena31Q==";
        String encryptedStr2 = "tfEn9b9J8xYeRcG4MjSUjfVHuPvi0T26L3ssxTK1LjqSHWYkPj5exZf38hOwna1TEwGgvQ==";
        String key = "key";

        //when
        String decryptedStr1 = AES256Util.decrypt( encryptedStr1, key );
        String decryptedStr2 = AES256Util.decrypt( encryptedStr2, key );

        //then
        assertThat( decryptedStr1 ).isEqualTo( "msg" );
        assertThat( decryptedStr2 ).isEqualTo( "msg" );
    }
}