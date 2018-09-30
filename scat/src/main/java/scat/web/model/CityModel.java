package scat.web.model;

import lombok.Getter;
import lombok.Setter;
import scat.data.Country;
import scat.data.Region;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
@Getter
@Setter
public class CityModel {

    @NotBlank
    private String name;
    private Region region;
    @NotNull
    private Country country;

}
