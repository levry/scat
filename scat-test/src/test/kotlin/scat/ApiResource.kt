package scat

import io.restassured.RestAssured.*
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification

/**
 * @author levry
 */
class ApiResource(val path: String) {

    private val spec: RequestSpecification = RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .addFilter(ResponseLoggingFilter())
            .addFilter(RequestLoggingFilter())
            .build()


    inline fun <reified T> create(data: Any): T =
        testCreateAndStatusOk(data).extract().`as`(T::class.java)

    fun create(data: Any) = testCreateAndStatusOk(data)

    inline fun <reified T> get(id: Any?): T? =
            get("$path/{id}", id).then().statusCode(200).extract().`as`(T::class.java)


    // tests

    fun testGetAndStatusOk(): ValidatableResponse =
            given(spec).get(path).then().statusCode(200)

    fun testCreate(data: Any): ValidatableResponse =
            given(spec).body(data).post(path).then()

    fun testCreateAndStatusOk(data: Any): ValidatableResponse =
            testCreate(data).statusCode(200)

    fun testDeleteAndStatusNoContent(id: Number): ValidatableResponse =
            given(spec).delete("$path/{id}", id).then().statusCode(204)

    fun testUpdateAnsStatusOk(id: Number, data: Any): ValidatableResponse =
            given(spec).body(data).put("$path/{id}", id).then().statusCode(200)

    inline fun <reified T> testUpdate(id: Number, data: Any): T =
            testUpdateAnsStatusOk(id, data).extract().`as`(T::class.java)

}