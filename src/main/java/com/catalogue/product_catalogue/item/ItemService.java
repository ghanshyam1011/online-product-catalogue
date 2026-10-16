package com.catalogue.product_catalogue.item;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ItemService {

    public static final int DEFAULT_THRESHOLD = 5;

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> search(String q, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return itemRepository.findAll(ItemSpecifications.search(q, category, minPrice, maxPrice), Sort.by("name"));
    }

    public Item get(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    public List<String> categories() {
        return itemRepository.findCategories();
    }

    public List<Item> alerts() {
        return itemRepository.findLowOrOutOfStock();
    }

    @Transactional
    public Item create(Item form) {
        Item item = new Item();
        copy(form, item);
        return itemRepository.save(item);
    }

    @Transactional
    public Item update(Long id, Item form) {
        Item existing = get(id);
        copy(form, existing);
        return itemRepository.save(existing);
    }

    private void copy(Item from, Item to) {
        to.setName(from.getName().trim());
        to.setCategory(from.getCategory().trim());
        to.setPrice(from.getPrice());
        to.setQuantity(from.getQuantity());
        to.setLowStockThreshold(from.getLowStockThreshold() == null ? DEFAULT_THRESHOLD : from.getLowStockThreshold());
    }
}