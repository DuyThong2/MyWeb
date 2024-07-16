<%@page import="com.sun.javafx.scene.control.skin.VirtualFlow.ArrayLinkedList"%>
<%@page import="dto.plan.MealPlan"%>
<%@page import="dao.account.UserDAO"%>
<%@page import="dto.account.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.discount.DiscountDAO"%>
<%@page import="Utility.Tool"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="dto.product.Product"%>
<%@page import="dao.product.MealDAO"%>
<%@page import="java.util.Map"%>
<%@page import="dto.product.Meal"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Shop</title>        <!-- Include CSS files if not included in header.jsp -->
        <%@include file="../../cssAdder.jsp" %>


        <style>
            .container {

            }
            .fruite-item {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                height: 100%;
                margin-bottom: 20px; /* Add space between items */
            }
            .fruite-img img {
                height: 200px;
                object-fit: cover;
            }
            .fruite-item .details {
                flex-grow: 1;
            }
            .pagination-buttons {
                display: flex;
                justify-content: center;
                align-items: center;
                margin-top: 20px; /* Add space above pagination */
            }
            .pagination-buttons form {
                display: inline-block;
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
                height: 75px;
                width: 90px;
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
            .banner{
                opacity:0.8;
                border-radius:20px;
                width:100%;
                height:100%;
                object-fit:cover;
                z-index:0;
            }
            @media(max-width:1400px){
                .plan-card{
                    height:275px;
                }
                .plan-add-button{
                    font-size:2.2em;
                }
            }
            @media (max-width: 1200px){
                .plan-card{
                    height:300px;
                }
            }  
            .meal-plan-card {
                height: 250px;
                transition: 0.2s ease-in-out;
                border: 1.5px solid grey;
                box-shadow: 5px 5px 5px rgba(0,0,0,0.2);
                border-radius: 10px;
                overflow: hidden;
            }

            .meal-plan-card div {
                height: 100%;
            }

            .meal-plan-card-img-container img {
                height: 100%;
                width: 100%;
                object-fit: cover;
            }

            .meal-plan-card-img-container {
                overflow: hidden;
            }

            .meal-plan-card:hover {
                box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.5);
            }

            .meal-plan-card:hover img {
                transform: scale(1.2);
            }

            .meal-plan-card img {
                transition: 0.4s ease-in-out;
            }

            .meal-plan-card a {
                overflow: hidden;
                width: 100%;
            }

            /* Plan Add Button */
            .meal-plan-add-button {
                text-align: center;
                height: 75px;
                width: 90px;
                font-size: 2.4em;
                background-color: #F07B07;
                color: white;
                transition: all 0.3s ease;
            }

            .meal-plan-add-button:hover {
                text-align: center;
                background: linear-gradient(45deg, #F07B07, #FFA500);
                /* Example gradient colors */
            }

            .banner{
                opacity:0.8;
                border-radius:20px;
                width:100%;
                height:100%;
                object-fit:cover;
                z-index:0;
            }

            @media(max-width:1400px){
                .meal-plan-card{
                    height:275px;
                }
                .meal-plan-add-button{
                    font-size:2.2em;
                }
            }

            @media (max-width: 1200px){
                .meal-plan-card{
                    height:300px;
                }
            }

        </style>
    </head>
    <body>
        <%  String redirectUrl = request.getContextPath() + "/MainController?action=plan";
            String detailMealPlanUrl = request.getContextPath() + "/MainController?action=mealPlanDetailPage";

            List<MealPlan> mpList = (List<MealPlan>) session.getAttribute("mpList");
            List<MealPlan> filterList = (List<MealPlan>) session.getAttribute("filterList");
            if (mpList == null) {
                response.sendRedirect(redirectUrl);
                return;
            }
            if (filterList != null) {
                mpList = filterList;
            }
            List<List<MealPlan>> mealPlanList = (List<List<MealPlan>>) Tool.splitToPage(mpList, 8);
            List<MealPlan> showMealPlan = new ArrayList<>();
            int pageNumber = 1;
            int pageSize = mealPlanList.size();

            Object numPage = (Object) session.getAttribute("pageNumber");
            if (numPage != null) {
                pageNumber = (int) numPage;
                if (pageNumber < 1 || pageNumber > pageSize) {
                    pageNumber = 1;
                }
            }
            if (mealPlanList != null && !mealPlanList.isEmpty()) {
                showMealPlan = mealPlanList.get(pageNumber - 1);
            }

        %>

        <%@include file="header.jsp" %>

        <div style="min-witdth:100vw; min-height:75vh;" class="bg-white mt-5">
            <div class="container bg-white py-5" style="min-width:90vw;">
                <div class="py-5">
                    <h1 class="mb-4" style="justify-content: center;color: orange">Customized Meal Plan</h1>
                    <div class="row g-4">
                        <div class="col-lg-12">
                            <div class="row g-4">
                                <div class="col-xl-3">
                                    <form action="<%=redirectUrl%>" method="POST" class="w-100 mx-auto d-flex">
                                        <input type="search" name="txtsearch" class="form-control p-3" placeholder="Search for Meal Plan" value="${sessionScope.localSearch}" aria-describedby="search-icon-1">
                                        <button type="submit" id="search-icon-1" class="input-group-text p-3"><i class="fa fa-search"></i></button>
                                    </form>
                                </div>
                                <div class="col-6"></div>
                            </div>
                            <div class="row g-4">
                                <div class="col-lg-3">
                                    <div class="row g-4">
                                        <!-- category -->
                                        <div class="col-lg-12">
                                            <div class="sort-section mb-3">
                                                <div class="card-header">
                                                    <h4>Sort Option</h4>
                                                </div>
                                                <div class="card-body">
                                                    <form action="<%=redirectUrl%>" method="POST">
                                                        <div class="form-check">
                                                            <input class="form-check-input" type="radio" name="type" id="vegan" value="Vegan">
                                                            <label class="form-check-label" for="vegan">Vegan</label>
                                                        </div>
                                                        <div class="form-check">
                                                            <input class="form-check-input" type="radio" name="type" id="vegetarian" value="Vegetarian">
                                                            <label class="form-check-label" for="vegetarian">Vegetarian</label>
                                                        </div>
                                                        <div class="form-check">
                                                            <input class="form-check-input" type="radio" name="type" id="meat" value="Meat">
                                                            <label class="form-check-label" for="meat">Meat</label>
                                                        </div>                                                       
                                                        <div class="w-100 d-flex justify-content-center">
                                                            <button type="submit" class="btn btn-primary btn-lg mt-3">Apply</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- show expensive and beautiful meal -->

                                        <div class="col-lg-12 w-100" style="height:2000px;">
                                            <div class="position-relative w-100 banner-container" style="height:100%;">
                                                <!-- Link to Vietnamese cuisine -->
                                                <a href="#" class="banner-link">
                                                    <img src="<%= request.getContextPath()%>/view/images/cat.jpg" class="banner" alt="">
                                                    <div class="position-absolute" style="top: 50%; right: 10px; transform: translateY(-50%);">
                                                        <h3 class="text-secondary fw-bold">Fresh <br> Fruits <br> Banner</h3>
                                                    </div>
                                                </a>
                                            </div>
                                        </div>


                                    </div>
                                </div>
                                <div class="col-lg-9">
                                    <div class="row g-4 justify-content-center"> 
                                        <%
                                            if (showMealPlan != null && !showMealPlan.isEmpty()) {
                                                // Iterate over the list of meal plans
                                                for (MealPlan mealPlan : showMealPlan) {
                                                    if (mealPlan != null) {
                                                        // Extract attributes of the meal plan with null checks
                                                        String mealPlanId = (mealPlan.getId() != null) ? String.valueOf(mealPlan.getId()) : "";
                                                        String imgUrl = (mealPlan.getImgUrl() != null) ? request.getContextPath() + "/" + mealPlan.getImgUrl() : "";
                                                        String name = (mealPlan.getName() != null) ? mealPlan.getName() : "No Name";
                                                        String type = (mealPlan.getType() != null) ? mealPlan.getType() : "Unknown Type";
                                                        String content = (mealPlan.getContent() != null) ? mealPlan.getContent() : "No Content";
                                        %>
                                        <!-- HTML structure for displaying each meal plan -->
                                        <div class="col-md-11">
                                            <div class="meal-plan-card row my-4">
                                                <div class="meal-plan-card-img-container col-xl-5 col-lg-5 p-0">
                                                    <a href="<%= detailMealPlanUrl%>&mealPlanId=<%= mealPlanId%>">
                                                        <img src="<%= imgUrl%>" alt="<%= name%>" class="meal-plan-card-img-top">
                                                    </a>
                                                </div>
                                                <div class="col-xl-5 col-lg-4 p-3">
                                                    <div style="height:50%;">
                                                        <h3><%= name%></h3>
                                                        <p><%= type%></p>
                                                    </div>
                                                    <div class="d-flex align-items-center" style="height:50%">
                                                        <p><%= content%></p>
                                                    </div>
                                                </div>
                                                <div class="col-xl-2 col-lg-3 d-flex align-items-center">
                                                    <a href="<%= detailMealPlanUrl%>&mealPlanId=<%= mealPlanId%>" class="meal-plan-add-button btn btn-lg text-center d-flex justify-content-center align-items-center">
                                                        <span class="p-0 m-0">ADD</span>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>

                                        <%
                                                }
                                            }
                                        } else {
                                        %>
                                        <!-- Message to display if no meal plans are available -->
                                        <p class="h4 p-5">No meal plans available.</p>
                                        <%
                                            }
                                        %> 
                                    </div>
                                    <div class="col-md-12 d-flex align-items-center justify-content-center">
                                        <form action="<%= redirectUrl%>" method="POST" class="form-inline">
                                            <input type="hidden" name="pageNumber" value="<%= pageNumber - 1%>">
                                            <button type="submit" class="btn btn-secondary mr-2">&lt;</button>
                                        </form>

                                        <form action="<%= redirectUrl%>" method="POST" class="form-inline">
                                            <input  name="pageNumber" type="number" value="<%= pageNumber%>" class="form-control page-number"  min="1" max="<%= pageSize%>">
                                        </form>

                                        <span class="ml-2 mr-2">/</span>
                                        <span class="total-pages"><%= pageSize%></span>

                                        <form action="<%= redirectUrl%>" method="POST" class="form-inline">
                                            <input type="hidden" name="pageNumber" value="<%= Math.min(pageNumber + 1, pageSize)%>">
                                            <button type="submit" class="btn btn-secondary ml-2">&gt;</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="mainFooter.jsp" %>

        <%@include file="../../jsAdder.jsp" %>
    </body>

</html>
