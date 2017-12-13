package scat.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author levry
 */
@Entity
public class Region {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country")
    private Country country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Region region = (Region) o;
        return Objects.equals(id, region.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
