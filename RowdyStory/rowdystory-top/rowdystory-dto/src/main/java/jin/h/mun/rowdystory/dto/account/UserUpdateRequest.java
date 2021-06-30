package jin.h.mun.rowdystory.dto.account;

import lombok.*;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

	private String password;
	
	private String userName;
	
	private String picture;
	
	private String roleType;
}
