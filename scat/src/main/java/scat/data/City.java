package scat.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
@Data
@Entity
@EqualsAndHashCode(of = "id")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
