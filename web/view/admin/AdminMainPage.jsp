<!DOCTYPE html>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Page</title>
        <!-- Bootstrap CSS -->
        <%@include file="adminCssAdder.jsp" %>
        <style>
            body {
            }
            .dropdown-item button {
                margin-left: 10px;
            }
        </style>
    </head>

    <%
        String orderDetailURL = request.getContextPath() + "/AMainController?action=orderDetail";
        String userDetailURL = request.getContextPath() + "/AMainController?action=userDetail";
        String updateUserStatusURL = request.getContextPath() + "/AMainController?action=deleteUser";
        String updateOrderStatusURL = request.getContextPath() + "/AMainController?action=updateOrderStatus";

        if (request.getAttribute("orders") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/home");
            return;
        }


    %>


    <body>

        <%@include file="AdminHeader.jsp" %>
        <div class="container">
            <div class="row w-100">
                <h1 class="w-100 text-center">Welcome to the Admin Page </br>
                    Admin DashBoard
                </h1>
            </div>
            
                
           
            <!-- Other admin content goes here -->
            <div class="container mt-3">
                
                <div class="row">
                    <div class="col-md-4">
                        <div class="mb-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Total Users</h5>
                                    <p class="card-text">${totalUsers}</p>
                                </div>
                            </div>
                        </div>
                        <div class="mb-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Total Orders</h5>
                                    <p class="card-text">${totalOrders}</p>
                                </div>
                            </div>
                        </div>
                        <div class="mb-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Total Items Ordered</h5>
                                    <p class="card-text">${totalItemsOrdered}</p>
                                </div>
                            </div>
                        </div>
                        <div class="mb-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Today Warning People</h5>
                                    <p class="card-text">${todayWarningPeople}</p>
                                </div>
                            </div>
                        </div>

                        <h2 class="mb-4">Warning Users</h2>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Email</th>
                                    <th>Name</th>
                                    <th>Phone</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="user" items="${users}">
                                    <tr>
                                        <td>${user.email}</td>
                                        <td>${user.name}</td>
                                        <td>${user.phone}</td>
                                        <td>
                                            <a href="<%=userDetailURL%>&userId=${user.id}" class="btn btn-info btn-sm">Detail</a>
                                            <a href="<%=updateUserStatusURL%>&deleteUserId=${user.id}"class="btn btn-danger btn-sm">Disable</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="col-md-8">
                        <h2 class="mb-4">New Orders</h2>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Order Date</th>
                                    <th>Status</th>
                                    <th>Address</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="status" value='${["pending","processing","abort","completed"]}'/>
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td>${order.orderDate}</td>
                                        <td>${status[order.status-1]}</td>
                                        <td>${order.address}</td>
                                        <td>
                                            <a href="<%=orderDetailURL%>&orderId=${order.orderID}" class="btn btn-info btn-sm">Detail</a>
                                            <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=3" class="btn btn-warning btn-sm">Abort</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS and dependencies -->
        <%@include file="adminJs.jsp" %>





    </body>

</html>