package jin.h.mun.knutalk.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import jin.h.mun.knutalk.domain.PersistHelper;
import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.dto.account.UserRegisterRequest;
import jin.h.mun.knutalk.dto.post.PostRegisterRequest;

public class ThumbUpPostTest {

	private PersistHelper persistHelper;
	
	private User jin;
	
	private Post postOfJin;
	
	private List<User> users;
	
	private User user1, user2, user3, user4, user5;
	
	@Before
	public void setUp() {
		persistHelper = new PersistHelper( "domain" );
		
		jin = new User( UserRegisterRequest.builder()
							.email( "jin@naver.com" )
							.password( "1234" )
							.userName( "jin" )
							.picture( "picture1" )
							.build() );
		
		//일반 게시글
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
	
	@Test
	public void thumbUp() {
		//given
		persistHelper.persist( jin );
		users.stream().forEach( user -> persistHelper.persist( user ) );
		
		//when
		users.stream().forEach( user -> persistHelper.persist( new ThumbUpPost( user, postOfJin ) ) );
		persistHelper.clearEntityManager();
		
		Post findPostInDB = persistHelper.find( Post.class , postOfJin.getId() );
		
		//then
		assertThat( findPostInDB.thumbUpCount() ).isEqualTo( 5 );
	}
	
	@Test
	// 유저를 저장하면 유저의 게시물은 자동으로 DB에 저장되어야함.
	// 게시물을 저장하면 게시물의 up 정보는 자동으로 DB에 저장되어야함.
	// 게시물의 up 정보가 DB에 저장되려면 게시물에 up을 한 사용자는 먼저 DB에 저장되어 있어야함.
	public void persistenceTransitionAfterPersistUser() {
		//given
		ThumbUpPost thumbUpUser1 = new ThumbUpPost( user1, postOfJin );
		ThumbUpPost thumbUpUser2 = new ThumbUpPost( user2, postOfJin );
		assertThat( jin.getId() ).isNull();
		assertThat( postOfJin.getId() ).isNull();
		assertThat( thumbUpUser1.getId() ).isNull();
		assertThat( thumbUpUser2.getId() ).isNull();
		
		//when
		persistHelper.persist( user1 ); persistHelper.persist( user2 );
		persistHelper.persist( jin );
		
		//then
		assertThat( jin.getId() ).isNotNull();
		assertThat( postOfJin.getId() ).isNotNull();
		assertThat( thumbUpUser1.getId() ).isNotNull();
		assertThat( thumbUpUser2.getId() ).isNotNull();
		assertThat( postOfJin.thumbUpCount() ).isEqualTo( 2 );
	}
	
	@Test
	// 게시물이 지워지면 thumbUpCount 정보는 자동으로 DB에서 지워져야함.
	// 게시물 주인이 게시물을 지우는 경우.
	public void persistenceTransitionAfterDeletePost() {
		//given
		ThumbUpPost thumbUpUser1 = new ThumbUpPost( user1, postOfJin );
		ThumbUpPost thumbUpUser2 = new ThumbUpPost( user2, postOfJin );
		persistHelper.persist( user1 ); persistHelper.persist( user2 );
		persistHelper.persist( jin );
		persistHelper.clearEntityManager();
		
		//when
		Post findPostInDB = persistHelper.find( Post.class, postOfJin.getId() );	
		persistHelper.delete( findPostInDB );
		persistHelper.clearEntityManager();
		
		ThumbUpPost thumbUpByUser1 = persistHelper.find( ThumbUpPost.class, thumbUpUser1.getId() );
		ThumbUpPost thumbUpByUser2 = persistHelper.find( ThumbUpPost.class, thumbUpUser2.getId() );
		
		//then
		assertThat( thumbUpByUser1 ).isNull();
		assertThat( thumbUpByUser2 ).isNull();
	}

}
