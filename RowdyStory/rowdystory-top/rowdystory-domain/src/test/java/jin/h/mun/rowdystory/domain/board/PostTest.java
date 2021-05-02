package jin.h.mun.rowdystory.domain.board;

import jin.h.mun.rowdystory.domain.PersistHelper;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.post.PostRegisterRequest;
import jin.h.mun.rowdystory.dto.post.PostUpdateRequest;
import jin.h.mun.rowdystory.dto.post.SecretPostRegisterRequest;
import jin.h.mun.rowdystory.dto.post.SecretPostUpdateRequest;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

	private static PersistHelper persistHelper;
	
	private User owner;
	
	private Post post;
	
	private SecretPost secretPost;
	
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

		persistHelper.persist( owner, post, secretPost );
	}
	
	@AfterEach
	public void tearDown() {
		persistHelper.deleteAll( Post.class );
		persistHelper.deleteAll( SecretPost.class );
		persistHelper.deleteAll( User.class );
		assertThat( persistHelper.countRow( Post.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( SecretPost.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( User.class ) ).isEqualTo( 0 );
	}
	
	@Test
	@DisplayName( "persist 된 엔티티의 id는 null 이 아니어야 한다." )
	public void idAfterPersist() {
		assertThat( owner.getId() ).isNotNull();
		assertThat( post.getId() ).isNotNull();
		assertThat( secretPost.getId() ).isNotNull();
	}
	
	@Test
	@DisplayName( "유저가 지워지면 유저의 게시물은 자동으로 지워져야함. (유저가 회원 탈퇴하는 경우)" )
	public void persistenceTransitionAfterDeleteUser() {
		//given
		persistHelper.clearEntityManager();
		User findOwnerInDB = persistHelper.find( User.class, owner.getId() );
		
		//when : 게시물 주인인 유저만 삭제
		persistHelper.delete( findOwnerInDB );
		persistHelper.clearEntityManager();
		
		//then : 게시물도 삭제되었는지 확인
		Post findPost = persistHelper.find( Post.class, post.getId() );
		SecretPost findSecretPost = persistHelper.find( SecretPost.class, secretPost.getId() );
		assertThat( findPost ).isNull();
		assertThat( findSecretPost ).isNull();
	}
	
	@Test
	@DisplayName( "조회수는 정상적으로 업데이트 되어야 한다." )
	public void viewCount() {
		//given : 유저 및 게시물 저장
		int count = 5;

		//when : viewCount 증가
		for( int i = 0; i < count; i++ ) {
			persistHelper.update( post::increaseViewCount );
			persistHelper.update( secretPost::increaseViewCount );
		}
		persistHelper.clearEntityManager();
		
		//then : viewCount 확인
		Post findPost = persistHelper.find( Post.class, post.getId() );
		SecretPost findSecretPost = persistHelper.find( SecretPost.class, secretPost.getId() );
		assertThat( findPost.getViewCount() ).isEqualTo( count );
		assertThat( findSecretPost.getViewCount() ).isEqualTo( count );
	}

	@Test
	@DisplayName( "값이 null 이 아니라면 post 의 필드는 업데이트 되어야 한다." )
	public void fieldUpdateWithValue() {
	    //given
		PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
				.title( "title2" )
				.content( "content2" ).build();
		SecretPostUpdateRequest secretPostUpdateRequest = SecretPostUpdateRequest.builder()
				.postUpdateRequest( postUpdateRequest )
				.password( "1234" )
				.anonymous( false ).build();

		//when
		persistHelper.update( secretPost::update, secretPostUpdateRequest );
		persistHelper.clearEntityManager();

	    //then
		SecretPost findSecretPost = persistHelper.find(SecretPost.class, this.secretPost.getId());
		assertThat( findSecretPost.getTitle() ).isEqualTo( postUpdateRequest.getTitle() );
		assertThat( findSecretPost.getContent() ).isEqualTo( postUpdateRequest.getContent() );
		assertThat( findSecretPost.getPassword() ).isEqualTo( secretPostUpdateRequest.getPassword() );
		assertThat( findSecretPost.getAnonymous() ).isEqualTo( secretPostUpdateRequest.getAnonymous() );
	}

	@Test
	@DisplayName( "값이 null 이라면 post 의 필드는 업데이트 되지 않아야 한다." )
	public void fieldUpdateWithNull() {
		//given
		PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
				.title( null )
				.content( null ).build();
		SecretPostUpdateRequest secretPostUpdateRequest = SecretPostUpdateRequest.builder()
				.postUpdateRequest( postUpdateRequest )
				.password( null )
				.anonymous( null ).build();

		//when
		persistHelper.update( secretPost::update, secretPostUpdateRequest );
		persistHelper.clearEntityManager();

		//then
		SecretPost findSecretPost = persistHelper.find(SecretPost.class, this.secretPost.getId());
		assertThat( findSecretPost.getTitle() ).isNotNull();
		assertThat( findSecretPost.getContent() ).isNotNull();
		assertThat( findSecretPost.getPassword() ).isNotNull();
		assertThat( findSecretPost.getAnonymous() ).isNotNull();
	}

}
