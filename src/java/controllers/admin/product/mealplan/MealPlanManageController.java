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
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import javafx.print.Collation;
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

@WebServlet(urlPatterns = {"/admin/MealPlanManageController"})
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

            //
            String cate = request.getParameter("cate");
            String sort = request.getParameter("sort");

            String type = request.getParameter("type");

            if (changeStatusId != null) {
                mpdao.changeStatusById(changeStatusId.trim());
                mealPlanList = mpdao.getAllMealPlans();
            }
            if (search != null) {
                String localSearch = (String) session.getAttribute("localSearch");
                if (localSearch == null || !localSearch.equalsIgnoreCase(search)) {
                    session.setAttribute("localSearch", search);
                    mealPlanList = searchNameList(search, mpdao);
                    session.setAttribute("currentList", mealPlanList);
                }
            } else {
                    mealPlanList = mpdao.getAllMealPlans();
                    session.setAttribute("currentList", mealPlanList);
            }
            if (cate != null && sort != null) {
                mealPlanList = sortMealPlan(mealPlanList, sort.trim(), cate.trim());
            }

            if (type != null) {
                mealPlanList = getListType(mealPlanList, type);
            }

            request.setAttribute("MealPlanList", mealPlanList);
            request.setAttribute("NumPage", numPage);
            request.getRequestDispatcher("/AMainController?action=MealPlanPage").forward(request, response);
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

    private ArrayList<MealPlan> sortMealPlan(ArrayList<MealPlan> list, String sort, String cate) {
        int sortOrder = sort.equalsIgnoreCase("max") ? 1 : -1;
        Comparator<MealPlan> comparator = null;
        switch (cate) {
            case "id":
                comparator = (o1, o2) -> o1.getId().compareTo(o2.getId()) * sortOrder;
                break;
            case "name":
                comparator = (o1, o2) -> o1.getName().compareTo(o2.getName()) * sortOrder; //To change body of generated lambdas, choose Tools | Templates.

                break;
            case "type":
                comparator = (o1, o2) -> o1.getType().compareTo(o2.getType()) * sortOrder;
                break;

            case "status":
                comparator = (o1, o2) -> (o1.getStatus() - o2.getStatus()) * sortOrder;
                break;

        }
        Collections.sort(list, comparator);
        return list;
    }

    private ArrayList<MealPlan> getListType(ArrayList<MealPlan> mealPlanList, String type) {
       
        return (ArrayList<MealPlan>) mealPlanList.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
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
