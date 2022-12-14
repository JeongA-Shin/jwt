package group.jwtproject.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter1 implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
       /*
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        if(req.getMethod().equals("POST")){ //POST일 때만 검증함
            String headerAuth = req.getHeader("Authorization"); //request header에서의 auth 부분만 받아오기
            //req header의 auth 값이 우리가 원하는 값일 때만 필터 체인을 태움
            if(headerAuth.equals("sth")) chain.doFilter(req,res);
            else{
                PrintWriter outPrintWriter = res.getWriter();
                outPrintWriter.println("not authorized");
            }
        }
        */
     
        System.out.println("filter 1");
        //필터 체인에 등록 - 필터에서 끝나지 않고, req,res를 받은 프로세스가 진행되려면 필터 체인 자체에 등록해야 함
        chain.doFilter(request,response);
  
    }
}
