package scat.web.model;

import lombok.Getter;
import lombok.Setter;
import scat.data.SchoolType;

import javax.validation.constraints.NotBlank;

/**
 * @author levry
 */
@Getter
@Setter
public class SchoolTypeModel {

    @NotBlank
    private String name;

    public SchoolType createSchoolType() {
        SchoolType type = new SchoolType();
        type.setName(name);
        return type;
    }
}
