package es.codeurjc.backend.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.codeurjc.backend.model.User;

@Service
public class ChartService {

    @Autowired
    private ThreadService threadService;
    @Autowired
    private PostService postService;

    public String getWeeklyThreadsChart(User user, LocalDate startOfWeek) throws JsonProcessingException{
        int[] weeklyThreadCounts = new int[7];
        for (int i = 0; i < 7; i++) {
            long count = threadService.getTotalThreadsForDay(user, startOfWeek.plusDays(i));
            weeklyThreadCounts[i] = (int) count;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String weeklyThreadCountsJson = objectMapper.writeValueAsString(weeklyThreadCounts);
        return weeklyThreadCountsJson;
    }

    public String getMonthlyThreadsChart(User user, LocalDate today) throws JsonProcessingException{
        int[] monthlyThreadCounts = new int[12];
        for (int i = 1; i <= 12; i++) {
            long count = threadService.getTotalThreadsForMonth(user, i, today.getYear());
            monthlyThreadCounts[i - 1] = (int) count;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String monthlyThreadCountsJson = objectMapper.writeValueAsString(monthlyThreadCounts);
        return monthlyThreadCountsJson;
    }

    public String getAnnuallyThreadsChart(User user, LocalDate today) throws JsonProcessingException{
        Map<Integer, Integer> annuallyThreadCounts = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            int year = today.getYear() + i;
            long count = threadService.getTotalThreadsForYear(user, year);
            annuallyThreadCounts.put(year, (int) count);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String annuallyThreadCountsJson = objectMapper.writeValueAsString(annuallyThreadCounts);
        return annuallyThreadCountsJson;
    }

    public String getWeeklyPostsChart(User user, LocalDate startOfWeek) throws JsonProcessingException{
        int[] weeklyPostCounts = new int[7];
        for (int i = 0; i < 7; i++) {
            long count = postService.getTotalPostsForDay(user, startOfWeek.plusDays(i));
            weeklyPostCounts[i] = (int) count;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String weeklyPostCountsJson = objectMapper.writeValueAsString(weeklyPostCounts);
        return weeklyPostCountsJson;
    }

    public String getMonthlyPostsChart(User user, LocalDate today) throws JsonProcessingException{
        int[] monthlyPostCounts = new int[12];
        for (int i = 1; i <= 12; i++) {
            long count = postService.getTotalPostsForMonth(user, i, today.getYear());
            monthlyPostCounts[i - 1] = (int) count;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String monthlyPostCountsJson = objectMapper.writeValueAsString(monthlyPostCounts);
        return monthlyPostCountsJson;
    }

    public String getAnnuallyPostsChart(User user, LocalDate today) throws JsonProcessingException{
        Map<Integer, Integer> annuallyPostCounts = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            int year = today.getYear() + i;
            long count = postService.getTotalPostsForYear(user, year);
            annuallyPostCounts.put(year, (int) count);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String annuallyPostCountsJson = objectMapper.writeValueAsString(annuallyPostCounts);

        return annuallyPostCountsJson;
    }
}
