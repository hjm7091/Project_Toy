package jin.h.mun.knutalk.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jin.h.mun.knutalk.domain.PersistHelper;
import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.dto.account.UserRegisterRequest;
import jin.h.mun.knutalk.dto.post.PostRegisterRequest;
import jin.h.mun.knutalk.dto.post.SecretPostRegisterRequest;

public class PostTest {

	private PersistHelper persistHelper;
	
	private User owner;
	
	private Post post;
	
	private SecretPost secretPost;

	@Before
	public void setUp() {
		persistHelper = new PersistHelper( "domain" );
		
		owner = new User( UserRegisterRequest.builder()
						   .email( "hjm7091@naver.com" )
						   .password( "1234" )
						   .userName( "jin" )
						   .picture( "picture1" )
						   .build() );
		
		//일반 게시글
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
													.title( "title1" )
													.content( "content1" )
													.build();
		post = new Post( postRegisterRequest, owner );
		
		//비밀 게시글
		SecretPostRegisterRequest secretPostRegisterRequest = SecretPostRegisterRequest.builder()
																.postRegisterRequest( postRegisterRequest )
																.password( "4567" )
																.anonymous( true )
																.build();
		secretPost = new SecretPost( secretPostRegisterRequest, owner );
	}
	
	@After
	public void tearDown() {
		persistHelper.closeAll();
	}

	@Test
	public void baseField() throws InterruptedException {
		//given
		/*
		 * post가 생성된 시점과 테스트가 실행되는 시점의 텀이 너무 짧아 시간이 
		 * 동일할 수 있으므로 시간이 조금 흘렀다고 가정하기 위해 스레드를 잠시 재운다.
		 */
		Thread.sleep( 100L );
		LocalDateTime nowTime = LocalDateTime.now();
		
		//then
		assertThat( nowTime.isAfter( post.getCreatedAt() ) ).isTrue();
		assertThat( nowTime.isAfter( post.getUpdatedAt() ) ).isTrue();
		assertThat( post.getViewCount() ).isEqualTo( 0 );
		
		assertThat( nowTime.isAfter( secretPost.getCreatedAt() ) ).isTrue();
		assertThat( nowTime.isAfter( secretPost.getUpdatedAt() ) ).isTrue();
		assertThat( secretPost.getViewCount() ).isEqualTo( 0 );
	}
	
	@Test
	public void persistenceTransitionAfterPersistUser() {
		//given
		assertThat( owner.getId() ).isNull();
		assertThat( post.getId() ).isNull();
		assertThat( secretPost.getId() ).isNull();
		
		//when
		persistHelper.persist( owner );
		persistHelper.clearEntityManager();
		
		//then
		assertThat( owner.getId() ).isNotNull();
		assertThat( post.getId() ).isNotNull();
		assertThat( secretPost.getId() ).isNotNull();
	}
	
	@Test
	public void persistenceTransitionAfterDeleteUser() {
		//given
		assertThat( owner.getId() ).isNull();
		assertThat( post.getId() ).isNull();
		assertThat( secretPost.getId() ).isNull();
		
		persistHelper.persist( owner );
		persistHelper.clearEntityManager();
		
		//when
		User findOwnerInDB = persistHelper.find( User.class, owner.getId() );
		
		persistHelper.delete( findOwnerInDB );
		persistHelper.clearEntityManager();
		
		Post findPost = persistHelper.find( Post.class, post.getId() );
		SecretPost findSecretPost = persistHelper.find( SecretPost.class, secretPost.getId() );
		
		//then
		assertThat( findPost ).isNull();
		assertThat( findSecretPost ).isNull();
	}
	
	@Test
	public void viewCount() {
		//given
		assertThat( post.getViewCount() ).isEqualTo( 0 );
		assertThat( secretPost.getViewCount() ).isEqualTo( 0 );
		int count = 100;
		
		//when
		increaseViewCount( count );
		
		persistHelper.persist( owner );
		persistHelper.clearEntityManager();
		
		Post findPost = persistHelper.find( Post.class, post.getId() );
		SecretPost findSecretPost = persistHelper.find( SecretPost.class, secretPost.getId() );
		
		//then
		assertThat( findPost.getViewCount() ).isEqualTo( count );
		assertThat( findSecretPost.getViewCount() ).isEqualTo( count );
	}
	
	public void increaseViewCount( int count ) {
		for( int i = 0; i < count; i++ ) {
			post.increaseViewCount();
			secretPost.increaseViewCount();
		}
	}
}
