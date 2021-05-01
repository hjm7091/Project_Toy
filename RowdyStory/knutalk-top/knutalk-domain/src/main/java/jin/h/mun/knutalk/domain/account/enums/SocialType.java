package jin.h.mun.knutalk.domain.account.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SocialType {
	
	GOOGLE( "google" ),
	FACEBOOK( "facebook" ),
	KAKAO( "kakao" ),
	NAVER( "naver" );
	
    private final String ROLE_PREFIX = "ROLE_";
    private final String name;

    public String getSocialType() { return ROLE_PREFIX + name.toUpperCase(); }

    public String getValue() { return name; }
    
    public static SocialType getSocialType( final String type ) {
    	
    	for ( SocialType socialType : SocialType.values() ) {
    		if ( socialType.getValue().equals( type ) ) {
    			return socialType;
    		}
    	}
    	
    	throw new IllegalArgumentException( "invalid parameter : " + type );
    }

    public boolean isEquals( final String authority ) {
        return this.name.equals( authority );
    }
	
}
