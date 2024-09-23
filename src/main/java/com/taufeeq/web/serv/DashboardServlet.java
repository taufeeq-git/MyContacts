package com.taufeeq.web.serv;

import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.getUserById(userId);

        if (user != null) {
            request.setAttribute("user", user);
//            request.setAttribute("userID", userId);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        UserDAO userDAO = new UserDAOImpl();

        if ("logout".equals(action)) {
            if (session != null) {
                session.invalidate(); 
            }
            response.sendRedirect("login.jsp"); 
            return; 
        } else if ("addEmail".equals(action)) {
            Integer userId = (Integer) session.getAttribute("userId");
            String email = request.getParameter("email");
            userDAO.addUserEmail(userId, email);
            response.sendRedirect("dashboard");
            return; 
        } else if ("addPhoneNumber".equals(action)) {
            Integer userId = (Integer) session.getAttribute("userId");
            String phone = request.getParameter("number");
            userDAO.addUserPhoneNumber(userId, phone);
            response.sendRedirect("dashboard");
            return;
        }
    }

}
