package stepdefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.ErrorObject;
import models.User;
import org.json.simple.JSONObject;
import org.testng.Assert;
import utils.Endpoints;

import static io.restassured.RestAssured.given;
import static utils.JsonInputParser.data;
import static utils.TestngListener.userData;

public class MyStepDef {
    User users;
    JSONObject jsonObject, jsonObject1;
    JsonPath jsonPath;
    ObjectMapper objectMapper=new ObjectMapper();
    Response response;
    User responseUser;

    @Given("user details")
    public void userDetails() {
        jsonObject = (JSONObject) userData.get("createRequest");
        jsonObject1 = (JSONObject) userData.get("createRequest1");
    }

    @When("creating a user")
    public void creatingAUser() throws JsonProcessingException {
        users =new User(
                (Long) jsonObject.get("id"),
                (String) jsonObject.get("name"),
                (Long) jsonObject.get("age"),
                (String) jsonObject.get("email"));
        response=given()
                .body(users)
                .when().post(Endpoints.userEndpoint)
                .then()
                .statusCode(200).extract().response();
        responseUser=objectMapper.readValue(response.asString(),User.class);
    }

    @Then("user details displayed")
    public void userDetailsDisplayed() {
        response=given()
                .body(users)
                .when().get(Endpoints.userEndpoint)
                .then()
                .statusCode(200).extract().response();
    }

    @Then("single user displayed")
    public void singleUserDisplayed() throws JsonProcessingException {
        response=given()
                .body(users)
                .when().get(Endpoints.userEndpoint+"/"+responseUser.getId())
                .then()
                .statusCode(200).extract().response();
        User responseUser=objectMapper.readValue(response.asString(),User.class);
        Assert.assertEquals(users.getId(),responseUser.getId());
    }

    @Then("user must be created")
    public void userMustBeCreated() throws JsonProcessingException {
        User responseUser=objectMapper.readValue(response.asString(),User.class);
        Assert.assertEquals(users.getEmail(),responseUser.getEmail());
    }

    @And("updating user")
    public void updatingUser() {
        JSONObject testData = (JSONObject) data.get("putRequest");
        users = new User(
                (Long) testData.get("id"),
                (String) testData.get("name"),
                (Long) testData.get("age"),
                (String) testData.get("email"));
        response=given()
                .body(users)
                .when().put(Endpoints.userEndpoint+"/"+responseUser.getId())
                .then()
                .statusCode(200).extract().response();
    }

    @Then("user details updated")
    public void userDetailsUpdated() throws JsonProcessingException {
        User responseUser=objectMapper.readValue(response.asString(),User.class);
        Assert.assertEquals(users.getAge(),responseUser.getAge());
    }

    @Then("user deleted")
    public void userDeleted() {
        response=given()
                .when().delete(Endpoints.userEndpoint+"/"+responseUser.getId())
                .then()
                .statusCode(200).extract().response();
        Assert.assertEquals(response.getBody().asString(),"id"+responseUser.getId()+"is deleted successfully");
    }

    @When("creating without name")
    public void creatingWithoutName() {
        users =new User(
                (Long) jsonObject.get("id"),
                (String) jsonObject.get(""),
                (Long) jsonObject.get("age"),
                (String) jsonObject.get("email"));
    }

    @Then("error thrown")
    public void errorThrown() {
        response=given()
                .body(users)
                .when().post(Endpoints.userEndpoint)
                .then()
                .statusCode(400).extract().response();
        jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(jsonPath.getString("message"),"Name is required");
    }

    @When("creating without email")
    public void creatingWithoutEmail() {
        users =new User(
                (Long) jsonObject.get("id"),
                (String) jsonObject.get("name"),
                (Long) jsonObject.get("age"),
                (String) jsonObject.get(""));
        response=given()
                .body(users)
                .when().post(Endpoints.userEndpoint)
                .then()
                .statusCode(400).extract().response();
    }

    @Then("email error thrown")
    public void emailErrorThrown() throws JsonProcessingException {
        ErrorObject errorObject = objectMapper.readValue(response.asString(),ErrorObject.class);
        Assert.assertEquals(errorObject.getMessage(),"Email is required");
    }

    @When("creating without age")
    public void creatingWithoutAge() {
        users = new User(
                (Long) jsonObject.get("id"),
                (String) jsonObject.get("name"),
                0,
                (String) jsonObject.get("email"));

    }

    @Then("age error thrown")
    public void ageErrorThrown() {
        response=given()
                .body(users)
                .when().post(Endpoints.userEndpoint)
                .then()
                .statusCode(400).extract().response();
        jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(jsonPath.getString("message"),"Age is required");
    }

    @And("updating without endpoint")
    public void updatingWithoutEndpoint() {
        JSONObject testData = (JSONObject) data.get("putRequest");
        users= new User(
                (Long) testData.get("id"),
                (String) testData.get("name"),
                (Long) testData.get("age"),
                (String) testData.get("email"));
        response = given()
                .body(users)
                .when().put()
                .then()
                .statusCode(405).extract().response();
    }

    @Then("method error")
    public void methodError() {
        jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(jsonPath.getString("error"),"Method Not Allowed");
    }

    @And("deleting user")
    public void deletingUser() {
        response = given()
                .when().delete()
                .then()
                .statusCode(405).extract().response();
    }

    @When("creating without id")
    public void creatingWithoutId() {
        users = new User(
                0,
                (String) jsonObject.get("name"),
                (Long) jsonObject.get("age"),
                (String) jsonObject.get("email"));
        response=given()
                .body(users)
                .when().post(Endpoints.userEndpoint)
                .then()
                .statusCode(500).extract().response();
    }

    @Then("Internal error thrown")
    public void internalErrorThrown() {
        jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(jsonPath.getString("error"),"Internal Server Error");
    }

    @And("duplicate delete")
    public void duplicateDelete() {
        response = given()
                .when().delete(Endpoints.userEndpoint + "/" + responseUser.getId())
                .then()
                .statusCode(500).extract().response();

    }

}
