package scat.web.model;

import lombok.Getter;
import lombok.Setter;
import scat.data.City;
import scat.data.SchoolType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
@Getter
@Setter
public class SchoolModel {

    @NotBlank
    private String name;
    private Integer number;
    @NotNull
    private SchoolType type;
    @NotNull
    private City city;

}
