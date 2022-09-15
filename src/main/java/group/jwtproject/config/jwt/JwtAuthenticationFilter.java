package group.jwtproject.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.jwtproject.config.auth.PrincipalDetails;
import group.jwtproject.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        //1. username, password 받아서 (json으로 온다고 가정)
        try {
            
            //objectMapper를 통해 받은 값을 User 객체에 담아줌
           ObjectMapper om = new ObjectMapper();
           User user = om.readValue(request.getInputStream(), User.class);
           
           // 받은 로그인 요청 정보를 바탕으로 토큰 생성  - 이 때의 토큰은 jwt 토큰이 아님. 별개의 토큰임. jwt 토큰 생성은 다른 함수(아래에 있음) 에서 생성됨
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
            
            //2. 해당 토큰이 정상인지 로그인을 시도 :  요청 받은 정보로 만든 토큰으로 로그인 시도하는 것
            // 즉 만든 토큰을 생성자의 파라미터로 줘서 authentication 객체를 만드는 것
            //2-1. 이 때, authenticationManager로 로그인을 시도하면, principalDetailsService가 호출됨
            //     즉 loadByUsername이 자동으로 실행되는 것임
            //     그리고 loadByUsername이 정상적으로 리턴되었다면 authentication 객체 안에는 내가 로그인한 정보가 담김
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            
            //이제 정보 꺼냄 - 즉 아래처럼 principalDetails에서 User 객체를 꺼낼 수 있다는 거는 로그인이 정상적으로 되었다는 것
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
    
            //4. authentication을 응답해주면 됨
            //   그리고 authentication 객체는 "리턴 될 때" 세션 영역에 저장됨
            //그리고 해당 authentication을 굳이 리턴해주는 이유는  권한 관리를 위한 것
            //(권한 관리를 security가 대신 해주기 때문에 편하려고)
            // 굳이 jwt 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리 때문에 권한 처리시 좀 더 편하려고 session에 넣어둠
            return authentication;
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * attemptAuthentication 실행 후 인증이 정상적으로 되었으면 실행됨
     * 여기서 jwt 토큰을 만들어서 request 요청한 사용자에게 해당 jwt 토큰을 응답해주면 됨
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
     //성공한 로그인의 유저 정보는 authResult에 담겨져 있음(Authentication 객체)
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
    
        //rsa 방식은 아니고 hash암호 방식
        //특징: 서버만 알고 있는 secret값이 있어야 함
        String jwtToken = JWT.create()
            .withSubject("cos token")//토큰 이름
            .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10))) //해당 토큰의 만료 시간 //1분*10
            .withClaim("id", principalDetails.getUser().getId())
            .withClaim("username", principalDetails.getUser().getUsername())
            .sign(Algorithm.HMAC512("cos")); //서버만 알고 있는 고유한 값
        
        response.addHeader("Authorization","Bearer "+jwtToken);
    }
}
