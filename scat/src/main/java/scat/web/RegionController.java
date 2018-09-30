package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.Region;
import scat.repo.RegionRepository;
import scat.web.model.RegionModel;
import scat.web.search.RegionSearch;

import javax.validation.Valid;

import static scat.web.search.RegionSearch.*;

/**
 * @author levry
 */
@RestController
@RequestMapping("regions")
public class RegionController {

    private final RegionRepository repository;

    public RegionController(RegionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<Region> index(@ModelAttribute RegionCriteria criteria) {
        return new RegionSearch(repository).findBy(criteria);
    }

    @GetMapping("/{id}")
    public Region get(@PathVariable Integer id) {
        return repository.findOne(id);
    }

    @PostMapping
    public Region save(@Valid @RequestBody RegionModel data) {
        Region region = new Region();
        region.setName(data.getName());
        region.setCountry(data.getCountry());
        return repository.save(region);
    }

    @PutMapping("/{id}")
    public Region update(@PathVariable Integer id, @Valid @RequestBody RegionModel data) {
        Region region = repository.getOne(id);
        region.setName(data.getName());
        region.setCountry(data.getCountry());
        return repository.save(region);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }

}
