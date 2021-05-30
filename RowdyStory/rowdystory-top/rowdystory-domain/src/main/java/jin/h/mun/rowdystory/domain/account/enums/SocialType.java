package jin.h.mun.rowdystory.domain.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SocialType {
	
	GOOGLE( "google" ),
	FACEBOOK( "facebook" ),
	KAKAO( "kakao" ),
	NAVER( "naver" );
	
    public final static String ROLE_PREFIX = "ROLE_";

    @Getter
    private final String name;

    public String getSocialName() { return ROLE_PREFIX + name.toUpperCase(); }

    public static SocialType getSocialTypeFrom( final String type ) {
    	
    	for ( SocialType socialType : SocialType.values() ) {
    		if ( socialType.getName().equals( type ) ) {
    			return socialType;
    		}
    	}
    	
    	throw new IllegalArgumentException( "invalid parameter : " + type );
    }

}
