/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.customerplan;

import dao.plan.CustomerPlanDAO;
import dao.product.MealDAO;
import dto.account.User;
import dto.product.Meal;
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
 * @author ASUS
 */
@WebServlet(name = "CustomerPlanUpdateController", urlPatterns = {"/user/CustomerPlanUpdateController"})
public class CustomerPlanUpdateController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String loginURL = "/MainController?action=login";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        CustomerPlanDAO cpdao = new CustomerPlanDAO();
        MealDAO mdao = new MealDAO();

        User user = (User) session.getAttribute("LoginedUser");
        if (user == null) {
            request.getRequestDispatcher(loginURL).forward(request, response);
            return;
        }

        // Get parameters
        String id = request.getParameter("customerPlanId");
        String search = request.getParameter("txtsearch");

        // Validate parameters
        List<Meal> mealList = new ArrayList<>();
        if (search != null && !search.trim().isEmpty()) {
            mealList = mdao.getMealsByNameForCustomer(search);  // Assume this returns List<Meal>
            session.setAttribute("searchingMealList", mealList);
            session.setAttribute("localSearchScheduled", search);
        }

        // Set attributes for forwarding
        request.setAttribute("customerPlanId", id);

        // Forward to JSP
        
        request
                .getRequestDispatcher("/MainController?action=customerPlanUpdatePage&customerPlanId="+id).forward(request, response);
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
