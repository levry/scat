package scat.batch.address;

import org.springframework.core.style.ToStringCreator;

/**
 * @author levry
 */
public class Address {

    public String country;
    public String region;
    public String city;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("country", country)
                .append("region", region)
                .append("city", city)
                .toString();
    }

}
