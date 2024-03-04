package es.codeurjc.backend.restcontroller;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.PostService;
import es.codeurjc.backend.service.ThreadService;
import es.codeurjc.backend.service.UserService;

@RestController
@RequestMapping("/chart-rest")
public class ChartRestController {

    @Autowired
    private ThreadService threadService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping("/threads/weekly")
    public String getWeeklyThreadsChart(Model model, Principal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int[] weeklyThreadCounts = new int[7];
        for (int i = 0; i < 7; i++) {
            long count = threadService.getTotalThreadsForDay(owner, startOfWeek.plusDays(i));
            weeklyThreadCounts[i] = (int) count;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String weeklyThreadCountsJson = objectMapper.writeValueAsString(weeklyThreadCounts);
        return weeklyThreadCountsJson;
    }

    @GetMapping("/threads/monthly")
    public String getMonthlyThreadsChart(Model model, Principal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate today = date != null ? date : LocalDate.now();
        int[] monthlyThreadCounts = new int[12];
        for (int i = 1; i <= 12; i++) {
            long count = threadService.getTotalThreadsForMonth(owner, i, today.getYear());
            monthlyThreadCounts[i - 1] = (int) count;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String monthlyThreadCountsJson = objectMapper.writeValueAsString(monthlyThreadCounts);
        return monthlyThreadCountsJson;
    }

    @GetMapping("/threads/annually")
    public String getAnnuallyThreadsChart(Model model, Principal principal,
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate today = date != null ? date : LocalDate.now();
        Map<Integer, Integer> annuallyThreadCounts = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            int year = today.getYear() + i;
            long count = threadService.getTotalThreadsForYear(owner, year);
            annuallyThreadCounts.put(year, (int) count);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String annuallyThreadCountsJson = objectMapper.writeValueAsString(annuallyThreadCounts);
        return annuallyThreadCountsJson;
    }

    @GetMapping("/posts/weekly")
    public String getWeeklyPostsChart(Model model, Principal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int[] weeklyPostCounts = new int[7];
        for (int i = 0; i < 7; i++) {
            long count = postService.getTotalPostsForDay(owner, startOfWeek.plusDays(i));
            weeklyPostCounts[i] = (int) count;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String weeklyPostCountsJson = objectMapper.writeValueAsString(weeklyPostCounts);
        return weeklyPostCountsJson;
    }

    @GetMapping("/posts/monthly")
    public String getMonthlyPostsChart(Model model, Principal principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate today = date != null ? date : LocalDate.now();
        int[] monthlyPostCounts = new int[12];
        for (int i = 1; i <= 12; i++) {
            long count = postService.getTotalPostsForMonth(owner, i, today.getYear());
            monthlyPostCounts[i - 1] = (int) count;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String monthlyPostCountsJson = objectMapper.writeValueAsString(monthlyPostCounts);
        return monthlyPostCountsJson;
    }

    @GetMapping("/posts/annually")
    public String getAnnuallyPostsChart(Model model, Principal principal,
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws JsonProcessingException {
        User owner = userService.getUserByUsername(principal.getName());
        LocalDate today = date != null ? date : LocalDate.now();
        Map<Integer, Integer> annuallyPostCounts = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            int year = today.getYear() + i;
            long count = postService.getTotalPostsForYear(owner, year);
            annuallyPostCounts.put(year, (int) count);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String annuallyPostCountsJson = objectMapper.writeValueAsString(annuallyPostCounts);

        return annuallyPostCountsJson;
    }
}
