package jin.h.mun.knutalk.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jin.h.mun.knutalk.domain.PersistHelper;
import jin.h.mun.knutalk.domain.account.enums.RoleType;
import jin.h.mun.knutalk.dto.account.UserRegisterRequest;
import jin.h.mun.knutalk.dto.account.UserUpdateRequest;

import javax.persistence.PersistenceException;

public class UserTest {
	
	private static PersistHelper persistHelper;
	
	private User user;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		persistHelper = new PersistHelper();
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		persistHelper.closeAll();
	}
	
	@Before
	public void setUp() {
		user = new User( UserRegisterRequest.builder()
				   .email( "hjm7091@naver.com" )
				   .password( "1234" )
				   .userName( "jin" )
				   .picture( "picture1" )
				   .build() );
	}
	
	@After
	public void tearDown() {
		persistHelper.deleteAll( User.class );
		assertThat( persistHelper.countRow( User.class ) ).isEqualTo( 0 );
	}
	
	@Test
	public void idAfterPersist() {
		//given : user의 id가 null인지 확인
		assertThat( user.getId() ).isNull();
		
		//when : user를 저장
		persistHelper.persist( user );
		
		//then : user의 id가 null이 아닌지 확인
		assertThat( user.getId() ).isNotNull();
	}
	
	@Test
	public void fieldAfterUpdate() {
		//given : user를 저장한다, db에서 user를 찾기 위해 엔티티 매니저를 비운다.
		persistHelper.persist( user );
		persistHelper.clearEntityManager();
		
		//when : user를 db에서 찾고 업데이트한다. db에서 user를 찾기 위해 엔티티 매니저를 비운다.
		User findUserInDB = persistHelper.find( User.class, user.getId() );
		
		UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
				.password( null )
				.userName( null )
				.picture( "picture2" )
				.roleType( "admin" )
				.build();
		
		persistHelper.update( findUserInDB::update, userUpdateRequest );
		persistHelper.clearEntityManager();
		
		//then : user를 db에서 찾는다. user의 필드가 업데이트 되었는지 확인한다. null값으로는 업데이트 되지 않아야함.
		findUserInDB = persistHelper.find( User.class, user.getId() );
		assertThat( findUserInDB.getPassword() ).isEqualTo( "1234" );
		assertThat( findUserInDB.getUserName() ).isEqualTo( "jin" );
		assertThat( findUserInDB.getPicture() ).isEqualTo( "picture2" );
		assertThat( findUserInDB.getRoleType() ).isEqualTo( RoleType.ADMIN );
	}
	
	@Test
	public void userAfterDelete() {
		//given : user를 저장한다, db에서 user를 찾기 위해 엔티티 매니저를 비운다.
		persistHelper.persist( user );
		persistHelper.clearEntityManager();
		
		//when : user를 db에서 찾고 지운다. db에서 user를 찾기 위해 엔티티 매니저를 비운다.
		User findUserInDB = persistHelper.find( User.class, user.getId() );
		persistHelper.delete( findUserInDB );
		persistHelper.clearEntityManager();
		
		//then : user를 db에서 찾는다. user를 지웠으므로 null인지 확인한다.
		findUserInDB = persistHelper.find( User.class, user.getId() );
		assertThat( findUserInDB ).isNull();
	}

	@Test
	public void saveTwoUserHavingSameEmail() {
	    //given : 이메일이 동일한 두 유저 생성
		User user1 = User.builder()
				.email( "hjm7091@naver.com" )
				.userName( "jin" )
				.build();
		User user2 = User.builder()
				.email( "hjm7091@naver.com" )
				.userName( "hak" )
				.build();

		//when : 유저를 저장
		persistHelper.persist( user1, user2 );

		//then : 이메일은 같고 유저 아이디는 다른지 확인
		assertThat( user1.getEmail() ).isEqualTo( user2.getEmail() );
		assertThat( user1.getId() ).isNotEqualTo( user2.getId() );
	}

	@Test( expected = PersistenceException.class )
	public void saveUserWithoutEmail() {
	    //given : 이메일이 없는 유저 생성
		User user1 = User.builder()
				.userName( "jin" )
				.build();

	    //when : 유저를 저장
		persistHelper.persist( user1 );
	}
}
