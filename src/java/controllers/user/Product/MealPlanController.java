package controllers.user.Product;

import dao.plan.MealPlanDAO;
import dto.plan.MealPlan;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/user/MealPlanController")
public class MealPlanController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        MealPlanDAO mpdao = new MealPlanDAO();
        String pageNumberStr = request.getParameter("pageNumber");
        int currentNumPage = 1;
        if (pageNumberStr != null) {
            currentNumPage = Integer.parseInt(pageNumberStr);
        }

        List<MealPlan> mealPlanList = (List<MealPlan>) session.getAttribute("mpList");
        List<MealPlan> filterList = (List<MealPlan>) session.getAttribute("filterList");

        if (mealPlanList == null) {
            mealPlanList = mpdao.getCustomerAllMealPlans();
            session.setAttribute("mpList", mealPlanList);
        }

        // Handle search
        String search = request.getParameter("txtsearch");
        if (search != null) {
            session.setAttribute("localSearch", search);
            mealPlanList = searchNameList(search.trim(), mpdao);
            session.setAttribute("mpList", mealPlanList);
            filterList =null;
            session.removeAttribute("filterList");
        } else {
            search = (String) session.getAttribute("localSearch");
            if (search != null && !search.isEmpty()) {
                mealPlanList = searchNameList(search, mpdao);
            } else {
                mealPlanList = mpdao.getCustomerAllMealPlans();
            }
            session.setAttribute("mpList", mealPlanList);
        }

        // Handle type filter
        String type = request.getParameter("type");
        if (type != null) {
            filterList = getListType(mealPlanList, type.trim());
            currentNumPage=1;
        }

        session.setAttribute("pageNumber", currentNumPage);
        session.setAttribute("mpList", mealPlanList);
        session.setAttribute("filterList", filterList);
        request.getRequestDispatcher("/MainController?action=planPage").forward(request, response);
    }

    private List<MealPlan> searchNameList(String search, MealPlanDAO mpdao) {
        List<MealPlan> returnList = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            returnList = mpdao.getCustomerAllMeanPlanByName(search);
        } else {
            returnList = mpdao.getCustomerAllMealPlans();
        }
        return returnList;
    }

    private List<MealPlan> getListType(List<MealPlan> mealPlanList, String type) {
        return (List<MealPlan>) mealPlanList.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        MealPlanDAO mpdao = new MealPlanDAO();
        List<MealPlan> mealPlanList = mpdao.getCustomerAllMealPlans();
        session.setAttribute("mpList", mealPlanList);
        session.setAttribute("pageNumber", 1);
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
