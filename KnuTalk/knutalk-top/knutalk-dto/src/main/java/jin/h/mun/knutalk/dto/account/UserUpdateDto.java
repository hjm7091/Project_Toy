package jin.h.mun.knutalk.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UserUpdateDto {

	private String password;
	
	private String nickName;
	
	private String userLevel;

	@Builder
	public UserUpdateDto(String password, String nickName, String userLevel) {
		this.password = password;
		this.nickName = nickName;
		this.userLevel = userLevel;
	}
}
