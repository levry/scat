package scat.data

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import scat.ApiResource
import scat.ApiTest

/**
 * @author levry
 */
class RegionApiTest: ApiTest("/regions") {

    private val countries = ApiResource("/countries")

    @Test
    fun `get region by id`() {
        val region = createRegion("Japan", "Shikoku")

        val resource = resources.get<Region>(region.id)

        Assertions.assertThat(resource).isEqualTo(region)
    }

    @Test
    fun `get regions`() {
        resources.testGetAndStatusOk()
    }

    @Test
    fun `post new region`() {
        val country = createCountry("Japan 2")

        val data = RegionData(name = "Kansai", country = country.id)
        val region = resources.create<Region>(data)

        Assertions.assertThat(region.id).isNotNull()
        Assertions.assertThat(region).isEqualToIgnoringGivenFields(
                Region(id = 0, name = data.name, country = country), "id"
        )
    }

    @Test
    fun `update region`() {
        val country = createCountry("Japan 3")
        val region = createRegion("Japan 4", "Chub")

        val updated = resources.testUpdate<Region>(region.id, RegionData(name = "Chubu", country = country.id))

        Assertions.assertThat(updated).isEqualTo(Region(id = region.id, name = "Chubu", country = country))
    }

    @Test
    fun `delete region`() {
        val region = createRegion("Japan 5", "Kanto")

        resources.testDeleteAndStatusNoContent(region.id)
    }

    private fun createCountry(name: String): Country {
        return countries.create<Country>(CountryData(name = name))
    }

    private fun createRegion(country: String, region: String): Region {
        val countryResource = createCountry(country)
        return resources.create<Region>(RegionData(name = region, country = countryResource.id))
    }

}
