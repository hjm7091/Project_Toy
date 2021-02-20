package jin.h.mun.knutalk.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jin.h.mun.knutalk.domain.PersistHelper;
import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.dto.account.UserRegisterDto;
import jin.h.mun.knutalk.dto.post.PostRegisterDto;

public class CommentTest {
	
	private PersistHelper persistHelper;
	
	private User jin;
	
	private Post postOfJin;
	
	private User hak;
	
	@Before
	public void setUp() {
		persistHelper = new PersistHelper("domain");
		
		//게시물 주인
		jin = User.createUser(UserRegisterDto.builder()
				.email("jin@gmail.com")
				.password("1234")
				.nickName("jin").build());
		
		//게시물에 글을 쓸 유저
		hak = User.createUser(UserRegisterDto.builder()
				.email("hak@gmail.com")
				.password("1234")
				.nickName("hak").build());
		
		//jin의 게시물
		PostRegisterDto postRegisterDto = PostRegisterDto.builder()
				.title("title1")
				.content("content1")
				.build();
		postOfJin = Post.createPost(postRegisterDto, jin);
		
	}

	@After
	public void tearDown() {
		persistHelper.closeAll();
	}

	@Test
	public void writeComment() {
		//given
		persistHelper.persist(jin);
		persistHelper.persist(hak);
		
		Comment comment = Comment.builder()
			.writer(hak)
			.postToWrite(postOfJin)
			.content("hello. my name is hak!!")
			.build();
		
		//when
		persistHelper.persist(comment);
		persistHelper.clearEntityManager();
		
		Post findPostInDB = persistHelper.find(Post.class, postOfJin.getId());
		Comment findCommentInDB = persistHelper.find(Comment.class, comment.getId());
		
		//then
		assertThat(findPostInDB.getComments().size()).isEqualTo(1);
		assertThat(findCommentInDB.getPost()).isEqualTo(postOfJin);
		assertThat(findCommentInDB.getPost().getOwner()).isEqualTo(jin);
		assertThat(findCommentInDB.getContent()).isEqualTo("hello. my name is hak!!");
	}

}
