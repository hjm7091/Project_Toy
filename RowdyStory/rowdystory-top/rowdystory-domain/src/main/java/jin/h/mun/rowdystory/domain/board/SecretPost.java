package jin.h.mun.rowdystory.domain.board;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.post.SecretPostRegisterRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter( value = AccessLevel.PRIVATE )
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@Entity
@Table( name = "tbSecretPost" )
@DiscriminatorValue( "SECRET" )
public class SecretPost extends Post {

	@Column( name = "postPassword", nullable = false )
	String password;
	
	@Column( name = "postAnonymous", nullable = false )
	Boolean anonymous;
	
	public SecretPost( SecretPostRegisterRequest request, User owner ) {
		this.setTitle( request.getPostRegisterRequest().getTitle() );
		this.setContent( request.getPostRegisterRequest().getContent() );
		this.setPassword( request.getPassword() );
		this.setAnonymous( request.getAnonymous() );

		this.setOwner( owner );
		owner.getPosts().add( this );
	}
	
	public void changePassword( String password ) {
		this.password = password;
	}
}