package jin.h.mun.knutalk.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SecretPostRegisterDto {

	PostRegisterDto postRegisterDto;
	
	String password;
	
	Boolean anonymous;

	@Builder
	public SecretPostRegisterDto(PostRegisterDto postRegisterDto, String password, Boolean anonymous) {
		this.postRegisterDto = postRegisterDto;
		this.password = password;
		this.anonymous = anonymous;
	}
}
