package jin.h.mun.rowdystory.domain.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleType {

    GUEST( "guest", "손님" ),
    USER( "user", "일반 사용자" ),
	ADMIN( "admin", "관리자" );

	public final static String ROLE_PREFIX = "ROLE_";

	@Getter
    private final String name;

	@Getter
	private final String description;
    
	public String getRoleName() { return ROLE_PREFIX + name.toUpperCase(); }
	
	public static RoleType getRoleTypeFrom( final String type ) {
		
		for ( RoleType roleType : RoleType.values() ) {
			if ( roleType.getName().equals( type ) ) {
				return roleType;
			}
		}

		return null;
	}
    
}
