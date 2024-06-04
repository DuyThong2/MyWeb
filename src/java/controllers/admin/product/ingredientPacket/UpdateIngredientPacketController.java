/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin.product.ingredientPacket;

import dao.product.IngredientDAO;
import dao.product.IngredientPacketDAO;
import dto.product.Ingredient;
import dto.product.IngredientPacket;
import dto.product.Meal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@WebServlet(name = "UpdateIngredientPacketController", urlPatterns = {"/admin/packet/UpdateIngredientPacketController"})
public class UpdateIngredientPacketController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String SUCCESS_URL = "/AMainController?action=success";
    private final String ERROR_URL = "/AMainController?action=error";
    private final String DESTINATION_URL = "/AMainController?action=PacketUpdatePage";
     private final String MANAGE_URL = "/AMainController?action=MealManagePage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String packetId = request.getParameter("packetId");
            System.out.println(packetId);
            
             
            if (packetId== null){
                request.getRequestDispatcher(MANAGE_URL).forward(request, response);
                return;
            }else{
                request.setAttribute("packetId", packetId);
            }

            HttpSession session = request.getSession();
            IngredientPacketDAO packetDao = new IngredientPacketDAO();
            IngredientDAO ingredientDao = new IngredientDAO();
            Map<Ingredient, Integer> inserted = (Map<Ingredient, Integer>) session.getAttribute("inserted");
            if (inserted == null) {
                inserted = packetDao.getAllIngredientsFromMealId(packetId);
                session.setAttribute("inserted", inserted);
            }

            if (request.getParameter("quantity") != null && request.getParameter("ingredientId") != null) {
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
                Ingredient ingredient = ingredientDao.getIngredientFromId(ingredientId);
                inserted.merge(ingredient, quantity,Integer::sum);
                session.setAttribute("inserted", inserted);
            }

            String ingredientName = request.getParameter("ingredientName");
            if (ingredientName != null) {
                List<Ingredient> listOfIngredient = ingredientDao.getIngredientsByName(ingredientName);
                request.setAttribute("ingredientList", listOfIngredient);
            }

            if ("true".equals(request.getParameter("finalize"))) {
                packetDao.updatePacketInfoForMeal(packetId, inserted);
                request.getRequestDispatcher(SUCCESS_URL).forward(request, response);
                return;
            } else if (request.getParameter("removeId") != null) {
                int removeID = Integer.parseInt(request.getParameter("removeId"));
                removeIngredient(inserted, removeID);
            }

            request.getRequestDispatcher(DESTINATION_URL).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            
            response.sendRedirect(ERROR_URL);
            return;
        }
    }

    private void removeIngredient(Map<Ingredient, Integer> inserted, int id) {
        inserted.entrySet().removeIf(entry -> entry.getKey().getId() == id);
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
