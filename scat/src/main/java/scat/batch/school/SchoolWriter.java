package scat.batch.school;

import org.springframework.stereotype.Component;
import scat.data.City;
import scat.data.School;
import scat.data.SchoolType;
import scat.repo.CityRepository;
import scat.repo.SchoolRepository;
import scat.repo.SchoolTypeRepository;

import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author levry
 */
// TODO fix cvs source: собрать правильный csv (с экранированием)
@Component
public class SchoolWriter {

    private final SchoolTypeRepository schoolTypes;
    private final CityRepository cities;
    private final SchoolRepository schools;

    public SchoolWriter(SchoolTypeRepository schoolTypes,
                        CityRepository cities,
                        SchoolRepository schools) {
        this.schoolTypes = schoolTypes;
        this.cities = cities;
        this.schools = schools;
    }

    public SchoolWriteResult put(Iterable<SchoolData> input) {

        SchoolWriteResult result = new SchoolWriteResult();

        Function<SchoolData, City> cityFind = cityFinder();
        Function<String, SchoolType> typeFind = typeFinder(result);

        for (SchoolData data : input) {
            City city = cityFind.apply(data);
            if(null == city) {
                result.cityMissed();
                continue;
            }

            SchoolType type = typeFind.apply(data.getType());

            boolean schoolAdded = addSchool(data, city, type);
            result.schoolAdded(schoolAdded);
        }

        return result;
    }

    private Function<SchoolData, City> cityFinder() {

        final City notFound = new City();
        notFound.setId(-1L);

        Map<String, City> cities = new HashMap<>();

        return data -> {
            City city = cities.computeIfAbsent(data.keyCity(), key -> {
                City exists = findCityByData(data);
                return null == exists ? notFound : exists;
            });

            return notFound == city ? null : city;
        };
    }

    private City findCityByData(SchoolData data) {
        return cities.findOne((root, query, cb) -> {
            String city = data.getCity();
            Predicate byName = cb.equal(
                cb.lower(root.get("name")), city.toLowerCase()
            );

            String country = data.getCountry();
            Predicate byCountry = cb.equal(
                cb.lower(root.join("country").get("name")), country.toLowerCase()
            );

            String region = data.getRegion();
            Predicate byRegion = null != region ?
                cb.equal(
                    cb.lower(root.join("region").get("name")), region.toLowerCase()
                ) :
                cb.isNull(root.get("region"));

            return cb.and(byName, byCountry, byRegion);
        });
    }

    private Function<String, SchoolType> typeFinder(SchoolWriteResult result) {

        final Map<String, SchoolType> types = schoolTypes.findAll().stream()
                .collect(Collectors.toMap(
                    type -> type.getName().toLowerCase(),
                    Function.identity()
                ));

        return name -> {
            String key = name.toLowerCase();
            return types.computeIfAbsent(key, n -> {
                result.typeAdded();
                return createSchoolType(name);
            });
        };
    }

    private SchoolType createSchoolType(String name) {
        SchoolType schoolType = new SchoolType();
        schoolType.setName(name);
        schoolTypes.save(schoolType);
        return schoolType;
    }

    private boolean addSchool(SchoolData data, City city, SchoolType type) {
        if (hasSchool(data, city)) {
            return false;
        }

        School school = new School();
        school.setName(data.getName());
        school.setNumber(data.getNumber());
        school.setCity(city);
        school.setType(type);
        schools.save(school);
        return true;
    }

    private boolean hasSchool(SchoolData data, City city) {
        return 0 != schools.count((root, query, cb) -> {
            Predicate byName = cb.equal(cb.lower(root.get("name")), data.getName().toLowerCase());
            Predicate byCity = cb.equal(root.get("city"), city);
            return cb.and(byName, byCity);
        });
    }
}
