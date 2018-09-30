package scat.data

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import scat.ApiResource
import scat.ApiTest

/**
 * @author levry
 */
class CityApiTest: ApiTest("cities") {

    private val countries = ApiResource("/countries")
    private val regions = ApiResource("/regions")

    @Test
    fun `get city by id`() {
        val city = createCity("France A", "Ile-de-France", "Paris")

        val resource = resources.get<City>(city.id)

        Assertions.assertThat(resource).isEqualTo(city)
    }

    @Test
    fun `get cities`() {
        resources.testGetAndStatusOk()
    }

    @Test
    fun `post new city`() {
        val region = createRegion("France B", "Corsica")
        val country = region.country

        val city = resources.create<City>(CityData(
                name = "Corte",
                country = country.id,
                region = region.id
        ))


        Assertions.assertThat(city.id).isNotNull()
        Assertions.assertThat(city).isEqualToIgnoringGivenFields(
                City(
                        id = 0,
                        name = "Corte",
                        country = Entity(country.id, country.name),
                        region = Entity(id = region.id, name = region.name)
                ),
                "id"
        )
    }

    @Test
    fun `update city`() {
        val city = createCity("France C", "Brittany", "Rennes")
        val region = createRegion("France D", "Briton")

        val updated = resources.testUpdate<City>(city.id, CityData(
                name = "Rennes 2", country = region.country.id, region = region.id
        ))

        Assertions.assertThat(updated).isEqualTo(City(
                id = city.id,
                name = "Rennes 2",
                country = Entity(id = region.country.id, name = "France D"),
                region = Entity(id = region.id, name = "Briton")
        ))
    }

    @Test
    fun `delete city`() {
        val city = createCity("France F", "Martinique", "Marigot")

        resources.testDeleteAndStatusNoContent(city.id)
    }
    private fun createCountry(name: String): Country {
        return countries.create<Country>(CountryData(name = name))
    }

    private fun createRegion(country: String, region: String): Region {
        val countryResource = createCountry(country)
        return regions.create<Region>(RegionData(name = region, country = countryResource.id))
    }

    private fun createCity(country: String, region: String, city: String): City {
        val regionResource = createRegion(country, region)
        return resources.create<City>(CityData(
                name = city,
                country = regionResource.country.id,
                region = regionResource.id
        ))
    }
}