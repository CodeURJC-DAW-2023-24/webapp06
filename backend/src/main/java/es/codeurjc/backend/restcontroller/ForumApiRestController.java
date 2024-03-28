package es.codeurjc.backend.restcontroller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.service.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/forums")
public class ForumApiRestController {

  @Autowired
  private ForumService forumService;

  final private String ERROR_OCURRED = "Error occurred, try again later";
  
  @GetMapping
  @Operation(summary = "Gets all forums", description = "Provides a list of all the forums in the web.", responses = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "500", description = "Internal server error")
})
public ResponseEntity<?> getForums() {
  try {
    List<Forum> forums = forumService.getAllForumsExcp();
    return ResponseEntity.ok(forums);
  } catch (JsonProcessingException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_OCURRED);
  }
}

@GetMapping("/{id}")
@Operation(summary = "Gets a forum by id", description = "Provides a forum based on the specified id.", responses = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "500", description = "Internal server error")
})
public ResponseEntity<?> getForumById(@PathVariable Long id) {
  try {
    Forum forum = forumService.getForumById(id);
    return ResponseEntity.ok(forum);
  } catch (JsonProcessingException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_OCURRED);
  }
}
}
