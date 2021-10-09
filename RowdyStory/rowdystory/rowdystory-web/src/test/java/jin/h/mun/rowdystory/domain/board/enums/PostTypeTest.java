package jin.h.mun.rowdystory.domain.board.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTypeTest {

    @Test
    @DisplayName( "PostType 의 name 테스트" )
    public void values() {
        PostType.Values values = new PostType.Values();
        assertThat( values ).isNotNull();
        assertThat( PostType.Values.NORMAL ).isEqualTo( PostType.NORMAL.getName() );
        assertThat( PostType.Values.SECRET ).isEqualTo( PostType.SECRET.getName() );
    }

}