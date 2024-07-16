<%@page import="dao.order.OrderDAO"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="dto.product.Product"%>
<%@page import="dao.product.MealDAO"%>
<%@page import="dto.product.Meal"%>
<%@page import="dao.order.OrderItemDAO"%>
<%@page import="Utility.Tool"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dto.order.Order"%>
<%@page import="java.util.List"%>
<%@page import="dto.account.User"%>
<%@page import="dao.account.UserDAO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


        <%@include file="../../../cssAdder.jsp" %>
        <style>
            .user-info, .user-options {
                height: auto;
                margin-bottom: 20px;
            }

            .fixed-pagination {
                position: sticky;
                bottom: 0;
                background: #f8f9fa;
                padding: 10px 0;
                z-index: 1;
            }
            .card-header {
                background-color: #007bff;
                color: white;
            }
            .card-body p{
                font-size:1.3rem;
            }
            .table th, .table td {
                vertical-align: middle;
            }
            img{
                width:100%;
                height:100%;
                object-fit:cover;
            }

        </style>
        <title>User Detail</title>
    </head>
    <%
        String redirectURL = request.getContextPath() + "/MainController?action=userDetail";
        String updateStatusURL = request.getContextPath() + "/MainController?action=userUpdatePage";
        String orderDetailURL = request.getContextPath() + "/MainController?action=orderDetailPage";
        String updateOrderStatusURL = request.getContextPath() + "/MainController?action=orderUpdate";
        String loginURL = request.getContextPath() + "/MainController?action=login";
        User user = (User) session.getAttribute("LoginedUser");

        if (user == null) {
            response.sendRedirect(loginURL);
            return;

        }

        session.setAttribute("user", user);

        Map<Integer, Order> orderList = (Map<Integer, Order>) session.getAttribute("orderList");

    %>



    <body>
        <%@include file="../../user/header.jsp" %>
        <div class="container  p-4" style="min-width:100vw; background-color:rgb(235,235,235);">
            <div class="container p-4 bg-white" style="min-width:95vw">
                <!-- User Info and Options -->
                <div class="row mt-4 d-flex justify-content-center " >
                    <div class="col-md-12">
                        <div class="card user-info"> 
                            <div class="card-header bg-black col-12">
                                <h3>User Information</h3>
                            </div> 
                            <div class="row"> 
                                <div class="col-4">
                                    <img src="${pageContext.request.contextPath}/${user.getImgURL()}" alt="User Image" class="">
                                </div>
                                <div class="col-8 px-0">

                                    <div class="card-body px-0">
                                        <p><strong>Name:</strong> ${user.getName()}</p>
                                        <p><strong>Email:</strong> ${user.getEmail()}</p>
                                        <p><strong>Phone:</strong> ${user.getPhone()}</p>
                                        <p><strong>Address:</strong> ${user.getAddress()}</p>
                                        <p><strong>Status:</strong> ${user.getStatus()}</p>
                                        <a href="<%=updateStatusURL%>" class="btn btn-success btn-lg mt-2">Edit User</a>
                                </div>
                            </div>

                        </div>
                    </div>

                </div>

                <!-- User Orders -->
                <div class="row mt-4">
                    <div class="col-lg-12">
                        <h2>Orders</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover">
                                <thead class="thead-light">
                                    <tr>
                                        <th>Order status</th>
                                        <th>Order Date</th>
                                        <th>Checking Date</th>
                                        <th>Abort Date</th>

                                        <th>Options</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        List<Order> orders = new ArrayList<>(orderList.values());
                                        List<List<Order>> pages = new ArrayList();
                                        pages = Tool.splitToPage(orders, 8);
                                        int pageNum = 1;
                                        Object numString = session.getAttribute("numPage");
                                        if (numString != null) {
                                            pageNum = (int) numString;
                                            if (pageNum < 1 || pageNum > pages.size()) {
                                                pageNum = 1;
                                            }
                                        }

                                        if (orders != null && !orders.isEmpty()) {
                                            int realPage = pageNum - 1;
                                            List<Order> elementInPage = pages.get(realPage);
                                            String[] statusString = {"pending", "processing", "aborted", "completed"};
                                            for (Order order : elementInPage) {
                                    %>
                                    <tr>
                                        <td><%= statusString[order.getStatus() - 1]%></td>
                                        <td><%= Tool.parseTime(order.getOrderDate())%></td>
                                        <td><%= Tool.parseTime(order.getCheckingDate())%></td>
                                        <td><%= Tool.parseTime(order.getAbortDate())%></td>

                                        <td>
                                            <a href="<%=orderDetailURL%>&orderId=<%= order.getOrderID()%>" class="btn btn-primary btn-sm">Detail</a>
                                            <c:choose>

                                                <c:when test="<%=order.getStatus() == 3%>">
                                                    <button class="btn btn-secondary btn-sm">Aborted</button>
                                                </c:when>
                                                <c:when test="<%=order.getStatus() == 4%>">
                                                    <button class="btn btn-success btn-sm">Delivered</button>
                                                </c:when>
                                                <c:when test="<%=order.getStatus() != 3%>">
                                                    <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&status=3" class="btn btn-danger btn-sm">Abort</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <button class="btn btn-detail btn-sm"><%= statusString[order.getStatus() - 1]%></button>
                                                </c:otherwise>

                                            </c:choose>

                                        </td>
                                    </tr>
                                    <%
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>

                        <!-- Pagination -->
                        <div class="w-100 d-flex align-items-center justify-content-center">
                            <form action="<%= redirectURL%>&userId=<%=user.getId()%>" method="POST" class="form-inline">
                                <input type="hidden" name="numPage" value="<%= pageNum - 1%>">
                                <button type="submit" class="btn btn-secondary mr-2">&lt;</button>
                            </form>

                            <form action="<%= redirectURL%>&userId=<%=user.getId()%>" method="POST" class="form-inline">
                                <input name="numPage" type="number" value="<%= pageNum%>" class="form-control page-number"  min="1" max="<%= pages.size()%>">

                            </form>

                            <span class="ml-2 mr-2">/</span>
                            <span class="total-pages"><%= pages.size()%></span>

                            <form action="<%= redirectURL%>&userId=<%=user.getId()%>" method="POST" class="form-inline">
                                <input type="hidden" name="numPage" value="<%= pageNum + 1%>">
                                <button type="submit" class="btn btn-secondary ml-2">&gt;</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="../mainFooter.jsp" %>

        <%@include file="../../../jsAdder.jsp" %>

    </body>
</html>
