<%@page import="java.util.ArrayList"%>
<%@page import="dto.order.Order"%>
<%@page import="Utility.Tool"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="dao.order.OrderItemDAO"%>
<%@page import="dto.order.OrderItem"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Order Management</title>
        <%@include file="../adminCssAdder.jsp"%>
        <style>
            body {
                background-color: #f8f9fa;
            }
            .container {
                margin-top: 30px;
            }
            .table-responsive {
                max-height: 1000px;
                overflow: auto;
            }


            .fixed-search-bar {
                position: sticky;
                bottom: 0;
                background-color: white;
                padding: 10px 0;
            }
            .btn-block {
                width: 100%;
            }
            .header-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }
            .header-row .form-inline {
                margin-left: auto;
            }
        </style>

        <%

            String orderDetailURL = request.getContextPath() + "/AMainController?action=orderDetail";
            String redirectURL = request.getContextPath() + "/AMainController?action=orderManage";

            // Mock data for demonstration. Replace with actual data from the database
            List<Order> orders = session.getAttribute("orders") != null
                    ? (List<Order>) session.getAttribute("orders") : new ArrayList();
        %>
    </head>
    <body>
        <header>
            <%@include file="../AdminHeader.jsp" %>
        </header>


        <div class="container" style="margin-top: 150px">
            <div class="row">
                <!-- First Row -->
                <div class="col-lg-10">
                    <div class="header-row">
                        <h2>Orders</h2>
                        <form method="POST" action="<%= redirectURL%>" class="form-inline">
                            <div class="form-group mb-0">
                                <label for="status" class="mr-2">Order Status:</label>
                                <select id="status" name="status" class="form-control mr-2">
                                    <option value="4">Completed</option>
                                    <option value="1">Pending</option>
                                    <option value="2">Processing</option>
                                    <option value="3">Aborted</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">Search</button>
                        </form>
                    </div>

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
                                    List<List<Order>> pages = new ArrayList();
                                    pages = Tool.splitToPage(orders, 20);
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
                                        List<Order> list = pages.get(realPage);
                                        for (Order order : list) {
                                %>
                                <tr>
                                    <td><%= order.getAddress()%></td>
                                    <td><%= Tool.parseTime(order.getOrderDate())%></td>
                                    <td><%= Tool.parseTime(order.getCheckingDate())%></td>
                                    <td><%= Tool.parseTime(order.getAbortDate())%></td>

                                    <td><%= order.getTotalItem()%> items</td>
                                    <td><%= order.getTotalPrice()%></td>
                                    <td>
                                        <div class="d-flex flex-column">
                                            <div class="mb-2">
                                                <a href="<%=orderDetailURL%>&orderId=<%= order.getOrderID()%>" class="btn btn-primary btn-sm btn-block">Detail</a>
                                            </div>
                                            <div class="btn-group-vertical">
                                                <%
                                                    int status = order.getStatus();
                                                    switch (status) {
                                                        case 2:
                                                %>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=1" class="btn btn-warning btn-sm mb-2">Pending</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=4" class="btn btn-success btn-sm mb-2 ">Completed</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=3" class="btn btn-danger btn-sm mb-2 " onclick="return confirm('Are you sure?')">Abort</a>
                                                <%
                                                        break;
                                                    case 3:
                                                %>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=1" class="btn btn-warning btn-sm mb-2">Pending</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=2" class="btn btn-info btn-sm mb-2 ">Processing</a>
                                                <%
                                                        break;
                                                    case 1:
                                                %>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=2" class="btn btn-info btn-sm mb-2">Processing</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=4" class="btn btn-success btn-sm mb-2">Completed</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=3" class="btn btn-danger btn-sm mb-2" onclick="return confirm('Are you sure?')">Abort</a>
                                                <%
                                                            break;
                                                    }
                                                %>
                                            </div>
                                        </div>
                                    </td>

                                </tr>
                                <%
                                        }
                                    }
                                %>
                            </tbody>
                        </table>

                        <div class="row mt-4 fixed-search-bar">
                            <div class="col-md-2 d-flex align-items-center">
                                <form action="<%= redirectURL%>" method="POST" class="form-inline">
                                    <input type="hidden" name="numPage" value="<%= pageNum - 1%>">
                                    <button type="submit" class="btn btn-secondary mr-2">&lt;</button>
                                </form>

                                <form action="<%= redirectURL%>" method="POST" class="form-inline">
                                    <input name="numPage" type="number" value="<%= pageNum%>" class="form-control page-number"  min="1" max="<%= pages.size()%>">

                                </form>

                                <span class="ml-2 mr-2">/</span>
                                <span class="total-pages"><%= pages.size()%></span>

                                <form action="<%= redirectURL%>" method="POST" class="form-inline">
                                    <input type="hidden" name="numPage" value="<%= pageNum + 1%>">
                                    <button type="submit" class="btn btn-secondary ml-2">&gt;</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-2">


                    <h2>Filter</h2>
                    <form action="<%=redirectURL%>" method="POST">
                        <div class="form-group">
                            <label for="searchCriteria">Search By:</label>
                            <select name="searchCriteria" id="searchCriteria" class="form-control">
                                <option value="address">Destination</option>
                                <option value="name">Name</option>
                                <option value="phone">Phone</option>
                                <option value="email">Email</option>
                                <option value="date">order date</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="searchValue">Search For:</label>
                            <input type="text" name="searchValue" id="searchValue" class="form-control" placeholder="time(dd/MM/yyyy)">
                        </div>
                        <input type="submit" value="Search" class="btn btn-primary mt-2">
                    </form>


                    <form action="<%=redirectURL%>" method="POST">
                        <fieldset class="form-group">
                            <legend>Category</legend>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" id="category1" name="category" value="category1">
                                <label class="form-check-label" for="category1">total price</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" id="category2" name="category" value="category2">
                                <label class="form-check-label" for="category2">number of items</label>
                            </div>

                            <hr>
                            <div class="form-check">
                                <input name="sort" type="radio" id="category-checkbox" value="max" class="form-check-input">
                                <label for="category-checkbox" class="form-check-label">Show max </label>
                                <br>
                                <input name="sort" type="radio" id="category-checkbox" value="min" class="form-check-input">
                                <label for="category-checkbox" class="form-check-label">Show min </label>
                            </div>
                        </fieldset>
                        <button type="submit" class="btn btn-primary">Filter</button>
                    </form>
                </div>
            </div>

        </div>
        <%@include file="../adminJs.jsp" %>
    </body>
</html>
