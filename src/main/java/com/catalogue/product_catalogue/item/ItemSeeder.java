package com.catalogue.product_catalogue.item;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ItemSeeder implements CommandLineRunner {

    private final ItemRepository itemRepository;

    public ItemSeeder(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) {
        if (itemRepository.count() > 0) {
            return;
        }
        itemRepository.saveAll(List.of(
                new Item("Wireless Mouse", "Electronics", new BigDecimal("799.00"), 25, 5),
                new Item("USB-C Cable", "Electronics", new BigDecimal("299.00"), 4, 5),
                new Item("Bluetooth Speaker", "Electronics", new BigDecimal("2499.00"), 0, 5),
                new Item("Phone Stand", "Electronics", new BigDecimal("349.00"), 40, 5),
                new Item("Notebook A5", "Stationery", new BigDecimal("120.00"), 60, 10),
                new Item("Gel Pen Pack", "Stationery", new BigDecimal("90.00"), 8, 10),
                new Item("Desk Lamp", "Home", new BigDecimal("1499.00"), 12, 3),
                new Item("Water Bottle", "Home", new BigDecimal("399.00"), 0, 5)));
    }
}