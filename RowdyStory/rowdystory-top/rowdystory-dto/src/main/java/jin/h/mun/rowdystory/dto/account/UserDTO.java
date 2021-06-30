package jin.h.mun.rowdystory.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDTO {

    private final Long id;
    private final String email;
    private final String userName;
    private final String picture;

}
