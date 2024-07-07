<%@ page import="dto.plan.MealPlan" %>
<%@ page import="dao.plan.MealPlanDAO" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%@include file="../../adminCssAdder.jsp" %> <!-- Including CSS styles -->
    <title>Update Meal Plan</title>
    <style>
        body {
            background-color: rgb(237, 235, 235);
        }

        * {
            margin: 0;
            box-sizing: border-box;
        }

        .main-container {
        }

        .meal-plan-img {
            height: 500px;
        }

        thead.thead-dark th.table-head {
            vertical-align: middle;
        }

        .description-col {
            max-width: 250px;
            word-wrap: break-word;
        }

        .img-col {
            min-width: 125px;
            max-width: 150px;
            text-align: center;
        }

        img {
            max-width: 100%;
        }

        .popup-error {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
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
            right: 0px;
            font-size: 35px;
            color: white;
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

        .bottom {
            margin-top: 200px;
        }
    </style>
</head>

<body>
    <% 
        MealPlanDAO mpdao = new MealPlanDAO();
        String id = request.getParameter("id");
        MealPlan mealPlan = mpdao.getMealPlanById(id);
       
        if (mealPlan == null) {
            request.getRequestDispatcher(request.getContextPath() + "/AMainController?action=MealPlan").forward(request, response);
        }
        pageContext.setAttribute("mealPlan", mealPlan);
    %>

    <%@include file="../../AdminHeader.jsp" %> <!-- Including Header -->

    <div class="container main-container bg-white border border-warning" style="border-radius: 10px;">
        <div class="container p-4">
            <div class="row d-flex justify-content-center">
                <h2 class="text-danger">Update Meal Plan</h2>
            </div>
            <hr>
            <div class="row px-3">
                <div class="row px-3">
                    <h4 class="text-primary m-0"><span>Your Current Meal Plan</span></h4>
                </div>
                <table class="table table-striped mt-4">
                    <thead class="thead-dark">
                        <tr>
                            <th class="table-head img-col">Images</th>
                            <th class="table-head id-col">Id</th>
                            <th class="table-head name-col">Name</th>
                            <th class="table-head type-col">Type</th>
                            <th class="table-head description-col-wide">Description</th>
                            <th class="table-head">Option</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="img-col">
                                <img src="${mealPlan.imgUrl}" alt="${mealPlan.name}">
                            </td>
                            <td>${mealPlan.id}</td>
                            <td>${mealPlan.name}</td>
                            <td>${mealPlan.type}</td>
                            <td class="description-col-wide">${mealPlan.content}</td>
                            <td class="d-flex flex-column justify-content-center">
                                <a href="<%= request.getContextPath() %>/AMainController?action=MealPlanDetail&id=${mealPlan.id}" class="btn btn-md btn-dark mb-2">Detail</a>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <form action="<%= request.getContextPath() %>/AMainController?action=MealPlanUpdate&id=${mealPlan.id}" method="post" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-md-6 p-3">
                        <div class="form-group">
                            <label for="MealPlanName">Meal Plan Name</label>
                            <input class="form-control" type="text" id="MealPlanName" name="name" placeholder="Enter Meal Name" value="${mealPlan.name}">
                        </div>
                    </div>
                    <div class="col-md-6 d-flex align-items-center justify-content-center p-3">
                        <div class="form-group mb-0 mt-3">
                            <label for="meal-plan-image" class="mb-0">Image</label>
                            <input class="mb-0" type="file" name="imgUrl" id="meal-plan-image">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="MealPlanDescription">Meal Plan Description</label>
                    <textarea class="form-control" type="text" id="MealPlanDescription" name="content" rows="8" placeholder="Enter Meal Plan Description">${mealPlan.content}</textarea>
                </div>
                <div class="form-group">
                    <label for="meal-plan-type">Meal Plan Type</label>
                    <select name="type" id="meal-plan-type" class="form-control">
                        <option value="Vegan" <c:if test="${mealPlan.type == 'Vegan'}">selected</c:if>>Vegan</option>
                        <option value="Vegetarian" <c:if test="${mealPlan.type == 'Vegetarian'}">selected</c:if>>Vegetarian</option>
                        <option value="Meat" <c:if test="${mealPlan.type == 'Meat'}">selected</c:if>>Meat</option>
                    </select>
                </div>
                <div class="text-center d-flex justify-content-center">
                    <input class="btn btn-lg btn-success" type="submit" value="Update Meal Plan">
                </div>
            </form>
        </div>
    </div>
    <script>
        function closeMessage() {
            const message = document.querySelector('.popup-error');
            message.style.top = "-100%";
            const overlay = document.querySelector('.overlay');
            overlay.classList.remove('overlay');
        }
    </script>
    <%@include file="../../adminJs.jsp" %> <!-- Including JavaScript -->
</body>

</html>
