package scat.batch.school;

import lombok.Getter;
import lombok.Setter;

/**
 * @author levry
 */
@Getter
@Setter
public class SchoolData {

    private String name;
    private Integer number;
    private String type;
    private String country;
    private String region;
    private String city;

    String keyCity() {
        return (country + region + city).toLowerCase();
    }
}
