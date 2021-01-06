package scat.domain.batch.school;

import lombok.Getter;

/**
 * @author levry
 */
@Getter
public class SchoolWriteResult {

    private long citiesMissed;
    private long typesAdded;
    private long schoolsAdded;
    private long schoolsExists;

    void cityMissed() {
        citiesMissed++;
    }

    void typeAdded() {
        typesAdded++;
    }

    void schoolAdded(boolean added) {
        if (added) {
            schoolsAdded++;
        } else {
            schoolsExists++;
        }
    }

}
