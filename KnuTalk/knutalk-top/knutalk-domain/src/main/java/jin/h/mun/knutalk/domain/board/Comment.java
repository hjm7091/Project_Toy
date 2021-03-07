package jin.h.mun.knutalk.domain.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.domain.common.BaseField;
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
public class Comment extends BaseField {

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
	
	@Builder
	public Comment( User writer, Post targetPost, String content ) {
		
		this.setWriter( writer );
		this.setPost( targetPost );
		this.setContent( content );
		
		targetPost.getComments().add( this );
		
	}
}
