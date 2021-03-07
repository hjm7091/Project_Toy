package jin.h.mun.knutalk.domain.account.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleType {

    GUEST( "guest", "손님" ),
    USER( "user", "일반 사용자" ),
	ADMIN( "admin", "관리자" );

	private final String ROLE_PREFIX = "ROLE_";
    private final String name;
    private final String description;
    
	public String getRoleType() { return ROLE_PREFIX + name.toUpperCase(); }
	
	public String getValue() { return name; }
	
	public String getDescription() { return description; }
	
	public static RoleType getRoleType( final String type ) {
		
		for ( RoleType roleType : RoleType.values() ) {
			if ( roleType.getValue().equals( type ) ) {
				return roleType;
			}
		}
		
		throw new IllegalArgumentException( "invalid parameter : " + type );
	}
    
}
