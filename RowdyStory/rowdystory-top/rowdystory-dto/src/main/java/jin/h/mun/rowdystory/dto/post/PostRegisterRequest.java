package jin.h.mun.rowdystory.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PostRegisterRequest {

	@NonNull
	private String title;
	
	@NonNull
	private String content;

	@Builder
	public PostRegisterRequest( String title, String content ) {
		this.title = title;
		this.content = content;
	}
}
