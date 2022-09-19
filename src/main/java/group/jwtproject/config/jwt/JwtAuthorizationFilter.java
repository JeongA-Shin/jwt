package group.jwtproject.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import group.jwtproject.config.auth.PrincipalDetails;
import group.jwtproject.model.User;
import group.jwtproject.repository.UserRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//시큐리티가 가지고 있는 필터 중 BasicAuthentiactionFilter가 있음
//권한이나 인증이 필요한 특정 주소를 요청하면 위의 필터를 무조건 타게 되어 있음
//만약 권한이나 인증이 필요한 주소가 아니라면 위의 필터를 타지 않음
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    
    private UserRepository userRepository;
    public JwtAuthorizationFilter(
        AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }
    
    /**
     * 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
     * 그리고 그 필터의 내부 로직을 뭔가 추가하거나 조정하고 싶으면 doFilterInternal함수를 오버라이딩해서 구현해줌
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {
    
        System.out.println("doFilterInternal");
        
        //super.doFilterInternal(request, response, chain); // 지워야 함 왜냐면 이 함수의 마지막에 내가 chain.doFilter(request,response);로 해주고 있기 때문에
        //System.out.println("request that needs to be authorized");
        
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader: "+jwtHeader);
        
        //이제 클라이언트의 요청 헤더에 담긴 jwt 토큰을 검증해서 정상적인 요청인지(=사용자인지) 확인
        //1. 일단 jwt 토큰을 담은 헤더가 있는지 확인
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            // 만약 authorization 헤더가 없거나 bearer 토큰이 없다면 
            //그냥 바로 필터 타게 해버리고 종료함
            chain.doFilter(request,response);
            return;
        }
        
        //2. jwt 토큰을 담은 헤더가 있다면 검증을 해서 정상적인 사용자인지 확인
        // 일단 Bearer 문구를 없앰
        String jwtToken = request.getHeader("Authorization").replace("Bearer ","");
        String username = JWT.require(Algorithm.HMAC512("cos"))//서버만 알고 있는 시크릿 값은 cos
            .build()
            .verify(jwtToken) //서명하기
            .getClaim("username").asString(); //서명한 후에 유저 네임 값을 가져옴
        
        //이제 서명한 값이 정상적인 서명 값과 일치하는지 확인
        if(username != null){
            User userEntity =userRepository.findByUsername(username);//해당 유저네임에 해당되는 객체 찾음
            
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            //Authentication 객체 만듦 - 이번에는 로그인을 통해서 만드는 게 아니라 강제로 만듦
            // 즉 JWT 서명을 통해서 그 서명이 정상이면 Authentication 객체를 만들어 준다
            ////파라미터 중 password는 일단 null이라고 넣음
            //그리고 권한이 꼭 들어가야 함
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null,principalDetails.getAuthorities());
            //강제로 시큐리티 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //그리고 필터 체인 태움
            chain.doFilter(request,response);
        }
    }
}
