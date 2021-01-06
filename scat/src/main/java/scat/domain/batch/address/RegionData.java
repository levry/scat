package scat.domain.batch.address;

import org.springframework.util.StringUtils;
import scat.domain.model.Region;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * @author levry
 */
class RegionData {

    private final String country;
    private final String name;
    private final int hashCode;

    private RegionData(String country, String name) {
        this.country = country;
        this.name = name;
        this.hashCode = Objects.hashCode((country + '_' + name).toLowerCase());
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RegionData data = (RegionData) o;

        return country.equalsIgnoreCase(data.country)
                && name.equalsIgnoreCase(data.name);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "Region{" + country + ", " + name + "}";
    }

    static RegionData of(Address address) {
        if (!StringUtils.hasText(address.getRegion())) {
            return null;
        }
        return new RegionData(address.getCountry(), address.getRegion());
    }

    static RegionData of(Region region) {
        return new RegionData(region.getCountry().getName(), region.getName());
    }

    static Collection<String> toNames(Set<RegionData> data) {
        return data.stream()
                .filter(r -> !StringUtils.isEmpty(r.name))
                .map(r -> (r.country + ' ' + r.name).toLowerCase())
                .collect(toList());
    }
}
