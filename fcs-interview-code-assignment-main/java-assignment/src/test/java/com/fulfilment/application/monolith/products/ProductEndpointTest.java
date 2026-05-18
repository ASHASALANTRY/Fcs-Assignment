package com.fulfilment.application.monolith.products;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNot.not;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@QuarkusTest
public class ProductEndpointTest {
    private static final String PATH = "product";

  @Test
  public void testCrudProduct() {

    // List all, should have all 3 products the database has initially:
    given()
        .when()
        .get(PATH)
        .then()
        .statusCode(200)
        .body(containsString("TONSTAD"), containsString("KALLAX"), containsString("BESTÅ"));

    // Delete the TONSTAD:
    given().when().delete(PATH + "/1").then().statusCode(204);

    // List all, TONSTAD should be missing now:
    given()
        .when()
        .get(PATH)
        .then()
        .statusCode(200)
        .body(not(containsString("TONSTAD")), containsString("KALLAX"), containsString("BESTÅ"));
  }


    @Test
    void shouldGetSingleProductById() {
        given()
                .when()
                .get(PATH + "/1")
                .then()
                .statusCode(200)
                .body("name", notNullValue());
    }


    @Test
    void shouldReturn404WhenProductNotFound() {
        given()
                .when()
                .get(PATH + "/9999")
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("error", containsString("does not exist"))
                .body("exceptionType", containsString("WebApplicationException"));
    }

    /* ------------------- POST ------------------- */

    @Test
    void shouldCreateProduct() {
        Product product = new Product();
        product.name = "LACK";
        product.description = "Small table";
        product.price = new BigDecimal("19.99");
        product.stock = 10;

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(product)
                .when()
                .post(PATH)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("LACK"));
    }

    @Test
    void shouldFailWhenCreatingProductWithId() {
        Product product = new Product();
        product.id = 100L;
        product.name = "INVALID";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(product)
                .when()
                .post(PATH)
                .then()
                .statusCode(422)
                .body("code", equalTo(422))
                .body("error", containsString("Id was invalidly set"))
                .body("exceptionType", containsString("WebApplicationException"));    }


    @Test
    void shouldFailUpdateWhenNameIsMissing() {
        Product update = new Product();
        update.description = "No name";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .when()
                .put(PATH + "/2")
                .then()
                .statusCode(422)
                .body("code", equalTo(422))
                .body("error", containsString("Product Name was not set"))
                .body("exceptionType", containsString("WebApplicationException"));
    }

    @Test
    void shouldFailUpdateWhenProductNotFound() {
        Product update = new Product();
        update.name = "UNKNOWN";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .when()
                .put(PATH + "/9999")
                .then()
                .statusCode(404)
                // ErrorMapper assertions
                .body("code", equalTo(404))
                .body("error", containsString("does not exist"))
                .body("exceptionType", containsString("WebApplicationException"));

    }



    @Test
    void shouldFailDeleteWhenProductNotFound() {
        given()
                .when()
                .delete(PATH + "/9999")
                .then()
                .statusCode(404)
                .body("error", containsString("does not exist"));
    }
    @Test
    void shouldMapUnexpectedExceptionTo500() {

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("null")
                .when()
                .post("/product")
                .then()
                .statusCode(500)
                .body("code", equalTo(500))
                .body("exceptionType", notNullValue());
    }
}
