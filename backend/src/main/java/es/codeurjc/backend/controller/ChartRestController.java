package es.codeurjc.backend.controller;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.ChartService;
import es.codeurjc.backend.service.UserService;

@RestController
@RequestMapping("/chart-rest")
public class ChartRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private ChartService chartService;


    @GetMapping("/threads/weekly")
    public String getWeeklyThreadsChart(Model model, Principal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return chartService.getWeeklyThreadsChart(owner, startOfWeek);
    }

    @GetMapping("/threads/monthly")
    public String getMonthlyThreadsChart(Model model, Principal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate today = date != null ? date : LocalDate.now();
        return chartService.getMonthlyThreadsChart(owner, today);
    }

    @GetMapping("/threads/annually")
    public String getAnnuallyThreadsChart(Model model, Principal principal,
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate today = date != null ? date : LocalDate.now();
        return chartService.getAnnuallyThreadsChart(owner, today);
    }

    @GetMapping("/posts/weekly")
    public String getWeeklyPostsChart(Model model, Principal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return chartService.getWeeklyPostsChart(owner, startOfWeek);
    }

    @GetMapping("/posts/monthly")
    public String getMonthlyPostsChart(Model model, Principal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate today = date != null ? date : LocalDate.now();
        return chartService.getMonthlyPostsChart(owner, today);
    }

    @GetMapping("/posts/annually")
    public String getAnnuallyPostsChart(Model model, Principal principal,
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate today = date != null ? date : LocalDate.now();
        return chartService.getAnnuallyPostsChart(owner, today);
    }
}
