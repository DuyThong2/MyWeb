/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.login;

import dao.account.UserDAO;
import dto.account.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     *
     */
    private static final String EMAIL_PATTERN
            = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String ADMIN_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@happicook\\.com$";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String userName = request.getParameter("txtUserName");
            String phone = request.getParameter("txtPhone");
            String email = request.getParameter("txtEmail");
            String password = request.getParameter("txtPassword");
            String confirmPassword = request.getParameter("txtPasswordConfirm"); // Fixed parameter name
            UserDAO userDao = new UserDAO();
            HttpSession session = request.getSession();
            session.removeAttribute("REGISTER_ERROR");
            String error = null;
            if (userName == null || userName.isEmpty() || phone == null || phone.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
                error = "Empty";
                session.setAttribute("REGISTER_ERROR", error);
                request.setAttribute("userName", userName);
                request.setAttribute("phone", phone);
                request.setAttribute("email", email);
                request.getRequestDispatcher("AMainController?action=registerform").forward(request, response);
            } else {
                if (!email.matches(EMAIL_PATTERN)||email.matches(ADMIN_EMAIL_PATTERN)) {
                    error = "Email";
                    session.setAttribute("REGISTER_ERROR", error);
                    request.setAttribute("userName", userName);
                    request.setAttribute("phone", phone);
                    request.setAttribute("email", email);
                    request.getRequestDispatcher("AMainController?action=registerform").forward(request, response);
                } else if (!password.equals(confirmPassword)) {
                    error = "Password";
                    session.setAttribute("REGISTER_ERROR", error);
                    request.setAttribute("userName", userName);
                    request.setAttribute("phone", phone);
                    request.setAttribute("email", email);
                    request.getRequestDispatcher("AMainController?action=registerform").forward(request, response);
                } else {
                    User user = new User(userName, phone, email, password);
                    int result = userDao.registerNewAccount(user);
                    if (result == -2) {
                        error = "FoundEmail";
                        session.setAttribute("REGISTER_ERROR", error);
                        request.setAttribute("userName", userName);
                        request.setAttribute("phone", phone);
                        request.setAttribute("email", email);
                        request.getRequestDispatcher("AMainController?action=registerform").forward(request, response);

                    } else {
                        session.setAttribute("REGISTER_SUCCESS", "Success");
                        request.getRequestDispatcher("AMainController?action=registerform").forward(request, response);
                    }
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
