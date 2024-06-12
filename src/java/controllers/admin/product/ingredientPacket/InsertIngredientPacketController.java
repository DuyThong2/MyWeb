package controllers.admin.product.ingredientPacket;

import dao.product.IngredientDAO;
import dao.product.IngredientPacketDAO;
import dao.product.MealDAO;
import dto.product.Ingredient;
import dto.product.IngredientPacket;
import dto.product.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@WebServlet(name = "InsertIngredientPacketController", urlPatterns = {"/admin/packet/InsertIngredientPacketController"})
public class InsertIngredientPacketController extends HttpServlet {

    private final String SUCCESS_URL = "/AMainController?action=success";
    private final String ERROR_URL = "/AMainController?actione=error";
    private final String DESTINATION_URL = "/AMainController?action=PacketInsertPage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            String packetId = request.getParameter("packetId");
            System.out.println(packetId);
            HttpSession session = request.getSession();
            Meal meal = (Meal) session.getAttribute("mealInfo");

            if (meal == null && packetId == null) {
                response.sendRedirect("view/admin/Product/Meal/MManage.jsp");
                return;
            }
            
            if (meal == null){
                MealDAO mealDao = new MealDAO();
                meal = mealDao.getMealFromId("M"+packetId.substring(1));
                session.setAttribute("mealInfo", meal);
            }
            IngredientDAO ingredientDao = new IngredientDAO();
            Map<Ingredient, Integer> inserted = (Map<Ingredient, Integer>) session.getAttribute("inserted");
            if (inserted == null) {
                inserted = new HashMap<>();
                session.setAttribute("inserted", inserted);
            }

            if (request.getParameter("quantity") != null && request.getParameter("ingredientId") != null) {
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
                Ingredient ingredient = ingredientDao.getIngredientFromId(ingredientId);
                inserted.put(ingredient, quantity);
                session.setAttribute("inserted", inserted);
            }

            String ingredientName = request.getParameter("ingredientName");
            if (ingredientName != null) {
                List<Ingredient> listOfIngredient = ingredientDao.getIngredientsByName(ingredientName);
                request.setAttribute("ingredientList", listOfIngredient);
            }

            if ("true".equals(request.getParameter("finalize"))) {
                String packetID = String.format("P%s", meal.getId().substring(1));
                String name = String.format("Meal kit - %s", meal.getName());
                String packetDescription = String.format("%s ingredients kit", meal.getName());
                boolean isOnSale = false;

                IngredientPacket ingredientPacket = new IngredientPacket(packetID, name, packetDescription, isOnSale, 0,0,0, "active");
                for (Entry<Ingredient, Integer> entry : inserted.entrySet()) {
                    ingredientPacket.addIngredient(entry.getKey(), entry.getValue());
                }

                IngredientPacketDAO packetDao = new IngredientPacketDAO();
                packetDao.insertPacket(ingredientPacket);

                request.getRequestDispatcher(SUCCESS_URL).forward(request, response);
                return;
            } else if (request.getParameter("removeId") != null) {
                int removeID = Integer.parseInt(request.getParameter("removeId"));
                removeIngredient(inserted, removeID);
            }

            request.getRequestDispatcher(DESTINATION_URL).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher(ERROR_URL).forward(request, response);
        }
    }

    private void removeIngredient(Map<Ingredient, Integer> inserted, int id) {
        inserted.entrySet().removeIf(entry -> entry.getKey().getId() == id);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
