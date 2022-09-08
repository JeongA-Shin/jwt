package group.jwtproject.config;

import group.jwtproject.filter.MyFilter1;
import group.jwtproject.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    

    private final CorsConfig corsConfig; //corsConfig에서 bean으로 등록되어 있으므로 이렇게 di 가능
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        // jwt 방식을 도입하면 #1, #2, #3은 기본 세팅임
        
        //security필터에 내가 만든 필터도 걸기 - 기본적인 BasicAutenticatinFilter가 실행되지 전에 내가 만든 필터가 추가되도록 함
        //이렇게 해도 되긴 하지만 나는 FilterConfig에서 따로 내가 만든 필터를 추가해줌
        //단!!! 내가 만든 필터보다 security 필터체인이 무조건 우선 순위임, 내가 스스로 우선순위를 부여해도 그건 일단 security 필터가 먼저 수행되고 나서의 우선순위임!
        //필터에서 우선순위를 잘 부여하려면, security filter 외의 다른 필터의 우선순위와 before, after 함수를 잘 파악해줘야 함
        //즉, 필터 실행시 우선 순위를 따질 때, security 필터 체인 내에서의 필터 우선수위를 먼저 잘 파악해야 함
        //http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
        
        //시큐리티 필터가 동작하기 전에 내가 만든 필터가 동작하도록
        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        
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
