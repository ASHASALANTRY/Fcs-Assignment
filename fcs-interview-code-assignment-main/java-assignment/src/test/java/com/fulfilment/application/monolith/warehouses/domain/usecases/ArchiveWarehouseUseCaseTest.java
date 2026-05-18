package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class ArchiveWarehouseUseCaseTest {

    @Inject
    CreateWarehouseUseCase createUseCase;

    @Inject
    ArchiveWarehouseUseCase archiveUseCase;

    @Inject
    WarehouseRepository warehouseStore;

    @Test
    void shouldArchiveWarehouse() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU-ARCH";
        w.location = "AMSTERDAM-001";
        w.capacity = 50;
        w.stock = 10;

        createUseCase.create(w);
        archiveUseCase.archive(w);

        Warehouse archived =
                warehouseStore.findByBusinessUnitCode("BU-ARCH");
        assertNotNull(archived.archivedAt);
    }
    @Test
    void shouldFailWhenArchivingNonExistingWarehouse() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU-NOT-EXIST";

        IllegalArgumentException ex =
                org.junit.jupiter.api.Assertions.assertThrows(
                        IllegalArgumentException.class,
                        () -> archiveUseCase.archive(w)
                );

        org.junit.jupiter.api.Assertions.assertTrue(
                ex.getMessage().contains("Warehouse not found")
        );
    }


}
