<%-- 
    Document   : ProductManage
    Created on : May 20, 2024, 8:45:33 AM
    Author     : Admin
--%>
<%@page import="dto.product.Meal"%>
<%@page import="dao.discount.DiscountDAO"%>


<%@page import="java.nio.file.Paths"%>
<%@page import="Utility.Tool"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dto.product.Ingredient"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="style.css" rel="stylesheet">
        <title>Product Management</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <header>
            <!-- Your header content here -->
        </header>
        <%
            String disableMealURL = request.getContextPath() + "/AMainController?action=ProductDelete";
            String redirectUrl = request.getContextPath() + "/AMainController?action=MealManage";
            List<Meal> mList = (List<Meal>) session.getAttribute("mealList");

            if (mList == null) {
                response.sendRedirect(redirectUrl);
                return;
            }
            

            List<List<Meal>> pages = Tool.splitToPage(mList, 10);

            Object numString = session.getAttribute("numPage");
            int pageNum = 1;
            if (numString != null) {
                pageNum = (int) numString;
                if (pageNum < 1 || pageNum > pages.size()) {
                    pageNum = 1;
                }
            }
            int realPage = pageNum - 1;
            List<Meal> list = pages.get(realPage);
            DiscountDAO dao = new DiscountDAO();
            request.setAttribute("table", list);
        %>
        <div class="container">
            <div class="row">
                <!-- Table section taking 80% of the row -->
                <div id="table" class="col-md-10">
                    <!-- Table Name and Form in the same row -->
                    <div class="row align-items-center">
                        <div class="col-md-6">
                            <h1>Meal:</h1>
                        </div>
                        <div class="col-md-6">
                            <!-- Additional buttons can go here -->
                        </div>
                    </div>

                    <div>
                        <table class="table table-striped mt-4">
                            <thead>
                                <tr>
                                    <th>Images</th>
                                    <th>Id</th>
                                    <th>Name</th>
                                    <th>Price</th>
                                    <th>Category</th>
                                    <th>Description</th>
                                    <th>Discount (%)</th>
                                    <th>Option</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Meal item : list) { %>
                                <tr>    
                                    <td>
                                        <img src="${pageContext.request.contextPath}/<%=item.getImageURL()%>" alt="<%= item.getName() %>" width="100" height="100">
                                    </td>
                                    <td><%= item.getId() %></td>
                                    <td><%= item.getName() %></td>
                                    <td><%= String.format("%.2f", item.getPrice()) %></td>
                                    <td><%= item.getCategory() %></td>
                                    <td><%= item.getDescription() %></td>
                                    
                                    <td>
                                        <%= item.isOnSale() ? item.getDiscountPercent() + " %" : "0%" %>
                                    </td>
                                    <td>
                                        <a href="<%= request.getContextPath() %>/AMainController?action=addSale&id=<%= item.getId() %>" class="btn btn-sm btn-warning w-100 mb-2">Add Sale</a>
                                        <a href="<%= request.getContextPath() %>/AMainController?action=MealDetail&mealId=<%= item.getId() %>" class="btn btn-sm btn-info w-100 mb-2">Detail</a>

                                        <%
                                            String disableUrl = disableMealURL + "&status=";
                                            String status = item.getStatus();
                                            String mealId = item.getId();
                                            String disableLink = "";
                                            String disableBtnClass = status.equals("active") ? "btn-danger" : "btn-success";
                                            String disableBtnText = status.equals("active") ? "Disable" : "Enable";

                                            disableLink = "<a href='" + disableUrl + (status.equals("active") ? "disable" : "active") + "&mealId=" + mealId + "' class='btn btn-sm " + disableBtnClass + " w-100 mb-2'>" + disableBtnText + "</a>";
                                        %>
                                        <%= disableLink %>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>

                    <!-- Search bar and insert buttons in the same row -->
                    <div class="row mt-4 fixed-search-bar">
                        <div class="col-md-2 d-flex align-items-center">
                            <form action="<%= redirectUrl %>" method="POST" class="form-inline">
                                <input type="hidden" name="numPage" value="<%= pageNum - 1 %>">
                                <button type="submit" class="btn btn-secondary mr-2">&lt;</button>
                            </form>

                            <form action="<%= redirectUrl %>" method="POST" class="form-inline">
                                <input name="numPage" type="number" value="<%= pageNum %>" class="form-control page-number"  min="1" max="<%= pages.size() %>">
                                
                            </form>

                            <span class="ml-2 mr-2">/</span>
                            <span class="total-pages"><%= pages.size() %></span>

                            <form action="<%= redirectUrl %>" method="POST" class="form-inline">
                                <input type="hidden" name="numPage" value="<%= pageNum + 1 %>">
                                <button type="submit" class="btn btn-secondary ml-2">&gt;</button>
                            </form>
                        </div>

                        <div class="col-md-6">
                            <form action="<%= redirectUrl %>" method="POST" class="form-inline w-100">
                                <input name="searching" type="text" class="form-control flex-grow-1" placeholder="Search for name">
                                <input type="submit" value="Search" class="btn btn-primary ml-2">
                            </form>
                        </div>
                        <div class="col-md-4 d-flex justify-content-end">
                            <button class="btn btn-secondary w-100" onclick="location.href = '<%= request.getContextPath()+"/AMainController?action=MealInsertPage"%>'">Insert Meal</button>
                        </div>
                    </div>
                </div>

                <!-- Category section taking 20% of the row -->
                <div id="category" class="col-md-2 fixed-category">
                    <h1>Option</h1>
                    <form action="<%= redirectUrl %>" method="POST">
                        <div class="form-check">
                            <input name="sort" type="radio" id="category-checkbox" value="max" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Show max (have discount)</label>
                            <input name="sort" type="radio" id="category-checkbox" value="min" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Show min (no discount)</label>

                            <hr>
                            <input name="cate" type="radio" id="category-checkbox" value="price" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Price</label>
                            <br>

                            <input name="cate" type="radio" id="category-checkbox" value="id" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">ID</label>
                            <br>

                            <input name="cate" type="radio" id="category-checkbox" value="category" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Category</label>
                            <br>

                            <input name="cate" type="radio" id="category-checkbox" value="isOnSale" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Is on discount</label>
                            <br>
                        </div>
                        <input type="submit" value="Sort" class="btn btn-primary mt-2">
                    </form>
                </div>
            </div>
        </div>

        <footer>
            <!-- Your footer content here -->
        </footer>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
