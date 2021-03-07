package jin.h.mun.knutalk.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserRegisterRequest {

	@NonNull
	private String email;
	
	private String password;
	
	private String userName;
	
	private String picture;

	@Builder
	public UserRegisterRequest( final String email, final String password, final String userName, final String picture ) {
		this.email = email;
		this.password = password;
		this.userName = userName;
		this.picture = picture;
	}
}
