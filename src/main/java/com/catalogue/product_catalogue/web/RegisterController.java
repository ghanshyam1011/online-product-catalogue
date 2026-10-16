package com.catalogue.product_catalogue.web;

import com.catalogue.product_catalogue.user.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String submit(@Valid @ModelAttribute("form") RegistrationForm form, BindingResult result) {
        if (!result.hasFieldErrors("email") && userService.emailTaken(form.getEmail())) {
            result.rejectValue("email", "duplicate", "An account with this email already exists");
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.register(form.getName(), form.getEmail(), form.getPassword());
        return "redirect:/login?registered";
    }
}