<%@page import="dao.product.MealDAO"%>

<%@page import="dto.product.Meal"%>
<%@page import="dao.product.IngredientDAO"%>
<%@page import="java.io.File"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="dto.product.Ingredient"%>
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
            MealDAO dao = new MealDAO();
            if (request.getParameter("mealId") == null) {
                response.sendRedirect(request.getContextPath() + "/AMainController?action=MealManagePage");
                return;
            }
            String id = request.getParameter("mealId");
            Meal oldMeal = dao.getMealFromId(id);

        %>
        <%@include file="../../AdminHeader.jsp" %>
        <div class="container">
            <div class="row">
                <!-- Left side: Display old information -->
                <div class="col-md-6">
                    <h3>Old Information</h3>
                    <c:set var="item" value= "<%=oldMeal%>" />
                    <div class="card">
                        <div class="card-body">
                            <p><strong>ID:</strong> ${item.getId()}</p>
                            <p><strong>Name:</strong> ${item.getName()}</p>
                            <p><strong>Price:</strong> ${item.getPrice()} USD</p>
                            <p><strong>Description:</strong> ${item.getDescription()}</p>
                            <p><strong>Content : </strong> <br> ${item.getDescription()}</p>
                            <p><strong>Image:</strong></p>
                            <img src="${pageContext.request.contextPath}/${item.getImageURL()}" alt="Old Image" class="img-fluid">
                        </div>
                    </div>
                </div>

                <!-- Right side: Form to input new values -->
                <div class="col-md-6">
                    <h4>Update Meal Details</h4>
                    <!-- Form for inserting meal details -->
                    <form id="mealForm" action="<%= request.getContextPath() + "/AMainController?action=MealUpdate"%>" method="post" enctype="multipart/form-data">
                        <div class="form-group">

                            <input type="hidden" class="form-control" id="mealID" name="id" value="<%=oldMeal.getId()%>" required="">
                        </div>
                        <div class="form-group">
                            <label for="mealName">Meal Name</label>
                            <input type="text" class="form-control" id="mealName" name="name" value="<%=oldMeal.getName()%>" placeholder="Enter meal name" required="">
                        </div>
                        <div class="form-group">
                            <label for="mealPrice">Meal Price</label>
                            <input type="text" class="form-control" id="mealPrice" value = <%=oldMeal.getPrice()%> name="price" placeholder="Enter meal price" required="">
                        </div>
                        <div class="form-group">
                            <label for="mealDescription">Meal Description</label>
                            <textarea class="form-control" id="mealDescription" rows="2" value="<%=oldMeal.getDescription()%>" name="description" placeholder="Enter meal description" required=""><%=oldMeal.getDescription()%></textarea>
                        </div>
                        <div class="form-group">
                            <label for="mealContent">Content and Instruction</label>
                            <textarea class="form-control" id="mealContent" rows="10" value="<%=oldMeal.getContent()%>" name="content" placeholder="Enter meal content nad methods" required=""><%=oldMeal.getContent()%></textarea>
                        </div>
                        <div class="form-group">
                            <label for="mealCategory">Category</label>
                            <select class="form-control" id="mealCategory" value="<%=oldMeal.getCategory()%>" name="mealCategory">
                                <option value="breakfast">Breakfast</option>
                                <option value="dinner">Dinner</option>
                                <option value="lunch">Lunch</option>
                                <option value="fast-food">Fast-Food</option>
                            </select>
                        </div>
                        <div class="form-group row">
                            <label for="imgURL" class="col-sm-2 col-form-label">Image File</label>
                            <div class="col-sm-10">
                                <input type="file" class="form-control-file" id="imgURL" name="imgURL" required>
                            </div>
                        </div>
                        <div class="text-center">
                            <div class="btn-group d-inline-block">
                                <a href="#" id="finishMealButton" class="btn btn-primary mr-2">Finish Meal</a>
                                <a href="#" id="addIngredientsButton" class="btn btn-success">Add Ingredients</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <%
            request.removeAttribute("errorMessage");

        %>


        <script>
            document.getElementById('finishMealButton').addEventListener('click', function (event) {
                event.preventDefault(); // Prevent the default anchor behavior
                var form = document.getElementById('mealForm');
                form.action += '&status=done';
                form.submit();
            });

            document.getElementById('addIngredientsButton').addEventListener('click', function (event) {
                event.preventDefault(); // Prevent the default anchor behavior
                var form = document.getElementById('mealForm');
                form.action += '&status=continue';
                form.submit();
            });
            function closeMessage() {
                const message = document.querySelector('.popup-error');
                message.style.top = "-100%";
                const overlay = document.querySelector('.overlay');
                overlay.classList.remove('overlay');
            }
        </script>


        <!-- Bootstrap JS and dependencies -->
        <%@include file="../../adminJs.jsp" %>
    </body>
</html>