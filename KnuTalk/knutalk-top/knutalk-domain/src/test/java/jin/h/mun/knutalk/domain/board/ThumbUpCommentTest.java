package jin.h.mun.knutalk.domain.board;

import jin.h.mun.knutalk.domain.PersistHelper;
import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.dto.account.UserRegisterRequest;
import jin.h.mun.knutalk.dto.post.PostRegisterRequest;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ThumbUpCommentTest {

	private static PersistHelper persistHelper;
	
	private User jin, hak;
	
	private Post postOfJin;
	
	private Comment commentOfHak;
	
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
				.picture( "picture1" )
				.build() );
		
		//jin의 게시물
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
													.title( "title1" )
													.content( "content1" )
													.build();
		postOfJin = new Post( postRegisterRequest, jin );
		
		//게시물에 글을 쓸 유저
		hak = new User( UserRegisterRequest.builder()
				.email( "hak@gmail.com" )
				.password( "1234" )
				.userName( "hak" ).build() );
		
		commentOfHak = Comment.builder()
				.writer( hak )
				.targetPost( postOfJin )
				.content( "hello. my name is hak!!" )
				.build();
		
		user1 = User.builder().email( "user1@test.com" ).userName( "user1" ).build();
		user2 = User.builder().email( "user2@test.com" ).userName( "user2" ).build();
		user3 = User.builder().email( "user3@test.com" ).userName( "user3" ).build();
		user4 = User.builder().email( "user4@test.com" ).userName( "user4" ).build();
		user5 = User.builder().email( "user5@test.com" ).userName( "user5" ).build();
		
		users = Stream.of( user1, user2, user3, user4, user5 ).collect( Collectors.toList() );
	}

	@AfterEach
	public void tearDown() {
		persistHelper.deleteAll( ThumbUpComment.class );
		persistHelper.deleteAll( Comment.class );
		persistHelper.deleteAll( Post.class );
		persistHelper.deleteAll( User.class );
		assertThat( persistHelper.countRow( ThumbUpPost.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( Comment.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( Post.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( User.class ) ).isEqualTo( 0 );
	}

	@Test
	@DisplayName( "코멘트의 좋아요 갯수는 유저 수와 같아야 한다." )
	public void thumbUpComment() {
		//given : 유저, 게시물, 코멘트 저장
		persistHelper.persist( jin, postOfJin );
		persistHelper.persist( hak, commentOfHak );
		users.stream().forEach( user -> persistHelper.persist( user ) );
		
		//when : thumbUpComment 저장
		users.stream().forEach( user -> persistHelper.persist( new ThumbUpComment( user, commentOfHak ) ) );
		persistHelper.clearEntityManager();
		
		//then : 코멘트의 thumbUpComment 갯수 확인
		Comment findCommentInDB = persistHelper.find( Comment.class, commentOfHak.getId() );
		assertThat( findCommentInDB.thumbUpCount() ).isEqualTo( users.size() );
	}
	
	@Test
	@DisplayName( "코멘트가 지워지면 thumbUpComment 정보는 자동으로 DB 에서 지워져야함. (코멘트를 작성한 사람이 코멘트를 지우는 경우)" )
	public void persistenceTransitionAfterDeleteComment() {
		//given : 유저, 게시물, 코멘트, thumbUpComment 저장
		persistHelper.persist( jin, postOfJin, hak, commentOfHak );
		persistHelper.persist( user1, user2 );
		ThumbUpComment thumbUpByUser1 = new ThumbUpComment( user1, commentOfHak );
		ThumbUpComment thumbUpByUser2 = new ThumbUpComment( user2, commentOfHak );
		persistHelper.persist( thumbUpByUser1, thumbUpByUser2 );
		persistHelper.clearEntityManager();
		
		//when : 코멘트를 지운다.
		Comment findCommentInDB = persistHelper.find( Comment.class, commentOfHak.getId() );
		persistHelper.delete( findCommentInDB );
		persistHelper.clearEntityManager();
		
		//then : thumbUpComment 정보가 지워졌는지 확인
		ThumbUpComment findThumbUpByUser1InDB = persistHelper.find( ThumbUpComment.class, thumbUpByUser1.getId() );
		ThumbUpComment findThumbUpByUser2InDB = persistHelper.find( ThumbUpComment.class, thumbUpByUser2.getId() );
		assertThat( findThumbUpByUser1InDB ).isNull();
		assertThat( findThumbUpByUser2InDB ).isNull();
	}

}
