package controllers.admin.order;

import dao.account.UserDAO;
import dao.order.OrderDAO;
import dao.order.OrderItemDAO;
import dao.product.MealDAO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/order/orderManage")
public class OrderManageController extends HttpServlet {

    private final String MANAGE_URL = "/AMainController?action=orderManagePage";
    private final String UPDATE_ORDER_STATUS = "/AMainController?action=updateOrderStatus";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = request.getParameter("status");
            HttpSession session = request.getSession();
            OrderDAO orderDAO = new OrderDAO();

            // Fetch orders based on the status
            Map<Integer, Order> ordersMap = (Map<Integer, Order>) session.getAttribute("map");
            List <Order> orders = (List<Order>) session.getAttribute("orders");
            
            if (status != null) {
                ordersMap = orderDAO.getAllOrdersByStatus(Integer.parseInt(status));
                session.setAttribute("map", ordersMap);
                orders = new ArrayList<>(ordersMap.values());
                session.setAttribute("orders", orders);
            }

            //change status if needed
            String orderStatus = request.getParameter("OrderStatus");
            if (orderStatus != null) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                
                Order order = ordersMap.get(orderId);
                order.setStatus(Integer.parseInt(orderStatus));
                request.getRequestDispatcher(UPDATE_ORDER_STATUS).forward(request, response);
            } else {
                
                //changing page
                String numPageStr = request.getParameter("numPage");
                int numPage = 1;
                numPage = numPageStr != null ? Integer.parseInt(numPageStr)
                        : session.getAttribute("numPage") != null
                        ? (int) session.getAttribute("numPage") : 1;
                System.out.println(numPage);

                //sort or search
                List<Order> copyList = searchingForOrder(request);
                orders = copyList!= null ? copyList:orders;
                
                sortListFromRequest(request, orders);
                
                // Set attributes and forward to JSP sort the list if needed
                session.setAttribute("orders", orders);
                session.setAttribute("numPage", numPage);
                request.getRequestDispatcher(MANAGE_URL).forward(request, response);
            }

            //get current page:
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortListFromRequest(HttpServletRequest request, List<Order> list) {

        if (request != null) {
            String sortOrder = request.getParameter("sort");
            String sortBy = request.getParameter("category");
            OrderItemDAO dao = new OrderItemDAO();
            if (sortOrder != null && sortBy != null) {
                Comparator<Order> comparator;
                switch (sortBy) {
                    case "category1":
                        comparator = Comparator.comparing(Order::getTotalPrice);
                        break;
                    case "category2":
                        comparator = Comparator.comparing(Order::getTotalItem);
                        break;
                    default:
                        comparator = Comparator.comparing((Order order) -> order.getAddress().getCity()).
                                thenComparing((Order order) -> order.getAddress().getDistrict())
                                .thenComparing((Order order) -> order.getAddress().getWard());
                        ;
                        break;
                }
                if (sortOrder.matches("max")) {
                    comparator = comparator.reversed();
                }
                Collections.sort(list, comparator);
                
            }
        }
    }
    
    private List<Order> searchingForOrder(HttpServletRequest request){
        
        
        if (request != null) {
            String seachValue = request.getParameter("searchValue");
            String searchCategory = request.getParameter("searchCriteria");
            if (seachValue != null && searchCategory != null) {
                UserDAO userDao = new UserDAO();
                List<User> copyList = userDao.getUsersByCategory(seachValue, searchCategory);
                OrderDAO orderDAO = new OrderDAO();
                List<Order> orderList = new LinkedList<>();
                copyList.stream()
                        .map(user -> user.getId())
                        .forEach(id -> {
                            Map<Integer,Order> map = orderDAO.getOrdersByCustomerId(id);
                            orderList.addAll(map.values());
                            });
                return orderList;
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
