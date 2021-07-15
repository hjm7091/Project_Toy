package jin.h.mun.rowdystory.domain.board;

import jin.h.mun.rowdystory.domain.PersistHelper;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.post.PostRegisterRequest;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ThumbUpPostTest {

	private static PersistHelper persistHelper;
	
	private User jin;
	
	private Post postOfJin;
	
	private List<User> users;
	
	private User user1, user2, user3, user4, user5;
	
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
		//게시물 주인
		jin = new User( UserRegisterRequest.builder()
							.email( "jin@naver.com" )
							.password( "1234" )
							.userName( "jin" )
							.build() );
		
		//jin의 게시물
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
													.title( "title1" )
													.content( "content1" )
													.build();
		postOfJin = new Post( postRegisterRequest, jin );
		
		user1 = User.builder().email( "user1@test.com" ).userName( "user1" ).build();
		user2 = User.builder().email( "user2@test.com" ).userName( "user2" ).build();
		user3 = User.builder().email( "user3@test.com" ).userName( "user3" ).build();
		user4 = User.builder().email( "user4@test.com" ).userName( "user4" ).build();
		user5 = User.builder().email( "user5@test.com" ).userName( "user5" ).build();
		
		users = Stream.of( user1, user2, user3, user4, user5 ).collect( Collectors.toList() );
	}
	
	@AfterEach
	public void tearDown() {
		persistHelper.deleteAll( ThumbUpPost.class );
		persistHelper.deleteAll( Post.class );
		persistHelper.deleteAll( User.class );
		assertThat( persistHelper.countRow( ThumbUpPost.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( Post.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( User.class ) ).isEqualTo( 0 );
	}
	
	@Test
	@DisplayName( "게시물의 좋아요 갯수는 유저 수와 같아야 한다." )
	public void thumbUpToPost() {
		//given : 유저 및 게시물 저장
		persistHelper.persist( jin, postOfJin );
		users.stream().forEach( user -> persistHelper.persist( user ) );
		
		//when : thumbUpPost 저장
		users.stream().forEach( user -> persistHelper.persist( new ThumbUpPost( user, postOfJin ) ) );
		persistHelper.clearEntityManager();
		
		//then : 게시물의 thumbUpPost 갯수 확인
		Post findPostInDB = persistHelper.find( Post.class , postOfJin.getId() );
		assertThat( findPostInDB.thumbUpCount() ).isEqualTo( users.size() );
	}
	
	@Test
	@DisplayName( "게시물이 지워지면 thumbUpPost 정보는 자동으로 DB 에서 지워져야함. (게시물 주인이 게시물을 지우는 경우)" )
	public void persistenceTransitionAfterDeletePost() {
		//given : 유저, 게시물, thumbUpPost 저장
		persistHelper.persist( jin, user1, user2, postOfJin );
		ThumbUpPost thumbUpByUser1 = new ThumbUpPost( user1, postOfJin );
		ThumbUpPost thumbUpByUser2 = new ThumbUpPost( user2, postOfJin );
		persistHelper.persist( thumbUpByUser1, thumbUpByUser2 );
		persistHelper.clearEntityManager();
		
		//when : 게시물을 지운다.
		Post findPostInDB = persistHelper.find( Post.class, postOfJin.getId() );	
		persistHelper.delete( findPostInDB );
		persistHelper.clearEntityManager();
		
		//then : thumbUpPost 정보가 지워졌는지 확인
		ThumbUpPost findThumbUpByUser1InDB = persistHelper.find( ThumbUpPost.class, thumbUpByUser1.getId() );
		ThumbUpPost findThumbUpByUser2InDB = persistHelper.find( ThumbUpPost.class, thumbUpByUser2.getId() );
		assertThat( findThumbUpByUser1InDB ).isNull();
		assertThat( findThumbUpByUser2InDB ).isNull();
	}

}
