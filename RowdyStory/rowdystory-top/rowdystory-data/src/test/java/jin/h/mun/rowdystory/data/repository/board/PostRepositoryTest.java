package jin.h.mun.rowdystory.data.repository.board;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.board.Post;
import jin.h.mun.rowdystory.domain.board.SecretPost;
import jin.h.mun.rowdystory.domain.board.enums.PostType;
import jin.h.mun.rowdystory.dto.post.PostRegisterRequest;
import jin.h.mun.rowdystory.dto.post.SecretPostRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

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

        int postCount = 50;

        for (int i = 1; i <= postCount; i++ ) {
            PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
                    .title( "post" + i )
                    .content( "this is normal post" + i )
                    .build();
            posts.add( new Post( postRegisterRequest, ownerOfPost) );

            SecretPostRegisterRequest secretPostRegisterRequest = SecretPostRegisterRequest.builder()
                    .postRegisterRequest( new PostRegisterRequest("secretPost" + i, "this is secret post" + i ) )
                    .anonymous( false ).password( Integer.toString( i ) ).build();
            secretPosts.add( new SecretPost( secretPostRegisterRequest, ownerOfPost) );
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
    public void findPostByType() {
        //when
        List<Post> findNormalPosts = postRepository.findByPostType( PostType.NORMAL );
        List<Post> findSecretPosts = postRepository.findByPostType( PostType.SECRET );

        //then
        assertThat( posts ).isEqualTo( findNormalPosts );
        assertThat( secretPosts ).isEqualTo( findSecretPosts );
    }

}
