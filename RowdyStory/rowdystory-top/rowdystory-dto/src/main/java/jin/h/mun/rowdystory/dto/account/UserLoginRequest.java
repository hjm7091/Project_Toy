package jin.h.mun.rowdystory.dto.account;

import lombok.*;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest {

    private String email;
    private String password;

}
