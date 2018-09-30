package scat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityNotFoundException;

/**
 * @author levry
 */
@NoRepositoryBean
public interface DataRepository<T, I> extends JpaRepository<T, I> {

    default T findOne(I id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
