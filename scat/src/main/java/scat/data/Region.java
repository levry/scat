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
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country")
    private Country country;

}
