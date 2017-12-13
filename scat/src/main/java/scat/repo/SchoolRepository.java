package scat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import scat.data.School;

/**
 * @author levry
 */
public interface SchoolRepository extends JpaRepository<School, Long> {
}
