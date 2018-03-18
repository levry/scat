package scat.batch.school;

/**
 * @author levry
 */
public class SchoolWriteResult {

    private long citiesMissed;
    private long typesAdded;
    private long schoolsAdded;
    private long schoolsExists;

    public long getCitiesMissed() {
        return citiesMissed;
    }

    public long getTypesAdded() {
        return typesAdded;
    }

    public long getSchoolsAdded() {
        return schoolsAdded;
    }

    public long getSchoolsExists() {
        return schoolsExists;
    }

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
