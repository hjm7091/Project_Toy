package jin.h.mun.rowdystory.domain.account.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SocialTypeTest {

    @Test
    @DisplayName( "getSocialType() 호출시 결과 값은 'ROLE_'로 시작해야 한다." )
    public void getSocialType() {
        //given
        SocialType google = SocialType.GOOGLE;
        SocialType facebook = SocialType.FACEBOOK;
        SocialType kakao = SocialType.KAKAO;
        SocialType naver = SocialType.NAVER;

        //when
        String googleSocialName = google.getSocialName();
        String facebookSocialName = facebook.getSocialName();
        String kakaoSocialName = kakao.getSocialName();
        String naverSocialName = naver.getSocialName();

        //then
        assertThat( googleSocialName ).startsWith( SocialType.ROLE_PREFIX );
        assertThat( facebookSocialName ).startsWith( SocialType.ROLE_PREFIX );
        assertThat( kakaoSocialName ).startsWith( SocialType.ROLE_PREFIX );
        assertThat( naverSocialName ).startsWith( SocialType.ROLE_PREFIX );
    }

    @Test
    @DisplayName( "getSocialTypeFrom() 호출시 파라미터가 유효하지 않으면 예외가 발생해야 한다." )
    public void getSocialTypeFromInvalidParameter() {
        assertThrows( NullPointerException.class, () -> SocialType.getSocialTypeFrom( null ) );
        assertThrows( InvalidParameterException.class, () -> SocialType.getSocialTypeFrom( "invalid" ) );
    }

    @Test
    @DisplayName( "getSocialTypeFrom() 호출시 파라미터가 유효하면 해당 파라미터에 맞는 소셜 타입이 반환되어야 한다." )
    public void getSocialTypeFromValidParameter() {
        assertThat( SocialType.getSocialTypeFrom( "google" ) ).isEqualTo( SocialType.GOOGLE );
        assertThat( SocialType.getSocialTypeFrom( "facebook" ) ).isEqualTo( SocialType.FACEBOOK );
        assertThat( SocialType.getSocialTypeFrom( "kakao" ) ).isEqualTo( SocialType.KAKAO );
        assertThat( SocialType.getSocialTypeFrom( "naver" ) ).isEqualTo( SocialType.NAVER );
    }
}