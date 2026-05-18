package com.fulfilment.application.monolith.products;

import com.fulfilment.application.monolith.fulfilment.database.WarehouseFulfilmentEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public Product findByProductId(Long productId){
        return findById(productId);
    }
}
