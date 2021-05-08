package jin.h.mun.rowdystory.data.config;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.data.repository.board.PostRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.board.Post;
import jin.h.mun.rowdystory.domain.board.SecretPost;
import jin.h.mun.rowdystory.dto.post.PostRegisterRequest;
import jin.h.mun.rowdystory.dto.post.SecretPostRegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class DataConfig {

    @Bean
    public CommandLineRunner initSampleData( final UserRepository userRepository, final PostRepository postRepository ) {
        return args -> {

            Objects.requireNonNull( userRepository );
            Objects.requireNonNull( postRepository );

            User admin = User.builder()
                    .email( "hjm7091@naver.com" )
                    .userName( "admin" )
                    .password( "1234" )
                    .roleType( RoleType.ADMIN )
                    .build();

            userRepository.save( admin );

            Post normalPost = new Post( new PostRegisterRequest( "normal post", "this is normal post" ), admin );

            SecretPostRegisterRequest secretPostRegisterRequest = SecretPostRegisterRequest.builder()
                    .postRegisterRequest( new PostRegisterRequest( "secret post", "this is secret post" ) )
                    .anonymous( false ).password( "11111" ).build();
            SecretPost secretPost = new SecretPost( secretPostRegisterRequest, admin );

            postRepository.save( normalPost );
            postRepository.save( secretPost );
        };
    }

}
