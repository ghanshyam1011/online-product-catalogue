package com.catalogue.product_catalogue.item;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    @Query("select distinct i.category from Item i order by i.category")
    List<String> findCategories();

    @Query("select i from Item i where i.quantity <= i.lowStockThreshold order by i.quantity, i.name")
    List<Item> findLowOrOutOfStock();
}