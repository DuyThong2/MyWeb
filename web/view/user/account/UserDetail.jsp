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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            .user-info, .user-options {
                height: auto;
                margin-bottom: 20px;
            }
            .user-info img {
                object-fit: cover;
                border-radius: 50%;
                width: 150px;
                height: 150px;
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
            .table th, .table td {
                vertical-align: middle;
            }
        </style>
        <title>User Detail</title>
    </head>
    <%
        

        if (user == null) {
            String home = request.getContextPath() + "/AMainController?action=userManage";
            response.sendRedirect(home);
            return;
        }


    %>



    <body>
        <div class="container">
            <!-- User Info and Options -->
            <div class="row mt-4">
                <div class="col-md-6">
                    <div class="card user-info">
                        <div class="card-header">
                            <h3>User Information</h3>
                        </div>
                        <div class="card-body">
                            <p><strong>Name:</strong> ${user.getName()}</p>
                            <p><strong>Email:</strong> ${user.getEmail()}</p>
                            <p><strong>Phone:</strong> ${user.getPhone()}</p>
                            <p><strong>Address:</strong> ${user.getAddress()}</p>
                            <img src="${pageContext.request.contextPath}/${user.getImgURL()}" alt="User Image" class="img-thumbnail mt-3">
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card user-options">
                        <div class="card-header">
                            <h3>Options</h3>
                        </div>
                        <div class="card-body">
                            <p><strong>Status:</strong> ${user.getStatus()}</p>
                            <c:choose>
                                <c:when test="${user.getStatus() =='active'}">
                                    <form method="post" action="<%=disableURL%>">
                                        <input type="hidden" name="userId" value="${user.getId()}">
                                        <input type="hidden" name="status" value="disable">
                                        <button type="submit" class="btn btn-danger btn-sm">Disable User</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form method="post" action="<%=disableURL%>">
                                        <input type="hidden" name="userId" value="${user.getId()}">
                                        <input type="hidden" name="status" value="active">
                                        <button type="submit" class="btn btn-success btn-sm">Activate User</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                            <a href="<%=updateStatusURL%>&userId=${user.id}" class="btn btn-secondary btn-sm mt-2">Edit User</a>
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
                                    <th>Customer ID</th>
                                    <th>Order Date</th>
                                    <th>Checking Date</th>
                                    <th>Abort Date</th>
                                    <th>Total</th>
                                    <th>Price</th>
                                    <th>Options</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    List<Order> orders = new ArrayList<>(user.getOrderHistory().values());
                                    List<List<Order>> pages = new ArrayList<>();
                                    pages.add(new ArrayList<Order>());
                                    if (orders != null) {
                                        pages = Tool.splitToPage(orders, 12);
                                    }
                                    Object numString = session.getAttribute("numPage");
                                    int pageNum = 1;
                                    if (numString != null) {
                                        pageNum = (int) numString;
                                        if (pageNum < 1 || pageNum > pages.size()) {
                                            pageNum = 1;
                                        }
                                    }
                                    int realPage = pageNum - 1;
                                    List<Order> list = pages.get(realPage);

                                    if (orders != null) {
                                        OrderItemDAO dao = new OrderItemDAO();
                                        for (Order order : list) {
                                %>
                                <tr>
                                    <td><%= order.getCustomerID()%></td>
                                    <td><%= Tool.parseTime(order.getOrderDate())%></td>
                                    <td><%= Tool.parseTime(order.getCheckingDate())%></td>
                                    <td><%= Tool.parseTime(order.getAbortDate())%></td>
                                    <td><%= dao.sumQuantitiesByOrderId(order)%> items</td>
                                    <td><%= dao.sumTotalPriceByOrderId(order)%></td>
                                    <td>
                                        <a href="<%= orderDetailURL%>&orderId=<%= order.getOrderID()%>" class="btn btn-primary btn-sm">Detail</a>
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
                    <div class="col-md-2 d-flex align-items-center">
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

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
