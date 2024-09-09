package hanu.devteria.controller;


import hanu.devteria.dto.request.UserCreationRequest;
import hanu.devteria.dto.request.UserUpdateRequest;
import hanu.devteria.dto.response.ApiResponse;
import hanu.devteria.dto.response.UserResponse;
import hanu.devteria.model.User;
import hanu.devteria.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ApiResponse<User> creatUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping()
    public List<User> getAllUser() {
        return userService.findAllUser();
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable("userId") int id) {
        return userService.findUserById(id);
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
