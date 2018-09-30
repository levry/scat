package scat.web.model;

import scat.data.Country;

import javax.validation.constraints.NotBlank;

/**
 * @author levry
 */
public class CountryModel {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country createCountry() {
        Country country = new Country();
        country.setName(name);
        return country;
    }
}
