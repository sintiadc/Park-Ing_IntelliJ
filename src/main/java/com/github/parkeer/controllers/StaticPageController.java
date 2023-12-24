package com.github.parkeer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class StaticPageController {

    @GetMapping("/")
    public String index(final Model model) {
        model.addAttribute("title", "Parkeer");

        return "landingPage";
    }
}
