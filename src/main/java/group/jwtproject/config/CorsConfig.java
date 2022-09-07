package group.jwtproject.config;

import org.springframework.web.filter.CorsFilter; //이거 import 잘 해야 함. apache가 아님
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        CorsConfiguration config = new CorsConfiguration();
        ////내 서버가 응답으로 돌려주는 json등을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        //만약 얘가 false면 자바스크립트로 요청을 했을 때 응답이 오지 않음
        config.setAllowCredentials(true);
        //모든 ip에 대한 응답을 허용하겠다
        config.addAllowedOrigin("*");
        //모든 header에 대한 응답을 허용하겠다
        config.addAllowedHeader("*");
        //모든 put, get, delete등의 메서드에 대한 응답을 허용하겠다
        config.addAllowedMethod("*");
        
        source.registerCorsConfiguration("/api/**",config); // // 해당 URL 들은 이 config 를 수행한다
        
        return new CorsFilter(source);
        
    }
}
