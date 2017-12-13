package scat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import scat.data.SchoolType;

/**
 * @author levry
 */
public interface SchoolTypeRepository extends JpaRepository<SchoolType, Integer> {
}
