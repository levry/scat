package scat.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scat.domain.model.Region;
import scat.adapter.persistence.RegionJpaRepository;
import scat.domain.repo.RegionRepository;
import scat.domain.service.dto.RegionInput;

/**
 * @author levry
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository repository;

    @Override
    public Iterable<Region> findBy(RegionJpaRepository.RegionCriteria criteria) {
        return repository.findBy(criteria);
    }

    @Override
    public Region findOne(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public Region save(RegionInput data) {
        Region region = new Region();
        region.setName(data.getName());
        region.setCountry(data.getCountry());
        return repository.save(region);
    }

    @Override
    public Region update(Integer id, RegionInput data) {
        Region region = repository.findOne(id);
        region.setName(data.getName());
        region.setCountry(data.getCountry());
        return repository.save(region);
    }

    @Override
    public void remove(Integer id) {
        repository.deleteById(id);
    }

}
