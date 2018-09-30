package scat.web.model;

import lombok.Getter;
import lombok.Setter;
import scat.data.Country;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
@Getter
@Setter
public class RegionModel {

    @NotBlank
    private String name;
    @NotNull
    private Country country;

}
