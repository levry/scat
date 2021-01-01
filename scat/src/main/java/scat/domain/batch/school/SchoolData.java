package scat.domain.batch.school;

import lombok.*;

/**
 * @author levry
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
