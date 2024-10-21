package com.taufeeq.web.serv;

import com.taufeeq.web.dao.SessionDAO;
import com.taufeeq.web.dao.SessionDAOImpl;
import com.taufeeq.web.scheduler.SessionScheduler;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@WebFilter("/*")
public class SessionFilter implements Filter {

    private static final Logger accessLogger = Logger.getLogger("AccessLogger");
    private static final Logger applicationLogger = Logger.getLogger("ApplicationLogger");

    private SessionDAO sessionDAO = new SessionDAOImpl();

    static {
        try {
        	FileHandler accessFileHandler = new FileHandler("access.log", 10 * 1024 * 1024, 5, true);

            accessLogger.addHandler(accessFileHandler);
            accessFileHandler.setFormatter(new SimpleFormatter());

            FileHandler applicationFileHandler = new FileHandler("application.log", 1024*1024, 5, true);
            applicationLogger.addHandler(applicationFileHandler);
            applicationFileHandler.setFormatter(new SimpleFormatter());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String clientIP = httpRequest.getRemoteAddr();
        String httpMethod = httpRequest.getMethod();
        String userAgent = httpRequest.getHeader("User-Agent");
        long startTime = System.currentTimeMillis();

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = dateTime.format(formatter);

        if (requestURI.endsWith("index.jsp") || requestURI.endsWith("login.jsp") ||
            requestURI.endsWith("signup.jsp") || requestURI.endsWith("/login") ||
            requestURI.endsWith("/signup")) {
            chain.doFilter(request, response);
            return;
        }

        Cookie[] Scookies = httpRequest.getCookies();
        String sessionId = null;
        if (Scookies != null) {
            for (Cookie cookie : Scookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        
        
        int userId = sessionDAO.getUserIdBySessionId(sessionId);
        request.setAttribute("userId", userId);


        if ((requestURI.endsWith("/")) && sessionId != null) {
            httpResponse.sendRedirect("dashboard");
            return;
        }

        if (sessionId == null || !sessionDAO.isValidSession(sessionId)) {
            httpResponse.sendRedirect("login.jsp");
            applicationLogger.warning("Invalid session for Client IP: " + clientIP + ", Session ID: " + sessionId);
            return;
        }

        SessionScheduler sessionScheduler = (SessionScheduler) httpRequest.getServletContext().getAttribute("sessionScheduler");

        if (!isComingFromLogin(httpRequest)) {
            sessionScheduler.addSession(sessionId, new Timestamp(System.currentTimeMillis()));
            applicationLogger.info("Session updated for Session ID: " + sessionId);
        }


        int statusCode = httpResponse.getStatus();
        accessLogger.info("Access log - Client IP: " + clientIP + ", Request URI: " + requestURI +
                          ", HTTP Method: " + httpMethod + ", Session ID: " + sessionId +
                          ", User ID: " + userId + ", Time: " + formattedTime +
                          ", User-Agent: " + userAgent + ", Status Code: " + statusCode);

 
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        applicationLogger.info("Request processing time for URI: " + requestURI + " - " + executionTime + " ms");

        chain.doFilter(request, response);
    }

    private boolean isComingFromLogin(HttpServletRequest httpRequest) {
        return httpRequest.getHeader("Referer") != null &&
               httpRequest.getHeader("Referer").endsWith("login.jsp");
    }
}
