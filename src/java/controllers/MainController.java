/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
public class MainController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     */
    private final String SUCCESS = "view/success.jsp";
    private final String ERROR = "view/error.jsp";

    private final String LOGIN = "index.jsp";

    private final String SHOP = "view/user/shop.jsp";
    private final String SHOP_CONTROLLER = "/user/MealController";

    private final String PLAN = "view/user/plan.jsp";
    private final String PLAN_CONTROLLER = "/user/MealPlanController";

    private final String CUSTOMER_PLAN_PAGE = "view/user/customerPlan/CustomerPlanDetail.jsp";
    private final String CUSTOMER_PLAN_CONTROLLER = "/user/CustomerPlanDetailController";
    
    
    private final String CUSTOMER_PLAN_UPDATE_PAGE ="view/user/customerPlan/CustomerPlanUpdate.jsp";
    
    private final String CUSTOMER_PLAN_UPDATE_CONTROLLER="/user/CustomerPlanUpdateController";
    private final String ADD_TO_CUSTOMER_PLAN="/user/AddToCustomerPlan";
    private final String ADD_TO_CUSTOMER_DAY_PLAN="/user/AddToCustomerDayPlan";
    private final String DELETE_CUSTOMER_DAY_PLAN="/user/DeleteCustomerDayPlan";
    private final String MAIN_PAGE = "view/user/mainPage.jsp";
    private final String MAIN_PAGE_CONTROLLER = "";

    private final String CART_DISPLAY = "view/user/cart/CartDisplay.jsp";
    private final String CART_DISPLAY_CONTROLLER = "/user/cart/ShowCart";

    private final String ADD_TO_CART = "";
    private final String ADD_TO_CART_CONTROLLER = "/user/cart/AddToCart";
    
    
    
    private final String MEAL_DETAIL = "view/user/product/MealDetail.jsp";
    private final String MEAL_PLAN_DETAIL = "view/user/customerPlan/MealPlanDetailC.jsp";

    private final String PROCESS_CART = "";
    private final String PROCESS_CART_CONTROLLER = "/user/cart/OrderCart";
    private final String ADD_ALL_TO_CART = "/user/cart/AddAllToCart";

    private final String USER_DETAIL = "view/user/account/UserUserDetail.jsp";
    private final String USER_DETAIL_CONTROLLER = "/user/account/UserDetailController";

    private final String USER_UPDATE = "view/user/account/UserUpdate.jsp";
    private final String USER_UPDATE_CONTROLLER = "/user/account/UserUpdateController";

    private final String ORDER_UPDATE = "";
    private final String ORDER_UPDATE_CONTROLLER = "/user/order/updateOrderStatusController";

    private final String ORDER_DETAIL = "view/user/order/UserOrderDetail.jsp";
    private final String ORDER_DETAIL_CONTROLLER = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        System.out.println(action);
        String url = "";
        if (action == null){
            action ="blah";
        }
        action = action.trim();

        switch (action) {
            //error manage
            case "success":
                url = SUCCESS;
                break;
            case "error":
                url = ERROR;
                break;

            //mainPage and shop
            case "mainPagePage":
                url = MAIN_PAGE;
                break;

            case "shop":
                url = SHOP_CONTROLLER;
                break;
            case "shopPage":
                url = SHOP;
                break;

            //plan 
            case "plan":
                url = PLAN_CONTROLLER;
                break;
            case "planPage":
                url = PLAN;
                break;
            case "addAllToCart":
                url = ADD_ALL_TO_CART;
                break;
            //customerplan/
            case "customerPlanPage":
                url = CUSTOMER_PLAN_PAGE;
                break;
            case "customerPlan":
                url = CUSTOMER_PLAN_CONTROLLER;
                break;
            case "customerPlanUpdatePage":
                url = CUSTOMER_PLAN_UPDATE_PAGE;
                break;
            case "deleteCustomerDayPlan":
                url=DELETE_CUSTOMER_DAY_PLAN;
                break;
            case "customerPlanUpdate":
                url=CUSTOMER_PLAN_UPDATE_CONTROLLER;
                break;
            case "addToCustomerPlan":
                url= ADD_TO_CUSTOMER_PLAN;
                break;
            case "AddToCustomerDayPlan":
                url= ADD_TO_CUSTOMER_DAY_PLAN;
                break;
            //cart manage
            case "addToCart":
                url = ADD_TO_CART_CONTROLLER;
                break;
            case "addToCartPage":
                break;

            case "cartDisplayPage":
                url = CART_DISPLAY;
                break;
            case "cartDisplay":
                url = CART_DISPLAY_CONTROLLER;
                break;

            case "processCart":
                url = PROCESS_CART_CONTROLLER;
                break;//order item
            //shopping detail
            case "mealDetailPage":
                url = MEAL_DETAIL;
                break;
            case "mealPlanDetailPage":
                url = MEAL_PLAN_DETAIL;
                break;

            //user 
            case "userDetailPage":
                url = USER_DETAIL;
                break;
            case "userDetail":
                url = USER_DETAIL_CONTROLLER;
                break;

            case "userUpdatePage":
                url = USER_UPDATE;
                break;
            case "userUpdate":
                url = USER_UPDATE_CONTROLLER;
                break;

            //order
            case "orderUpdate":
                url = ORDER_UPDATE_CONTROLLER;
                break;
            case "orderDetailPage":
                url = ORDER_DETAIL;
                break;

            case "login":
                url = LOGIN;
                break;

            default:
                url = "index.html";
                break;
        }

        request.getRequestDispatcher(url).forward(request, response);

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
