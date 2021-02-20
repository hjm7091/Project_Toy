package jin.h.mun.knutalk.domain.board;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.dto.post.SecretPostRegisterDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter(value = AccessLevel.PRIVATE)
@Entity
@Table(name = "tbSecretPost")
@DiscriminatorValue("SECRET")
public class SecretPost extends Post {

	@Column(name = "postPassword", nullable = false)
	String password;
	
	@Column(name = "postAnonymous", nullable = false)
	Boolean anonymous;
	
	public static SecretPost createPost(SecretPostRegisterDto dto, User owner) {
		SecretPost secretPost = new SecretPost();

		secretPost.setTitle(dto.getPostRegisterDto().getTitle());
		secretPost.setContent(dto.getPostRegisterDto().getContent());
		secretPost.setPassword(dto.getPassword());
		secretPost.setAnonymous(dto.getAnonymous());
		secretPost.setCreatedAt(LocalDateTime.now());
		secretPost.setUpdatedAt(LocalDateTime.now());
		
		secretPost.setOwner(owner);
		owner.getPosts().add(secretPost);
		
		return secretPost;
	}
	
	public void changePassword(String password) {
		this.setUpdatedAt(LocalDateTime.now());
		this.password = password;
	}
}
