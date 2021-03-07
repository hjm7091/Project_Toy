package jin.h.mun.knutalk.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jin.h.mun.knutalk.domain.PersistHelper;
import jin.h.mun.knutalk.domain.account.enums.RoleType;
import jin.h.mun.knutalk.dto.account.UserRegisterRequest;
import jin.h.mun.knutalk.dto.account.UserUpdateRequest;

public class UserTest {
	
	private PersistHelper persistHelper;
	
	private User user;

	@Before
	public void setUp() {
		persistHelper = new PersistHelper( "domain" );
		
		user = new User( UserRegisterRequest.builder()
						   .email( "hjm7091@naver.com" )
						   .password( "1234" )
						   .userName( "jin" )
						   .picture( "picture1" )
						   .build() );
	}
	
	@After
	public void tearDown() {
		persistHelper.closeAll();
	}
	
	@Test
	public void baseField() throws InterruptedException {
		//given
		/*
		 * user가 생성된 시점과 테스트가 실행되는 시점의 텀이 너무 짧아 시간이 
		 * 동일할 수 있으므로 시간이 조금 흘렀다고 가정하기 위해 스레드를 잠시 재운다.
		 */
		Thread.sleep( 100L );
		LocalDateTime nowTime = LocalDateTime.now();
		
		//then
		assertThat( nowTime.isAfter( user.getCreatedAt() ) ).isTrue();
		assertThat( nowTime.isAfter( user.getUpdatedAt() ) ).isTrue();
	}
	
	@Test
	public void idAfterPersist() {
		//given
		assertThat( user.getId() ).isNull();
		
		//when
		persistHelper.persist( user );
		
		//then
		assertThat( user.getId() ).isNotNull();
	}
	
	@Test
	public void fieldAfterUpdate() {
		//given
		LocalDateTime startTime = LocalDateTime.now();
		
		UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
												.password( null )
												.userName( null )
												.picture( null )
												.roleType( "admin" )
												.build();
		
		persistHelper.persist( user );
		persistHelper.clearEntityManager();
		
		//when
		User findUserInDB = persistHelper.find( User.class, user.getId() );
		
		persistHelper.update( ( user, request ) -> {
			user.changePassword( request.getPassword() );
			user.changeUserName( request.getUserName() );
			user.changePicture( request.getPicture() );
			user.changeRoleType( RoleType.getRoleType( request.getRoleType() ) );
		}, findUserInDB, userUpdateRequest );
		
		persistHelper.clearEntityManager();
		
		findUserInDB = persistHelper.find( User.class, user.getId() );
		
		//then
		assertThat( findUserInDB.getPassword() ).isEqualTo( "1234" );
		assertThat( findUserInDB.getUserName() ).isEqualTo( "jin" );
		assertThat( findUserInDB.getRoleType() ).isEqualTo( RoleType.ADMIN );
		assertThat( findUserInDB.getUpdatedAt().isAfter( startTime ) ).isTrue();
	}
	
	@Test
	public void userAfterDelete() {
		//given
		persistHelper.persist( user );
		persistHelper.clearEntityManager();
		
		//when
		User findUserInDB = persistHelper.find( User.class, user.getId() );
		
		persistHelper.delete( findUserInDB );
		persistHelper.clearEntityManager();
		
		findUserInDB = persistHelper.find( User.class, user.getId() );
		
		//then
		assertThat( findUserInDB ).isNull();
	}
	
}
