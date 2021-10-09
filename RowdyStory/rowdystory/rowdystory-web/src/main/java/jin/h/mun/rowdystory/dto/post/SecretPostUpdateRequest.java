package jin.h.mun.rowdystory.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class SecretPostUpdateRequest {

    private PostUpdateRequest postUpdateRequest;

    private String password;

    private Boolean anonymous;

    @Builder
    public SecretPostUpdateRequest( PostUpdateRequest postUpdateRequest, String password, Boolean anonymous ) {
        this.postUpdateRequest = postUpdateRequest;
        this.password = password;
        this.anonymous = anonymous;
    }
}
