package apiauto;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ReqresApiTest {

    @BeforeClass //Setup or define base URL
    public void baseURL() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test //Positive Test 1: GET request for the list of user page 2
    public void testGetUserSuccess() {
        RestAssured
                .given()
                .header("x-api-key", "reqres-free-v1").when()
                .get("users?page=2")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(2))
                .assertThat().body("data", Matchers.not(Matchers.empty()));
    }

    @Test //Positive Test 2: GET request for the specific user by ID (single user)
    public void testGetSpecificUserSuccess() {
        RestAssured
                .given()
                .header("x-api-key", "reqres-free-v1").when()
                .get("users/5")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("data.id", Matchers.equalTo(5))
                .assertThat().body("data", Matchers.not(Matchers.empty()));
    }

    @Test //Positive Test 3: POST request to create user
    public void testCreateUserSuccess() {
        String requestBody = "{ \"name\": \"Agus Sumaryanto\", \"job\": \"QA Engineer\" }";

        RestAssured
                .given()
                .header("x-api-key", "reqres-free-v1")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody).when()
                .post("/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo("Agus Sumaryanto"))
                .assertThat().body("job", Matchers.equalTo("QA Engineer"))
                .assertThat().body("id", Matchers.notNullValue())
                .assertThat().body("createdAt", Matchers.notNullValue());
    }

    @Test //Negative Test 1: GET request for non-existent user / invalid ID
    public void testGetUserNotFound() {
        RestAssured
                .given()
                .header("x-api-key", "reqres-free-v1").when()
                .get("api/users/23")
                .then().log().all()
                .assertThat().statusCode(404)
                .assertThat().body("id", Matchers.not(Matchers.empty()));
    }

    @Test //Negative Test 2: POST request with missing fields (login without password)
    public void testCreateUserMissingFields() {
        String requestBody = "{ \"email\": \"peter@klaven\"}";

        RestAssured
                .given()
                .header("x-api-key", "reqres-free-v1")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody).when()
                .post("/login")
                .then().log().all()
                .assertThat().statusCode(400)
                .assertThat().body("error", Matchers.containsString("Missing password"));
    }

    @Test //Negative Test 3: DELETE request for non-existent user / invalid ID
    public void testDeleteNonExistingUser() {
        RestAssured
                .given()
                .header("x-api-key", "reqres-free-v1")
                .when()
                .delete("users/9999")
                .then().log().all()
                .assertThat().statusCode(204) // return to: 204 No Content
                .assertThat().body(Matchers.emptyOrNullString());
    }

}
