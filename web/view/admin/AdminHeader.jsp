<%-- 
    Document   : AdminHeader
    Created on : Jun 27, 2024, 10:12:36 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String homeURL = request.getContextPath() + "/AMainController?action=adminmainpage";
    String OrderManageURL = request.getContextPath() + "/AMainController?action=orderManagePage";
    String UserManageURL = request.getContextPath() + "/AMainController?action=userManage";
    String IngredientURL = request.getContextPath() + "/AMainController?action=ingredientManage";
    String MealManageURL = request.getContextPath() + "/AMainController?action=MealManag";
    String planURL = request.getContextPath() + "/AMainController?action=MealPlan";
    String saleURL = request.getContextPath() + "";

%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="<%=homeURL%>">Admin</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="<%=OrderManageURL%>">Orders</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=UserManageURL%>">Users</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="mealDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Meal
                </a>
                <div class="dropdown-menu" aria-labelledby="mealDropdown">
                    <a class="dropdown-item" href="<%=IngredientURL%>">Ingredients</a>
                    <a class="dropdown-item" href="<%=MealManageURL%>">Meal</a>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<%=planURL%>">Plan</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Sale</a>
            </li>
        </ul>
        <ul class="navbar-nav">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Notifications
                </a>
                <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown" id="notificationDropdown">
                    <a class="dropdown-item" href="#">No new notifications</a>
                </div>
            </li>
        </ul>
    </div>
</nav>
<div style="height:170px; width:100vw"></div>
