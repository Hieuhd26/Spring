package security.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("home")
public class HelloController {
    @GetMapping()
    public String home(HttpServletRequest request){
        return "This is homepage. with session id: " + request.getSession().getId();
    }
}

