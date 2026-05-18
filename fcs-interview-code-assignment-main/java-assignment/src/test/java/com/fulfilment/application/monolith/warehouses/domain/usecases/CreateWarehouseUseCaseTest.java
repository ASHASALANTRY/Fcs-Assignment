package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CreateWarehouseUseCaseTest {


    @Inject
    CreateWarehouseUseCase useCase;
    @Inject
    WarehouseRepository warehouseStore;

    @Inject
    ArchiveWarehouseUseCase archiveWarehouseUseCase;
//should fail when stock exceeds capacity
    @Test
    void shouldFailWhenStockExceedsCapacity() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU-STOCK-FAIL";
        w.location = "AMSTERDAM-001";
        w.capacity = 10;
        w.stock = 50;

        assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(w)
        );
    }
//should fail when business already exist
    @Test
    void shouldFailWhenBusinessUnitAlreadyExists() {
        Warehouse first = new Warehouse();
        first.businessUnitCode = "BU-DUP";
        first.location = "AMSTERDAM-001";
        first.capacity = 100;
        first.stock = 10;

        useCase.create(first);

        Warehouse duplicate = new Warehouse();
        duplicate.businessUnitCode = "BU-DUP";
        duplicate.location = "AMSTERDAM-001";
        duplicate.capacity = 100;
        duplicate.stock = 10;

        IllegalArgumentException ex =
                org.junit.jupiter.api.Assertions.assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(duplicate)
                );

        assertTrue(
                ex.getMessage().contains("already exists")
        );
    }
//fail when location is invalid
    @Test
    void shouldFailWhenLocationIsInvalid() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU-INVALID-LOC";
        w.location = "UNKNOWN-LOC";
        w.capacity = 50;
        w.stock = 10;

        IllegalArgumentException ex =
                org.junit.jupiter.api.Assertions.assertThrows(
                        IllegalArgumentException.class,
                        () -> useCase.create(w)
                );

        assertTrue(
                ex.getMessage().contains("location")
        );
    }


    @Test
    void shouldFailWhenLocationMaxWarehousesReached() {
        // Create warehouses until limit (maxNumberOfWarehouses = 1 for VETSBY-001)
        Warehouse first = new Warehouse();
        first.businessUnitCode = "BU-001";
        first.location = "VETSBY-001";
        first.capacity = 50;
        first.stock = 10;
        useCase.create(first);

        Warehouse second = new Warehouse();
        second.businessUnitCode = "BU-002";
        second.location = "VETSBY-001";
        second.capacity = 50;
        second.stock = 10;

        //   Creating warehouse when location already at max
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(second)
        );
        assertTrue(ex.getMessage().contains("Max warehouses reached"));

        //Archive warehouse and again try to create again
        archiveWarehouseUseCase.archive(first);
        useCase.create(second);

        Warehouse secondCreated =
                warehouseStore.findByBusinessUnitCode("BU-002");
        assertNull(secondCreated.archivedAt);
        assertEquals(50,secondCreated.capacity);
    }

    //Creating warehouse with capacity exceeding location limit
    @Test
    void shouldFailWhenWarehouseCapacityExceedsLocationCapacity() {
        // AMSTERDAM-001 has maxCapacity = 100
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU-TOO-BIG";
        w.location = "AMSTERDAM-001";
        w.capacity = 150; // Exceeds location maxCapacity
        w.stock = 10;

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.create(w)
        );

        assertTrue(ex.getMessage().contains("capacity exceeds location capacity"));
    }



    @Test
    void shouldCreateWarehouseWhenStockEqualsCapacity() {
        // Boundary: stock == capacity (should pass)
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU-BOUNDARY";
        w.location = "EINDHOVEN-001";
        w.capacity = 50;
        w.stock = 50; // Exactly at capacity

        assertDoesNotThrow(() -> useCase.create(w));
    }

    @Test
    void shouldCreateWarehouseWithZeroStock() {
        // Edge case: empty warehouse
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU-ZERO-STOCK";
        w.location = "EINDHOVEN-001";
        w.capacity = 50;
        w.stock = 0; // Zero stock

        assertDoesNotThrow(() -> useCase.create(w));
    }

    @Test
    void shouldFailWhenCreatingWarehouseWithZeroCapacity() {
        // Edge case: zero capacity doesn't make sense
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU-ZERO-CAP";
        w.location = "HELMOND-001";
        w.capacity = 0; // Zero capacity
        w.stock = 0;

        assertThrows(IllegalArgumentException.class, () -> useCase.create(w));
    }

}
