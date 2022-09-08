package group.jwtproject.config;

import group.jwtproject.filter.MyFilter1;
import group.jwtproject.filter.MyFilter2;
import javax.servlet.FilterRegistration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    
    
    /**
    단!!! 우선 순위를 따질 때, security 필터 체인 내에서의 필터 우선수위를 먼저 잘 파악해야 함
     */

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");//모든 요청에 대해서 해당 필터가 적용됨
        bean.setOrder(0);//필터의 우선순위 정하기 - 0이면 제일 높음
        return bean;
    }
    
    
    @Bean
    public FilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");//모든 요청에 대해서 해당 필터가 적용됨
        bean.setOrder(1);//필터의 우선순위 정하기 - -낮을 수록 높음
        return bean;
    }
}
