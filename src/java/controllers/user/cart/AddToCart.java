/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.cart;

import dto.product.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
@WebServlet(name = "AddToCart", urlPatterns = {"/user/cart/AddToCart"})
public class AddToCart extends HttpServlet {

    private final String shopURL = "/MainController?action=shopPage";
    private final String loginURL = "/MainController?action=login";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        
        Map<Product,Integer> cart = (Map<Product,Integer>) session.getAttribute("cart");
        Map<String,Product> products = (Map<String,Product>) session.getAttribute("customerMealList");
        if (cart == null){
            //redirect login
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }else{
            String productId = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            if (productId != null){
                if (quantityStr != null){
                    int quantity = Integer.parseInt(quantityStr);
                    Product productToAdd = products.get(productId);
                    cart.merge(productToAdd, quantity, Integer::sum);
                    
                }else{
                    Product productToAdd = products.get(productId);
                    cart.merge(productToAdd, 1, Integer::sum);
                }
            }
            
            cart.entrySet().forEach(entry -> {
                System.out.println(entry.getKey() + "contain : "+ entry.getValue());
            });
            
            session.setAttribute("cart", cart);
            request.getRequestDispatcher(shopURL).forward(request, response);
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
