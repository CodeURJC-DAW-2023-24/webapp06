package es.codeurjc.backend.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.ForumService;
import es.codeurjc.backend.service.UserService;
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
  @Autowired 
  private UserService userService;

  final private String ERROR_OCURRED = "Error occurred, try again later";


  @GetMapping
  @Operation(summary = "Gets trending forums", description = "Provides a list of the trending forums in the web.", responses = { @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "500", description = "Internal server error") })
   public ResponseEntity<?> getTrendingForums(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null){
      List<Forum> trendingForums = forumService.getTrendingForumsNoLog();
      return ResponseEntity.ok(trendingForums);
    } else{
      User logged = userService.getUserByUsername(userDetails.getUsername());
      Long id = logged.getId();
      List<Forum> trendingForums = forumService.getTrendingForumsLog(id);
      return ResponseEntity.ok(trendingForums);
    }
  }
}
