package com.fulfilment.application.monolith.fulfilment.database;

import com.fulfilment.application.monolith.fulfilment.model.AssignWarehouseDto;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarehouseFulfilmentRepository implements com.fulfilment.application.monolith.fulfilment.Ports.WarehouseFulfilmentRepository,PanacheRepository<WarehouseFulfilmentEntity> {
    @Override
    public Long warehouseAlreadyAssigned(Store store, Product product, String warehouseBusinessUnitCode){
        return count("store ?1 and product.id= ?2 and warehouseBusinessUnitCode= ?3", store,product,warehouseBusinessUnitCode);
    }

    @Override
    public Long countofWareHousesPerStoreOverProduct(Store store, Product product){
        return count("store = ?1 and product = ?2", store,product);
    }
    @Override
    public Integer MaxWarehousesPerStore(Store store){
    Integer count=find("select distinct warehouseBusinessUnitCode from WarehouseFulfilment where store = ?1",store)
                .list()
                .size();
    return count;}
    @Override
    public Integer maxProductTypePerWarehouse(AssignWarehouseDto warehouseDto){
        Integer count=find("select distinct product.id from WarehouseFulfilment where warehouseBusinessUnitCode = ?1",
                warehouseDto.warehouseBusinessUnitCode)
                .list()
                .size();
    return count;
    }
    @Override
    public void create(WarehouseFulfilmentEntity warehouseFulfilmentEntity){
      persist(warehouseFulfilmentEntity);
    }


}
