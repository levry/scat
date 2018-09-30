package scat.web.model;

import scat.data.SchoolType;

import javax.validation.constraints.NotBlank;

/**
 * @author levry
 */
public class SchoolTypeModel {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SchoolType createSchoolType() {
        SchoolType type = new SchoolType();
        type.setName(name);
        return type;
    }
}
