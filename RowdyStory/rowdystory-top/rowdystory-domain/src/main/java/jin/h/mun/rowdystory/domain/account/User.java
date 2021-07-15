package jin.h.mun.rowdystory.domain.account;

import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.domain.board.Post;
import jin.h.mun.rowdystory.domain.common.BaseTimeField;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@EqualsAndHashCode( of = { "id", "email" }, callSuper = false )
@ToString( of = { "id", "email", "userName" } )
@Entity
@Table(
	name = "tbUser",
	uniqueConstraints = {
		@UniqueConstraint( columnNames = { "userEmail" } )
	},
	indexes = {
		@Index( name = "i_userEmail", columnList = "userEmail" )
	}
)
public class User extends BaseTimeField {

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
	
	@Column( name = "userSocialType", updatable = false )
	@Enumerated( EnumType.STRING )
	private SocialType socialType;
	
    @Column( name = "userRoleType" )
    @Enumerated( EnumType.STRING )
    private RoleType roleType;
	
	@OneToMany( mappedBy = "owner", cascade = CascadeType.REMOVE )
	private final List<Post> posts = new ArrayList<>();
	
	public User( UserRegisterRequest request ) {
		this.email = request.getEmail();
		this.password = request.getPassword();
		this.userName = request.getUserName();
		this.roleType = RoleType.USER;
	}
	
	@Builder
	public User( Long id, String email, String password, String userName, String picture, SocialType socialType, RoleType roleType ) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.userName = userName;
		this.picture = picture;
		this.socialType = socialType;
		this.roleType = roleType;
	}
		
	public User changePassword( @NonNull final String password ) {
		this.password = password;
		return this;
	}
	
	public User changeUserName( @NonNull final String userName ) {
		this.userName = userName;
		return this;
	}
	
	public User changeRoleType( @NonNull final RoleType roleType ) {
		this.roleType = roleType;
		return this;
	}
	
	public User changePicture( @NonNull final String picture ) {
		this.picture = picture;
		return this;
	}
	
	public User update( final UserUpdateRequest request ) {
		changePassword( request.getPassword() );
		changeUserName( request.getUserName() );
		changePicture( request.getPicture() );
		changeRoleType( RoleType.getRoleTypeFrom( request.getRoleType() ) );
		return this;
	}

	public boolean isSocialUser() {
		return this.socialType != null;
	}

	public UserDTO toDTO() {
		return UserDTO.builder()
				.id( this.id )
				.email( this.email )
				.userName( this.userName )
				.picture( this.picture )
				.build();
	}
}
