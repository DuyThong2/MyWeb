
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add New Meal</title>
        <!-- Bootstrap CSS -->
        <%@include file="../../adminCssAdder.jsp" %>
    </head>

    <%
        String finishURL = request.getContextPath() + "/AMainController?action=MealInsert";
        String error = (String) request.getAttribute("errorMessage");
        System.out.println(error);
    %>
    <body>
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
        <div class="container">
            <!-- Meal Insert Form -->
            <div class="">
                <h4>Insert Meal Details</h4>
                <!-- Form for inserting meal details -->
                <form id="mealForm" action="<%=finishURL%>" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="mealID">Meal ID</label>
                        <input type="text" class="form-control" id="mealID" name="id" placeholder="Enter meal ID (Mxxx)" required="">
                    </div>
                    <div class="form-group">
                        <label for="mealName">Meal Name</label>
                        <input type="text" class="form-control" id="mealName" name="name" placeholder="Enter meal name" required="">
                    </div>
                    <div class="form-group">
                        <label for="mealPrice">Meal Price</label>
                        <input type="number" class="form-control" id="mealPrice" name="price" min="1" max="100" placeholder="Enter meal price" required="">
                    </div>
                    <div class="form-group">
                        <label for="mealDescription">Meal Description</label>
                        <textarea class="form-control" id="mealDescription" rows="2" name="description" placeholder="Enter meal description" required=""></textarea>
                    </div>
                    <div class="form-group">
                        <label for="mealContent">Content and Instruction</label>
                        <textarea class="form-control" id="mealContent" rows="10" name="content" placeholder="Enter meal content nad methods" required=""></textarea>
                    </div>
                    <div class="form-group">
                        <label for="mealCategory">Category</label>
                        <select class="form-control" id="mealCategory" name="mealCategory">
                            <option value="breakfast">Breakfast</option>
                            <option value="dinner">Dinner</option>
                            <option value="lunch">Lunch</option>
                            <option value="fast-food">Fast-Food</option>
                        </select>
                    </div>
                    <div class="form-group row">
                        <label for="imgURL" class="col-sm-2 col-form-label">Image File</label>
                        <div class="col-sm-10">
                            <input type="file" class="form-control-file" id="imgURL" name="imgURL">
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
        <%request.removeAttribute("errorMessage");%>
        <%@include file="../../adminJs.jsp" %>
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
    </body>



</html>
