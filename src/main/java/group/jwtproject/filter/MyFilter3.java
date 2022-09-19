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

public class MyFilter3 implements Filter {
    
    //내가 임의로 만든 필터 태워보기
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
    
        /**
         * 토큰 : 비굣값을 만들어줘야 함. id, pw 정상적으로 들어와서 로그인이 완료되면(로그인 요청마다) 토큰을 만들어주고 그걸 응답으로 보내줌
         * 클라이언트가 요청할 때마다 header의 authorization에 value값으로 토큰을 가지고 있게 되는
         * 그 때 넘어온 토큰을 내가 전에 해당 사용자에게 응답으로 보내준 토큰과 동일한지만 검증하면 됨
         */
    
        //계속해서 필터 체인 타게 해줌 - 이거 안 해주면 여기서 해당 필터에서 걸리고 다음 단계로 진행을 못함
        chain.doFilter(req, res);
    
        
        /*
        if(req.getMethod().equals("POST")) { //POST일 때만 검증함
            String headerAuth = req.getHeader("Authorization"); //request header에서의 auth 부분만 받아오기
            //req header의 auth 값이 우리가 원하는 값일 때만 필터 체인을 태움
            if (headerAuth.equals("sth"))
                chain.doFilter(req, res);
            else {
                PrintWriter outPrintWriter = res.getWriter();
                outPrintWriter.println("not authorized");
            }
        }
        */
    }
}