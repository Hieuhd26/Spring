package security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import security.demo.model.User;
import security.demo.service.JwtService;
import security.demo.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;



    @PostMapping("/register")
    public User register(@RequestBody User user){
      return userService.register(user);
    }
    @PostMapping("/loginnn")
    public String loginnnn(@RequestBody User user){
       return  userService.verify(user);
    }

}
