package com.fulfilment.application.monolith.fulfilment.Ports;

import com.fulfilment.application.monolith.fulfilment.database.WarehouseFulfilmentEntity;
import com.fulfilment.application.monolith.fulfilment.model.AssignWarehouseDto;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;

public interface WarehouseFulfilmentRepository {



    Long warehouseAlreadyAssigned(Store store, Product product, String warehouseBusinessUnitCode);

    Long countofWareHousesPerStoreOverProduct(Store store, Product product);

    Integer MaxWarehousesPerStore(Store store);

    Integer maxProductTypePerWarehouse(AssignWarehouseDto warehouseDto);

    void create(WarehouseFulfilmentEntity warehouseFulfilmentEntity);
}
