package jin.h.mun.knutalk.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserRegisterDto {

	private String email;
	
	private String password;
	
	private String nickName;

	@Builder
	public UserRegisterDto(String email, String password, String nickName) {
		this.email = email;
		this.password = password;
		this.nickName = nickName;
	}
}
