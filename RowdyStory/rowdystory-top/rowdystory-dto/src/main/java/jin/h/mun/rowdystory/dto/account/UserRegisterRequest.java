package jin.h.mun.rowdystory.dto.account;

import lombok.*;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {

	@NonNull
	private String email;

	@NonNull
	private String password;

	@NonNull
	private String userName;
	
	private String picture;

}
