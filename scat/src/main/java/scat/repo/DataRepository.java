package scat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityNotFoundException;

/**
 * @author levry
 */
@NoRepositoryBean
public interface DataRepository<T, ID> extends JpaRepository<T, ID> {

    default T findOne(ID id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
