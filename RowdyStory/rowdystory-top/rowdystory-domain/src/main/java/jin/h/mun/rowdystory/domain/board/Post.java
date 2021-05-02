package jin.h.mun.rowdystory.domain.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.common.BaseTimeField;
import jin.h.mun.rowdystory.dto.post.PostRegisterRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter( value = AccessLevel.PROTECTED )
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@EqualsAndHashCode( of = { "id" }, callSuper = false )
@ToString( of = { "id", "title", "content" } )
@Entity
@Table( name = "tbPost" )
@Inheritance( strategy = InheritanceType.JOINED )
@DiscriminatorColumn( name = "postType" )
@DiscriminatorValue( "NORMAL" )
public class Post extends BaseTimeField {

	@Id @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "postId" )
	private Long id;
	
	@Column( name = "postTitle", nullable = false )
	private String title;
	
	@Column( name = "postContent", nullable = false )
	private String content;
	
	@Column( name = "postViewCount" )
	private Integer viewCount = 0;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "userId" )
	private User owner;
	
	@OneToMany( mappedBy = "post", cascade = CascadeType.REMOVE )
	private List<Comment> comments = new ArrayList<>();
	
	@OneToMany( mappedBy = "post" ,cascade = CascadeType.REMOVE )
	private List<ThumbUpPost> thumbUpPosts = new ArrayList<>();
	
	public Post( PostRegisterRequest request, User owner ) {
		this.title = request.getTitle();
		this.content = request.getContent();

		this.setOwner( owner );
		owner.getPosts().add( this );
	}
	
	public void changeTitle( String title ) {
		this.title = title;
	}
	
	public void changeContent( String content ) {
		this.content = content;
	}
	
	public void increaseViewCount() {
		this.viewCount++;
	}
	
	public int thumbUpCount() {
		return thumbUpPosts.size();
	}
	
}
