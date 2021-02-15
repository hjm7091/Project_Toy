package jin.h.mun.knutalk.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PostRegisterDto {

	private String title;
	
	private String content;

	@Builder
	public PostRegisterDto(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
