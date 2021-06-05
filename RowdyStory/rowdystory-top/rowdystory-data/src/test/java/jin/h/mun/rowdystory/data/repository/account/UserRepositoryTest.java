package jin.h.mun.rowdystory.data.repository.account;

import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName( "repository 에서 유저의 이메일을 조건으로 해서 유저를 찾을 수 있어야함." )
    public void findByEmail() {
        //given
        String userEmail = "hjm7091@naver.com";
        User user = new User( UserRegisterRequest.builder()
                .email( userEmail )
                .password( "1234" )
                .userName( "jin" )
                .picture( "picture1" )
                .build() );
        testEntityManager.persist( user );

        //when
        Optional<User> findUser = userRepository.findByEmail( userEmail );

        //then
        assertThat( findUser.isPresent() ).isTrue();
        assertThat( findUser.get() ).isEqualTo( user );
    }

    @Test
    @DisplayName( "repository 에서 유저의 이메일과 소셜 타입을 조건으로 해서 유저를 찾을 수 있어야함." )
    public void findByEmailAndSocialType() {
        //given
        String userEmail = "hjm7091@naver.com";
        SocialType userSocialType = SocialType.GOOGLE;
        User user = User.builder()
                        .email( userEmail )
                        .password( "1234" )
                        .userName( "jin" )
                        .socialType( userSocialType )
                        .build();
        testEntityManager.persist( user );

        //when
        Optional<User> findUser = userRepository.findByEmailAndSocialType( userEmail, userSocialType );

        //then
        assertThat( findUser.isPresent() ).isTrue();
        assertThat( findUser.get() ).isEqualTo( user );
    }

    @Test
    @DisplayName( "유저 이름이 같은 유저는 여러명일 수 있다." )
    public void findByUserName() {
        //given
        String userName = "jin";
        User user1 = User.builder()
                .email( "hjm7091@naver.com" )
                .userName( userName )
                .build();
        User user2 = User.builder()
                .email( "hjm7091@daum.net" )
                .userName( userName )
                .build();
        testEntityManager.persist( user1 );
        testEntityManager.persist( user2 );

        //when
        List<User> findUsers = userRepository.findByUserName( userName );

        //then
        assertThat( findUsers.size() ).isEqualTo( 2 );
        assertThat( findUsers.contains( user1 ) ).isTrue();
        assertThat( findUsers.contains( user2 ) ).isTrue();
    }
}