package group.jwtproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApi {
    
    @GetMapping("/home")
    public String home(){
        return "home";
    }
    
}
