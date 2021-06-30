package jin.h.mun.rowdystory.domain.account;

import jin.h.mun.rowdystory.domain.PersistHelper;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.dto.account.UserDTO;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.account.UserUpdateRequest;
import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
	
	private static PersistHelper persistHelper;
	
	private User user;
	
	@BeforeAll
	public static void setUpBeforeClass() {
		persistHelper = new PersistHelper();
	}
	
	@AfterAll
	public static void tearDownAfterClass() {
		persistHelper.closeAll();
	}
	
	@BeforeEach
	public void setUp() {
		user = new User( UserRegisterRequest.builder()
				   .email( "hjm7091@naver.com" )
				   .password( "1234" )
				   .userName( "jin" )
				   .picture( "picture1" )
				   .build() );

		persistHelper.persist( user );
	}
	
	@AfterEach
	public void tearDown() {
		persistHelper.deleteAll( User.class );
		assertThat( persistHelper.countRow( User.class ) ).isEqualTo( 0 );
	}
	
	@Test
	@DisplayName( "persist 된 엔티티의 id는 null 이 아니어야 한다." )
	public void idAfterPersist() {
		assertThat( user.getId() ).isNotNull();
	}

	@Test
	@DisplayName( "persist 된 엔티티의 socialType 은 null 이어야 한다." )
	public void socialTypeAfterPersist() {
		assertThat( user.getSocialType() ).isNull();
	}

	@Test
	@DisplayName( "소셜 유저 여부 체크" )
	public void isSocialUser() {
		//given
		User normalUser = User.builder().build();
		User googleUser = User.builder().socialType( SocialType.GOOGLE ).build();

		//then
		assertThat( normalUser.isSocialUser() ).isFalse();
		assertThat( googleUser.isSocialUser() ).isTrue();
	}

	@Test
	@DisplayName( "UserDTO 확인" )
	public void toDTO() {
	    //given
		UserDTO userDTO = user.toDTO();

		//then
		assertThat( userDTO.getEmail() ).isEqualTo( user.getEmail() );
		assertThat( userDTO.getUserName() ).isEqualTo( user.getUserName() );
		assertThat( userDTO.getPicture() ).isEqualTo( user.getPicture() );
	}
	
	@Test
	@DisplayName( "업데이트 요청의 값이 null 이 아니라면 user 의 필드는 업데이트 되어야 한다." )
	public void fieldUpdateWithValue() {
		//given
		UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
				.password( "5678" )
				.userName( "hak" )
				.picture( "picture2" )
				.roleType( "admin" )
				.build();

		//when
		persistHelper.update( user::update, userUpdateRequest );
		persistHelper.clearEntityManager();
		
		//then
		User findUserInDB = persistHelper.find( User.class, user.getId() );
		assertThat( findUserInDB.getPassword() ).isEqualTo( userUpdateRequest.getPassword() );
		assertThat( findUserInDB.getUserName() ).isEqualTo( userUpdateRequest.getUserName() );
		assertThat( findUserInDB.getPicture() ).isEqualTo( userUpdateRequest.getPicture() );
		assertThat( findUserInDB.getRoleType() ).isEqualTo( RoleType.getRoleTypeFrom( userUpdateRequest.getRoleType() ) );
	}

	@Test
	@DisplayName( "change 메서드 호출시 파라미터가 null 이라면 NullPointerException 예외가 발생해야 한다." )
	public void fieldUpdateWithNull() {
		assertThrows( NullPointerException.class, () -> persistHelper.update( user::changePassword, null ) );
		assertThrows( NullPointerException.class, () -> persistHelper.update( user::changeUserName, null ) );
		assertThrows( NullPointerException.class, () -> persistHelper.update( user::changePicture, null ) );
		assertThrows( NullPointerException.class, () -> persistHelper.update( user::changeRoleType, null ) );
	}
	
	@Test
	@DisplayName( "db 에서 삭제된 유저를 찾은 경우에는 null 이 나와야한다." )
	public void userAfterDelete() {
		//when
		persistHelper.delete( user );
		persistHelper.clearEntityManager();
		
		//then : user 를 db 에서 찾는다. user 를 지웠으므로 null 인지 확인한다.
		User findUserInDB = persistHelper.find(User.class, this.user.getId());
		assertThat( findUserInDB ).isNull();
	}

	@Test
	@DisplayName( "이메일이 같은 유저는 저장될 수 없다." )
	public void saveTwoUserHavingSameEmail() {
	    //given : 이메일이 동일한 두 유저 생성
		User jin = User.builder()
				.email( "hjm7091@naver.com" )
				.userName( "jin" )
				.build();
		User hak = User.builder()
				.email( "hjm7091@naver.com" )
				.userName( "hak" )
				.build();

		//when : 유저를 저장
		assertThrows( PersistenceException.class, () -> persistHelper.persist( jin, hak ) );
	}

	@Test
	@DisplayName( "이메일이 없는 유저는 저장될 수 없다." )
	public void saveUserWithoutEmail() {
	    //given : 이메일이 없는 유저 생성
		User user1 = User.builder()
				.userName( "jin" )
				.build();

	    //when : 유저를 저장
		assertThrows( PersistenceException.class, () -> persistHelper.persist( user1 ) );
	}

	@Test
	@DisplayName( "user 의 toString() 은 id, email, userName 필드가 있어야함." )
	public void ToString() {
		//when
		String userToString = user.toString();

		//then
		assertThat( userToString.contains( "id" ) ).isTrue();
		assertThat( userToString.contains( "email" ) ).isTrue();
		assertThat( userToString.contains( "userName" ) ).isTrue();
	}

	@Test
	@DisplayName( "user 의 id, email 이 같다면 같은 객체이다." )
	public void equalsAndHashCode() {
	    //given
		User user1 = User.builder().id( 1L ).email( "hjm7091@naver.com" ).userName( "hak" ).build();
		User user2 = User.builder().id( 1L ).email( "hjm7091@naver.com" ).userName( "jin" ).build();

	    //then
		assertThat( user1 == user2 ).isFalse();
		assertThat( user1.equals( user2 ) ).isTrue();
		assertThat( user1.hashCode() ).isEqualTo( user2.hashCode() );
	}

}
