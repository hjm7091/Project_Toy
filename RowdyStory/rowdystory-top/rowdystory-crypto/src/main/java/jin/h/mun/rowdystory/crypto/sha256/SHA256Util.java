package jin.h.mun.rowdystory.crypto.sha256;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Util {

    private SHA256Util() {}

    public static String encrypt( String str ) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance( "SHA-256" );
        messageDigest.update( str.getBytes( StandardCharsets.UTF_8 ) );
        return bytesToHex( messageDigest.digest() );
    }

    private static String bytesToHex( byte[] bytes ) {
        StringBuilder builder = new StringBuilder();
        for ( byte b : bytes ) {
            builder.append( String.format( "%02x", b ) );
        }
        return builder.toString();
    }
}
