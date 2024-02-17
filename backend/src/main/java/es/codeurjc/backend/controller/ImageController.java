package es.codeurjc.backend.controller;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.repository.UserRepository;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private UserRepository userRepository;

     @GetMapping("user/{username}")
    public ResponseEntity<Object> downloadTweetImage1(@PathVariable String username)  throws SQLException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && user.get().getImageFile() != null) {
            Resource file = new InputStreamResource(user.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .contentLength(user.get().getImageFile().length()).body(file);
        }

        return ResponseEntity.notFound().build();
    }
}
