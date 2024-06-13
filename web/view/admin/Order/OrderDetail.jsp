<%@page import="dto.product.Meal"%>
<%@page import="dto.account.User"%>
<%@page import="dto.order.OrderItem"%>
<%@page import="java.util.List"%>
<%@page import="dto.order.Order"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Order Details</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<%
    String updateOrderStatusURL = request.getContextPath() + "/AMainController?action=updateOrderStatus";
    String redirectURL = request.getContextPath() + "/AMainController?action=orderDetail";
    Order order = (Order) request.getAttribute("order");
    if (order == null) {
        response.sendRedirect(redirectURL);
        return;
    }

    List<OrderItem> orderItems = (List<OrderItem>) request.getAttribute("orderItems");
    User customer = (User) request.getAttribute("user");
%>

<div class="container-fluid">
    <!-- First Row: Order Information -->
    <div class="row mt-4">
        <div class="col-lg-8">
            <h2>Order Information</h2>
            <table class="table table-bordered">
                <thead class="thead-light">
                <tr>
                    <th>Order ID</th>
                    <th>Order Date</th>
                    <th>Checking Date</th>
                    <th>Abort Date</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>${order.orderID}</td>
                    <td>${order.orderDate}</td>
                    <td>${order.checkingDate}</td>
                    <td>${order.abortDate}</td>
                    <td>${order.status}</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="col-lg-4">
            <div class="btn-group">
                <c:choose>
                    <c:when test="${order.status == 'processing'}">
                        <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=pending" class="btn btn-warning btn-sm">Pending</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=completed" class="btn btn-success btn-sm">Completed</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=abort" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?')">Abort</a>
                    </c:when>
                    <c:when test="${order.status == 'abort'}">
                        <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=pending" class="btn btn-warning btn-sm">Pending</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=processing" class="btn btn-info btn-sm">Processing</a>
                    </c:when>
                    <c:when test="${order.status == 'pending'}">
                        <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=processing" class="btn btn-info btn-sm">Processing</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=completed" class="btn btn-success btn-sm">Completed</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=${order.orderID}&OrderStatus=abort" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?')">Abort</a>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Second Row: Order Items and Customer Info -->
    <div class="row mt-4">
        <div class="col-lg-8">
            <h2>Order Items</h2>
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="thead-light">
                    <tr>
                        <th>Image</th>
                        <th>Product Name</th>
                        <th>Description</th>
                        <th>Quantity</th>
                        <th>Price</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${not empty orderItems}">
                        <c:forEach var="item" items="${orderItems}">
                            <tr>
                                <td>
                                    <img src="${pageContext.request.contextPath}/${item.product.getImageURL()}" alt="${item.product.name}" width="100" height="100">
                                </td>
                                <td>${item.product.name}</td>
                                <td>${item.product.description}</td>
                                <td>${item.getQuantity()}</td>
                                <td>${item.getPrice()*item.getQuantity()}</td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-lg-4">
            <h2>Customer Info</h2>
            <ul class="list-group">
                <li class="list-group-item">Name: ${user.name}</li>
                <li class="list-group-item">Email: ${user.email}</li>
                <li class="list-group-item">Phone: ${user.phone}</li>
                <li class="list-group-item">Address: ${user.address}</li>
                <c:if test="${not empty customer.imgURL}">
                    <li class="list-group-item">
                        <img src="${pageContext.request.contextPath}/${user.imgURL}" alt="Customer Image" class="img-fluid">
                    </li>
                    <li class="list-group-item">
                        <a href="${request.contextPath}/AMainController?action=userDetail&userId=${user.id}" class="btn btn-primary btn-sm">Detail</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
