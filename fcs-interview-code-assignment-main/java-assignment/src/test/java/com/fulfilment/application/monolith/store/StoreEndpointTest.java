package com.fulfilment.application.monolith.store;

import com.fulfilment.application.monolith.stores.Store;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class StoreEndpointTest {
    private static final String PATH = "/stores";


    @Test
    void shouldListStores() {
        given()
                .when()
                .get(PATH)
                .then()
                .statusCode(200);
    }

    @Test
    void shouldGetStoreById() {
        given()
                .when()
                .get(PATH + "/1")
                .then()
                .statusCode(200)
                .body("name", notNullValue());
    }

    @Test
    void shouldReturn404WhenStoreNotFound() {
        given()
                .when()
                .get(PATH + "/9999")
                .then()
                .statusCode(404)
                .body("error", containsString("does not exist"));
    }

    @Test
    void shouldCreateStore() {
        Store store = new Store();
        store.name = "Amsterdam";
        store.quantityProductsInStock = 10;

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(store)
                .when()
                .post(PATH)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Amsterdam"));
    }

    @Test
    void shouldFailCreateWhenIdIsSet() {
        Store store = new Store();
        store.id = 100L;
        store.name = "Invalid";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(store)
                .when()
                .post(PATH)
                .then()
                .statusCode(422)
                 .body("code", equalTo(422))
                .body("error", containsString("Id was invalidly set"))
                .body("exceptionType", containsString("WebApplicationException"));

    }

    @Test
    void shouldUpdateStore() {
        Store update = new Store();
        update.name = "Updated Store";
        update.quantityProductsInStock = 50;

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .when()
                .put(PATH + "/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated Store"));
    }

    @Test
    void shouldFailUpdateWhenNameMissing() {
        Store update = new Store();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .when()
                .put(PATH + "/1")
                .then()
                .statusCode(422)
          .body("code", equalTo(422))
                .body("error", containsString("Store Name was not set"))
                .body("exceptionType", containsString("WebApplicationException"));
    }

    @Test
    void shouldFailUpdateWhenStoreNotFound() {
        Store update = new Store();
        update.name = "Unknown";

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .when()
                .put(PATH + "/9999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldPatchStore() {
        Store patch = new Store();
        patch.quantityProductsInStock = 99;

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(patch)
                .when()
                .patch(PATH + "/1")
                .then()
                .statusCode(200)
                .body("quantityProductsInStock", equalTo(99));
    }

    @Test
    void shouldDeleteStore() {
        Store store = new Store();
        store.name = "Temp Store";
        store.quantityProductsInStock = 1;

        Integer id =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(store)
                        .when()
                        .post(PATH)
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("id");

        given()
                .when()
                .delete(PATH + "/" + id)
                .then()
                .statusCode(204);
    }

    @Test
    void shouldFailDeleteWhenStoreNotFound() {
        given()
                .when()
                .delete(PATH + "/9999")
                .then()
                .statusCode(404);
    }

}

