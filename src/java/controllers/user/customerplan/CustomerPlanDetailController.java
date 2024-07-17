/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.customerplan;

import dao.plan.CustomerPlanDAO;
import dao.plan.MealPlanDAO;
import dto.account.User;
import dto.plan.CustomerPlan;
import dto.plan.MealPlan;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
@WebServlet(name = "CustomerPlanDetailController", urlPatterns = {"/user/CustomerPlanDetailController"})
public class CustomerPlanDetailController extends HttpServlet {

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
    private final String customerUrl = "/MainController?action=customerPlanPage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        CustomerPlanDAO cpdao = new CustomerPlanDAO();
        User user = (User) session.getAttribute("LoginedUser");
        if (user == null) {
            request.getRequestDispatcher(loginURL).forward(request, response);
            return;
        }
        //Scheduled Meal Plan detail
        TreeMap<Integer, CustomerPlan> customerPlanList = (TreeMap<Integer, CustomerPlan>) session.getAttribute("customerPlanList");
        String currentWeek = (String) request.getParameter("week");
        int weekNumber = 1;
        if (currentWeek != null) {
            currentWeek = (String)request.getAttribute("insertWeek");
           if(currentWeek!=null){
                weekNumber = Integer.parseInt(currentWeek);
           }
        }
        if (customerPlanList == null) {
            customerPlanList = cpdao.getAllCustomerPlansById(user.getId());
            if (customerPlanList == null) {
                customerPlanList = new TreeMap<>();
            }
        }
        //Search Meal Plan
        MealPlanDAO mealPlanDAO = new MealPlanDAO();
        List<MealPlan> searchMealPlan = new ArrayList<MealPlan>();
        String search = request.getParameter("txtsearch");
        if (search != null) {
            searchMealPlan = mealPlanDAO.getCustomerAllMeanPlanByName(search);
            session.setAttribute("searchMealPlan", searchMealPlan);
            session.setAttribute("localSearchScheduled",search);
        }

        //delete id 
        String deleteId = request.getParameter("deleteId");
        String deleteWeek = request.getParameter("deleteWeek");
        if (deleteId != null && !deleteId.isEmpty() && deleteWeek != null && !deleteWeek.isEmpty()) {
            int result = cpdao.deleteCustomerPlan(Integer.parseInt(deleteId.trim()));
            if (result > 0) {
                customerPlanList.put(Integer.parseInt(deleteWeek.trim()), null);
            }else{//error message
                
            }
        }
        request.setAttribute("week", currentWeek);
        request.setAttribute("customerPlanList", customerPlanList);
        request.getRequestDispatcher(customerUrl).forward(request, response);
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
