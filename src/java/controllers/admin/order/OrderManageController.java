package controllers.admin.order;




import dao.account.UserDAO;
import dao.order.OrderDAO;
import dao.order.OrderItemDAO;
import dto.account.User;
import dto.order.Order;
import dto.order.OrderItem;
import dto.product.Meal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/order/orderManage")
public class OrderManageController extends HttpServlet {

    private final String MANAGE_URL = "/AMainController?action=orderManagePage";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = request.getParameter("status");
            String category = request.getParameter("category");
            HttpSession session = request.getSession();
            OrderDAO orderDAO = new OrderDAO();

            // Fetch orders based on the status
            List<Order> orders = null;
            System.out.println(status);
            if (status != null) {
                orders = orderDAO.getAllOrdersByStatus(status);
                orders.forEach(System.out::println);
            }

            //get current page:
            String numPageStr = request.getParameter("numPage");
            int numPage = 1;
            numPage = numPageStr!= null ? Integer.parseInt(numPageStr):
                    session.getAttribute("numPage")!= null ? 
                    (int) session.getAttribute("numPage") : 1;

            // Set attributes and forward to JSP sort the list if needed
            request.setAttribute("orders", orders);

            List<Order> list = (List<Order>) request.getAttribute("orders");
            List<Order> copyList = sortListFromRequest(request, list);
            if (copyList != null) {
                request.setAttribute("orders", copyList);
            }
            session.setAttribute("numPage", numPage);
            request.getRequestDispatcher(MANAGE_URL).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Order> sortListFromRequest(HttpServletRequest request, List<Order> list) {

        if (request != null) {
            String sortOrder = request.getParameter("sort");
            String sortBy = request.getParameter("category");
            OrderItemDAO dao = new OrderItemDAO();
            if (sortOrder != null && sortBy != null) {
                Comparator<Order> comparator;
                switch (sortBy) {
                    case "category1":
                        comparator = Comparator.comparing(dao::sumTotalPriceByOrderId);
                        break;
                    case "category2":
                        comparator = Comparator.comparing(dao::sumQuantitiesByOrderId);
                        break;
                    default:
                        comparator = Comparator.comparing(Order::getOrderDate);
                        break;
                }
                if (sortOrder.matches("max")) {
                    comparator = comparator.reversed();
                }
                List<Order> copyList = new ArrayList<>(list);
                Collections.sort(copyList, comparator);
                return copyList;
            }
        }
        return null;

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get status filter parameter
        processRequest(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
