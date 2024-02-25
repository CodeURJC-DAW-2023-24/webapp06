package es.codeurjc.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartController {

    @GetMapping("/chart")
    public String getChart() {
        return "chart_template";
    }
}
