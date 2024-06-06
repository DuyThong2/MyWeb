/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.account;

import Utility.Tool;
import dao.account.UserDAO;
import dto.account.User;
import dto.order.Order;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns = {"/admin/account/UserDetail"})
public class UserDetail extends HttpServlet {

    private final String SHOW_URL = "/AMainController?action=userDetailPage";
    private final String REDIRECT_URL = "/AMainController?action=userManage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String userId = request.getParameter("userId");
        
        UserDAO dao = new UserDAO();

        if (userId != null) {
            User user = dao.getUserWithOrders(Integer.parseInt(userId));
            request.setAttribute("user", user);
            
            String status = request.getParameter("status");

            HttpSession session = request.getSession();
            dao = new UserDAO();
            if (status != null) {
                dao.updateCustomerStatus(Integer.parseInt(userId), status);
            }

            String numPageStr = request.getParameter("numPage");
            
            int numPage = numPageStr != null ? Integer.parseInt(numPageStr)
                    : session.getAttribute("numPage") != null
                    ? (int) session.getAttribute("numPage") : 1;
            session.setAttribute("numPage", numPage);
        } else {
            request.getRequestDispatcher(REDIRECT_URL).forward(request, response);
        }

        request.getRequestDispatcher(SHOW_URL).forward(request, response);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
