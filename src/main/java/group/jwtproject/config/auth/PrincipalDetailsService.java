package group.jwtproject.config.auth;

import group.jwtproject.model.User;
import group.jwtproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//원래는 로그인 요청이 오면 얘가 동작함 (localhost:8080/login)
//근데 지금 SecurityConfig에서 formLogin을 disable했으므로 위의 주소가 동작하지 않음
//따라서 아예 필터를 만들어줘야함
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
        System.out.println("loadUserByUsername");
        
        User userEntity = userRepository.findByUsername(username);
        
        return new PrincipalDetails((userEntity));
    }
}
