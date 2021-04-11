package jin.h.mun.knutalk.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jin.h.mun.knutalk.domain.PersistHelper;
import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.dto.account.UserRegisterRequest;
import jin.h.mun.knutalk.dto.post.PostRegisterRequest;

public class CommentTest {
	
	private static PersistHelper persistHelper;
	
	private User jin;
	
	private Post postOfJin;
	
	private User hak;
	
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
	
	@After
	public void tearDown() {
		persistHelper.deleteAll( Comment.class );
		persistHelper.deleteAll( Post.class );
		persistHelper.deleteAll( User.class );
		assertThat( persistHelper.countRow( Comment.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( Post.class ) ).isEqualTo( 0 );
		assertThat( persistHelper.countRow( User.class ) ).isEqualTo( 0 );
	}

	@Test
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
		Comment findCommentInDB = persistHelper.find( Comment.class, comment.getId() );
		assertThat( findPostInDB.getComments().size() ).isEqualTo( 1 );
		assertThat( findCommentInDB.getPost() ).isEqualTo( postOfJin );
		assertThat( findCommentInDB.getPost().getOwner() ).isEqualTo( jin );
		assertThat( findCommentInDB.getContent() ).isEqualTo( content );
	}

}
