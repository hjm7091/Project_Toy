package jin.h.mun.rowdystory.domain.board;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.post.SecretPostRegisterRequest;
import jin.h.mun.rowdystory.dto.post.SecretPostUpdateRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
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
		this.title = request.getPostRegisterRequest().getTitle();
		this.content = request.getPostRegisterRequest().getContent();
		this.password = request.getPassword();
		this.anonymous = request.getAnonymous();
		this.owner = owner;
		owner.getPosts().add( this );
	}
	
	public SecretPost changePassword( String password ) {
		if ( password != null ) {
			this.password = password;
		}
		return this;
	}

	public SecretPost changeAnonymous( Boolean anonymous ) {
		if ( anonymous != null ) {
			this.anonymous = anonymous;
		}
		return this;
	}

	public void update( SecretPostUpdateRequest secretPostUpdateRequest ) {
		super.update( secretPostUpdateRequest.getPostUpdateRequest() );
		changePassword( secretPostUpdateRequest.getPassword() );
		changeAnonymous( secretPostUpdateRequest.getAnonymous() );
	}
}
