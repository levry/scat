package scat.batch.address;

import org.springframework.stereotype.Component;
import scat.data.City;
import scat.data.Country;
import scat.data.Region;
import scat.repo.CityRepository;
import scat.repo.CountryRepository;
import scat.repo.RegionRepository;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.function.Function;

/**
 * @author levry
 */
@Component
public class AddressWriter {

    private CountryRepository countryRepository;
    private RegionRepository regionRepository;
    private CityRepository cityRepository;

    public AddressWriter(CountryRepository countryRepository,
                         RegionRepository regionRepository,
                         CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.cityRepository = cityRepository;
    }

    public void put(Collection<Address> input) {

        Set<CountryData> countries = new HashSet<>();
        Set<RegionData> regions = new HashSet<>();
        for(Address address : input) {
            countries.add(CountryData.of(address));
            RegionData region = RegionData.of(address);
            if (null != region) {
                regions.add(region);
            }
        }

        Map<CountryData, Country> countryMap = putCountries(countries);
        Map<RegionData, Region> regionMap = putRegions(regions, countryMap);

        putCities(input, countryMap, regionMap);
    }

    private Map<CountryData, Country> putCountries(Collection<CountryData> data) {
        return putData(data,
                countryRepository.findAllByNames(CountryData.toNames(data)),
                CountryData::of,
                this::createCountry);
    }

    private Country createCountry(final CountryData data) {
        Country country = new Country();
        country.setName(data.getName());
        return countryRepository.save(country);
    }

    private Map<RegionData, Region> putRegions(Set<RegionData> data, Map<CountryData, Country> countryMap) {
        return putData(data,
                regionRepository.findAllByNames(RegionData.toNames(data)),
                RegionData::of,
                createRegion(countryMap));
    }

    private Function<RegionData, Region> createRegion(Map<CountryData, Country> countries) {
        return (data) -> {
            Country country = countries.get(CountryData.of(data.getCountry()));

            Region region = new Region();
            region.setName(data.getName());
            region.setCountry(country);
            return regionRepository.save(region);
        };
    }

    private <D, T> Map<D, T> putData(Collection<D> input,
                                     List<T> existsData,
                                     Function<T, D> toData,
                                     Function<? super D, ? extends T> creator) {

        Map<D, T> result = new HashMap<>(input.size());
        for (T item : existsData) {
            D key = toData.apply(item);
            result.put(key, item);
        }
        input.removeAll(result.keySet());
        for(D key: input) {
            result.computeIfAbsent(key, creator);
        }
        return result;
    }

    private void putCities(Collection<Address> input,
                           Map<CountryData, Country> countries,
                           Map<RegionData, Region> regions) {
        for (Address address : input) {
            Region region = regions.get(RegionData.of(address));
            Country country = countries.get(CountryData.of(address));

            City city = new City();
            city.setCountry(country);
            city.setRegion(region);
            city.setName(address.city);
            boolean existsCity = hasCity(city);
            if(!existsCity) {
                cityRepository.save(city);
            }
        }
    }

    private boolean hasCity(City city) {
        return 0 != cityRepository.count((root, query, cb) -> {
            Predicate byName = cb.equal(cb.lower(root.get("name")), city.getName().toLowerCase());
            Predicate byCountry = cb.equal(root.get("country"), city.getCountry());
            Predicate byRegion = city.getRegion() != null ?
                    cb.equal(root.get("region"), city.getRegion()) :
                    cb.isNull(root.get("region"));
            return cb.and(byName, byCountry, byRegion);
        });
    }

}
