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
import jin.h.mun.knutalk.domain.common.BaseTimeField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter( value = AccessLevel.PRIVATE )
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@Entity
@Table( name = "tbThumbUpComment" )
public class ThumbUpComment extends BaseTimeField {

	@Id @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "thumbUpCommentId" )
	private Long id;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "userId" )
	private User user;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "commentId" )
	private Comment comment;

	public ThumbUpComment( User user, Comment comment ) {
		this.user = user;
		this.comment = comment;
		comment.getThumbUpComments().add( this );
	}
	
}
