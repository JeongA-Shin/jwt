package group.jwtproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    

    private final CorsConfig corsConfig; //corsConfig에서 bean으로 등록되어 있으므로 이렇게 di 가능
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        // jwt 방식을 도입하면 #1, #2, #3은 기본 세팅임
        
        http.csrf().disable();
        //세션을 사용하지 않겠다 == stateless 서버로 만들겠다
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //#1
            .and()
            .addFilter(corsConfig.corsFilter()) //cross-origin 정책에서 벗어남
            .formLogin().disable() //jwt 쓸 거니까 폼로그인 안 씀 #2
            .httpBasic().disable() //로그인에 기본적인 http 방식을 안 씀 - Bearer 방식 쓸 거임#3
            .authorizeRequests()
            .antMatchers("/api/v1/user/**")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/manager/**")
            .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/admin/**")
            .access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll();
    }
}
