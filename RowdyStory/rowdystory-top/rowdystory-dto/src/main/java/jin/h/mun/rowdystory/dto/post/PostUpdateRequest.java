package jin.h.mun.rowdystory.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PostUpdateRequest {

    private String title;

    private String content;

    @Builder
    public PostUpdateRequest( String title, String content ) {
        this.title = title;
        this.content = content;
    }
}
