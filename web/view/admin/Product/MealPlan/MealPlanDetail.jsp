<%@page import="java.util.TreeMap"%>
<%@page import="dto.plan.DayPlan"%>
<%@page import="dto.plan.MealPlan"%>
<%@page import="dto.product.Meal"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

        <title>Meal Plan Detail</title>
        <style>
            body {
                background-color: rgb(237, 235, 235);
            }

            * {
                margin: 0;
                box-sizing: border-box;
            }

            .main-container {
                margin-top: 100px;
            }

            .meal-plan-img {
                height: 500px;

            }



            thead.thead-dark th.table-head {
                vertical-align: middle;

            }


            .description-col {
                max-width: 250px;
                /* Adjust the width as needed */
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
        </style>
    </head>

    <body>
        <%
            MealPlan mealPlan = (MealPlan) request.getAttribute("mealPlan");
            if (mealPlan == null) {
                request.getRequestDispatcher(request.getContextPath() + "/AMainController?action=MealPlanDetail");
            }
        %>
        <c:set var="disablePage" value="${pageContext.request.contextPath}/AMainController?action=MealPlan&id=${mealPlan.id}"/>
        <div class="container main-container bg-white border border-warning" style="border-radius:10px;">
            <div class="container p-4 " style="border-bottom: 2px solid orange;">
                <div class="row mb-4">
                    <h1 class="main-title text-center w-100 text-warning">MEAL DETAIL</h1>
                </div>
                <hr>
                <div class="row">
                    <div class="col-lg-4 meal-plan-img">
                        <img src="${mealPlan.imgUrl}" alt="${mealPlan.name}">
                    </div>
                    <div class="col-lg-8 meal-plan-info">
                        <div class="row py-3 pr-3">
                            <h2 class="col-lg-10 meal-plan-title mb-0">${mealPlan.name}</h2>
                            <c:choose>
                                <c:when test="${mealPlan.status==0}">
                                    <a href="${disablePage}"
                                       class="col-lg-2 d-flex align-items-center justify-content-center btn btn-success rounded"><span>Enable</span></a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${disablePage}"
                                       class="col-lg-2 d-flex align-items-center justify-content-center btn btn-danger rounded"><span>Disable</span></a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <hr>
                        <div class="row mb-4">
                            <h4 class="col-lg-3 meal-plan-type text-primary" style="font-weight:normal;">Type</h4>
                            <h4 class="col-lg-9 meal-plan-type" style="font-weight:normal;">${mealPlan.type}</h4>
                        </div>
                        <div class="row mb-4">
                            <h4 class="meal-plan-description col-lg-3 text-primary" style="font-weight:normal">
                                Description</h4>
                            <h5 class="meal-plan-description col-lg-9" style="font-weight:normal;">${mealPlan.content}</h5>
                        </div>
                        <hr>
                        <div class="row button-section h-25 mt-5 mb-4">
                            <div class="row w-100">
                                <div class="col-lg-6 text-center p-3">
                                    <a class="btn btn-primary btn-lg p-3" href="">Edit Meal Plan Info</a>
                                </div>
                                <div class="col-lg-6 text-center p-3">
                                    <a class="btn btn-success btn-lg p-3" href="">Edit Weekly Meal Plan</a>
                                </div>

                            </div>
                            <div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container p-3">
                <h2></h2>
                <table class="table table-striped">
                    <thead class="thead-dark">
                        <tr scope="row"> 
                            <th class="table-head img-col">Images</th>
                            <th class="table-head">Id</th>
                            <th class="table-head">Name</th>
                            <th class="table-head">Type</th>
                            <th class="table-head description-col">Description</th>
                            <th class="table-head">Day In Week</th>
                            <th class="table-head">Option</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="dayString" value='${["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"]}'/>

                        <c:forEach var="entry" items="${mealList}">
                            <tr scope="row">
                                <td class="img-col">
                                    <img src="${entry.value.imageURL}" alt="${entry.value.name}">
                                </td>
                                <td>${entry.value.id}</td>
                                <td>${entry.value.name}</td>
                                <td>${entry.value.category}</td>
                                <td class="description-col">${entry.value.content}</td>
                                <td>${dayString[entry.key]}</td>
                                <td class="d-flex flex-column justify-content-space-between w-100">
                                    <c:choose>
                                        <c:when test="${entry.value.id== null}">
                                            <p></p>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="" class="btn btn-primary btn-md mb-3">Detail</a>
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                            </tr>
                        </c:forEach>


                    </tbody>
                </table>
            </div>
        </div>




        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>

</html>