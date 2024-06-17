/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.login;

import dao.account.StaffDAO;
import dao.account.UserDAO;
import dto.account.Staff;
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
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String ADMIN_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@happicook\\.com$";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String email = request.getParameter("txtEmail");
            String password = request.getParameter("txtPassword");

            HttpSession session = request.getSession();
            String errorMessage = "";
            if (email.matches("^[a-zA-Z0-9._%+-]+@happicook\\.com$")) {
                StaffDAO staffDao = new StaffDAO();
                Staff loginUser = staffDao.logInStaffByEmail(email, password);
                if(loginUser!=null){
                    if(loginUser.getName().equalsIgnoreCase("NotFound")){
                        errorMessage = "WrongPassword";
                    }else{
                        session.setAttribute("LoginedUser",loginUser);
                        request.getRequestDispatcher("AMainController?action=adminmainpage").forward(request, response);
                        return;
                    }
                }
            } else {
                UserDAO userDao = new UserDAO();
                User loginUser = userDao.getUserByEmail(email, password);
                if (loginUser != null) {
                    if (loginUser.getStatus().equalsIgnoreCase("xxx")) {
                        errorMessage = "WrongPassword";
                    } else if (!loginUser.getStatus().equalsIgnoreCase("ACTIVE")) {
                        errorMessage = "Banned";
                    } else {
                        session.setAttribute("LoginedUser", loginUser);
                        request.getRequestDispatcher("AMainController?action=mainpage").forward(request, response);
                        return;
                    }
                } else {
                    errorMessage = "NotFound";
                }
            }

            session.setAttribute("ERROR", errorMessage);
            session.setAttribute("ReturnedEmail", email);
            request.getRequestDispatcher("AMainController?action=").forward(request, response);
            return;
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
