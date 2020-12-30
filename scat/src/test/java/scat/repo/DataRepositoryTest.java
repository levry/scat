package scat.repo;

import org.junit.jupiter.api.Test;
import scat.domain.model.City;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author levry
 */
class DataRepositoryTest {
    @Test
    void findOne_should_return_object_if_exists() {
        City expected = new City();

        DataRepository<City, Long> repository = mock(DataRepository.class, CALLS_REAL_METHODS);
        when(repository.findById(eq(81L))).thenReturn(Optional.of(expected));

        City actial = repository.findOne(81L);

        assertThat(actial).isEqualTo(expected);
    }

    @Test
    void findOne_should_throws_if_not_exists_object() {
        DataRepository<City, Long> repository = mock(DataRepository.class, CALLS_REAL_METHODS);
        when(repository.findById(eq(81L))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()  -> repository.findOne(81L));
    }
}