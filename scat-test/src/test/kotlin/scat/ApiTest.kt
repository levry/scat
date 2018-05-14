package scat

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeAll

/**
 * @author levry
 */
abstract class ApiTest(val resources: ApiResource) {

    constructor(path: String) : this(ApiResource(path))

    @BeforeAll
    fun setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

}