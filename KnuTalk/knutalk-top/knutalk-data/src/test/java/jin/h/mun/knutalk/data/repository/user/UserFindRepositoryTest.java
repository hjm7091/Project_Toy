package jin.h.mun.knutalk.data.repository.user;

import jin.h.mun.knutalk.domain.account.User;
import jin.h.mun.knutalk.domain.account.enums.SocialType;
import jin.h.mun.knutalk.dto.account.UserRegisterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith( SpringRunner.class )
@DataJpaTest
public class UserFindRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserFindRepository userFindRepository;

    @Test
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
        Optional<User> findUser = userFindRepository.findByEmail( userEmail );

        //then
        assertThat( findUser.isPresent() ).isTrue();
        assertThat( findUser.get() ).isEqualTo( user );
    }

    @Test
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
        Optional<User> findUser = userFindRepository.findByEmailAndSocialType( userEmail, userSocialType );

        //then
        assertThat( findUser.isPresent() ).isTrue();
        assertThat( findUser.get() ).isEqualTo( user );
    }
}