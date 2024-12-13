package com.taufeeq.web.serv;

import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.GroupDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.dao.GroupDAOImpl;
import com.taufeeq.web.model.Group;
import com.taufeeq.web.model.User;
import com.taufeeq.web.dao.SessionDAO;  
import com.taufeeq.web.dao.SessionDAOImpl;  
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
//        String sessionId = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("sessionId".equals(cookie.getName())) {
//                    sessionId = cookie.getValue();
//                    break;
//                }
//            }
//        }
    	int userId=(int) request.getAttribute("userId");

//        SessionDAO sessionDAO = new SessionDAOImpl();
//        int userId = sessionDAO.getUserIdBySessionId(sessionId); 

        UserDAO userDAO = new UserDAOImpl();
        
        User user = userDAO.getUserById(userId); 
//        System.out.println(user.getUsername());

        

        if (user != null) {
            GroupDAO groupDAO = new GroupDAOImpl();
            List<Group> groupList = groupDAO.getUserGroupsWithIds(userId);
         
            request.setAttribute("user", user);
            request.setAttribute("groupList", groupList);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        SessionDAO sessionDAO = new SessionDAOImpl();
//
//       
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        int userId=(int) request.getAttribute("userId");
        

        if ("logout".equals(action)) {
            if (sessionId != null) {
                sessionDAO.deleteSessionById(sessionId); 
            }
            response.sendRedirect("login.jsp");
            return;
        } else if ("setFormat".equals(action)) {
            String selectedFormat = request.getParameter("dateFormat");
            if (selectedFormat != null) {
            	UserDAO userDAO = new UserDAOImpl();
            	userDAO.updateFormat(userId, selectedFormat);
//            	System.out.println(selectedFormat+"format");
            }
            response.sendRedirect("dashboard");
            return;
        }
        else if ("addEmail".equals(action)) {
//            Integer userId = sessionDAO.getUserIdBySessionId(sessionId); 
            String email = request.getParameter("email");
            UserDAO userDAO = new UserDAOImpl();
            userDAO.addUserEmail(userId, email); 
            response.sendRedirect("dashboard");
            return;
        } else if ("addPhoneNumber".equals(action)) {
//            Integer userId = sessionDAO.getUserIdBySessionId(sessionId); 
            String phone = request.getParameter("number");
            UserDAO userDAO = new UserDAOImpl();
            userDAO.addUserPhoneNumber(userId, phone); 
            response.sendRedirect("dashboard");
            return;
        }
    }
}
