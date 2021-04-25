package jin.h.mun.knutalk.domain.account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import jin.h.mun.knutalk.domain.account.enums.RoleType;
import jin.h.mun.knutalk.domain.account.enums.SocialType;
import jin.h.mun.knutalk.domain.board.Post;
import jin.h.mun.knutalk.domain.common.BaseField;
import jin.h.mun.knutalk.dto.account.UserRegisterRequest;
import jin.h.mun.knutalk.dto.account.UserUpdateRequest;
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
@ToString( of = { "id", "email", "userName" } )
@Entity
@Table( name = "tbUser", uniqueConstraints = @UniqueConstraint( columnNames = { "userId", "userEmail" } ))
public class User extends BaseField {

	@Id @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "userId" )
	private Long id;
	
	@Column( name = "userEmail", nullable = false )
	private String email;
	
	@Column( name = "userPassword" )
	private String password;
	
	@Column( name = "userName" )
	private String userName;
	
	@Column( name = "userPicture" )
	private String picture;
	
	@Column( name = "userSocialType" )
	@Enumerated( EnumType.STRING )
	private SocialType socialType;
	
    @Column( name = "userRoleType" )
    @Enumerated( EnumType.STRING )
    private RoleType roleType;
	
	@OneToMany( mappedBy = "owner", cascade = CascadeType.REMOVE )
	private List<Post> posts = new ArrayList<>();
	
	public User( UserRegisterRequest request ) {
		this.email = request.getEmail();
		this.password = request.getPassword();
		this.userName = request.getUserName();
		this.picture = request.getPicture();
		this.roleType = RoleType.USER;
		this.setCreatedAt( LocalDateTime.now() );
		this.setUpdatedAt( LocalDateTime.now() );
	}
	
	@Builder
	public User( String email, String password, String userName, String picture, SocialType socialType, RoleType roleType ) {
		this.email = email;
		this.password = password;
		this.userName = userName;
		this.picture = picture;
		this.socialType = socialType;
		this.roleType = roleType;
		this.setCreatedAt( LocalDateTime.now() );
		this.setUpdatedAt( LocalDateTime.now() );
	}
		
	public User changePassword( final String password ) {
		if ( password != null ) {
			this.setUpdatedAt( LocalDateTime.now() );
			this.password = password;
		}
		return this;
	}
	
	public User changeUserName( final String userName ) {
		if ( userName != null ) {
			this.setUpdatedAt( LocalDateTime.now() );
			this.userName = userName;
		}
		return this;
	}
	
	public User changeRoleType( final RoleType roleType ) {
		if ( roleType != null ) {
			this.setUpdatedAt( LocalDateTime.now() );
			this.roleType = roleType;
		}
		return this;
	}
	
	public User changePicture( final String pictrue ) {
		if ( pictrue != null ) {
			this.setUpdatedAt( LocalDateTime.now() );
			this.picture = pictrue;
		}
		return this;
	}
	
	public void update( final UserUpdateRequest request ) {
		changePassword( request.getPassword() );
		changeUserName( request.getUserName() );
		changePicture( request.getPicture() );
		changeRoleType( RoleType.getRoleType( request.getRoleType() ) );
	}
}
