package jin.h.mun.rowdystory.dto.account.api;

import jin.h.mun.rowdystory.dto.annotation.Encoding;
import lombok.*;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

	private String email;

	@Encoding
	private String password;

	private String userName;

}
