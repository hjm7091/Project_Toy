package jin.h.mun.rowdystory.service.account.social.attribute;

import java.util.Map;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class OAuth2Attributes {

    private final Map<String, Object> attributes;
    private final String registrationId;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;

    @Builder
    public OAuth2Attributes( Map<String, Object> attributes, String nameAttributeKey, String registrationId, String name, String email, String picture ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.registrationId = registrationId;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuth2Attributes of( final String registrationId, final String userNameAttributeName, final Map<String, Object> attributes ) {
    	
    	log.info( "registrationId : {}, userNameAttributeName : {}, attributes : {}", registrationId, userNameAttributeName, attributes );
        
    	if( registrationId.equals( "naver" ) ) {
            return ofNaver( registrationId, userNameAttributeName, attributes );
        } else if ( registrationId.equals( "kakao" ) ) {
        	return ofKakao( registrationId, userNameAttributeName, attributes );
        }
    	
        return ofCommon( registrationId, userNameAttributeName, attributes );
    }

    /*
     * google 데이터 예시
     * registrationId : google, userNameAttributeName : sub
     * attributes : {
			sub=107583451936747689804, 
			name=문학진, 
			given_name=학진, 
			family_name=문, 
			picture=https://lh6.googleusercontent.com/-XGrs1zwfLWI/AAAAAAAAAAI/AAAAAAAAAAA/AMZuucnqmGuPvz7uaMAzeYMn3_BjjfJ5qg/s96-c/photo.jpg, 
			email=hjm5014@gmail.com, 
			email_verified=true, 
			locale=ko
		}
		
		facebook 데이터 예시
		registrationId : facebook, userNameAttributeName : id
		attributes : {
			id=427083948366230, 
			name=Hak Jin Mun, 
			email=hjm7091@naver.com
		}
     */
    private static OAuth2Attributes ofCommon( final String registrationId, final String userNameAttributeName, final Map<String, Object> attributes ) {
        return OAuth2Attributes.builder()
                .name( ( String ) attributes.get( "name" ) )
                .email( ( String ) attributes.get( "email" ) )
                .picture( ( String ) attributes.get( "picture" ) )
                .attributes( attributes )
                .nameAttributeKey( userNameAttributeName )
                .registrationId( registrationId )
                .build();
    }

    /*
     * naver 데이터 예시
     * registrationId : naver, userNameAttributeName : response
     * attributes : {
     * 		resultcode=00, 
     * 		message=success, 
     * 		response={
     * 			id=71181210, 
     * 			profile_image=https://ssl.pstatic.net/static/pwe/address/img_profile.png, 
     * 			email=hjm7091@naver.com,
     * 			name=문학진
     * 		}
     * }
     */
    @SuppressWarnings( "unchecked" )
    private static OAuth2Attributes ofNaver( final String registrationId, final String userNameAttributeName, final Map<String, Object> attributes ) {
    	
		Map<String, Object> response = ( Map<String, Object> ) attributes.get( "response" );

        return OAuth2Attributes.builder()
                .name( ( String ) response.get( "name" ) )
                .email( ( String ) response.get( "email" ) )
                .picture( ( String ) response.get( "profile_image" ) )
                .attributes( attributes )
                .nameAttributeKey( userNameAttributeName )
                .registrationId( registrationId )
                .build();
    }
    
    /*
     * kakao 데이터 예시
     * registrationId : kakao, userNameAttributeName : id 
     * attributes : {
			id=1628781477, 
			connected_at=2021-02-14T09:54:12Z, 
			properties={nickname=문학진, profile_image=http://k.kakaocdn.net/dn/HOzH1/btqBYqCK32G/NHXwkehumAIX7G0RYHRB3K/img_640x640.jpg, thumbnail_image=http://k.kakaocdn.net/dn/HOzH1/btqBYqCK32G/NHXwkehumAIX7G0RYHRB3K/img_110x110.jpg}, 
			kakao_account={
				profile_needs_agreement=false, 
				profile={nickname=문학진, thumbnail_image_url=http://k.kakaocdn.net/dn/HOzH1/btqBYqCK32G/NHXwkehumAIX7G0RYHRB3K/img_110x110.jpg, profile_image_url=http://k.kakaocdn.net/dn/HOzH1/btqBYqCK32G/NHXwkehumAIX7G0RYHRB3K/img_640x640.jpg},
				has_email=true, 
				email_needs_agreement=false, 
				is_email_valid=true, 
				is_email_verified=true,
				email=hjm7091@naver.com
			}
		}
		kakao_account의 profile에 thumbnail_image_url, profile_image_url이 없을 수 있다.
     */
    @SuppressWarnings( "unchecked" )
    private static OAuth2Attributes ofKakao( final String registrationId, final String userNameAttributeName, final Map<String, Object> attributes ) {
    	
    	Map<String, Object> kakaoAccount = ( Map<String, Object> ) attributes.get( "kakao_account" );
    	Map<String, Object> profile = ( Map<String, Object> ) kakaoAccount.get( "profile" );
        Map<String, Object> properties = ( Map<String, Object> ) attributes.get( "properties" );

        return OAuth2Attributes.builder()
    			.name( ( String ) profile.get( "nickname" ) )
    			.email( ( String ) kakaoAccount.get( "email" ) )
    			.picture( ( String ) profile.getOrDefault( "profile_image_url", properties.get( "profile_image" ) ) )
    			.attributes( attributes )
    			.nameAttributeKey( userNameAttributeName )
    			.registrationId( registrationId )
    			.build();
    }

    public User toUserEntity() {
        return User.builder()
                .userName( name )
                .email( email )
                .picture( picture )
                .socialType( SocialType.getSocialTypeFrom( registrationId ) )
                .roleType( RoleType.USER )
                .build();
    }

}
