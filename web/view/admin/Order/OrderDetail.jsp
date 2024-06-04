<%@page import="dto.product.Meal"%>
<%@page import="dto.account.User"%>
<%@page import="dto.order.OrderItem"%>
<%@page import="java.util.List"%>
<%@page import="dto.order.Order"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Details</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    </head>

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
    <body>
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
                                <td><%= order != null ? order.getOrderID() : "N/A"%></td>
                                <td><%= order != null ? order.getOrderDate() : "N/A"%></td>
                                <td><%= order != null ? order.getCheckingDate() : "N/A"%></td>
                                <td><%= order != null ? order.getAbortDate() : "N/A"%></td>
                                <td><%= order != null ? order.getStatus() : "N/A"%></td>
                            </tr>
                        </tbody>
                    </table>
                </div>


                <div class="col-lg-4">
                    <div class="btn-group">
                        <%
                            String status = order.getStatus();
                            switch (status) {
                                case "processing":
                        %>
                        <a href="<%=updateOrderStatusURL%>&orderId=<%= order.getOrderID()%>&status=pending" class="btn btn-warning btn-sm ">Pending</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=<%= order.getOrderID()%>&status=completed" class="btn btn-success btn-sm ">Completed</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=<%= order.getOrderID()%>&status=abort" class="btn btn-danger btn-sm " onclick="return confirm('Are you sure?')">Abort</a>
                        <%
                                break;
                            case "abort":
                        %>
                        <a href="<%=updateOrderStatusURL%>&orderId=<%= order.getOrderID()%>&status=pending" class="btn btn-warning btn-sm ">Pending</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=<%= order.getOrderID()%>&status=processing" class="btn btn-info btn-sm ">Processing</a>
                        <%
                                break;
                            case "pending":
                        %>
                        <a href="<%=updateOrderStatusURL%>&orderId=<%= order.getOrderID()%>&status=processing" class="btn btn-info btn-sm ">Processing</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=<%= order.getOrderID()%>&status=completed" class="btn btn-success btn-sm ">Completed</a>
                        <a href="<%=updateOrderStatusURL%>&orderId=<%= order.getOrderID()%>&status=abort" class="btn btn-danger btn-sm " onclick="return confirm('Are you sure?')">Abort</a>
                        <%
                                    break;
                            }
                        %>
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
                                <%
                                    if (orderItems != null) {
                                        for (OrderItem item : orderItems) {
                                %>
                                <tr>
                                    <%
                                        if (item.getProduct() instanceof Meal) {
                                            Meal meal = (Meal) item.getProduct();
                                    %>

                                    <td>
                                        <img src="${pageContext.request.contextPath}/<%=meal.getImageURL()%>" alt="<%= meal.getName()%>" width="100" height="100">
                                    </td>

                                    <%} else {
                                    %>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/images/packet.png" alt="packet" width="100" height="100">
                                    </td>

                                    <%                                        }


                                    %>
                                    <td><%= item.getProduct().getName()%></td>
                                    <td><%= item.getProduct().getDescription()%></td>
                                    <td><%= item.getQuantity()%></td>
                                    <td><%= item.getPrice()%></td>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="col-lg-4">
                    <h2>Customer Info</h2>
                    <ul class="list-group">
                        <li class="list-group-item">Name: <%= customer != null ? customer.getName() : "N/A"%></li>
                        <li class="list-group-item">Email: <%= customer != null ? customer.getEmail() : "N/A"%></li>
                        <li class="list-group-item">Phone: <%= customer != null ? customer.getPhone() : "N/A"%></li>
                        <li class="list-group-item">Address: <%= customer != null ? customer.getAddress() : "N/A"%></li>
                            <%
                                if (customer != null && customer.getImgURL() != null && !customer.getImgURL().isEmpty()) {
                            %>
                        <li class="list-group-item">
                            <img src="<%= request.getContextPath()%>/<%= customer.getImgURL()%>" alt="Customer Image" class="img-fluid">
                        </li>
                        <li class="list-group-item">
                            <a href="editOrder?id=<%= customer.getId()%>" class="btn btn-primary btn-sm">Detail</a>
                        </li>
                        <%
                            }
                        %>
                    </ul>
                </div>
            </div>
        </div>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
