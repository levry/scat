package scat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scat.data.City;
import scat.domain.service.CityService;
import scat.domain.service.dto.CityInput;

import javax.validation.Valid;

import static scat.web.search.CitySearch.CityCriteria;

/**
 * @author levry
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService service;

    @GetMapping
    public Iterable<City> index(@ModelAttribute CityCriteria criteria) {
        return service.findBy(criteria);
    }

    @GetMapping("/{id}")
    public City get(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PostMapping
    public City save(@Valid @RequestBody CityInput data) {
        return service.save(data);
    }

    @PutMapping("/{id}")
    public City update(@PathVariable Long id, @Valid @RequestBody CityInput data) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.remove(id);
    }

}
