package group.jwtproject.controller;

import group.jwtproject.model.User;
import group.jwtproject.repository.UserRepository;
import javax.persistence.GeneratedValue;
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
    
    
    //user,manager, admin 권한들이 접근 가능
    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }
    
    //manager, admin 권한들이 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }
    
    //admin 권한이 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
    
    
    
}
