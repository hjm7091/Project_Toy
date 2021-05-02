package jin.h.mun.rowdystory.domain.common;

import jin.h.mun.rowdystory.domain.PersistHelper;
import jin.h.mun.rowdystory.domain.account.User;
import jin.h.mun.rowdystory.dto.account.UserRegisterRequest;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BaseTimeFieldTest {

    private static PersistHelper persistHelper;

    private User user;

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
        user = new User( UserRegisterRequest.builder()
                .email( "hjm7091@naver.com" )
                .password( "1234" )
                .userName( "jin" )
                .picture( "picture1" )
                .build() );
    }

    @AfterEach
    public void tearDown() {
        persistHelper.deleteAll( User.class );
        assertThat( persistHelper.countRow( User.class ) ).isEqualTo( 0 );
    }

    @Test
    @DisplayName( "엔티티 저장시 엔티티의 생성 시간과 수정 시간은 같아야 한다." )
    public void whenPersistEntity() {
        //given
        Assertions.assertThat( user.getCreatedDate() ).isNull();
        Assertions.assertThat( user.getUpdatedDate() ).isNull();

        //when
        persistHelper.persist( user );

        //then
        LocalDateTime createdDate = user.getCreatedDate();
        LocalDateTime updatedDate = user.getUpdatedDate();
        assertThat( createdDate ).isNotNull();
        assertThat( updatedDate ).isNotNull();
        assertThat( createdDate ).isEqualTo( updatedDate );
    }

    @Test
    @DisplayName( "엔티티 업데이트시 엔티티의 생성 시간과 수정 시간은 달라야 한다." )
    @SneakyThrows( InterruptedException.class )
    public void whenUpdateEntity() {
        //given
        persistHelper.persist( user );
        LocalDateTime createdDate = user.getCreatedDate();

        //when
        Thread.sleep( 100L );
        persistHelper.update( user::changeUserName, "hak" );

        //then
        LocalDateTime updatedDate = user.getUpdatedDate();
        assertThat( createdDate ).isNotEqualTo( updatedDate );
    }

}