<%@page import="dto.product.Ingredient"%>
<%@page import="dao.product.IngredientDAO"%>

<%@page import="java.io.File"%>
<%@page import="javax.servlet.http.HttpSession"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Update Ingredient</title>
        <!-- Bootstrap CSS -->
        <%@include file="../../adminCssAdder.jsp" %>
    </head>
    <body>
        <%
            // Mock old ingredient data (in practice, retrieve this from the database)
            String url = "/AMainController?action=IngredientUpdate";
            String error = (String) request.getAttribute("errorMessage");
            IngredientDAO dao = new IngredientDAO();
            if (request.getParameter("id") == null) {
                response.sendRedirect(request.getContextPath() + "/AMainController?action=ingredientManagePage");
                return;
            }
            int id = Integer.parseInt(request.getParameter("id"));
            Ingredient oldIngredient = dao.getIngredientFromId(id);

        %>

        <%@include file="../../AdminHeader.jsp" %>
        <% if (error != null) {%>
        <div class="popup-error">
            <p class="error-close" onclick="closeMessage()">x<p>
            <p class='error-text'>
                <%= error%>
            </p>
        </div>
        <div class="overlay"></div>

        <% }
        %>
        <div class="container ">
            <div class="row">
                <!-- Left side: Display old information -->
                <div class="col-md-6">
                    <h3>Old Information</h3>
                    <c:set var="item" value= "<%=oldIngredient%>" />
                    <div class="card">
                        <div class="card-body">
                            <p><strong>ID:</strong> ${item.getId()}</p>
                            <p><strong>Name:</strong> ${item.getName()}</p>
                            <p><strong>Price:</strong> ${item.getPrice()} USD</p>
                            <p><strong>Unit:</strong> ${item.getUnit()}</p>
                            <p><strong>Image:</strong></p>
                            <img src="${pageContext.request.contextPath}/${item.getImgURL()}" alt="Old Image" class="img-fluid">
                        </div>
                    </div>
                </div>

                <!-- Right side: Form to input new values -->
                <div class="col-md-6">
                    <h3>Update Information</h3>
                    <form action="<%=request.getContextPath() + url%>&id=${item.getId()}" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="ingredientName">Name</label>
                            <input type="text" class="form-control" id="ingredientName" value="${item.getName()}" name="ingredientName" required>
                        </div>
                        <div class="form-group">
                            <label for="price">Price (USD)</label>
                            <input type="number" step="0.01" class="form-control" value="${item.getPrice()}" id="price" min="1" name="price" required>
                        </div>
                        <div class="form-group">
                            <label for="unit">Unit</label>
                            <input type="text" class="form-control" id="unit" value=" ${item.getUnit()}" name="unit" required>
                        </div>
                        <div class="form-group">
                            <label for="imgURL">Image</label>
                            <input type="file" class="form-control-file" id="imgURL" name="imgURL" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Update Ingredient</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS and dependencies -->
        <%@include file="../../adminJs.jsp" %>
    </body>
</html>