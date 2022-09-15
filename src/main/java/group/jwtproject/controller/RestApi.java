package group.jwtproject.controller;

import group.jwtproject.model.User;
import group.jwtproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApi {
    
    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    
    @GetMapping("/home")
    public String home(){
        return "home";
    }
    
    
    @PostMapping("/token")
    public String token(){
        return "this is token";
    }
    
    
    @PostMapping("/join")
    public String join(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode((user.getPassword())));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        
        return "join success";
    }
    
    
}
