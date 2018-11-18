package scat.batch.address;

import lombok.*;

/**
 * @author levry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String country;
    private String region;
    private String city;

}
