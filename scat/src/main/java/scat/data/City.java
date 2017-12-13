package scat.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author levry
 */
@Entity
public class City {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "country")
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region")
    private Region region;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        City city = (City) o;
        return Objects.equals(id, city.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
