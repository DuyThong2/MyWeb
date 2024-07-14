<%-- 
    Document   : CustomerPlanDetail
    Created on : Jul 9, 2024, 12:55:38 AM
    Author     : ASUS
--%>

<%@page import="java.util.List"%>
<%@page import="dto.account.User"%>
<%@page import="dto.plan.DayPlan"%>
<%@page import="dao.product.MealDAO"%>
<%@page import="java.util.TreeMap"%>
<%@page import="dto.product.Meal"%>
<%@page import="dto.plan.MealPlan"%>
<%@page import="dao.plan.MealPlanDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meal Plan Detail</title>
        <%@include file="../../../cssAdder.jsp" %>
        <style>
            .main-plan-add-button {
                text-align: center;
                font-size: 1.5em; /* Smaller font size */
                background-color: #F07B07;
                color: white;
                transition: all 0.3s ease;
                width: 100%;
            }

            .main-plan-add-button:hover {
                text-align: center;
                background: linear-gradient(45deg, #F07B07, #FFA500);
            }
           
            .buy-button {
                background-color: #F07B07; 
                border: none;
                color: white;
                padding: 10px 20px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1.3em;
                transition: background 0.3s ease;
            }

            .card:hover .buy-button {
                background: linear-gradient(45deg, #F07B07, #FFA500);
            }

            .detail-button {
                background-color: #343a40; 
                border: none;
                color: white;
                padding: 10px 20px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1.3em;
                transition: background 0.3s ease;
            }

            .detail-button:hover {
                background: linear-gradient(45deg, #F07B07, #FFA500);
                color: transparent;
                background-clip: text;
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                box-shadow: 0 0 5px #F07B07;
            }
            .meal-type, .meal-description {
                font-size: 1.2em; /* Slightly larger font size */
                font-weight: bold;
            }
            #week, .plan-add-button {
                width: 100%;
                margin-top: 10px;
            }
            .plan-card {
                height: 250px;
                transition: 0.2s ease-in-out;
                border: 1.5px solid grey;
                box-shadow: 5px 5px 5px rgba(0,0,0,0.2);
                border-radius: 10px;
                overflow: hidden;
            }

            .plan-card div {
                height: 100%;
            }

            .plan-card-img-container img {
                height: 100%;
                width: 100%;
                object-fit: cover;
            }

            .plan-card-img-container {
                overflow: hidden;
            }

            .plan-card:hover {
                box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.5);
            }

            .plan-card:hover img {
                transform: scale(1.2);
            }

            .plan-card img {
                transition: 0.4s ease-in-out;
            }

            .plan-card a {
                overflow: hidden;
                width: 100%;
            }

            /* Plan Add Button */
            .plan-add-button {
                text-align: center;
                height: 4.688rem;
                width: 5.625rem;
                font-size: 2.4em;
                background-color: #F07B07;
                color: white;
                transition: all 0.3s ease;
            }

            .plan-add-button:hover {
                text-align: center;
                background: linear-gradient(45deg, #F07B07, #FFA500);
                /* Example gradient colors */
            }
            @media (max-width: 1200px){
                .plan-card{
                    height:300px;
                }
            } 
            .card-img-top {
                height: 150px;
            }

            .card {
                position: relative;
                height: 350px;
                transition: 0.2s ease-in-out;
                border: 1.5px solid grey;
                box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.2);
                border-radius: 10px;
                overflow: hidden;
            }

            .card:hover {
                box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.5);
            }

            .card-img-container {
                height: 200px;
                overflow: hidden;
            }

            .card:hover img {
                transform: scale(1.2);
            }

            .card a {
                overflow: hidden;
            }

            .card img {
                object-fit: cover;
                width: 100%;
                height: 100%;
                transition: 0.4s ease-in-out;
            }

            .card-body {
                display: flex;
                flex-direction: column;
                justify-content: space-around;
            }

            .card-title {
                font-size: 1.2rem;
            }

            .card-text {
                flex-grow: 1;
            }

            .buy-button {
                background-color: #F07B07;
                border: none;
                color: white;
                padding: 8px 16px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1.1em;
                transition: background 0.3s ease;
            }

            .card:hover .buy-button {
                background: linear-gradient(45deg, #F07B07, #FFA500);
            }

            .meal-plan {
                width:100%;
                display: flex;
                flex-wrap: nowrap;
                justify-content: center;
                background-color:grey;
            }

            .meal-plan .col {
                flex: 1 1 125px;
                max-width: 200px;
                margin: 5px;
                padding: 5px;
            }

            .add-button {
                background-color: #343a40;
                border: none;
                color: white;
                padding: 10px 20px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1.3em;
                transition: background 0.3s ease;
            }

            .add-button:hover {
                background: linear-gradient(45deg, #F07B07, #FFA500);
                color: transparent;
                background-clip: text;
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                box-shadow: 0 0 5px #F07B07;
            }

            @media(max-width:1330px) {
                .meal-plan {
                    flex-wrap: wrap;
                }
            }

            @media (max-width: 800px) {
                .meal-plan {
                    flex-direction: column;
                }

                .meal-plan .col {
                    max-width: 100%;
                }
            }

        </style>
    </head>
    <body>
        <%
            String detailMealUrl = request.getContextPath() + "/MainController?action=mealDetailPage";
            String detailMealPlanUrl = request.getContextPath() + "/MainController?action=mealPlanDetailPage";
            String addAllToCartUrl = request.getContextPath() + "/MainController?action=addAllToCart";
            String id = (String) request.getParameter("mealPlanId");
            User user = (User) session.getAttribute("LoginedUser");
            MealPlanDAO mpdao = new MealPlanDAO();
            MealPlan mealPlan = mpdao.getMealPlanById(id);
            if (mealPlan == null || mealPlan.getStatus() == 0) {
                response.sendRedirect(request.getContextPath() + "/MainController?action=shop");
                return;
            }
            String addCustomerPlanUrl = request.getContextPath() + "/MainController?action=addToCustomerPlan&mealPlanId=" + mealPlan.getId();
            MealDAO mdao = new MealDAO();
            TreeMap<Integer, Meal> mealList = new TreeMap<>();
            boolean daysMark[] = new boolean[7];
            if (mealPlan != null) {
                for (DayPlan dayPlan : mealPlan.getDayPlanContains()) {
                    String mealId = dayPlan.getMealId();
                    int day = dayPlan.getDayInWeek();
                    daysMark[day] = true;
                    Meal meal = mdao.getMealFromId(mealId);
                    mealList.put(day, meal);
                }
            }

            for (int i = 0; i < daysMark.length; i++) {
                if (!daysMark[i]) {
                    Meal meal = new Meal();
                    mealList.put(i, meal);
                }
            }
            List<MealPlan> relatedList = mpdao.getCustomerMealPlanListByType(mealPlan.getType(), 3);
            request.setAttribute("relatedList", relatedList);
        %>
        <%@include file="../header.jsp" %>
        <div class="mt-5 py-5" style="background-color: rgb(245,245,245)">
            <div class="container bg-white" style="min-width:95vw;">
                <div class="container-fluid py-5">
                    <div class="row g-4 mb-5">
                        <div class="col-lg-12 col-xl-12">
                            <div class="row g-4">
                                <div class="col-xl-6 col-lg-7">
                                    <div class="border rounded">
                                        <img src="<%= request.getContextPath()%>/<%= mealPlan.getImgUrl()%>" class="img-fluid rounded" alt="Image">
                                    </div>
                                </div>
                                <div class="col-xl-6 col-lg-5">
                                    <h1 style="color: orange" class="fw-bold mb-3"><%= mealPlan.getName()%></h1>
                                    <p class="meal-type mb-3"><span class="h3">Type:</span> <%= mealPlan.getType()%></p>
                                    <p class="meal-description mb-4"><%= mealPlan.getContent()%></p>
                                    <form id="orderForm" action="<%= addCustomerPlanUrl%>" method="POST" >
                                        <div class="row w-50 p-3 d-flex flex-column">
                                            <div class="mb-0   ">                       
                                                <select class="form-control" name="addWeek" id="week" required>
                                                    <option value="">Select Day</option>
                                                    <option value="1">Week 1</option>
                                                    <option value="2">Week 2</option>
                                                    <option value="3">Week 3</option>
                                                    <option value="4">Week 4</option>
                                                </select>  
                                            </div>
                                            <div class="mb-5">
                                                <button type="submit" class="main-plan-add-button btn btn-lg">ADD PLAN</button>
                                            </div>
                                            <a href="<%= addAllToCartUrl%>&mealPlanId=<%= mealPlan.getId()%>" class="detail-button btn btn-lg">ADD TO CART</a>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <hr class="py-1" style="border-top: 1.5px solid grey;">
                    <div class="row weekly-mealplan">
                        <div class="row col-md-12">
                            <h1 style="color:#F07B07; font-weight:bolder; font-size:2.5rem;" class="p-3">Weekly Meal Plan</h1>
                        </div>
                        <div class="row justify-content-center">
                            <div class="meal-plan w-100">
                                <%
                                    for (int i = 0; i < 7; i++) {
                                        Meal meal = mealList.get(i);
                                        String dayName = "";
                                        switch (i) {
                                            case 0:
                                                dayName = "Monday";
                                                break;
                                            case 1:
                                                dayName = "Tuesday";
                                                break;
                                            case 2:
                                                dayName = "Wednesday";
                                                break;
                                            case 3:
                                                dayName = "Thursday";
                                                break;
                                            case 4:
                                                dayName = "Friday";
                                                break;
                                            case 5:
                                                dayName = "Saturday";
                                                break;
                                            case 6:
                                                dayName = "Sunday";
                                                break;
                                        }
                                        if (meal != null) {
                                %>
                                <div class="col">
                                    <div class="card">
                                        <a href="<%= detailMealUrl%>&mealId=<%= meal.getId()%>" class="card-img-container">
                                            <img src="<%= meal.getImageURL() != null ? request.getContextPath() + "/" + meal.getImageURL() : request.getContextPath() + "/images/defaultMeal.jpg"%>" class="card-img-top" alt="<%= meal.getName()%>">
                                        </a>
                                        <div class="card-body">
                                            <h5 class="card-title"><%= meal.getName() != null ? meal.getName() : "No meal planned"%></h5>
                                            <div class="d-flex justify-content-between flex-lg-wrap">
                                                <% if (meal.isOnSale()) {%>
                                                <p class="text-danger fs-5 fw-bold mb-0" style="font-size:1.4rem;"><%= String.format("%.2f", meal.getPriceAfterDiscount())%>$</p>
                                                <p class="text-dark text-decoration-line-through" style="text-decoration-line: line-through;"><%= String.format("%.2f", meal.getPrice())%>$</p>
                                                <% } else {%>
                                                <p class="text-success fs-5 fw-bold mb-3" style="font-size:1.4rem;"><%= String.format("%.2f", meal.getPrice())%>$</p>
                                                <% }%>
                                            </div>
                                            <a href="<%= detailMealUrl%>&productId=<%= meal.getId()%>" class="buy-button btn btn-lg mb-2">DETAIL</a>
                                        </div>
                                        <div class="daily-title buy-button" style="font-weight:bolder; position:absolute; top:0; right:0;">
                                            <%= dayName%>
                                        </div>
                                    </div>
                                </div>
                                <% }
                                }%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="mt-5 py-5" style="background-color: rgb(245,245,245)">
            <div class="container bg-white" style="min-width:85vw;">
                <h1 class=" pt-4">Related Meal Plans</h1>
                <hr class="py-1" style="border-top: 1.5px solid grey;">

                <div class="row p-2 my-5 justify-content-center">
                    <c:forEach var="mealPlan" items="${requestScope.relatedList}">
                        <div class="col-md-9">
                            <div class="plan-card row my-4">
                                <div class="plan-card-img-container col-xl-5 col-lg-5 p-0">
                                    <a href="#">
                                        <img src="<%= request.getContextPath()%>/${mealPlan.imgUrl}" alt="${mealPlan.name}" class="plan-card-img-top">
                                    </a>
                                </div>
                                <div class="col-xl-5 col-lg-4 p-3">
                                    <div style="height:50%;">
                                        <h3>${mealPlan.name}</h3>
                                        <p>${mealPlan.type}</p>
                                    </div>
                                    <div class=" d-flex align-items-center" style="height:50%">
                                        <p>${mealPlan.content}</p>
                                    </div>
                                </div>
                                <div class="col-xl-2 col-lg-3 d-flex align-items-center">
                                    <a href="<%= detailMealPlanUrl%>&mealPlanId=${mealPlan.id}" class="plan-add-button btn btn-lg">ADD</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <%@include file="../mainFooter.jsp" %>
        <%@include file="../../../jsAdder.jsp" %>
    </body>
</html>