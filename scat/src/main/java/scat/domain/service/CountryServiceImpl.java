package scat.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scat.data.Country;
import scat.repo.CountryRepository;
import scat.domain.service.dto.CountryInput;

/**
 * @author levry
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Iterable<Country> findBy(CountryInput criteria) {
        Country example = criteria.createCountry();
        return repository.findBy(example);
    }

    @Transactional(readOnly = true)
    @Override
    public Country findOne(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public Country save(CountryInput data) {
        Country country = data.createCountry();
        return repository.save(country);
    }

    @Override
    public Country update(Integer id, CountryInput data) {
        Country country = repository.getOne(id);
        country.setName(data.getName());
        return repository.save(country);
    }

    @Override
    public void remove(Integer id) {
        repository.deleteById(id);
    }
}
