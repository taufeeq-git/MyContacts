package com.taufeeq.web.serv;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebFilter;

@WebFilter("/*")

public class SessionFilter implements Filter {


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String login = req.getContextPath() + "/login.jsp";
        String loginServlet = req.getContextPath() + "/login";
        String registerServlet = req.getContextPath() + "/signup";
        String index = req.getContextPath() + "/index.jsp"; 
        String register = req.getContextPath() + "/signup.jsp"; 
        boolean loggedIn = (session != null && session.getAttribute("userId") != null);
        boolean loginReq = req.getRequestURI().equals(login);
        boolean loginSer = req.getRequestURI().equals(loginServlet);
        boolean regSer = req.getRequestURI().equals(registerServlet);
        boolean indexReq = req.getRequestURI().equals(index);
        boolean regReq = req.getRequestURI().equals(register);
        if (loggedIn || loginReq|| indexReq || regReq || loginSer || regSer) {
            chain.doFilter(request, response); 
        } else {
            res.sendRedirect(req.getContextPath()+"/login.jsp"); 
        }
    }

}