package tests;

import io.qameta.allure.AllureId;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.qameta.allure.*;
import org.testng.asserts.SoftAssert;

@Feature("HungerStation Report")
public class FnHungerStation  {
    SoftAssert softAssert = new SoftAssert();
    public Response response = null;
    public String requestBody = "{\n" +
            "  \"id\": 0,\n" +
            "  \"category\": {\n" +
            "    \"id\": 0,\n" +
            "    \"name\": \"string\"\n" +
            "  },\n" +
            "  \"name\": \"doggie\",\n" +
            "  \"photoUrls\": [\n" +
            "    \"string\"\n" +
            "  ],\n" +
            "  \"tags\": [\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"name\": \"string\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"status\": \"available\"\n" +
            "}";

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2/pet";
    }

    @Test(priority = 1,description="Add a new pet to the store")
    public void addPET() {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body(requestBody)
                .post();
        String jsonResponse = response.jsonPath().getString("id");
        System.out.println("Id of the New Pet is: "+jsonResponse);
    }

    public String getID(){
        String jsonResponseID = response.jsonPath().getString("id");
        return jsonResponseID;
    }

    @Test(priority = 2,description="Find pet by ID")
    public void findPetByID() {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body(requestBody)
                .get("/"+getID());

        String name = response.jsonPath().getString("name");
        System.out.println("Name of the PET is: "+name);
    }

    @Test(priority = 3,description="Update an existing pet with the same ID in TC number One")
    public void updateExistingPET() {
        String requestBodyAfterUpdate = "{\n" +
                "  \"id\": "+getID()+",\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"string\"\n" +
                "  },\n" +
                "  \"name\": \"Lucky\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        response = RestAssured.given().contentType(ContentType.JSON)
                .body(requestBodyAfterUpdate)
                .put();
        String name = response.jsonPath().getString("name");
        System.out.println("The new name of the PET with id: "+getID()+" is "+name);
    }

    @Test(priority = 4,description="Deletes a pet")
    public void deletePET() {
        response = RestAssured.given().contentType(ContentType.JSON)
                .delete("/"+getID());
        String name = response.jsonPath().getJsonObject("name");
        softAssert.assertEquals(name,null,"The PET is not deleted Successfully");
        softAssert.assertAll();
    }
}