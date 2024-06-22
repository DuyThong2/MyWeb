/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.cart;

import dto.product.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;
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
@WebServlet(name = "ShowCart", urlPatterns = {"/user/cart/ShowCart"})
public class ShowCart extends HttpServlet {

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
    private final String LOGIN = "/MainController?action=login";
    private final String REDIRECT_PAGE = "/MainController?action=cartDisplayPage";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        Map<Product,Integer> cart = (Map<Product,Integer>) session.getAttribute("cart");
        if (cart == null){
            request.getRequestDispatcher(LOGIN).forward(request, response);
            return;
        }
        String productId = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");
        
        if (productId != null && quantityStr != null){
            Optional<Product> found = cart.keySet().stream()
                                .filter(product -> product.getId().matches(productId))
                                .findFirst();
            
            if (found.isPresent()){
                int quantity = Integer.parseInt(quantityStr);
                if (quantity >= 1){
                    cart.put(found.get(), quantity);
                }else{
                    cart.keySet().remove(found.get());
                }
                
            }
        }
        
        //delete if needed
        String deleteId = request.getParameter("deleteId");
        if (deleteId != null){
            cart.keySet().removeIf(product -> product.getId().matches(deleteId));
        }
        
        cart.entrySet().forEach(entry -> {
                System.out.println(entry.getKey() + "contain : "+ entry.getValue());
            });
        
        session.setAttribute("cart", cart);
        
        request.getRequestDispatcher(REDIRECT_PAGE).forward(request, response);
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
