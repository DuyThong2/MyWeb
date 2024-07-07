<%@page import="dto.plan.MealPlan"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@include file="../../adminCssAdder.jsp" %> <!-- Including CSS styles -->
        <title>Meal Plan Insert</title>
        <style>
            .main-container{
            }
            .popup-error {
                position: fixed;
                top: 50%;
                left: 50%; 
                transform: translate(-50%,-50%);
                background-color: white;
                width: 600px;
                height: 250px;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 10px;
                z-index: 10;
            }
            .error-close {
                position: absolute;
                top: 0px;
                right: 15px;
                font-size: 35px;
                color: rgb(200, 0, 0);
                cursor: pointer;
            }
            .error-close:hover {
                opacity: 0.8;
            }
            .error-text {
                font-size: 22px;
                text-align: center;
            }
            .overlay {
                position: fixed;
                top: 0;
                bottom: 0;
                left: 0;
                right: 0;
                z-index: 8;
                background-color: rgba(0, 0, 0, .8);
            }
            .bottom{
                margin-top:200px;
                height:100px;
                width:100vw;
            }
        </style>
    </head>
    <body>

        <% String insertUrl = request.getContextPath() + "/AMainController?action=MealPlanInsert";
            String error = (String) request.getAttribute("ERROR");
            MealPlan mealPlan = (MealPlan) request.getAttribute("mealPlan");
        %>


        <%@include file="../../AdminHeader.jsp" %> <!-- Including Header -->
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
        <div class="container main-container bg-white border border-warning" style="border-radius:10px;">
            <div class="container p-4">
                <div class="row d-flex justify-content-center">
                    <h2 class="text-danger">Insert Meal Plan</h2>
                </div>
                <hr>
                <form action="<%= insertUrl%>" method="POST" enctype="multipart/form-data">
                    <div class="row">
                        <div class="col-md-6 p-3">
                            <div class="form-group">
                                <label for="MealPlanId">Meal Plan ID</label>
                                <input class="form-control" type="text" id="MealPlanId" name="id"
                                       placeholder="Enter Meal Plan ID (MPxxx)" value="${mealPlan.id}" required>
                            </div>
                            <div class="form-group">
                                <label for="MealPlanName">Meal Plan Name</label>
                                <input class="form-control" type="text" id="MealPlanName" name="name" 
                                       placeholder="Enter Meal Name" value="${mealPlan.name}" required>
                            </div>
                        </div>
                        <div class="col-md-6 d-flex align-items-center justify-content-center">
                            <div class="form-group">
                                <label for="meal-plan-image">Image</label>
                                <input type="file" name="imgUrl" id="meal-plan-image" required>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="MealPlanDescription">Meal Plan Description</label>
                        <textarea class="form-control" type="text" id="MealPlanDescription" rows="8" name="content"
                                  placeholder="Enter Meal Plan Description" required>${mealPlan.content}</textarea>
                    </div>
                    <div class="form-group">
                        <label for="meal-plan-type">Meal Plan Type</label>
                        <select name="type" id="meal-plan-type" class="form-control">
                            <option value="Vegan" ${mealPlan.type == 'Vegan' ? 'selected' : ''}>Vegan</option>
                            <option value="Vegetarian" ${mealPlan.type == 'Vegetarian' ? 'selected' : ''}>Vegetarian</option>
                            <option value="Meat" ${mealPlan.type == 'Meat' ? 'selected' : ''}>Meat</option>
                        </select>
                    </div>
                    <div class="text-enter d-flex justify-content-center">
                        <input class="btn btn-lg btn-success" type="submit">
                    </div>
                </form>
            </div>
        </div>
        <%
            request.removeAttribute("ERROR");
            request.removeAttribute("mealPlan");
        %>
        <script>
            function closeMessage() {
                const message = document.querySelector('.popup-error');
                message.style.top = "-100%";
                const overlay = document.querySelector('.overlay');
                overlay.classList.remove('overlay');
            }

        </script>
        <%@include file="../../adminJs.jsp" %> <!-- Including JavaScript -->

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
