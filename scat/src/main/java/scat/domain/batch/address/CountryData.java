package scat.domain.batch.address;

import lombok.Value;
import scat.domain.model.Country;

import java.util.Collection;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author levry
 */
@Value(staticConstructor = "of")
class CountryData {

    String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CountryData data = (CountryData) o;

        return name != null ? name.equalsIgnoreCase(data.name) : data.name == null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name.toLowerCase());
    }

    static CountryData of(Address address) {
        return of(address.getCountry());
    }

    static CountryData of(Country country) {
        return of(country.getName());
    }

    static Collection<String> toNames(Collection<CountryData> names) {
        return names.stream()
                .map(c -> c.name.toLowerCase())
                .collect(toList());
    }
}
