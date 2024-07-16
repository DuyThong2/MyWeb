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
import java.util.List;
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
    
    private final String MEAL_PLAN_PAGE="/AMainController?action=MealPlanPage";
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
            List<MealPlan> mealPlanList = (ArrayList<MealPlan>) session.getAttribute("currentList");
            List<MealPlan> listFilter = (ArrayList<MealPlan>) session.getAttribute("listFilter");
            String search = request.getParameter("txtsearch");
            String changeStatusId = request.getParameter("id");

            //
            String cate = request.getParameter("cate");
            String sort = request.getParameter("sort");

            String type = request.getParameter("type");

            //changestatus
            if (changeStatusId != null) {
                mpdao.changeStatusById(changeStatusId.trim());
                mealPlanList.forEach((MealPlan mealPlan) -> {
                    if (mealPlan.getId().equals(changeStatusId)) {
                        mealPlan.changeStatus();
                    }
                });
                session.setAttribute("currentList", mealPlanList);
            }

            //search
            if (search != null) {
                session.setAttribute("localSearch", search);
                mealPlanList = searchNameList(search, mpdao);
                session.removeAttribute("listFilter");
                listFilter = null;
                session.setAttribute("currentList", mealPlanList);
                numPage = 1;
            } else {
                // Maintain search state across page navigations
                search = (String) session.getAttribute("localSearch");
                if (search != null && !search.isEmpty()) {
                    mealPlanList = searchNameList(search.trim(), mpdao);
                } else {
                    mealPlanList = mpdao.getAllMealPlans();

                }
                session.setAttribute("currentList", mealPlanList);
            }

            //sorting
            if (cate != null && sort != null) {
                if (listFilter == null) {
                    mealPlanList = sortMealPlan(mealPlanList, sort.trim(), cate.trim());
                } else {
                    listFilter = sortMealPlan(listFilter, sort.trim(), cate.trim());
                }
            }
            if (type != null) {
                listFilter = getListType(mealPlanList, type.trim());
                numPage = 1;
            }
            session.setAttribute("currentList", mealPlanList);
            session.setAttribute("listFilter", listFilter);
            session.setAttribute("NumPage", numPage);
            System.out.println(mealPlanList.size());
            request.getRequestDispatcher(MEAL_PLAN_PAGE).forward(request, response);
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

    private List<MealPlan> searchNameList(String search, MealPlanDAO mpdao) {

        List<MealPlan> returnList = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            returnList = mpdao.getAllMeanPlanByName(search);
        } else {
            returnList = mpdao.getAllMealPlans();
        }
        return returnList;
    }

    private List<MealPlan> sortMealPlan(List<MealPlan> list, String sort, String cate) {
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

    private List<MealPlan> getListType(List<MealPlan> mealPlanList, String type) {

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
        HttpSession session = request.getSession();
        MealPlanDAO mpdao = new MealPlanDAO();
        List<MealPlan> list = mpdao.getAllMealPlans();
        session.removeAttribute("listFilter");
        session.setAttribute("currentList", list);
        session.setAttribute("NumPage", 1);
        session.removeAttribute("localSearch");
        request.getRequestDispatcher(MEAL_PLAN_PAGE).forward(request, response);
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
