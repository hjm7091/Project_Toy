package jin.h.mun.rowdystory.data.repository.board;

import jin.h.mun.rowdystory.domain.board.Post;
import jin.h.mun.rowdystory.domain.board.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByPostType( PostType postType );

}
