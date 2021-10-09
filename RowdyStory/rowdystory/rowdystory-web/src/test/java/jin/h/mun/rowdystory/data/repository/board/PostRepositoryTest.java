package jin.h.mun.rowdystory.data.repository.board;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.board.Post;
import jin.h.mun.rowdystory.domain.board.SecretPost;
import jin.h.mun.rowdystory.domain.board.enums.PostType;
import jin.h.mun.rowdystory.dto.post.PostRegisterRequest;
import jin.h.mun.rowdystory.dto.post.SecretPostRegisterRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PostRepository postRepository;

    private final List<Post> posts = new ArrayList<>();

    private final List<SecretPost> secretPosts = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        User ownerOfPost = User.builder()
                .email( "hjm7091@naver.com" )
                .userName( "jin" )
                .password( "1234" )
                .build();

        testEntityManager.persist( ownerOfPost );

        int postCount = 3;

        for ( int i = 1; i <= postCount; i++ ) {
            PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
                    .title( "post" + i )
                    .content( "this is normal post" + i )
                    .build();
            posts.add( new Post( postRegisterRequest, ownerOfPost ) );
        }

        postCount = 4;

        for ( int i = 1; i <= postCount; i++) {
            SecretPostRegisterRequest secretPostRegisterRequest = SecretPostRegisterRequest.builder()
                    .postRegisterRequest( new PostRegisterRequest( "secretPost" + i, "this is secret post" + i ) )
                    .anonymous( false ).password( Integer.toString( i ) ).build();
            secretPosts.add( new SecretPost( secretPostRegisterRequest, ownerOfPost ) );
        }

        postRepository.saveAll( posts );
        postRepository.saveAll( secretPosts );
    }

    @Test
    @DisplayName( "모든 게시물 찾기" )
    public void findAllPost() {
        //when
        List<Post> findPosts = postRepository.findAll();

        //then
        assertThat( findPosts.size() ).isEqualTo( posts.size() + secretPosts.size() );
    }

    @Test
    @DisplayName( "게시물 타입에 따라 게시물 찾기" )
    public void findPostByPostType() {
        //when
        List<Post> findNormalPosts = postRepository.findPostByPostType( PostType.NORMAL );
        List<Post> findSecretPosts = postRepository.findPostByPostType( PostType.SECRET );

        //then
        assertThat( findNormalPosts ).allMatch( post -> post.getPostType() == PostType.NORMAL );
        assertThat( findSecretPosts ).allMatch( post -> post.getPostType() == PostType.SECRET );
    }

    @Test
    @DisplayName( "게시물 타입과 조회수가 많은 순으로 게시물 찾기" )
    public void findPostByPostTypeAndSort() {
        //given
        int viewCount = 100;
        for ( int i = 0; i < posts.size() - 1; i++ ) {
            Post post = posts.get( i );
            post.increaseViewCount( new Random().nextInt( viewCount ) );
        }
        posts.get( posts.size() - 1 ).increaseViewCount( viewCount );

        Sort viewCountDesc = Sort.by( Sort.Direction.DESC, "viewCount" );

        //when
        List<Post> findPosts = postRepository.findPostByPostType( PostType.NORMAL, viewCountDesc );
        Post postWithMostViews = findPosts.get( 0 );

        //then
        assertThat( findPosts.size() ).isEqualTo( posts.size() );
        assertThat( findPosts ).allMatch( post -> post.getPostType() == PostType.NORMAL );
        assertThat( postWithMostViews.getViewCount() ).isEqualTo( viewCount );
        assertThat( postWithMostViews ).isEqualTo( posts.get( posts.size() - 1 ) );
    }

    @Test
    @DisplayName( "최근에 생성된 순으로 게시물 찾기" )
    @SneakyThrows( InterruptedException.class )
    public void findPostByUpdateDateDesc() {
        //given
        postRepository.deleteAll();
        User user = User.builder().email( "test@email.com" ).userName( "test" ).build();
        testEntityManager.persist( user );

        int postCount = 5;
        for ( int i = 0; i < postCount; i++ ) {
            postRepository.saveAndFlush( new Post( new PostRegisterRequest( "post" + i, "post" + i ), user ) );
            Thread.sleep( 100L );
        }
        Sort createdDateDesc = Sort.by( Sort.Direction.DESC, "createdDate" );

        //when
        List<Post> findPosts = postRepository.findAll( createdDateDesc );

        //then
        LocalDateTime prev = findPosts.get( 0 ).getCreatedDate();
        for ( int i = 1; i < postCount; i++ ) {
            LocalDateTime now = findPosts.get( i ).getCreatedDate();
            assertThat( prev.isAfter( now ) ).isTrue();
            prev = now;
        }
    }

    @Test
    @DisplayName( "게시물 타입에 따라 페이징된 게시물 찾기" )
    public void findPostByPostTypeAndPaging() {
        //given
        int pageNum = 0, pageSize = 5;
        PageRequest pageRequest = PageRequest.of( pageNum, pageSize );

        //when
        Page<Post> findPosts = postRepository.findAll( pageRequest );
        Page<Post> findNormalPosts = postRepository.findPostByPostType( PostType.NORMAL, pageRequest );
        Page<Post> findSecretPosts = postRepository.findPostByPostType( PostType.SECRET, pageRequest );

        //then
        assertThat( findPosts.getContent().size() ).isEqualTo( pageSize ); //조회된 데이터 수
        assertThat( findPosts.getTotalElements() ).isEqualTo( posts.size() + secretPosts.size() ); //전체 데이터 수
        assertThat( findPosts.getNumber() ).isEqualTo( pageNum ); //페이지 번호
        assertThat( findPosts.getTotalPages() - 1 ).isEqualTo( ( posts.size() + secretPosts.size() ) / pageSize ); //전체 페이지 번호
        assertThat( findPosts.isFirst() ).isTrue();
        assertThat( findPosts.hasNext() ).isTrue();
        assertThat( findPosts.hasPrevious() ).isFalse();

        assertThat( findNormalPosts.getContent() ).allMatch( post -> post.getPostType() == PostType.NORMAL );
        assertThat( findNormalPosts.getContent().size() ).isLessThan( pageSize );
        assertThat( findNormalPosts.getTotalPages() - 1 ).isEqualTo( posts.size() / pageSize );

        assertThat( findSecretPosts.getContent() ).allMatch( post -> post.getPostType() == PostType.SECRET );
        assertThat( findSecretPosts.getContent().size() ).isLessThan( pageSize );
        assertThat( findSecretPosts.getTotalPages() - 1 ).isEqualTo( secretPosts.size() / pageSize );
    }

}
