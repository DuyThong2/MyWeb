/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.product.dayplan;

import dao.plan.DayPlanDAO;
import dao.plan.MealPlanDAO;
import dao.product.MealDAO;
import dto.plan.DayMeal;
import dto.plan.DayPlan;
import dto.plan.MealPlan;
import dto.product.Meal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
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
@WebServlet(name = "DayPlanInsertController", urlPatterns = {"/admin/DayPlanInsertController"})
public class DayPlanInsertController extends HttpServlet {

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

        /* TODO output your page here. You may use following sample code. */
        String id = (String) request.getParameter("id");
        MealPlanDAO dao = new MealPlanDAO();
        DayPlanDAO dpdao = new DayPlanDAO();
        MealDAO mdao = new MealDAO();
        HttpSession session = request.getSession();
        MealPlan mealPlan = (MealPlan) request.getAttribute("mealPlan");

        //MEALPLAN
        if (mealPlan == null) {
            mealPlan = dao.getMealPlanById(id);
        }

        //SEARCHING
        String search = (String) request.getParameter("txtsearch");
        ArrayList<Meal> searchList = (ArrayList<Meal>) request.getAttribute("mealList");
        if (search != null && !search.trim().isEmpty() && searchList == null) {
            searchList = (ArrayList<Meal>) mdao.getMealsByName(search).stream().limit(7).collect(Collectors.toList());
        }

        //INSERT DAYPLAN
        if (mealPlan != null && mealPlan.getDayPlanContains().isEmpty()) {
            dpdao.insertDayPlanList(id);
            mealPlan = dao.getMealPlanById(mealPlan.getId());
        }

        //CREATE MEAL LIST
        TreeMap<Integer, DayMeal> mealList = (TreeMap<Integer, DayMeal>) request.getAttribute("mealList");
        if (mealList == null) {
            mealList = new TreeMap<>();
            boolean[] daysInWeek = new boolean[7];
            for (DayPlan dayPlan : mealPlan.getDayPlanContains()) {
                int day = dayPlan.getDayInWeek();
                String mealId = dayPlan.getMealId();
                Meal meal = mdao.getMealFromId(mealId);
                daysInWeek[day] = true;
                DayMeal dayMeal = new DayMeal(dayPlan, meal);
                mealList.put(day, dayMeal);
            }
            for (int i = 0; i < 7; i++) {
                if (!daysInWeek[i]) {
                    mealList.put(i, null);
                }
            }
        }
        //ADD MEAL to Day Plan
        String addMealId = request.getParameter("mealId");
        if (addMealId != null) {
            int day = Integer.parseInt(request.getParameter("day"));
            DayPlan dayPlanToUpdate = mealPlan.getDayPlanContains().get(day);
            if (dayPlanToUpdate.getMealId() == null) {
                int dayPlanToUpdateId = dayPlanToUpdate.getId();
                Meal addMeal = mdao.getMealFromId(addMealId);

                DayMeal dayMeal = mealList.get(day);
                if (dayMeal == null) {
                    DayPlan dayPlan = new DayPlan();
                    dayPlan.setDayInWeek(day);
                    dayPlan.setMealId(addMealId);
                    dayMeal = new DayMeal(dayPlan, addMeal);
                    mealList.put(day, dayMeal);
                    mealPlan.getDayPlanContains().add(dayPlan); // Add new dayPlan to the mealPlan
                } else {
                    dayMeal.setMeal(addMeal);
                }
                mealPlan.getDayPlanContains().get(day).setMealId(addMealId);
                dpdao.updateMealIdOnDayPlan(addMealId, dayPlanToUpdateId);
            } else {
                request.setAttribute("ERROR", "Your Day Plan is already there! Can't add");
            }

        }
        //DELETE MEAL FROM DAYPLAN 
        String deleteDayPlanId = request.getParameter("dayPlanId");
        if (deleteDayPlanId != null) {
            int day = Integer.parseInt(request.getParameter("day"));
            DayPlan dayPlanToRemove = mealPlan.getDayPlanContains().get(day);
            dayPlanToRemove.setMealId(null);
            DayMeal dayMealRemove= mealList.get(day);
            dayMealRemove.setMeal(null);
            dayMealRemove.getDayPlan().setMealId(null);
            dpdao.removeMealIdOnDayPlan(dayPlanToRemove.getId());
        }

        session.setAttribute("searchList", searchList);
        request.setAttribute("mealList", mealList);
        request.setAttribute("mealPlan", mealPlan);
        request.setAttribute("searchholder", search);
        request.setAttribute("id", id);
        request.getRequestDispatcher("/AMainController?action=DayPlanInsertPage").forward(request, response);
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
