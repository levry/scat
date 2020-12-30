package scat.domain.service.dto;

import lombok.Getter;
import lombok.Setter;
import scat.domain.model.City;
import scat.domain.model.SchoolType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
@Getter
@Setter
public class SchoolInput {

    @NotBlank
    private String name;
    private Integer number;
    @NotNull
    private SchoolType type;
    @NotNull
    private City city;

}
