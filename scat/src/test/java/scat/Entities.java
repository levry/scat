package scat;

import scat.data.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

/**
 * @author levry
 */
public class Entities {

    private final EntityManager entityManager;

    public Entities(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Country country(String name) {
        return findCountry(name).orElseGet(() -> newCountry(name));
    }

    private Country newCountry(String name) {
        Country country = new Country();
        country.setName(name);
        return persist(country);
    }

    private Optional<Country> findCountry(String name) {
        try {
            return Optional.of(entityManager
                .createQuery("SELECT c FROM Country c WHERE LOWER(c.name) = LOWER(:name)", Country.class)
                .setParameter("name", name)
                .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private <T> T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public Region region(String name, Country country) {
        Region region = new Region();
        region.setName(name);
        region.setCountry(country);
        return persist(region);
    }

    public City city(String name, Region region, Country country) {
        City city = new City();
        city.setName(name);
        city.setRegion(region);
        city.setCountry(country);
        return persist(city);
    }

    public City city(String name, String countryName) {
        return city(name, null, countryName);
    }

    public City city(String name, String regionName, String countryName) {
        Country country = country(countryName);

        City city = new City();
        city.setName(name);
        if(null != regionName) {
            city.setRegion(region(regionName, country));
        }
        city.setCountry(country);
        return persist(city);
    }

    public SchoolType schoolType(String name) {
        SchoolType schoolType = new SchoolType();
        schoolType.setName(name);
        return persist(schoolType);
    }

    public School school(String name, String type, City city) {
        School school = new School();
        school.setName(name);
        school.setType(schoolType(type));
        school.setCity(city);
        return persist(school);
    }

    public School school(String name, int number, String type, City city) {
        School school = new School();
        school.setName(name);
        school.setNumber(number);
        school.setType(schoolType(type));
        school.setCity(city);
        return persist(school);
    }


}
