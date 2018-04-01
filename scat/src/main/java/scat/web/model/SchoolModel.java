package scat.web.model;

import scat.data.City;
import scat.data.SchoolType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
public class SchoolModel {

    @NotBlank
    private String name;

    private Integer number;

    @NotNull
    private SchoolType type;

    @NotNull
    private City city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public SchoolType getType() {
        return type;
    }

    public void setType(SchoolType type) {
        this.type = type;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
