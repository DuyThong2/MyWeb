/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.account;

import dao.account.UserDAO;
import dao.product.MealDAO;
import dto.account.User;
import dto.product.Meal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
@WebServlet(urlPatterns = {"/admin/account/UserManageController"})
public class UserManageController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String URL_PRODUCT_MANAGE = "/AMainController?action=userManagePage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        UserDAO dao = new UserDAO();
        HttpSession session = request.getSession();

        // Get the numPage parameter from the request
        String numPageStr = request.getParameter("numPage");

        int numPage = numPageStr != null ? Integer.parseInt(numPageStr)
                : session.getAttribute("numPage") != null
                ? (int) session.getAttribute("numPage") : 1;

        //create table if null
        List<User> list = dao.getAllUser();
        request.setAttribute("userList", list);

        List<User> copyList = searchingUsers(request, dao);
        //search name
        if (copyList != null) {
            request.setAttribute("userList", copyList);
        }

        //save last page access
        session.setAttribute("numPage", numPage);

        request.getRequestDispatcher(URL_PRODUCT_MANAGE).forward(request, response);
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
                        comparator = Comparator.comparing(Meal::getPrice);
                        break;
                    case "isOnSale":
                        comparator = Comparator.comparing(Meal::isOnSale);
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

    private List<User> searchingUsers(HttpServletRequest request, UserDAO dao) {
        if (request != null) {
            String seachValue = request.getParameter("searchValue");
            String searchCategory = request.getParameter("searchCriteria");
            if (seachValue != null && searchCategory != null) {
                List<User> copyList = dao.getUsersByCategory(seachValue, searchCategory);
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
