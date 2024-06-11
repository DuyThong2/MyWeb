<%@page import="dto.account.User"%>
<%@page import="dao.account.UserDAO"%>
<%@ page import="java.util.Map.Entry"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="dao.product.MealDAO"%>
<%@ page import="dto.product.Meal"%>
<%@ page import="java.util.Map"%>
<%@ page import="dto.product.Product"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Fruitables - Vegetable Website Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap" rel="stylesheet">

        <!-- Icon Font Stylesheet -->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Customized Bootstrap Stylesheet -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="css/style.css" rel="stylesheet">
    </head>

    <%
        String processURL = request.getContextPath() + "/MainController?action=processCart";
        String redirectURL = request.getContextPath() + "/MainController?action=cartDisplay";

        List<Meal> list = (List<Meal>) session.getAttribute("mealList");
        if (list == null) {
            //only for testing
            UserDAO userDao = new UserDAO();
            User user = userDao.getUserById(1);
            session.setAttribute("user", user);
            MealDAO mealDAO = new MealDAO();
            list = mealDAO.getCustomerMealList();
            session.setAttribute("mealList", list);
            Map<Product, Integer> cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        Map<Product, Integer> cart = (Map<Product, Integer>) session.getAttribute("cart");
        User user = (User) session.getAttribute("user");
        double totalPrice = 0.0;
        List<Entry<Product, Integer>> shoppingList = new ArrayList<>(cart.entrySet());
        for (Entry<Product, Integer> item : shoppingList) {
            double itemPrice = item.getKey().isOnSale() ? item.getKey().getPriceAfterDiscount() : item.getKey().getPrice();
            totalPrice += item.getValue() * itemPrice;
        }


    %>
    <body>
        <div class="container-fluid py-5">
            <div class="container py-5">

                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th scope="col">Products</th>
                                <th scope="col">Name</th>
                                <th scope="col">Price</th>
                                <th scope="col">Quantity</th>
                                <th scope="col">Total</th>
                                <th scope="col">Handle</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- loop -->
                            <% for (Entry<Product, Integer> item : shoppingList) {
                                    double itemPrice = item.getKey().isOnSale() ? item.getKey().getPriceAfterDiscount() : item.getKey().getPrice();
                            %>
                            <tr>
                                <th scope="row">
                                    <div class="d-flex align-items-center">
                                        <img src="${pageContext.request.contextPath}/<%= item.getKey().getImageURL()%>" class="img-fluid me-5 rounded-circle" style="width: 80px; height: 80px;" alt="">
                                    </div>
                                </th>
                                <td>
                                    <p class="mb-0 mt-4"><%= item.getKey().getName()%></p>
                                </td>
                                <td>
                                    <p class="mb-0 mt-4"><%= String.format("%.2f", itemPrice)%>$</p>
                                </td>
                                <!-- edit quantity option -->
                                <td>
                                    <div class="d-flex justify-content-center align-items-center">
                                        <!-- Decrement Button Form -->
                                        <form action="<%= redirectURL%>" method="POST" class="form-inline">
                                            <input type="hidden" name="productId" value="<%= item.getKey().getId()%>">
                                            <input type="hidden" name="quantity" value="<%= item.getValue() - 1%>">
                                            <button type="submit" class="btn btn-sm btn-minus rounded-circle bg-light border">
                                                <i class="fa fa-minus"></i>
                                            </button>
                                        </form>
                                        <!-- Quantity Input Form -->
                                        <form action="<%= redirectURL%>" method="POST" class="form-inline mx-2">
                                            <input type="hidden" name="productId" value="<%= item.getKey().getId()%>">
                                            <input type="number" name="quantity" id="quantity<%= item.getKey().getId()%>" class="form-control form-control-sm text-center border-0" value="<%= item.getValue()%>" min="1" onchange="this.form.submit()">
                                        </form>
                                        <!-- Increment Button Form -->
                                        <form action="<%= redirectURL%>" method="POST" class="form-inline">
                                            <input type="hidden" name="productId" value="<%= item.getKey().getId()%>">
                                            <input type="hidden" name="quantity" value="<%= item.getValue() + 1%>">
                                            <button type="submit" class="btn btn-sm btn-plus rounded-circle bg-light border">
                                                <i class="fa fa-plus"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                                <td>
                                    <p class="mb-0"><%= String.format("%.2f", item.getValue() * itemPrice)%>$</p>
                                </td>
                                <td>
                                    <a href="<%= redirectURL%>&deleteId=<%= item.getKey().getId()%>" class="btn btn-md rounded-circle bg-light border">
                                        <i class="fa fa-times text-danger"></i>
                                    </a>
                                </td>
                            </tr>
                            <% }%>
                        </tbody>
                    </table>
                </div>
                <%if (cart.size() > 0) {%>
                <div class="row g-4 justify-content-end">
                    <div class="col-8"></div>
                    <div class="col-sm-8 col-md-7 col-lg-6 col-xl-4">
                        <div class="bg-light rounded">
                            <div class="p-4">
                                <h1 class="display-6 mb-4">Cart <span class="fw-normal">Total</span></h1>
                                <div class="d-flex justify-content-between mb-4">
                                    <h5 class="mb-0 me-4">Subtotal:</h5>
                                    <p class="mb-0">$<%= String.format("%.2f", totalPrice)%></p>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <h5 class="mb-0 me-4">Shipping</h5>
                                    <div class="">
                                        <p class="mb-0">Flat rate: $3.00</p>
                                    </div>
                                </div>
                                <p class="mb-0 text-end">Shipping <%=user.getAddress()%></p>
                            </div>
                            <div class="py-4 mb-4 border-top border-bottom d-flex justify-content-between">
                                <h5 class="mb-0 ps-4 me-4">Total</h5>
                                <p class="mb-0 pe-4">$<%= String.format("%.2f", totalPrice + 3)%></p>
                            </div>
                                <a href="<%=processURL%>" class="btn border-secondary rounded-pill px-4 py-3 text-primary text-uppercase mb-4 ms-4" type="submit">Proceed Checkout</a>
                        </div>
                    </div>
                </div>
                <% }%>

            </div>
        </div>

    </body>
</html>
