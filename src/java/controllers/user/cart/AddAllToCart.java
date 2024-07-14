/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.cart;

import dao.plan.MealPlanDAO;
import dao.product.MealDAO;
import dao.product.ProductDAO;
import dto.account.User;
import dto.plan.DayPlan;
import dto.plan.MealPlan;
import dto.product.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
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
@WebServlet(name = "AddToCart", urlPatterns = {"/user/cart/AddAllToCart"})
public class AddAllToCart extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String cartURL = "/MainController?action=cartDisplayPage";
    private final String loginURL = "/MainController?action=login";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try {

            Map<Product, Integer> cart = (Map<Product, Integer>) session.getAttribute("cart");
            User user = (User) session.getAttribute("LoginedUser");

            if (cart == null || user == null) {
                //redirect login
                request.getRequestDispatcher(loginURL).forward(request, response);
            } else {
                String mealPlanId = request.getParameter("mealPlanId");
                PrintWriter out = response.getWriter();
                MealPlanDAO mpdao = new MealPlanDAO();
                MealPlan mealPlan = mpdao.getCustomerMealPlanById(mealPlanId);
                if (mealPlan != null) {
                    mealPlan.getDayPlanContains().forEach((DayPlan dayPlan) -> {
                        String mId = dayPlan.getMealId();
                        if (mId != null) {
                            Product product = ProductDAO.getProductById(mId);
                            if (product != null) {
                                Product foundProduct = null;
                                for (Product p : cart.keySet()) {
                                    if (p.getId().equalsIgnoreCase(product.getId())) {
                                        foundProduct = p;
                                        break;
                                    }
                                }
                                if (foundProduct != null) {
                                    int quantity = cart.get(foundProduct) + 1;
                                    cart.put(foundProduct, quantity);
                                } else {
                                    cart.put(product, 1);
                                }
                            }
                        }
                    });
                }
                session.setAttribute("cart", cart);
                request.getRequestDispatcher(cartURL).forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
