<%-- 
    Document   : DayPlanInsert
    Created on : Jul 4, 2024, 7:18:15 PM
    Author     : ASUS
--%>

<%@page import="java.util.TreeMap"%>
<%@page import="dto.product.Meal"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dto.plan.MealPlan"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../../adminCssAdder.jsp" %>
        <title>JSP Page</title>
        <style>
            .main-container {
                margin-top: 100px;
            }

            body {
                background-color: rgb(237, 235, 235);
            }

            thead.thead-dark th.table-head {
                vertical-align: middle;
            }
            .name-col{
                width:200px;
            }
            .description-col {
                width:300px;
                word-wrap: break-word;
            }

            .description-col-wide {
                width:450px;
                word-wrap: break-word;
            }

            .img-col {
                width:125px;
                text-align: center;
            }
            .type-col{
                width:125px;
            }


            img {
                max-width: 100%;
                object-fit: contain;
                width: 125px;
                height: auto;
            }
            .error-message {
                color: red;
                font-weight: bold;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <%
            String detailPageUrl = request.getContextPath() + "/AMainController?action=MealPlanDetail";
            String insertDayPlanUrl = request.getContextPath() + "/AMainController?action=DayPlanInsert";
            MealPlan mealPlan = (MealPlan) request.getAttribute("mealPlan");
            ArrayList<Meal> searchList = (ArrayList<Meal>) session.getAttribute("searchList");
            TreeMap<Integer, Meal> mealList = (TreeMap<Integer, Meal>) request.getAttribute("mealList");
            String id = (String) request.getAttribute("id");
            String searchMealUrl = request.getContextPath() + "/AMainController?action=DayPlanInsert&id=" + id;
            String addMealUrl = request.getContextPath() + "/AMainController?action=DayPlanInsert&id=" + id;
            String deleteMealUrl = request.getContextPath() + "/AMainController?action=DayPlanInsert&id=" + id;
            request.setAttribute("mealPlan", mealPlan);
            request.setAttribute("searchList", searchList);
            if (mealPlan == null) {
                response.sendRedirect("/AMainController?action=DayPlanInser&id=" + id);
            }
        %>
        <%@include file="../../AdminHeader.jsp" %>

        <div class="container main-container bg-white border border-warning" style="border-radius:10px">
            <div class="row d-flex justify-content-center pt-3">
                <h1 class="text-primary">Day Plan Edit</h1>
                <c:if test="${not empty requestScope.ERROR}">
                    <div class="alert alert-danger w-100 text-center">
                        <c:out value="${requestScope.ERROR}" />
                    </div>
                </c:if>
            </div>
            <hr>
            <div class="row px-3">
                <div class="w-100 py-3 d-flex justify-content-between align-items-center">
                    <h4 class="text-danger m-0"><span>Your Meal Plan</span></h4>
                    <a href="#search-bar" class="btn btn-lg btn-warning text-light mx-3" style="width:150px; font-weight:bold;">SEARCH</a>
                </div>
                <table class="table table-striped mt-4">
                    <thead class="thead-dark">
                        <tr>
                            <th class="table-head img-col">Images</th>
                            <th class="table-head id-col">Id</th>
                            <th class="table-head name-col">Name</th>
                            <th class="table-head type-col">Type</th>
                            <th class="table-head description-col-wide">Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="img-col">
                                <img src="${requestScope.mealPlan.imgUrl}" alt="${requestScope.mealPlan.name}">
                            </td>
                            <td>${requestScope.mealPlan.id}</td>
                            <td>${requestScope.mealPlan.name}</td>
                            <td>${requestScope.mealPlan.type}</td>
                            <td class="description-col-wide">${requestScope.mealPlan.content}</td>
                        </tr>
                        <!-- Additional rows -->
                    </tbody>
                </table>
                <c:set var="daysInWeek" value='${["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"]}' />

                <table class="table table-striped mt-4">
                    <thead class="thead" style="background-color:#0d6efd;">
                        <tr scope="row" class="text-light">
                            <th class="table-head img-col">DId</th>
                            <th class="table-head">MealId</th>
                            <th class="table-head name-col">Name</th>
                            <th class="table-head">Type</th>
                            <th class="table-head description-col">Description</th>
                            <th class="table-head">Day In Week</th>
                            <th class="table-head">Option</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${mealList}">
                            <tr scope="row">
                                <td class="img-col">
                                    ${entry.value.dayPlan.id}
                                </td>
                                <td>${entry.value.meal.id}</td>
                                <td>${entry.value.meal.name}</td>
                                <td>${entry.value.meal.category}</td>
                                <td class="description-col">${entry.value.meal.content}</td>
                                <td>${daysInWeek[entry.key]}</td>
                                <td class="d-flex flex-column justify-content-space-between w-100">

                                    <c:if test="${entry.value.meal != null}">
                                        <a href="<%=deleteMealUrl%>&dayPlanId=${entry.value.dayPlan.id}&day=${entry.key}" class="btn btn-danger btn-md">Remove</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row p-3" id="search-bar">
                <form action="<%= searchMealUrl%>" method="POST" class="container">
                    <div class="row form-group form-inline">
                        <label for="search-bar" class="col-md-3" style="font-size:1.6rem;">Search For Meal</label>
                        <input type="text" name="txtsearch" id="search-bar" class="form-control col-md-7" value="${requestScope.searchholder}" placeholder="">
                        <button type="submit" class="btn btn-primary btn-md btn-warning col-md-1">Search</button>
                    </div>
                </form>
                <table class="table table-striped mt-4">
                    <thead class="thead" style="background-color:orange;">
                        <tr scope="row">
                            <th class="table-head img-col">Image</th>
                            <th class="table-head">MealId</th>
                            <th class="table-head name-col">Name</th>
                            <th class="table-head">Type</th>
                            <th class="table-head description-col-wide">Description</th>
                            <th class="table-head">Option</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="meal" items="<%=searchList%>">
                            <tr scope="row">
                                <td class="img-col">
                                    <img src="${meal.imageURL}" alt="${meal.name}">
                                </td>
                                <td>${meal.id}</td>
                                <td>${meal.name}</td>
                                <td>${meal.category}</td>
                                <td class="description-col-wide">${meal.content}</td>
                        <form action='<%= addMealUrl%>&mealId=${meal.id}' method="POST">
                            <td class="d-flex flex-column justify-content-space-between w-100">
                                <select class="form-control" name="day" id="day" required>
                                    <option value="">Select Day</option>
                                    <option value="0">Monday</option>
                                    <option value="1">Tuesday</option>
                                    <option value="2">Wednesday</option>
                                    <option value="3">Thursday</option>
                                    <option value="4">Friday</option>
                                    <option value="5">Saturday</option>
                                    <option value="6">Sunday</option>
                                </select>
                                <button type="submit" class="btn btn-warning btn-md mb-3">Add</button>
                            </td>
                        </form>
                        </tr>
                    </c:forEach>   
                    </tbody>
                </table>
            </div>
        </div>


        <%@include file="../../adminJs.jsp" %>
    </body>


</html>
