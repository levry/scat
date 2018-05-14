package scat.data

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import scat.ApiTest

/**
 * @author levry
 */
class CountryApiTest : ApiTest("/countries") {

    @Test
    fun `get country by id`() {
        val country = resources.create<Country>(CountryData(name = "Great Britain"))

        val resource = resources.get<Country>(country.id)

        Assertions.assertThat(resource).isEqualTo(country)
    }

    @Test
    fun `get countries`() {
        resources.testGetAndStatusOk()
    }

    @Test
    fun `post new country`() {
        val data = CountryData(name = "Russia")

        val country = resources.create<Country>(data)

        Assertions.assertThat(country.id).isNotNull()
        Assertions.assertThat(country).isEqualToIgnoringGivenFields(data, "id")
    }

    @Test
    fun `should bad request if create duplicate by-name country`() {
        resources.create(CountryData(name = "Australia"))

        resources.testCreate(CountryData(name = "Australia")).statusCode(400)
    }

    @Test
    fun `update country`() {
        val country = resources.create<Country>(CountryData(name = "Fran"))

        val updated = resources.testUpdate<Country>(country.id, CountryData(name = "France"))

        Assertions.assertThat(updated).isEqualTo(Country(id = country.id, name = "France"))
    }

    @Test
    fun `delete country by id`() {
        val country = resources.create<Country>(CountryData(name = "Burgu"))

        resources.testDeleteAndStatusNoContent(country.id)
    }

}
