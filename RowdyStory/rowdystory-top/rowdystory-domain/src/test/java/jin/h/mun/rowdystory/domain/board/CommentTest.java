package jin.h.mun.rowdystory.domain.board;

import jin.h.mun.rowdystory.domain.PersistHelper;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import jin.h.mun.rowdystory.dto.post.PostRegisterRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {
	
	private static PersistHelper persistHelper;
	
	private User jin;
	
	private Post postOfJin;
	
	private User hak;
	
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
				.email( "jin@gmail.com" )
				.password( "1234" )
				.userName( "jin" ).build() );
		
		//게시물에 글을 쓸 유저
		hak = new User( UserRegisterRequest.builder()
				.email( "hak@gmail.com" )
				.password( "1234" )
				.userName( "hak" ).build() );
		
		//jin의 게시물
		PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
				.title( "title1" )
				.content( "content1" )
				.build();
		postOfJin = new Post( postRegisterRequest, jin );
		
	}
	
	@AfterEach
	public void tearDown() {
		persistHelper.deleteAll( Comment.class );
		persistHelper.deleteAll( Post.class );
		persistHelper.deleteAll( User.class );
		assertThat( persistHelper.countRow( Comment.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( Post.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( User.class ) ).isEqualTo( 0 );
	}

	@Test
	@DisplayName( "게시물의 코멘트는 유저가 작성한 코멘트와 같아야 한다." )
	public void writeComment() {
		//given : 유저 및 게시물 저장
		persistHelper.persist( jin, hak, postOfJin );
		String content = "hello. my name is hak!!";
		Comment comment = Comment.builder()
							.writer( hak )
							.targetPost( postOfJin )
							.content( content )
							.build();
		
		//when : 코멘트 저장
		persistHelper.persist( comment );
		persistHelper.clearEntityManager();
		
		//then : 게시물의 코멘트 갯수 확인, 코멘트가 쓰여진 게시물, 코멘트가 쓰여진 게시물의 주인, 코멘트 내용 확인
		Post findPostInDB = persistHelper.find( Post.class, postOfJin.getId() );
		Comment findComment = findPostInDB.getComments().get( 0 );
		assertThat( findPostInDB.getComments().size() ).isEqualTo( 1 );
		assertThat( findComment.getPost() ).isEqualTo( postOfJin );
		Assertions.assertThat( findComment.getPost().getOwner() ).isEqualTo( jin );
		assertThat( findComment.getContent() ).isEqualTo( content );
	}

}
