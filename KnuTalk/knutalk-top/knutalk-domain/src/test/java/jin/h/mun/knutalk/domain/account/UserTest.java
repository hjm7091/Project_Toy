package jin.h.mun.knutalk.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jin.h.mun.knutalk.dto.account.UserRegisterDto;
import jin.h.mun.knutalk.dto.account.UserUpdateDto;

public class UserTest {

	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	private EntityTransaction et;
	
	private User user;

	@Before
	public void setUp() {
		emf = Persistence.createEntityManagerFactory("domain");
		em = emf.createEntityManager();
		et = em.getTransaction();
		
		user = User.createUser(UserRegisterDto.builder()
				.email("jin@gmail.com")
				.password("1234")
				.nickName("jin").build());
	}
	
	@After
	public void tearDown() {
		em.close();
		emf.close();
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
		persist(user);
		
		//then
		assertThat(user.getId()).isNotNull();
	}
	
	public void persist(User user) {
		et.begin();
		
		em.persist(user);
		
		et.commit();
		em.clear(); //db에서 찾기 위해 영속성 컨텍스트를 비워준다.
	}
	
	@Test
	public void checkFieldAfterUpdate() {
		//given
		LocalDateTime startTime = LocalDateTime.now(); 
		assertThat(user.getId()).isNull();
		persist(user);
		
		//when
		UserUpdateDto userUpdateDto = UserUpdateDto.builder()
			.password("5678")
			.nickName("hak")
			.userLevel("VERIFIED").build();
		update(user.getId(), userUpdateDto);
		User findUser = em.find(User.class, user.getId());
		
		//then
		assertThat(findUser.getPassword()).isEqualTo("5678");
		assertThat(findUser.getNickName()).isEqualTo("hak");
		assertThat(findUser.getUserLevel()).isEqualTo(UserLevel.VERIFIED);
		assertThat(findUser.getUpdatedAt().isAfter(startTime)).isTrue();
	}
	
	public void update(Long userId, UserUpdateDto userUpdateDto) {
		et.begin();	

		User user = em.find(User.class, userId);
		user.changePassword(userUpdateDto.getPassword());
		user.changeNickName(userUpdateDto.getNickName());
		user.changeUserLevel(userUpdateDto.getUserLevel());
		
		et.commit();
		em.clear(); //db에서 찾기 위해 영속성 컨텍스트를 비워준다.
	}
	
	@Test
	public void checkUserAfterDelete() {
		//given
		assertThat(user.getId()).isNull();
		persist(user);
		
		//when
		delete(user.getId());
		User findUser = em.find(User.class, user.getId());
		
		//then
		assertThat(findUser).isNull();
	}
	
	public void delete(Long userId) {
		et.begin();	
		
		User user = em.find(User.class, userId);
		em.remove(user);
		
		et.commit();
		em.clear(); //db에서 찾기 위해 영속성 컨텍스트를 비워준다.
	}
	
}
