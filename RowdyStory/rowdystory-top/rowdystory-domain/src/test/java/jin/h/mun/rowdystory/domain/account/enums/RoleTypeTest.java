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
        String guestRoleType = guest.getRoleType();
        String userRoleType = user.getRoleType();
        String adminRoleType = admin.getRoleType();

        //then
        assertThat( guestRoleType ).startsWith( RoleType.ROLE_PREFIX );
        assertThat( userRoleType ).startsWith( RoleType.ROLE_PREFIX );
        assertThat( adminRoleType ).startsWith( RoleType.ROLE_PREFIX );
    }

}