package apiauto;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;

public class TestReqres {

    @Test
    public void  testGetListUsers() {

        File jsonSchema = new File("src/test/resources/jsonSchema/getListUserSchema.json");

        RestAssured
                .given().when()
                .get("https://reqres.in/api/users?page=2")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("per_page", Matchers.equalTo(6)) //bisa ilang jika ada jsonschema
                .assertThat().body("page", Matchers.equalTo(2)) //bisa ilang jika ada jsonschema
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
    }

    @Test
    public void  testPostCreateUsers() {
        String valueName = "AgusSumaryanto";
        String valueJob = "QA Engineer";

        JSONObject bodyObject = new JSONObject();

        bodyObject.put("name", valueName);
        bodyObject.put("job", valueJob);

        RestAssured.given()
                .header("x-api-key", "reqres-free-v1")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObject.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(valueName));
    }

}
