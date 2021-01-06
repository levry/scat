package scat.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author levry
 */
@Data
@Entity
@Table(name = "SCHOOLTYPE")
@EqualsAndHashCode(of = "id")
public class SchoolType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private String name;

}
