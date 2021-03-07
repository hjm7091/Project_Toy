package jin.h.mun.knutalk.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SecretPostRegisterRequest {

	private PostRegisterRequest postRegisterRequest;
	
	private String password;
	
	private Boolean anonymous;

	@Builder
	public SecretPostRegisterRequest( PostRegisterRequest postRegisterRequest, String password, Boolean anonymous ) {
		this.postRegisterRequest = postRegisterRequest;
		this.password = password;
		this.anonymous = anonymous;
	}
}
