package hanu.devteria.controller;


import hanu.devteria.dto.request.UserCreationRequest;
import hanu.devteria.dto.request.UserUpdateRequest;
import hanu.devteria.dto.response.ApiResponse;
import hanu.devteria.dto.response.UserResponse;
import hanu.devteria.model.User;
import hanu.devteria.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ApiResponse<UserResponse> creatUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping()
    public List<User> getAllUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: ", authentication.getName());
        authentication.getAuthorities().forEach(e -> log.info(e.getAuthority()));

        return userService.findAllUser();
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable("userId") int id) {
        return userService.findUserById(id);
    }

    @GetMapping("/myInfor")
    public ApiResponse<UserResponse> getMyInfor() {
    return ApiResponse.<UserResponse>builder()
            .result(userService.getInfor())
            .build();

    }

    @PutMapping("/{userId}")
    public UserResponse updateUserById(@RequestBody UserUpdateRequest request, @PathVariable("userId") int id) {
        return userService.updateUserById(id, request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUserById(@PathVariable("userId") int id) {
        userService.deleteUserById(id);
        return "User has been deleted";
    }
}
