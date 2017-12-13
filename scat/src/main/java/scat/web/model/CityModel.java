package scat.web.model;

import org.hibernate.validator.constraints.NotBlank;
import scat.data.Country;
import scat.data.Region;

import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
public class CityModel {

    @NotBlank
    private String name;

    private Region region;

    @NotNull
    private Country country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
