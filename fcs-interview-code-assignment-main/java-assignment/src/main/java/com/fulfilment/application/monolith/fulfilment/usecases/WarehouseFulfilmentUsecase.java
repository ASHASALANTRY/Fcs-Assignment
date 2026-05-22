package com.fulfilment.application.monolith.fulfilment.usecases;

import com.fulfilment.application.monolith.fulfilment.Ports.WarehouseFulfilmentRepository;
import com.fulfilment.application.monolith.fulfilment.database.WarehouseFulfilmentEntity;
import com.fulfilment.application.monolith.fulfilment.model.AssignWarehouseDto;
import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.util.Objects;

@ApplicationScoped
public class WarehouseFulfilmentUsecase {
    @Inject
    WarehouseFulfilmentRepository warehouseFulfilmentRepository;
    @Inject
    ProductRepository productRepository;

    @Inject
    WarehouseStore warehouseStore;

    @Transactional
    public AssignWarehouseDto assign(AssignWarehouseDto data) {
        Store store= Store.findById(data.storeId);
        if(Objects.isNull(store))
            throw new BadRequestException("Store does not exist");
        Product product= productRepository.findByProductId(data.productId);
        if(Objects.isNull(product))
            throw new BadRequestException("Product does not exist");

        Warehouse warehouse=warehouseStore.findByBusinessUnitCodeAndisArchived(data.warehouseBusinessUnitCode);
        if(Objects.isNull(warehouse))
            throw new BadRequestException("Active warehouse does not exist");

        if(warehouseFulfilmentRepository.warehouseAlreadyAssigned(store,product,data.warehouseBusinessUnitCode)>0)
            throw new BadRequestException("Warehouse already assigned");


        if (warehouseFulfilmentRepository.countofWareHousesPerStoreOverProduct(store,product) >= 2) {
            throw new BadRequestException("Product can have max 2 warehouses per store");
        }

        Integer warehousesForStore = warehouseFulfilmentRepository
                .MaxWarehousesPerStore(store)
                ;

        if (warehousesForStore >= 3) {
            throw new BadRequestException("Store can have max 3 warehouses");
        }

        Integer productTypesForWarehouse = warehouseFulfilmentRepository
                .maxProductTypePerWarehouse(data);

        if (productTypesForWarehouse >= 5) {
            throw new BadRequestException("Warehouse can store max 5 product types");
        }

        WarehouseFulfilmentEntity fulfilment=new WarehouseFulfilmentEntity();
        fulfilment.store=store;
        fulfilment.product=product;
        fulfilment.warehouseBusinessUnitCode=data.warehouseBusinessUnitCode;
        warehouseFulfilmentRepository.create(fulfilment);
        return data;
    }
}

