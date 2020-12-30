package scat.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scat.domain.model.SchoolType;
import scat.repo.SchoolTypeRepository;
import scat.domain.service.dto.SchoolTypeInput;

/**
 * @author levry
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SchoolTypeServiceImpl implements SchoolTypeService {

    private final SchoolTypeRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Iterable<SchoolType> findBy(SchoolTypeInput criteria) {
        return repository.findBy(criteria.createSchoolType());
    }

    @Transactional(readOnly = true)
    @Override
    public SchoolType findOne(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public SchoolType save(SchoolTypeInput data) {
        SchoolType type = data.createSchoolType();
        return repository.save(type);
    }

    @Override
    public SchoolType update(Integer id, SchoolTypeInput data) {
        SchoolType schoolType = repository.getOne(id);
        schoolType.setName(data.getName());
        return repository.save(schoolType);
    }

    @Override
    public void remove(Integer id) {
        repository.deleteById(id);
    }
}
