package es.codeurjc.backend.restcontroller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.ChartService;
import es.codeurjc.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/chart")
public class ChartApiRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChartService chartService;

    final private String AUTHENTICATED_USER_NOT_FOUND = "Authenticated user not found";
    final private String ERROR_OCURRED = "Error occurred, try again later";

    @GetMapping("/threads/weekly")
    @Operation(summary = "Gets weekly thread chart", description = "Provides a chart of weekly thread activity based on the specified date.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getWeeklyThreadsChart(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AUTHENTICATED_USER_NOT_FOUND);
        }
        try {
            User owner = userService.getUserByUsername(userDetails.getUsername());
            LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            String result = chartService.getWeeklyThreadsChart(owner, startOfWeek);
            return ResponseEntity.ok(result);
        } catch (JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_OCURRED);
        }
    }

    @GetMapping("/threads/monthly")
    @Operation(summary = "Gets monthly thread chart", description = "Provides a chart of monthly thread activity based on the specified date.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getMonthlyThreadsChart(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AUTHENTICATED_USER_NOT_FOUND);
        }
        try {
            User owner = userService.getUserByUsername(userDetails.getUsername());
            LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            String result = chartService.getMonthlyThreadsChart(owner, startOfWeek);
            return ResponseEntity.ok(result);
        } catch (JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_OCURRED);
        }
    }

    @GetMapping("/threads/annually")
    @Operation(summary = "Gets annually thread chart", description = "Provides a chart of annually thread activity based on the specified date.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getAnnuallyThreadsChart(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AUTHENTICATED_USER_NOT_FOUND);
        }
        try {
            User owner = userService.getUserByUsername(userDetails.getUsername());
            LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            String result = chartService.getAnnuallyThreadsChart(owner, startOfWeek);
            return ResponseEntity.ok(result);
        } catch (JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_OCURRED);
        }
    }

    @GetMapping("/posts/weekly")
    @Operation(summary = "Gets weekly post chart", description = "Provides a chart of weekly post activity based on the specified date.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getWeeklyPostsChart(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AUTHENTICATED_USER_NOT_FOUND);
        }
        try {
            User owner = userService.getUserByUsername(userDetails.getUsername());
            LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            String result = chartService.getWeeklyPostsChart(owner, startOfWeek);
            return ResponseEntity.ok(result);
        } catch (JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_OCURRED);
        }
    }

    @GetMapping("/posts/monthly")
    @Operation(summary = "Gets monthly post chart", description = "Provides a chart of monthly post activity based on the specified date.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getMonthlyPostsChart(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AUTHENTICATED_USER_NOT_FOUND);
        }
        try {
            User owner = userService.getUserByUsername(userDetails.getUsername());
            LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            String result = chartService.getMonthlyPostsChart(owner, startOfWeek);
            return ResponseEntity.ok(result);
        } catch (JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_OCURRED);
        }
    }

    @GetMapping("/posts/annually")
    @Operation(summary = "Gets annually post chart", description = "Provides a chart of annually post activity based on the specified date.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getAnnuallyPostsChart(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AUTHENTICATED_USER_NOT_FOUND);
        }
        try {
            User owner = userService.getUserByUsername(userDetails.getUsername());
            LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            String result = chartService.getAnnuallyPostsChart(owner, startOfWeek);
            return ResponseEntity.ok(result);
        } catch (JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_OCURRED);
        }
    }
}
