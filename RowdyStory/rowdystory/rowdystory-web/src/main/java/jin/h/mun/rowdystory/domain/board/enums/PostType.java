package jin.h.mun.rowdystory.domain.board.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostType {

    NORMAL( Values.NORMAL, "일반 게시물" ),
    SECRET( Values.SECRET, "비밀 게시물" );

    @Getter
    private final String name;

    @Getter
    private final String description;

    public static class Values {
        public static final String NORMAL = "NORMAL";
        public static final String SECRET = "SECRET";
    }
}
