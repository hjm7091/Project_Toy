package jin.h.mun.rowdystory.web.config;

import jin.h.mun.rowdystory.data.repository.account.UserRepository;
import jin.h.mun.rowdystory.data.repository.board.PostRepository;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.domain.account.enums.RoleType;
import jin.h.mun.rowdystory.domain.account.enums.SocialType;
import jin.h.mun.rowdystory.domain.board.Post;
import jin.h.mun.rowdystory.domain.board.SecretPost;
import jin.h.mun.rowdystory.dto.post.PostRegisterRequest;
import jin.h.mun.rowdystory.dto.post.SecretPostRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class DataConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner testData( final UserRepository userRepository, final PostRepository postRepository ) {
        return args -> {

            User admin = User.builder()
                    .email( "admin" )
                    .userName( "admin" )
                    .password( passwordEncoder.encode( "123456" ) )
                    .roleType( RoleType.ADMIN )
                    .build();

            User jin = User.builder()
                    .email( "hjm7091@naver.com" )
                    .userName( "jin" )
                    .password( passwordEncoder.encode("123456") )
                    .roleType( RoleType.USER )
                    .socialType( SocialType.GOOGLE )
                    .build();

            userRepository.save( admin );
            userRepository.save( jin );

            Post normalPost = new Post( new PostRegisterRequest( "normal post", "this is normal post" ), admin );

            SecretPostRegisterRequest secretPostRegisterRequest = SecretPostRegisterRequest.builder()
                    .postRegisterRequest( new PostRegisterRequest( "secret post", "this is secret post" ) )
                    .anonymous( false ).password( passwordEncoder.encode( "123456" ) ).build();
            SecretPost secretPost = new SecretPost( secretPostRegisterRequest, admin );

            postRepository.save( normalPost );
            postRepository.save( secretPost );
        };
    }

}
