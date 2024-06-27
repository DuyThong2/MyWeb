/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin;

import dao.account.UserDAO;
import dao.order.OrderDAO;
import dao.order.OrderItemDAO;
import dto.account.User;
import dto.order.Order;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
@WebServlet(name = "AdminMainPageController", urlPatterns = {"/admin/home"})
public class AdminMainPageController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String HOME = "/AMainController?action=adminmainpage";
    
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        UserDAO userDAO = new UserDAO();
        OrderDAO orderDAO = new OrderDAO();
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        
        
        List<User> users = userDAO.getWarningUser();
        Map<Integer,Order> orders = orderDAO.getNewestOrdersForMainPage(15);
        int totalUsers = userDAO.getTotalUsers();
        int totalOrders = orderDAO.getTotalOrders();
        int totalItemsOrdered = orderItemDAO.getTotalOrderItems();
        

        req.setAttribute("users", users);
        req.setAttribute("orders", new ArrayList<>(orders.values()));
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("todayWarningPeople", users.size());
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("totalItemsOrdered", totalItemsOrdered);
        
        req.getRequestDispatcher(HOME).forward(req, resp);
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
