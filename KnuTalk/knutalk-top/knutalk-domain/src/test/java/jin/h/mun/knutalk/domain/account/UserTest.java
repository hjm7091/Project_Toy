package jin.h.mun.knutalk.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jin.h.mun.knutalk.domain.PersistHelper;
import jin.h.mun.knutalk.dto.account.UserRegisterDto;
import jin.h.mun.knutalk.dto.account.UserUpdateDto;

public class UserTest {
	
	private PersistHelper persistHelper;
	
	private User user;

	@Before
	public void setUp() {
		persistHelper = new PersistHelper("domain");
		
		user = User.createUser(UserRegisterDto.builder()
				.email("jin@gmail.com")
				.password("1234")
				.nickName("jin").build());
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
		Thread.sleep(100L);
		LocalDateTime startTime = LocalDateTime.now();
		
		//then
		assertThat(startTime.isAfter(user.getCreatedAt())).isTrue();
		assertThat(startTime.isAfter(user.getUpdatedAt())).isTrue();
		assertThat(user.getUserLevel()).isEqualTo(UserLevel.UNVERIFIED);
	}
	
	@Test
	public void checkIdAfterPersist() {
		//given
		assertThat(user.getId()).isNull();
		
		//when
		persistHelper.persist(user);
		
		//then
		assertThat(user.getId()).isNotNull();
	}
	
	@Test
	public void checkFieldAfterUpdate() {
		//given
		LocalDateTime startTime = LocalDateTime.now();
		
		UserUpdateDto userUpdateDto = UserUpdateDto.builder()
				.password("5678")
				.nickName("hak")
				.userLevel("VERIFIED").build();
		
		persistHelper.persist(user);
		persistHelper.clearEntityManager();
		
		//when
		User findUserInDB = persistHelper.find(User.class, user.getId());
		
		persistHelper.update((user, dto) -> {
			user.changePassword(dto.getPassword());
			user.changeNickName(dto.getNickName());
			user.changeUserLevel(dto.getUserLevel());
		}, findUserInDB, userUpdateDto);
		
		persistHelper.clearEntityManager();
		
		findUserInDB = persistHelper.find(User.class, user.getId());
		
		//then
		assertThat(findUserInDB.getPassword()).isEqualTo("5678");
		assertThat(findUserInDB.getNickName()).isEqualTo("hak");
		assertThat(findUserInDB.getUserLevel()).isEqualTo(UserLevel.VERIFIED);
		assertThat(findUserInDB.getUpdatedAt().isAfter(startTime)).isTrue();
	}
	
	@Test
	public void checkUserAfterDelete() {
		//given
		persistHelper.persist(user);
		persistHelper.clearEntityManager();
		
		//when
		User findUserInDB = persistHelper.find(User.class, user.getId());
		
		persistHelper.delete(findUserInDB);
		persistHelper.clearEntityManager();
		
		findUserInDB = persistHelper.find(User.class, user.getId());
		
		//then
		assertThat(findUserInDB).isNull();
	}
	
}
