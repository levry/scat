package scat.domain.service.dto;

import lombok.Getter;
import lombok.Setter;
import scat.domain.model.Country;

import javax.validation.constraints.NotBlank;

/**
 * @author levry
 */
@Getter
@Setter
public class CountryInput {

    @NotBlank
    private String name;

    public Country createCountry() {
        Country country = new Country();
        country.setName(name);
        return country;
    }
}
