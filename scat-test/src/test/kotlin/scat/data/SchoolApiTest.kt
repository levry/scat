package scat.data

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import scat.ApiResource
import scat.ApiTest

/**
 * @author levry
 */
class SchoolApiTest: ApiTest("schools") {

    private val countries = ApiResource("countries")
    private val regions = ApiResource("regions")
    private val cities = ApiResource("cities")
    private val types = ApiResource("school_types")

    data class SchoolSource (
            val name: String,
            val number: Int,
            val type: String,
            val country: String,
            val region: String,
            val city: String
    )

    @Test
    fun `get school by id`() {
        val school = createSchool(SchoolSource(
                name = "School 14",
                number = 14,
                type = "School Type A",
                country = "Canada",
                region = "Ontario",
                city = "Toronto"
        ))

        val resource = resources.get<School>(school.id)

        Assertions.assertThat(resource).isEqualTo(school)
    }

    @Test
    fun `get schools`() {
        resources.testGetAndStatusOk()
    }

    @Test
    fun `post new school`() {
        val city = createCity("Canada A", "Quebec", "Quebec")
        val type = createType("School Type B")

        val school = resources.create<School>(SchoolData(
                name = "School 15", number = 15, type = type.id, city = city.id
        ))

        Assertions.assertThat(school.id).isNotNull()
        Assertions.assertThat(school).isEqualToIgnoringGivenFields(School(
                id = 0,
                name = "School 15",
                number = 15,
                type = Entity(id = type.id, name = "School Type B"),
                country = Entity(id = city.country.id, name = "Canada A"),
                region = Entity(id = city.region.id, name = "Quebec"),
                city = Entity(id = city.id, name = "Quebec")
        ), "id")
    }

    @Test
    fun `update school`() {
        val school = createSchool(SchoolSource(
                name = "School 16",
                number = 16,
                type = "School Type C",
                country = "Canada B",
                region = "Manitoba",
                city = "Winnipeg"
        ))
        val city = createCity("Canada C", "Alberta", "Edmonton")
        val type = createType("School Type D")

        val updated = resources.testUpdate<School>(school.id, SchoolData(
                name = "School 17", number = 17, type = type.id, city = city.id
        ))

        Assertions.assertThat(updated).isEqualTo(School(
                id = school.id,
                name = "School 17",
                number = 17,
                type = Entity(id = type.id, name = "School Type D"),
                country = Entity(id = city.country.id, name = "Canada C"),
                region = Entity(id = city.region.id, name = "Alberta"),
                city = Entity(id = city.id, name = "Edmonton")
        ))
    }

    @Test
    fun `delete school`() {
        val school = createSchool(SchoolSource(
                name = "School 18",
                number = 18,
                type = "School Type E",
                country = "Canada D",
                region = "British Columbia",
                city = "Victoria"
        ))

        resources.testDeleteAndStatusNoContent(school.id)
    }

    private fun createSchool(source: SchoolSource): School {
        val city = createCity(source.country, source.region, source.city)
        val type = createType(source.type)
        return resources.create<School>(SchoolData(
                name = source.name, number = source.number, type = type.id, city = city.id
        ))
    }

    private fun createCity(country: String, region: String, city: String): City {
        val countryResource = countries.create<Country>(CountryData(name = country))
        val regionResource = regions.create<Region>(RegionData(name = region, country = countryResource.id))
        return cities.create<City>(CityData(
                name = city,
                country = countryResource.id,
                region = regionResource.id
        ))
    }

    private fun createType(name: String): SchoolType = types.create<SchoolType>(SchoolTypeData(name = name))

}