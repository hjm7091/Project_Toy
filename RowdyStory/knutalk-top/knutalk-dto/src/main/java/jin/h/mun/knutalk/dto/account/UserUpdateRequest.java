package jin.h.mun.knutalk.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserUpdateRequest {

	private String password;
	
	private String userName;
	
	private String picture;
	
	private String roleType;

	@Builder
	public UserUpdateRequest( final String password, final String userName, final String picture, final String roleType ) {
		this.password = password;
		this.userName = userName;
		this.picture = picture;
		this.roleType = roleType;
	}
}
