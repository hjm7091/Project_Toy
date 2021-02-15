package jin.h.mun.knutalk.domain.board;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.domain.common.BaseField;
import jin.h.mun.knutalk.dto.post.PostRegisterDto;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "title", "content"})
@Entity
@Table(name = "tbPost")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "postType")
@DiscriminatorValue("NORMAL")
public class Post extends BaseField {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "postId")
	private Long id;
	
	@Column(name = "postTitle", nullable = false)
	private String title;
	
	@Column(name = "postContent", nullable = false)
	private String content;
	
	@Column(name = "postViewCount")
	private Integer viewCount = 0;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User owner;
	
	public static Post createPost(PostRegisterDto dto, User owner) {
		Post post = new Post();
		
		post.setTitle(dto.getTitle());
		post.setContent(dto.getContent());
		post.setCreatedAt(LocalDateTime.now());
		post.setUpdatedAt(LocalDateTime.now());
		
		post.setOwner(owner);
		owner.getPosts().add(post);
		
		return post;
	}
	
	public void changeTitle(String title) {
		this.setUpdatedAt(LocalDateTime.now());
		this.title = title;
	}
	
	public void changeContent(String content) {
		this.setUpdatedAt(LocalDateTime.now());
		this.content = content;
	}
	
	public void increaseViewCount() {
		this.viewCount++;
	}
	
}
