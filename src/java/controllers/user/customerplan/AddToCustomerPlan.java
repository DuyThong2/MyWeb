/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.customerplan;

import dao.plan.CustomerDayPlanDAO;
import dao.plan.CustomerPlanDAO;
import dao.plan.MealPlanDAO;
import dto.account.User;
import dto.plan.CustomerPlan;
import dto.plan.MealPlan;
import dto.product.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
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
@WebServlet(name = "AddToCustomerPlan", urlPatterns = {"/user/AddToCustomerPlan"})

public class AddToCustomerPlan extends HttpServlet {

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
        try (PrintWriter out = response.getWriter();) {
            HttpSession session = request.getSession();
            MealPlanDAO mpdao = new MealPlanDAO();
            CustomerPlanDAO cpdao = new CustomerPlanDAO();
            CustomerDayPlanDAO cdpdao = new CustomerDayPlanDAO();
            User user = (User) session.getAttribute("LoginedUser");
            if (user == null) {
                //redirect login
                request.getRequestDispatcher(loginURL).forward(request, response);
                return;
            }
            String mealPlanId = request.getParameter("mealPlanId");
            String week = request.getParameter("addWeek");

            if (mealPlanId == null || mealPlanId.trim().isEmpty() || week == null || week.trim().isEmpty()) {
                // Invalid input, return error message
                out.print("Invalid input");
                return;
            }
            int insertWeek = Integer.parseInt(week.trim());
            try {
                MealPlan mealPlan = null;
                if (mealPlanId.equalsIgnoreCase("MP999")) {
                    mealPlan = mpdao.getCustomizedMealPlan();
                } else {
                    mealPlan = mpdao.getCustomerMealPlanById(mealPlanId);
                }
                if (mealPlan != null) {
                    int result = cpdao.insertCustomerPlan(mealPlan, insertWeek, user.getId());
                    if (result > 0) {
                        int result2 = cdpdao.insertCustomerPlanListWithMealPlan(result, mealPlan);
                        if (result2 > 0) {
                            CustomerPlan customerPlan = cpdao.getCustomerPlanById(result);
                            System.out.println(customerPlan.toString());
                            TreeMap<Integer, CustomerPlan> customerPlanTree = (TreeMap<Integer, CustomerPlan>) session.getAttribute("customerPlanList");
                            if (customerPlanTree == null) {
                                customerPlanTree = new TreeMap<>();
                            }
                            customerPlanTree.put(insertWeek, customerPlan);
                            session.setAttribute("customerPlanList", customerPlanTree);
                        }
                    } else {
                        session.setAttribute("ADDMPERROR", "CANNOT ADD MEAL PLAN TO WEEK " + week.toUpperCase());
                    }
                } else {
                    // Meal plan not found
                }
            } catch (NumberFormatException e) {
                // Handle invalid week format
                out.print("Invalid week format");
            } catch (Exception e) {
                // General exception handling
                e.printStackTrace();
                out.print("An error occurred");
            }
            request.setAttribute("insertWeek", insertWeek);
            // Forward to MainController with action parameter
            response.sendRedirect("MainController?action=customerPlan&week=" + insertWeek);
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
