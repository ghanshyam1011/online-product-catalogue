package com.catalogue.product_catalogue.web;

import com.catalogue.product_catalogue.item.Item;
import com.catalogue.product_catalogue.item.ItemService;
import com.catalogue.product_catalogue.item.StockAlert;
import com.catalogue.product_catalogue.item.StockStatus;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminItemController {

    private final ItemService itemService;

    public AdminItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @InitBinder("item")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }

    @GetMapping("/items/new")
    public String newForm(Model model) {
        model.addAttribute("item", new Item());
        formDetails(model, "Add item", "/admin/items");
        return "admin/item-form";
    }

    @PostMapping("/items")
    public String create(@Valid @ModelAttribute("item") Item item, BindingResult result,
                         Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            formDetails(model, "Add item", "/admin/items");
            return "admin/item-form";
        }
        Item saved = itemService.create(item);
        flash(redirect, null, saved.getStatus(), "Item added");
        return "redirect:/items";
    }

    @GetMapping("/items/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("item", itemService.get(id));
        formDetails(model, "Update item", "/admin/items/" + id);
        return "admin/item-form";
    }

    @PostMapping("/items/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("item") Item item,
                         BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            formDetails(model, "Update item", "/admin/items/" + id);
            return "admin/item-form";
        }
        StockStatus before = itemService.get(id).getStatus();
        Item saved = itemService.update(id, item);
        flash(redirect, before, saved.getStatus(), "Item updated");
        return "redirect:/items";
    }

    @GetMapping("/alerts")
    public String alerts(Model model) {
        model.addAttribute("items", itemService.alerts());
        return "admin/alerts";
    }

    private void formDetails(Model model, String title, String action) {
        model.addAttribute("formTitle", title);
        model.addAttribute("formAction", action);
    }

    private void flash(RedirectAttributes redirect, StockStatus before, StockStatus after, String message) {
        redirect.addFlashAttribute("message", message);
        StockAlert alert = StockAlert.forChange(before, after);
        if (alert != null) {
            redirect.addFlashAttribute("stockAlert", alert);
        }
    }
}