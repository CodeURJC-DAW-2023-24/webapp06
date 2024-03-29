package es.codeurjc.backend.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.service.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/forums/trending")
public class ForumTrendingApiRestController {

  @Autowired
  private ForumService forumService;

  final private String ERROR_OCURRED = "Error occurred, try again later";


  @GetMapping
  @Operation(summary = "Gets trending forums", description = "Provides a list of the trending forums in the web.", responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "500", description = "Internal server error") })
  public ResponseEntity<?> getTrendingForums(@RequestParam(value = "logged", required = false, defaultValue = "false") boolean logged, @RequestParam(value = "userid", required = false) Long userId) {
    
    List<Forum> trendingForums = forumService.getTrendingForums(logged, userId);
    return ResponseEntity.ok(trendingForums);
  }
}
