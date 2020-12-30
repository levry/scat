package scat.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scat.data.City;
import scat.repo.CityRepository;
import scat.domain.service.dto.CityInput;
import scat.web.search.CitySearch;

/**
 * @author levry
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Iterable<City> findBy(CitySearch.CityCriteria criteria) {
        return repository.findBy(criteria);
    }

    @Transactional(readOnly = true)
    @Override
    public City findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public City save(CityInput data) {
        City city = new City();
        city.setName(data.getName());
        city.setRegion(data.getRegion());
        city.setCountry(data.getCountry());
        return repository.save(city);
    }

    @Override
    public City update(Long id, CityInput data) {
        City city = repository.getOne(id);
        city.setName(data.getName());
        city.setRegion(data.getRegion());
        city.setCountry(data.getCountry());
        return repository.save(city);
    }

    @Override
    public void remove(Long id) {
        repository.deleteById(id);
    }
}
