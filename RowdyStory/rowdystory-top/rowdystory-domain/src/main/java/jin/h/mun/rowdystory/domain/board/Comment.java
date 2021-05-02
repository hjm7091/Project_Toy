package jin.h.mun.rowdystory.domain.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.common.BaseTimeField;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter( value = AccessLevel.PRIVATE )
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@EqualsAndHashCode( of = { "id" }, callSuper = false )
@ToString( of = { "id", "content" } )
@Entity
@Table( name = "tbComment" )
public class Comment extends BaseTimeField {

	@Id @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "commentId" )
	private Long id;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "userId" )
	private User writer;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "postId" )
	private Post post;
	
	@Column( name = "commentContent", nullable = false )
	private String content;
	
	@OneToMany( mappedBy = "comment", cascade = CascadeType.REMOVE )
	private List<ThumbUpComment> thumbUpComments = new ArrayList<>();
	
	@Builder
	public Comment( User writer, Post targetPost, String content ) {
		
		this.setWriter( writer );
		this.setPost( targetPost );
		this.setContent( content );
		
		targetPost.getComments().add( this );
		
	}
	
	public int thumbUpCount() {
		return thumbUpComments.size();
	}
}
