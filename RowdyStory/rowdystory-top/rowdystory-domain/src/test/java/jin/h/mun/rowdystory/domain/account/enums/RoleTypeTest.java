package jin.h.mun.rowdystory.domain.account.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleTypeTest {

    @Test
    @DisplayName( "getRoleName 메서드 호출시 결과 값은 'ROLE_'로 시작해야 한다." )
    public void getRoleName() {
        //given
        RoleType guest = RoleType.GUEST, user = RoleType.USER, admin = RoleType.ADMIN;

        //when
        String guestRoleName = guest.getRoleName();
        String userRoleName = user.getRoleName();
        String adminRoleName = admin.getRoleName();

        //then
        assertThat( guestRoleName ).startsWith( RoleType.ROLE_PREFIX );
        assertThat( userRoleName ).startsWith( RoleType.ROLE_PREFIX );
        assertThat( adminRoleName ).startsWith( RoleType.ROLE_PREFIX );
    }

    @Test
    @DisplayName( "getRoleTypeFrom 메서드 호출시 파라미터가 유효하지 않으면 예외가 발생해야 한다." )
    public void getRoleTypeFromInvalidParameter() {
        assertThrows( NullPointerException.class, () -> RoleType.getRoleTypeFrom( null ) );
        assertThrows( InvalidParameterException.class, () -> RoleType.getRoleTypeFrom( "invalid" ) );
    }

    @Test
    @DisplayName( "getRoleTypeFrom 메서드 호출시 파라미터가 유효하면 해당 파라미터에 맞는 롤 타입이 반환되어야 한다." )
    public void getRoleTypeFromValidParameter() {
        assertThat( RoleType.getRoleTypeFrom( "guest" ) ).isEqualTo( RoleType.GUEST );
        assertThat( RoleType.getRoleTypeFrom( "user" ) ).isEqualTo( RoleType.USER );
        assertThat( RoleType.getRoleTypeFrom( "admin" ) ).isEqualTo( RoleType.ADMIN );
    }

}