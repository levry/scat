package scat.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scat.domain.model.City;
import scat.domain.repo.CityRepository;
import scat.domain.service.dto.CityInput;

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
    public Iterable<City> findBy(CityRepository.CityCriteria criteria) {
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
        City city = repository.findOne(id);
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
