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
import jin.h.mun.rowdystory.dto.post.PostUpdateRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
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
	protected Long id;
	
	@Column( name = "postTitle", nullable = false )
	protected String title;
	
	@Column( name = "postContent", nullable = false )
	protected String content;
	
	@Column( name = "postViewCount" )
	protected Integer viewCount = 0;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "userId" )
	protected User owner;
	
	@OneToMany( mappedBy = "post", cascade = CascadeType.REMOVE )
	protected final List<Comment> comments = new ArrayList<>();
	
	@OneToMany( mappedBy = "post" ,cascade = CascadeType.REMOVE )
	protected final List<ThumbUpPost> thumbUpPosts = new ArrayList<>();
	
	public Post( PostRegisterRequest request, User owner ) {
		this.title = request.getTitle();
		this.content = request.getContent();
		this.owner = owner;
		owner.getPosts().add( this );
	}
	
	public Post changeTitle( String title ) {
		if ( title != null ) {
			this.title = title;
		}
		return this;
	}
	
	public Post changeContent( String content ) {
		if ( content != null ) {
			this.content = content;
		}
		return this;
	}
	
	public void increaseViewCount() {
		this.viewCount++;
	}
	
	public int thumbUpCount() {
		return thumbUpPosts.size();
	}

	public void update( PostUpdateRequest postUpdateRequest ) {
		changeTitle( postUpdateRequest.getTitle() );
		changeContent(postUpdateRequest.getContent() );
	}
}
