package jin.h.mun.rowdystory.crypto.sha256;

import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
public class SHA256Util {

    private final String ALGORITHM;

    public String encrypt( String str ) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance( ALGORITHM );
            messageDigest.update( str.getBytes( StandardCharsets.UTF_8 ) );
            return bytesToHex( messageDigest.digest() );
        } catch ( NoSuchAlgorithmException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    private String bytesToHex( byte[] bytes ) {
        StringBuilder builder = new StringBuilder();
        for ( byte b : bytes ) {
            builder.append( String.format( "%02x", b ) );
        }
        return builder.toString();
    }
}
