package jin.h.mun.rowdystory.web.resolver.session;

import jin.h.mun.rowdystory.dto.account.UserDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SessionDefine {

    USER( "user", UserDTO.class );

    private final String name;

    private final Class<?> type;

}
