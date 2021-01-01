package scat.domain.batch.school;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import scat.domain.model.City;
import scat.domain.model.School;
import scat.domain.model.SchoolType;
import scat.domain.repo.CityRepository;
import scat.domain.repo.SchoolRepository;
import scat.domain.repo.SchoolTypeRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author levry
 */
@Component
@AllArgsConstructor
public class SchoolWriter {

    private final SchoolTypeRepository schoolTypes;
    private final CityRepository cityRepository;
    private final SchoolRepository schoolRepository;

    public SchoolWriteResult put(Iterable<SchoolData> input) {

        SchoolWriteResult result = new SchoolWriteResult();

        Function<SchoolData, City> cityFind = cityFinder();
        Function<SchoolData, SchoolType> typeFind = typeFinder(result);

        for (SchoolData data : input) {
            City city = cityFind.apply(data);
            if(null == city) {
                result.cityMissed();
                continue;
            }

            SchoolType type = typeFind.apply(data);

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
            City city = cities.computeIfAbsent(data.keyCity(),
                    key -> cityRepository.findBy(data).orElse(notFound));

            return notFound == city ? null : city;
        };
    }

    private Function<SchoolData, SchoolType> typeFinder(SchoolWriteResult result) {

        final Map<String, SchoolType> types = schoolTypes.findAll().stream()
                .collect(Collectors.toMap(
                    type -> type.getName().toLowerCase(),
                    Function.identity()
                ));

        return data -> {
            String name = data.getType();
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
        schoolRepository.save(school);
        return true;
    }

    private boolean hasSchool(SchoolData data, City city) {
        return schoolRepository.hasSchool(data.getName(), city);
    }

}
