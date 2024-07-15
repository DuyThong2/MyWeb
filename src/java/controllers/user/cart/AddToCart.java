/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.cart;

import dao.product.ProductDAO;
import dto.account.User;
import dto.product.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
@WebServlet(name = "AddToCart", urlPatterns = {"/user/cart/AddToCart"})
public class AddToCart extends HttpServlet {


    private final String shopURL = "/MainController?action=shop";
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
        try{
            
        
        Map<Product,Integer> cart = (Map<Product,Integer>) session.getAttribute("cart");
        User user = (User) session.getAttribute("LoginedUser");
        
        if (cart == null || user == null){
            //redirect login
            request.getRequestDispatcher(loginURL).forward(request, response);
        }else{
            String productId = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            if (productId != null){
                if (quantityStr != null){
                    try{
                        int quantity = Integer.parseInt(quantityStr);
                        if (quantity >=1){
                        addProductToExistCart(cart, quantity, productId);
                    }
                    }catch(NumberFormatException e){
                        addProductToExistCart(cart, 1, productId);
                    }
                    
                    
                    
                }else{
                    addProductToExistCart(cart, 1, productId);
                }
                
            }
            
            cart.entrySet().forEach(entry -> {
                System.out.println(entry.getKey() + "contain : "+ entry.getValue());
            });
            
            session.setAttribute("cart", cart);
            response.sendRedirect(shopURL);
//            request.getRequestDispatcher(shopURL).forward(request, response);
        }}catch(Exception e){
            e.printStackTrace();
            
        }
        
        
    }
    
    private void addProductToExistCart(Map<Product,Integer> cart,int quantity,String productId){
        
        Optional<Product> found = cart.keySet().stream()
                                .filter(product -> product.getId().matches(productId))
                                .findFirst();
        if (found.isPresent()){
            cart.merge(found.get(), quantity, Integer::sum);
        }else{
            Product newAddedProduct = ProductDAO.getProductById(productId);
            cart.put(newAddedProduct, quantity);
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
