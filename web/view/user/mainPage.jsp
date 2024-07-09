<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="dao.plan.MealPlanDAO"%>
<%@page import="dto.plan.MealPlan"%>
<%@page import="dto.plan.MealPlan"%>
<%@page import="dto.account.User"%>
<%@page import="dao.account.UserDAO"%>
<%@page import="java.util.HashMap"%>
<%@page import="dto.product.Product"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="dao.product.MealDAO"%>
<%@page import="dto.product.Meal"%>
<%@page import="java.util.List"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Main Page</title>
        <!-- Bootstrap CSS -->
        <%@include file="../../cssAdder.jsp" %>
        <!-- Custom CSS -->
        <style>
            /* Carousel */
            .body{
                overflow-x: hidden;
            }
            #carouselExampleSlidesOnly {
                height: 50vh;
                /* Reduced height for the carousel */
                margin-top: 160px;
                /* Add margin to avoid overlapping with navbar */
            }

            .carousel-inner img {
                height: 50vh;
                /* Adjust the height of the carousel images accordingly */
                width: 100%;
                object-fit: cover;
            }

            /* Cards */
            .card-img-top {
                height: 200px;
                object-fit: cover;
            }

            .card {
                height: 525px;
                transition: 0.2s ease-in-out;
                border: 1.5px solid grey;
                box-shadow: 5px 5px 5px rgba(0,0,0,0.2);
                border-radius: 10px;
                overflow: hidden;
            }

            .card:hover {
                box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.5);
            }
            .card-img-container{
                height:200px;
            }
            .card:hover img {
                transform: scale(1.2);
            }

            .card a {
                overflow: hidden;
            }

            .card img {
                height:100%;
                transition: 0.4s ease-in-out;
            }

            .card-body {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }

            .card-text {
                flex-grow: 1;
            }
          
            .buy-button {
                background-color: #F07B07; /* Primary button color */
                border: none;
                color: white;
                padding: 10px 20px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1.3em;
                transition: background 0.3s ease;
            }

            .buy-button:hover {
                background: linear-gradient(45deg, #F07B07, #FFA500);
            }

            .detail-button {
                background-color: #343a40; /* Dark button color */
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
                background:  linear-gradient(45deg, #F07B07, #FFA500);
                color: transparent;
                background-clip: text;
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                box-shadow: 0 0 5px #F07B07;
            }
            @media(max-width:1500px){
                .card{
                    height:475px;
                }
                .card-img-top,.card-img-container{
                    height: 190px;
                }
            }
            @media (max-width:1300px){
                .card{
                    height:450px;
                }
                .card-img-top,.card-img-container {
                    height: 180px;
                }
            }
            @media(max-width:1100px){
                .card{
                    height:425px;
                }
                .card-img-top,.card-img-container {
                    height: 170px;
                }
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

        </style>
    </head>

    <%

        String cartURL = request.getContextPath() + "/MainController?action=addToCart";
        String detailURL = request.getContextPath() + "/MainController?action=mealDetailPage";
        String detailMealPlanUrl = request.getContextPath() + "/MainController?action=mealPlanDetailPage";
        List<Meal> list = (List<Meal>) session.getAttribute("mealList");

        List<MealPlan> mealPlanList = (List<MealPlan>) session.getAttribute("mealPlanList");

        if (mealPlanList == null) {
            MealPlanDAO mpdao = new MealPlanDAO();
            mealPlanList = mpdao.getCustomerAllMealPlans();
            session.setAttribute("mealPlanList", mealPlanList);
        }
        if (list == null) {

            MealDAO mealDAO = new MealDAO();
            list = mealDAO.getCustomerMealList();
            session.setAttribute("mealList", list);

        }

//        List<Meal> carouselShow = list.stream().
//                filter((Meal meal) -> meal.getPrice() > 9.5).
//                limit(3).
//                collect(Collectors.toList());
//
//        List<Meal> homeShow = list.stream().
//                filter((Meal meal) -> meal.getPrice() > 7).
//                limit(8).
//                collect(Collectors.toList());
        List<Meal> carouselShow = new ArrayList<>();
        for (Meal meal : list) {
            if (meal.getPrice() > 9) {
                carouselShow.add(meal);
            }
            if (carouselShow.size() >= 3) {
                break;
            }
        }

        List<Meal> homeShow = new ArrayList<>();
        for (Meal meal : list) {
            if (meal.getPrice() > 8.5) {
                homeShow.add(meal);
            }
            if (homeShow.size() >= 8) {
                break;
            }
        }
//        Set<String> seenTypes = new HashSet<String>();
//        List<MealPlan> mealPlanShow = mealPlanList.stream()
//                .filter((MealPlan mealPlan) -> {
//                    return seenTypes.add(mealPlan.getType().toLowerCase());
//                }).limit(3).collect(Collectors.toList());

        Set<String> seenTypes = new HashSet<>();
        List<MealPlan> mealPlanShow = new ArrayList<>();
        for (MealPlan mealPlan : mealPlanList) {
            if (seenTypes.add(mealPlan.getType().toLowerCase())) {
                mealPlanShow.add(mealPlan);
            }
            if (mealPlanShow.size() >= 3) {
                break;
            }
        }
        request.setAttribute("mealPlanShow", mealPlanShow);
        request.setAttribute("carouselItems", carouselShow);
        request.setAttribute("homeShow", homeShow);


    %>
    <body>

        <!-- Navbar -->
        <%@include file="../user/header.jsp" %>
        <!-- Carousel -->
        <div stlye="min-witdth:100vw;" class="bg-white mt-5">
            <div style="min-width:100vw; background-color: rgb(239, 239, 239);">
                <div class="container mt-5 p-5" style="min-width:90vw; min-height: 70vh;">
                    <div class="row my-5">
                        <div class="col-5 d-flex-row justify-content-center">
                            <h1 style="color:#F07B07; font-weight:bolder;" class="display-4 py-2">Prepared Meal Plans</h1>
                            <p class="" style="font-size:1.6em">
                                Effortlessly plan your weekly meals with our convenient meal plan section. Choose from a variety of prepared meals or fresh ingredient packets, and schedule them in advance to fit your lifestyle.
                            </p>
                            <div class="d-flex justify-content-center align-items-center py-2">
                                <a class="btn btn-lg btn-dark text-white" style="font-size:1.6rem;">Custom Your Meal Plan</a>
                            </div>
                        </div>
                        <div class="col-7">
                            <div id="carouselExampleControls" class="carousel slide mb-4" data-ride="carousel" data-interval="5000">
                                <div class="carousel-inner">
                                    <c:forEach items="${carouselItems}" var="item" varStatus="status">
                                        <div class="carousel-item ${status.first ? 'active' : ''}">
                                            <a href="<%=detailURL%>&productId=${item.getId()}">
                                                <img src="${pageContext.request.contextPath}/${item.getImageURL()}" class="d-block w-100" alt="${item.getName()}">
                                            </a>
                                        </div>
                                    </c:forEach>
                                </div>
                                <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
                                    <span class="carousel-control-prev-icon" style="transform:scale(4); background-color: black;" aria-hidden="true"></span>
                                    <span class="sr-only">Previous</span>
                                </a>
                                <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                                    <span class="carousel-control-next-icon" style="transform:scale(4); background-color: black;" aria-hidden="true"></span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container mt-2 bg-white p-5 " style="min-width:90vw;">
                <div class="row p-2 my-5">
                    <div class="col-md-9">
                        <c:forEach var="mealPlan" items="${mealPlanShow}">
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
                        </c:forEach>
                    </div>
                    <div class="col-md-3 d-flex align-items-center">
                        <h1 style="color:#F07B07; font-weight:bolder;" class="display-4 py-2">Featured Meal Plans</h1>
                    </div>
                </div>
            </div>
            <hr class="py-1" style="border-top: 1.5px solid grey; ">

            <!-- Main Content -->
            <div class="container mt-2 bg-white p-5" style="min-width:90vw;">
                <h1 style="color:#F07B07; font-weight:bolder;" class="display-4 py-2">Featured Meals</h1>
                <hr class="py-1" style="border-top: 1.5px solid grey">
                <div class="row">
                    <!-- Dynamic Meal Cards -->
                    <%
                        for (Meal item : homeShow) {
                    %>
                    <div class="col-md-3 mb-5">
                        <div class="card">
                            <a href="<%=detailURL%>&productId=<%=item.getId()%>" class="card-img-container">
                                <img src="<%=request.getContextPath() + "/" + item.getImageURL()%>" class="card-img-top" alt="<%=item.getName()%>">
                            </a>
                            <div class="card-body">
                                <h5 class="card-title"><%=item.getName()%></h5>
                                <p class="card-text"><%=item.getDescription()%></p>
                                <% if (item.isOnSale()) {%>
                                <p class="text-danger fs-5 fw-bold mb-0" style="font-size:1.4rem;"><%=String.format("%.2f", item.getPriceAfterDiscount())%>$</p>
                                <p class="text-dark text-decoration-line-through" style="text-decoration-line: line-through;"><%=item.getPrice()%>$</p>
                                <% } else {%>
                                <p class="text-success fs-5 fw-bold mb-3 " style="font-size:1.4rem;"><%=String.format("%.2f", item.getPrice())%>$</p>
                                <% }%>
                                <a href="<%=cartURL%>&productId=<%=item.getId()%>" class="buy-button btn btn-lg  mb-2">BUY </a>
                                <a href="<%=detailURL%>&productId=<%=item.getId()%>" class="detail-button btn btn-lg ">DETAIL</a>
                            </div>
                        </div>
                    </div>
                    <% }%>
                </div>
            </div>


        </div>
        <%@include file="mainFooter.jsp" %>
        <!-- Bootstrap JS and jQuery -->
        <%@include file="../../jsAdder.jsp" %>
    </body>
</html>
