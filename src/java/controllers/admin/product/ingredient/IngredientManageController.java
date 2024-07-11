    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.product.ingredient;

import Utility.Tool;
import dao.product.IngredientDAO;
import dto.product.Ingredient;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns = {"/admin/ingredient/manage"})
public class IngredientManageController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String URL_PRODUCT_MANAGE = "/AMainController?action=ingredientManagePage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        IngredientDAO iDao = new IngredientDAO();
        HttpSession session = request.getSession();
        
        
        
        // Get the numPage parameter from the request
        String numPageStr = (String) request.getParameter("numPage");
        int numPage = 1;
        if (numPageStr != null) {
            numPage = Integer.parseInt(numPageStr);
        }

        
        //create table if null
        if (session.getAttribute("ingredientList") == null) {
            List<Ingredient> list = iDao.getAllIngredients();
            session.setAttribute("ingredientList", list);
            session.setAttribute("numPage", 1);
        } else {
            
            List<Ingredient> list = (List<Ingredient> ) session.getAttribute("ingredientList");
            List<Ingredient> copyList = searchingIngredient(request, iDao);
            //search name
            if(copyList != null){
                 session.setAttribute("ingredientList", copyList);
            }
            
            //check if sort
            copyList = sortListFromRequest(request, list);
            if(copyList != null){
                 session.setAttribute("ingredientList", copyList);
            }
            
            //save last page access
            session.setAttribute("numPage", numPage);
        }
        
        

        request.getRequestDispatcher(URL_PRODUCT_MANAGE).forward(request, response);
    }
    
    private List<Ingredient> sortListFromRequest(HttpServletRequest request,List<Ingredient> list){
        
        if (request != null){
            String sortOrder = (String) request.getParameter("sort");
            String sortBy = (String) request.getParameter("cate");
            if (sortOrder != null && sortBy!= null){
                Comparator<Ingredient> comparator = null;
                switch(sortBy){
                    case "price": comparator = Comparator.comparing(Ingredient::getPrice);break;
                    case "unit": comparator = Comparator.comparing(Ingredient::getUnit);break;
                    default: comparator = Comparator.comparing(Ingredient::getId); break;
                }
                if (sortOrder.matches("max")){
                    comparator = comparator.reversed();
                }
                List<Ingredient> copyList = new ArrayList<>(list);
                Collections.sort(copyList,comparator);
                return copyList;
            }
        }
        return null;
        
    }
    
     private List<Ingredient> searchingIngredient(HttpServletRequest request, IngredientDAO dao){
         if (request != null){
            String seachName = (String) request.getParameter("searching");
            
            if (seachName != null){
                List<Ingredient> copyList = dao.getIngredientsByName(seachName);
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
        response.setContentType("text/html;charset=UTF-8");
        IngredientDAO iDao = new IngredientDAO();
        HttpSession session = request.getSession();
        List<Ingredient> list = iDao.getAllIngredients();
        session.setAttribute("ingredientList", list);
        session.setAttribute("numPage", 1);
        request.getRequestDispatcher(URL_PRODUCT_MANAGE).forward(request, response);
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
