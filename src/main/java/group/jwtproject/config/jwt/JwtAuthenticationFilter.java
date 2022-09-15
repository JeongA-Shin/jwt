package group.jwtproject.config.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//원래 스프링 시큐리티에 UsernamePasswordAuthenticationFilter가 있음
// 원래는 /login을 통해 username, password를 전송하면 UsernamePasswordAuthenticationFilter가 동작함
//근데 지금 securityConfig에서 formLogin을 disable했기 때문에 위의 과정이 작동하지 않음
//따라서 이 필터를 따로 securityConfig에서 등록을 해줘야 이 필터를 거치게 됨
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    //UsernamePasswordAuthenticationFilter는 authenticationManager로 가지고 동작 절차를 밟기 때문에 필수임
    
    private final AuthenticationManager authenticationManager;
    
    
    /**
     * 로그인 시도시( /login) 실행되는 함수
     * 즉 걍 로그인 함수임
     * 따라서 여기서 로그인 정상 성공시 jwt 토큰 만들어주면 됨
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        System.out.println("login tried");
        //1. username, password 받아서
        //2. 정상인지 로그인을 시도함
        //2-1. 이 때, authenticationManager로 로그인을 시도하면, principalDetailsService가 호출됨
        //     즉 loadByUsername이 자동으로 실행되는 것임
        //3. PrincipalDetails를 세션에 담고  - 굳이,,? 근데 이 과정이 없으면 권한 관리가 안 됨. 즉 권한 관리를 위해서 
        //4. jwt 토큰을 만들어서 응답해주며 됨
        return super.attemptAuthentication(request, response);
    }
}
