package jin.h.mun.knutalk.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.dto.account.UserRegisterDto;
import jin.h.mun.knutalk.dto.post.PostRegisterDto;
import jin.h.mun.knutalk.dto.post.SecretPostRegisterDto;

public class PostTest {

	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	private EntityTransaction et;
	
	private User owner;
	
	private Post post;
	
	private SecretPost secretPost;

	@Before
	public void setUp() {
		emf = Persistence.createEntityManagerFactory("domain");
		em = emf.createEntityManager();
		et = em.getTransaction();
		
		owner = User.createUser(UserRegisterDto.builder()
				.email("jin@gmail.com")
				.password("1234")
				.nickName("jin").build());
		
		//일반 게시글
		PostRegisterDto postRegisterDto = PostRegisterDto.builder()
				.title("title1")
				.content("content1")
				.build();
		post = Post.createPost(postRegisterDto, owner);
		
		//비밀 게시글
		SecretPostRegisterDto secretPostRegisterDto = SecretPostRegisterDto.builder()
				.postRegisterDto(postRegisterDto)
				.password("4567")
				.anonymous(true)
				.build();
		secretPost = SecretPost.createPost(secretPostRegisterDto, owner);
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
		 * post가 생성된 시점과 테스트가 실행되는 시점의 텀이 너무 짧아 시간이 
		 * 동일할 수 있으므로 시간이 조금 흘렀다고 가정하기 위해 스레드를 잠시 재운다.
		 */
		Thread.sleep(100L);
		LocalDateTime startTime = LocalDateTime.now();
		
		//then
		assertThat(startTime.isAfter(post.getCreatedAt())).isTrue();
		assertThat(startTime.isAfter(post.getUpdatedAt())).isTrue();
		assertThat(post.getViewCount()).isEqualTo(0);
		
		assertThat(startTime.isAfter(secretPost.getCreatedAt())).isTrue();
		assertThat(startTime.isAfter(secretPost.getUpdatedAt())).isTrue();
		assertThat(secretPost.getViewCount()).isEqualTo(0);
	}
	
	@Test
	public void persistenceTransitionAfterPersistUser() {
		//given
		assertThat(owner.getId()).isNull();
		assertThat(post.getId()).isNull();
		assertThat(secretPost.getId()).isNull();
		
		//when
		persist(owner);
		Post findPost = em.find(Post.class, post.getId());
		SecretPost findSecretPost = em.find(SecretPost.class, secretPost.getId());
		
		//then
		assertThat(findPost).isNotNull();
		assertThat(findPost.getId()).isNotNull();
		assertThat(findSecretPost).isNotNull();
		assertThat(findSecretPost.getId()).isNotNull();
	}
	
	public void persist(User user) {
		et.begin();
		
		em.persist(user);
		
		et.commit();
		em.clear(); //db에서 찾기 위해 영속성 컨텍스트를 비워준다.
	}
	
	@Test
	public void persistenceTransitionAfterDeleteUser() {
		//given
		assertThat(owner.getId()).isNull();
		assertThat(post.getId()).isNull();
		assertThat(secretPost.getId()).isNull();
		persist(owner);
		
		//when
		delete(owner.getId());
		Post findPost = em.find(Post.class, post.getId());
		SecretPost findSecretPost = em.find(SecretPost.class, secretPost.getId());
		
		//then
		assertThat(findPost).isNull();
		assertThat(findSecretPost).isNull();
	}
	
	public void delete(Long userId) {
		et.begin();	
		
		User user = em.find(User.class, userId);
		em.remove(user);
		
		et.commit();
		em.clear(); //db에서 찾기 위해 영속성 컨텍스트를 비워준다.
	}
	
	@Test
	public void viewCount() {
		//given
		assertThat(post.getViewCount()).isEqualTo(0);
		assertThat(secretPost.getViewCount()).isEqualTo(0);
		int count = 100;
		
		//when
		increaseViewCount(count);
		persist(owner);
		Post findPost = em.find(Post.class, post.getId());
		SecretPost findSecretPost = em.find(SecretPost.class, secretPost.getId());
		
		//then
		assertThat(findPost.getViewCount()).isEqualTo(count);
		assertThat(findSecretPost.getViewCount()).isEqualTo(count);
	}
	
	public void increaseViewCount(int count) {
		for(int i = 0; i < count; i++) {
			post.increaseViewCount();
			secretPost.increaseViewCount();
		}
	}
}
