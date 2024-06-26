package es.codeurjc.backend.restcontroller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.backend.dto.UserDTO;
import es.codeurjc.backend.model.User;
import es.codeurjc.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/users")
public class UserApiRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    @Operation(summary = "Get all users", description = "Get all users without paginated")
    public List<User> getUsersPaginated() {
        return userService.getAllUsers();
    }

    @GetMapping("/paginated")
    @Operation(summary = "Paginated Users", description = "Get all users paginated")
    public Page<User> getUsersPaginated(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return userService.getUsersPaginated(page, size);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Users", description = "Search users by username starting with paginated")
    public Page<User> getUsersByUsernameStartingWithPaginated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "username") String username) {
        return userService.getUsersByUsernameStartingWith(username, page, size);
    }

    @GetMapping("/{userId}/image")
    @Operation(summary = "Get user image", description = "Get the image of an user by id", responses = {
            @ApiResponse(responseCode = "200", description = "User successfully found"),
            @ApiResponse(responseCode = "404", description = "User Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<?> getUserImage(@PathVariable Long userId) {
        Optional<User> o = userService.getUserById(userId);
        if (!o.isPresent()) {
            return new ResponseEntity<>("User with id " + userId + " not found.", HttpStatus.NOT_FOUND);
        } else {
            try {

                InputStream inputStream = o.get().getImageFile().getBinaryStream();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] imageBytes = outputStream.toByteArray();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);

                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/{userId}/status")
    @Operation(summary = "Get user status", description = "Get the status of an user by id", responses = {
            @ApiResponse(responseCode = "200", description = "User successfully found"),
            @ApiResponse(responseCode = "404", description = "User Not Found")
    })
    public ResponseEntity<?> checkUserStatus(@PathVariable Long userId) {
        Optional<User> o = userService.getUserById(userId);
        if (!o.isPresent()) {
            return new ResponseEntity<>("User with id " + userId + " not found.", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(o.get().getIsActive(), HttpStatus.OK);
        }
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user", description = "Get user by id", responses = {
            @ApiResponse(responseCode = "200", description = "User successfully found"),
            @ApiResponse(responseCode = "404", description = "User Not Found")
    })
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<User> o = userService.getUserById(userId);
        if (!o.isPresent()) {
            return new ResponseEntity<>("User with id " + userId + " not found.", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(o.get(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Delete user by id", responses = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User Not Found")
    })
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
        Optional<User> o = userService.getUserById(userId);
        if (!o.isPresent()) {
            return new ResponseEntity<>("User with id " + userId + " not found.", HttpStatus.NOT_FOUND);
        } else {
            userService.deleteUser(o.get().getUsername());
            return new ResponseEntity<>("User with id " + userId + " succesfully deleted", HttpStatus.OK);
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create user", description = "Create a new user and add it to the app.", responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully", headers = @Header(name = "Location", description = "Link to the newly created user")),
            @ApiResponse(responseCode = "400", description = "Bad request body", content = @Content),
    })
    public ResponseEntity<?> createUser(@RequestBody UserDTO u) {
        if (u.getEmail() == null || u.getUsername() == null || u.getPassword() == null) {
            return new ResponseEntity<>("Invalid Body", HttpStatus.BAD_REQUEST);
        } else {
            userService.createUser(u.getUsername(), u.getEmail(), u.getPassword());
            Optional<User> o = userService.getUserByUsernameNoException(u.getUsername());
            String userUrl = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(o.get().getId())
                    .toUriString();
            return ResponseEntity.created(URI.create(userUrl)).body(o.get());
        }
    }

    @PutMapping("/{userId}/activate")
    @Operation(summary = "Activate user", description = "Activate an user", responses = {
            @ApiResponse(responseCode = "200", description = "User successfully activated"),
            @ApiResponse(responseCode = "404", description = "User Not Found")
    })
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        Optional<User> o = userService.getUserById(userId);
        if (!o.isPresent()) {
            return new ResponseEntity<>("User with id " + userId + " not found.", HttpStatus.NOT_FOUND);
        } else {
            if (o.get().getIsActive().equals(true)) {
                return new ResponseEntity<>("User with id " + userId + " is already activated.", HttpStatus.OK);
            } else {
                userService.deleteUser(o.get().getUsername());
                o.get().setIsActive(true);
                userService.save(o.get());
                return new ResponseEntity<>("The user has been activated successfully", HttpStatus.OK);
            }

        }
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Edit user", description = "Edit user information.")
    public ResponseEntity<?> editUser(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId,
            @RequestBody UserDTO updatedUserInfo) {
        if (userDetails != null) {
            try {
                Optional<User> existingUserOptional = userService.getUserById(userId);
                if (!existingUserOptional.isPresent()) {
                    return new ResponseEntity<>("User with id " + userId + " not found.", HttpStatus.NOT_FOUND);
                }

                User existingUser = existingUserOptional.get();

                if (existingUser.getUsername().equals(userDetails.getUsername())
                        || userService.isAdmin(userDetails.getUsername())) {

                    if (updatedUserInfo.getUsername() != null) {
                        existingUser.setUsername(updatedUserInfo.getUsername());
                    }
                    if (updatedUserInfo.getEmail() != null) {
                        existingUser.setEmail(updatedUserInfo.getEmail());
                    }
                    if (updatedUserInfo.getPassword() != null) {
                        existingUser.setPassword(updatedUserInfo.getPassword());
                    }
                    userService.save(existingUser);

                    return new ResponseEntity<>("User with id " + userId + " successfully updated.", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(
                            "User with id " + userId + " can only be edited by its owner or an administrator.",
                            HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                return new ResponseEntity<>("An error occurred while updating user with id " + userId + ".",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Authenticated user not found.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{userId}/image")
    @Operation(summary = "Edit user image", description = "Edit user image by id.", responses = {
            @ApiResponse(responseCode = "200", description = "User image edited successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> editUserImage(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId,
            @RequestPart MultipartFile image) {
        if (userDetails != null) {
            try {

                Optional<User> existingUserOptional = userService.getUserById(userId);
                if (!existingUserOptional.isPresent()) {
                    return new ResponseEntity<>("User with id " + userId + " not found.", HttpStatus.NOT_FOUND);
                }

                User existingUser = existingUserOptional.get();

                if (existingUser.getUsername().equals(userDetails.getUsername())
                        || userService.isAdmin(userDetails.getUsername())) {

                    if (image == null) {
                        return new ResponseEntity<>("Include an image.", HttpStatus.BAD_REQUEST);
                    }

                    try {

                        existingUser.setImageFile(new SerialBlob(image.getBytes()));
                        userService.save(existingUser);

                        return new ResponseEntity<>("User image edited successfully.", HttpStatus.OK);
                    } catch (IOException | SQLException e) {
                        return new ResponseEntity<>("Error updating user image.", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {

                    return new ResponseEntity<>("User image can only be edited by its owner or an administrator.",
                            HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {

                return new ResponseEntity<>(
                        "An error occurred while updating user image for user with id " + userId + ".",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {

            return new ResponseEntity<>("Authenticated user not found.", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get information of the currently logged user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            Optional<User> userOptional = userService.getUserByUsernameNoException(username);
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            } else {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Authenticated user not found.", HttpStatus.UNAUTHORIZED);
        }
    }

}
