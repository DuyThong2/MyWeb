/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.cart;

import dao.account.UserDAO;
import dao.order.OrderDAO;
import dto.account.User;
import dto.product.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;
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
@WebServlet(name = "OrderCart", urlPatterns = {"/user/cart/OrderCart"})
public class OrderCart extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String SUCCESS_URL = "/MainController?action=success";
    private final String FAIL_URL = "/MainController?action=error";
    private final String LOGIN_URL = "/MainController?action=login";
    private final String UPDATE_PROFILE_URL = "/MainController?action=userUpdatePage";
    private final String SHOP_URL = "/MainController?action=shopPage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        //only for testing:
        // simulate real user

        Map<Product, Integer> cart = (Map<Product, Integer>) session.getAttribute("cart");
        User user = (User) session.getAttribute("LoginedUser");
        if (user == null) {
            request.getRequestDispatcher(LOGIN_URL).forward(request, response);
        }else if(user.getAddress() == null){
            request.getRequestDispatcher(UPDATE_PROFILE_URL).forward(request, response);
        }else {
            if (cart != null && !cart.isEmpty()) {
                System.out.println(user.getAddress() == null);
                OrderDAO orderDAO = new OrderDAO();
                try {
                    orderDAO.applyOrder(cart, user);
                    cart.clear();
                    request.setAttribute("successMessage", "thank for order");
                    request.getRequestDispatcher(SUCCESS_URL).forward(request, response);
                } catch (Exception e) {
                    request.setAttribute("errorMessage", "error with order");
                    request.getRequestDispatcher(FAIL_URL).forward(request, response);
                    e.printStackTrace();
                }
            } else{
                request.getRequestDispatcher(SHOP_URL).forward(request, response);
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
