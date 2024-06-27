<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="dto.product.Ingredient"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%
    String redirectURL = request.getContextPath() + "/AMainController?action=PacketInsert";
    Map<Ingredient, Integer> insertedIngredient = (Map<Ingredient, Integer>) session.getAttribute("inserted");
    if (insertedIngredient == null) {
        response.sendRedirect(redirectURL);
        return;
    }
    List<Ingredient> foundList = (List<Ingredient>) request.getAttribute("ingredientList");
%>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add New Meal</title>
        <!-- Bootstrap CSS -->
        <%@include file="../../adminCssAdder.jsp" %>
    </head>
    <body>
        <%@include file="../../AdminHeader.jsp" %>
        <div class="container">
            <h2 class="mt-3">Add packet info for ${mealInfo.getName()}</h2>

            <!-- Ingredient Insert Section -->
            <div class="row mt-3">
                <!-- Ingredient Search Section -->
                <div class="col-md-8">
                    <h4>Search Ingredient</h4>
                    <!-- Form for searching ingredients -->
                    <form class="form-inline mb-3" action="<%=redirectURL%>" method="POST">
                        <div class="form-group mr-2">
                            <input name="ingredientName" type="text" class="form-control" id="searchIngredient" placeholder="Search by name">
                        </div>
                        <button type="submit" class="btn btn-primary">Search</button>
                    </form>

                    <!-- Table to display search results -->
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Ingredient Name</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Loop to display search results -->
                            <c:forEach items="${ingredientList}" var="foundItem">
                                <tr>
                                    <form action="<%=redirectURL%>" method="POST">
                                        <td>${foundItem.getName()}</td>
                                        <td>
                                            <input type="number" name="quantity" class="form-control" required>
                                            <input type="hidden" name="ingredientId" value="${foundItem.getId()}">
                                        </td>
                                        <td>${foundItem.getPrice()}$ per ${foundItem.getUnit()} </td>
                                        <td>
                                            <button type="submit" class="btn btn-success">Insert</button>
                                        </td>
                                    </form>
                                </tr>
                            </c:forEach>
                            <!-- End of Loop -->
                        </tbody>
                    </table>
                </div>

                <!-- Inserted Ingredients Section -->
                <div class="col-md-4">
                    <h4>Inserted Ingredients</h4>
                    <!-- Table to display inserted ingredients -->
                    <table class="table mt-3">
                        <thead>
                            <tr>
                                <th>Ingredient Name</th>
                                <th>Quantity</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody id="insertedIngredients">
                            <!-- Inserted ingredients will be displayed here -->
                            <c:forEach items="<%=insertedIngredient%>" var="entry">
                                <tr>
                                    <td>${entry.key.getName()}</td>
                                    <td>${entry.value}</td>
                                    <td>
                                         <a href="<%=redirectURL%>&removeId=${entry.key.getId()}" class="btn btn-danger">Remove</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <!-- Finalize Button -->
                    <a href="<%=redirectURL%>&finalize=true" class="btn btn-primary mt-3" name="finalize">Finalize</a>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <%@include file="../../adminJs.jsp" %>
    </body>
</html>
