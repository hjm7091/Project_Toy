package jin.h.mun.rowdystory.domain.account.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTypeTest {

    @Test
    @DisplayName( "getRoleType() 호출시 결과 값은 'ROLE_'로 시작해야 한다." )
    public void getRoleType() {
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

}