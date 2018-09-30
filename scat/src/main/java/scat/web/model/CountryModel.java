package scat.web.model;

import lombok.Getter;
import lombok.Setter;
import scat.data.Country;

import javax.validation.constraints.NotBlank;

/**
 * @author levry
 */
@Getter
@Setter
public class CountryModel {

    @NotBlank
    private String name;

    public Country createCountry() {
        Country country = new Country();
        country.setName(name);
        return country;
    }
}
