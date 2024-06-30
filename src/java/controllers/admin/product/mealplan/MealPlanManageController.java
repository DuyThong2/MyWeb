/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.product.mealplan;

import dao.plan.MealPlanDAO;
import dto.plan.MealPlan;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ASUS
 */
public class MealPlanManageController extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
//            page control
            String currentNumPage = (String) request.getParameter("NumPage");
            int numPage = 1;
            if (currentNumPage != null) {
                numPage = Integer.parseInt(currentNumPage);
            }
            MealPlanDAO mpdao = new MealPlanDAO();
            HttpSession session = request.getSession();
            ArrayList<MealPlan> mealPlanList = (ArrayList<MealPlan>) session.getAttribute("currentList");
            String search = request.getParameter("txtsearch");
            String changeStatusId = request.getParameter("id");
            if(changeStatusId!=null){
                mpdao.changeStatusById(changeStatusId.trim());
                mealPlanList=mpdao.getAllMealPlans();
            }
            if (search != null) {
                String localSearch = (String) session.getAttribute("localSearch");
                if (localSearch == null || !localSearch.equalsIgnoreCase(search)) {
                    session.setAttribute("localSearch", search);
                    mealPlanList = searchNameList(search, mpdao);
                    session.setAttribute("currentList", mealPlanList);
                }
            } else {
                if (mealPlanList == null) {
                    mealPlanList = mpdao.getAllMealPlans();
                    session.setAttribute("currentList", mealPlanList);
                }
            }

            request.setAttribute("MealPlanList", mealPlanList);
            request.setAttribute("NumPage", numPage);
            request.getRequestDispatcher("AMainController?action=MealPlanPage").forward(request, response);
        }

    }

//    private ArrayList<MealPlan> searchNameList(HttpServletRequest request, MealPlanDAO mpdao) {
//
//        ArrayList<MealPlan> returnList = new ArrayList<>();
//        String search = (String) request.getParameter("txtsearch");
//        request.setAttribute("search",search);
//        if (search != null) {
//            returnList = mpdao.getAllMeanLanByName(search);
//        }else{
//            returnList= mpdao.getAllMealPlans();
//        }
//        return returnList;
//    }
    private ArrayList<MealPlan> searchNameList(String search, MealPlanDAO mpdao) {

        ArrayList<MealPlan> returnList = new ArrayList<MealPlan>();
        if (search != null && !search.isEmpty()) {
            returnList = mpdao.getAllMeanLanByName(search);
        } else {
            returnList = mpdao.getAllMealPlans();
        }
        return returnList;
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
