package jin.h.mun.rowdystory.dto.account.view;

import lombok.*;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginForm {

    private String email;
    private String password;

}
