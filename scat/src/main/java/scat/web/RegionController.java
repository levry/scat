package scat.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.Region;
import scat.repo.RegionRepository;
import scat.web.model.RegionModel;

import javax.validation.Valid;

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
    public Iterable<Region> index() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Region get(@PathVariable Integer id) {
        return repository.findOne(id);
    }

    @PostMapping
    public Region save(@Valid @RequestBody RegionModel model) {
        Region region = new Region();
        region.setName(model.getName());
        region.setCountry(model.getCountry());
        return repository.save(region);
    }

    @PutMapping("/{id}")
    public Region update(@PathVariable Integer id, @Valid @RequestBody Region model) {
        Region region = repository.getOne(id);
        region.setName(model.getName());
        region.setCountry(model.getCountry());
        return repository.save(region);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        repository.deleteById(id);
    }

}
