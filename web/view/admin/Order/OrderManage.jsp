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
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
              integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
        <style>
            .table-responsive {
                max-height: 400px;
                overflow: auto;
            }
        </style>
        <%

            String orderDetailURL = request.getContextPath() + "/AMainController?action=orderDetail";
            String redirectURL = request.getContextPath() + "/AMainController?action=orderManage";

            // Mock data for demonstration. Replace with actual data from the database
            List<Order> orders = session.getAttribute("orders") != null ?
                    (List<Order>)session.getAttribute("orders") : new ArrayList();
        %>
    </head>
    <body>
        

        <div class="container">
            <div class="row">
                <!-- First Row -->
                <div class="col-lg-10">
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
                                    List<List<Order>> pages = new ArrayList();
                                    pages = Tool.splitToPage(orders, 12);
                                    int pageNum = 1;
                                    Object numString = session.getAttribute("numPage");
                                        if (numString != null) {
                                            pageNum = (int) numString;
                                            if (pageNum < 1 || pageNum > pages.size()) {
                                                pageNum = 1;
                                            }
                                        }

                                    if (orders != null&& !orders.isEmpty()) {
                                        int realPage = pageNum - 1;
                                        List<Order> list = pages.get(realPage);
                                        for (Order order : list) {
                                %>
                                <tr>
                                    <td><%= order.getCustomerID()%></td>
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
                                                    String status = order.getStatus();
                                                    switch (status) {
                                                        case "processing":
                                                %>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=pending" class="btn btn-warning btn-sm ">Pending</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=completed" class="btn btn-success btn-sm ">Completed</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=abort" class="btn btn-danger btn-sm " onclick="return confirm('Are you sure?')">Abort</a>
                                                <%
                                                        break;
                                                    case "abort":
                                                %>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=pending" class="btn btn-warning btn-sm ">Pending</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=processing" class="btn btn-info btn-sm ">Processing</a>
                                                <%
                                                        break;
                                                    case "pending":
                                                %>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=processing" class="btn btn-info btn-sm ">Processing</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=completed" class="btn btn-success btn-sm ">Completed</a>
                                                <a href="<%=redirectURL%>&orderId=<%= order.getOrderID()%>&OrderStatus=abort" class="btn btn-danger btn-sm " onclick="return confirm('Are you sure?')">Abort</a>
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
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="searchValue">Search For:</label>
                            <input type="text" name="searchValue" id="searchValue" class="form-control" placeholder="Enter search term">
                        </div>
                        <input type="submit" value="Search" class="btn btn-primary mt-2">
                    </form>
                    <form method="POST" action="<%= redirectURL%>">
                        <div class="form-group">
                            <label for="status">Order Status:</label>
                            <select id="status" name="status" class="form-control">
                                <option value="completed">Completed</option>
                                <option value="pending">Pending</option>
                                <option value="processing">Processing</option>
                                <option value="abort">Aborted</option>
                            </select>
                        </div>


                        <button type="submit" class="btn btn-primary">search</button>
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
                            <div class="form-check">
                                <input class="form-check-input" type="radio" id="category3" name="category" value="category2">
                                <label class="form-check-label" for="category2">order address</label>
                            </div>
                            <hr>
                            <div class="form-check">
                                <input name="sort" type="radio" id="category-checkbox" value="max" class="form-check-input">
                                <label for="category-checkbox" class="form-check-label">Show max </label>
                                <input name="sort" type="radio" id="category-checkbox" value="min" class="form-check-input">
                                <label for="category-checkbox" class="form-check-label">Show min </label>
                            </div>
                        </fieldset>
                        <button type="submit" class="btn btn-primary">Filter</button>
                    </form>
                </div>
            </div>

        </div>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.3/js/bootstrap.min.js"></script>
    </body>
</html>
