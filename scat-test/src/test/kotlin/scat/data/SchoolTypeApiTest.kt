package scat.data

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import scat.ApiTest


/**
 * @author levry
 */
class SchoolTypeApiTest : ApiTest("/school_types") {

    @Test
    fun `get school type`() {
        val schoolType = resources.create<SchoolType>(SchoolTypeData(name = "Academy"))

        val resource = resources.get<SchoolType>(schoolType.id)

        Assertions.assertThat(resource).isEqualTo(schoolType)
    }

    @Test
    fun `get school types`() {
        resources.testGetAndStatusOk()
    }

    @Test
    fun `post new shool type`() {
        val data = SchoolTypeData(
                name = "University"
        )

        val schoolType = resources.create<SchoolType>(data)

        Assertions.assertThat(schoolType.id).isNotNull()
        Assertions.assertThat(schoolType).isEqualToIgnoringGivenFields(data, "id")
    }

    @Test
    fun `should bad request if create duplicate by name`() {
        resources.create(SchoolTypeData(name = "School"))

        resources.testCreate(SchoolTypeData(name = "School")).statusCode(400)
    }

    @Test
    fun `update school type`() {
        val schoolType = resources.create<SchoolType>(SchoolTypeData(name = "Uni"))

        val resource = resources.testUpdate<SchoolType>(schoolType.id, SchoolTypeData(name = "University"))

        Assertions.assertThat(resource).isEqualTo(SchoolType(id = schoolType.id, name = "University"))
    }

    @Test
    fun `delete school type by id`() {
        val schoolType = resources.create<SchoolType>(SchoolTypeData(name = "Colledge"))

        resources.testDeleteAndStatusNoContent(schoolType.id)
    }

}