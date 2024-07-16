/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.user.Product;

import dao.discount.DiscountDAO;
import dao.product.MealDAO;
import dto.product.Meal;
import dto.product.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
@WebServlet(name = "MealController", urlPatterns = {"/user/MealController"})
public class MealController extends HttpServlet {

    private final String SHOP_PAGE = "/MainController?action=shopPage";

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
        MealDAO dao = new MealDAO();
        HttpSession session = request.getSession();

        // Get the numPage parameter from the request
        String numPageStr = request.getParameter("numPage");
        int numPage = 1;
        if (numPageStr != null) {
            numPage = Integer.parseInt(numPageStr);
        }

        //create table if null
        if (session.getAttribute("mealList") == null) {
            List<Meal> list = dao.getCustomerMealList();
            session.setAttribute("mealList", list);
            //only for testing:

        }

        List<Meal> list = (List<Meal>) session.getAttribute("mealList");
        List<Meal> copyList = searchingMeal(request, dao);
        //search name
        if (copyList != null) {
            session.setAttribute("mealList", copyList);
        }

        //check if sort
        copyList = sortListFromRequest(request, list);
        if (copyList != null) {
            session.setAttribute("mealList", copyList);
        }
        //save last page access
        session.setAttribute("numPage", numPage);

        request.getRequestDispatcher(SHOP_PAGE).forward(request, response);
    }

    private List<Meal> sortListFromRequest(HttpServletRequest request, List<Meal> list) {

        if (request != null) {
            String sortOrder = request.getParameter("sort");
            String sortBy = request.getParameter("cate");
            if (sortOrder != null && sortBy != null) {
                Comparator<Meal> comparator = null;
                switch (sortBy) {
                    case "category":
                        comparator = Comparator.comparing(Meal::getCategory);
                        break;
                    case "price":
                        comparator = Comparator.comparing(Meal::getPriceAfterDiscount);
                        break;
                    case "isOnSale":
                        comparator = Comparator.comparing(Meal::getDiscountPercent);
                        break;
                    default:
                        comparator = Comparator.comparing(Meal::getId);
                        break;
                }
                if (sortOrder.matches("max")) {
                    comparator = comparator.reversed();
                }
                List<Meal> copyList = new ArrayList<>(list);
                Collections.sort(copyList, comparator);
                return copyList;
            }
        }
        return null;

    }

    private List<Meal> searchingMeal(HttpServletRequest request, MealDAO dao) {
        if (request != null) {
            String seachName = request.getParameter("searching");

            if (seachName != null) {
                List<Meal> copyList = dao.getMealsByNameForCustomer(seachName);
                return copyList;
            }
        }
        return null;

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
        MealDAO dao = new MealDAO();
        HttpSession session = request.getSession();
        List<Meal> list = dao.getCustomerMealList();
        session.setAttribute("mealList", list);
        session.setAttribute("numPage", 1);
        request.getRequestDispatcher(SHOP_PAGE).forward(request, response);
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
