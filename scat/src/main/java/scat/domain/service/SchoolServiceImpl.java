package scat.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scat.domain.model.School;
import scat.adapter.persistence.SchoolJpaRepository;
import scat.domain.repo.SchoolRepository;
import scat.domain.service.dto.SchoolInput;

import javax.validation.Valid;

/**
 * @author levry
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Iterable<School> findBy(SchoolJpaRepository.SchoolCriteria criteria) {
        return repository.findBy(criteria);
    }

    @Transactional(readOnly = true)
    @Override
    public School findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public School save(@Valid SchoolInput data) {
        School school = new School();
        school.setName(data.getName());
        school.setNumber(data.getNumber());
        school.setType(data.getType());
        school.setCity(data.getCity());
        return repository.save(school);
    }

    @Override
    public School update(Long id, @Valid SchoolInput data) {
        School school = repository.findOne(id);
        school.setName(data.getName());
        school.setNumber(data.getNumber());
        school.setType(data.getType());
        school.setCity(data.getCity());
        return repository.save(school);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
