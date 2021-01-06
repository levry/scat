package scat.adapter.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.domain.model.Region;
import scat.domain.service.RegionService;
import scat.domain.service.dto.RegionInput;

import javax.validation.Valid;

import static scat.adapter.persistence.RegionJpaRepository.RegionCriteria;

/**
 * @author levry
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("regions")
public class RegionController {

    private final RegionService service;

    @GetMapping
    public Iterable<Region> index(@ModelAttribute RegionCriteria criteria) {
        return service.findBy(criteria);
    }

    @GetMapping("/{id}")
    public Region get(@PathVariable Integer id) {
        return service.findOne(id);
    }

    @PostMapping
    public Region save(@Valid @RequestBody RegionInput data) {
        return service.save(data);
    }

    @PutMapping("/{id}")
    public Region update(@PathVariable Integer id, @Valid @RequestBody RegionInput data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.remove(id);
    }

}
