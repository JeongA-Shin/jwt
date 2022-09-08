package group.jwtproject.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyFilter2 implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        System.out.println("filter 2");
        //필터 체인에 등록 - 필터에서 끝나지ㅣ 않고 계속 프로세스가 진행되려면 필터 체인 자체에 등록해야 함
        chain.doFilter(request,response);
    }
}