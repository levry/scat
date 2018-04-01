package scat.web.model;

import scat.data.Country;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
public class RegionModel {

    @NotBlank
    private String name;
    @NotNull
    private Country country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
