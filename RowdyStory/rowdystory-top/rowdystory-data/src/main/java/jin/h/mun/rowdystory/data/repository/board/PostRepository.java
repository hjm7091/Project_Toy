package jin.h.mun.rowdystory.data.repository.board;

import com.sun.istack.NotNull;
import jin.h.mun.rowdystory.domain.board.Post;
import jin.h.mun.rowdystory.domain.board.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostByPostType( PostType postType );

    List<Post> findPostByPostType( PostType postType, Sort sort );

    Page<Post> findPostByPostType( PostType postType, Pageable pageable );
}
