<%-- 
    Document   : CustomerPlanDetail
    Created on : Jul 9, 2024, 12:55:38 AM
    Author     : ASUS
--%>

<%@page import="dto.product.Meal"%>
<%@page import="dto.plan.CustomerDayPlan"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@page import="dto.plan.CustomerPlan"%>
<%@page import="dao.plan.CustomerPlanDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%@include file="../../../cssAdder.jsp" %>
        <style>
            html, body {
                height: 100%;
                margin: 0;
                padding: 0;
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
                justify-content: center; /* Centers buttons vertically */
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
                background-color: rgb(245,245,245);
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

            .daily-title {
                background-color: #F07B07;
                color: white;
                padding: 5px 10px;
                font-weight: bold;
                position: absolute;
                right: 0;
                top: 0;
                left: 0;
                height: auto;
                text-align: center;
                z-index: 1;
            }

            @media (max-width:1086px) {
                /* nav bar font size shrink */

                .central-links {
                    display: collapse;
                }

                .central-links a {
                    display: none;
                }
            }

            .plan-card {
                height: 300px;
                transition: 0.2s ease-in-out;
                border: 1.5px solid grey;
                box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.2);
                border-radius: 10px;
                width: 95%;
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
            .plan-card-img-container a{
                overflow:hidden;
            }
            .plan-card img {
                transition: 0.4s ease-in-out;
            }

            .plan-card a {
                overflow: hidden;
                width: 100%;
            }

            .plan-add-button {
                text-align: center;
                font-size: 1.4em;
                background-color: #F07B07;
                color: white;
                transition: all 0.3s ease;
            }

            .plan-delete-button {
                text-align: center;
                font-size: 1.4em;
                color: white;
                transition: all 0.3s ease;
            }


            .weekly-title {
                background-color: #343a40;
                color: white;
                padding: 5px 10px;
                font-weight: bold;
                text-align: center;
                font-size: 25px;
                z-index: 1;
                border-radius: 10px;
            }
            .chosen-weekly{
                background-color: #F07B07;
            }
            .chosen-week{
                background-color:grey;
                border-radius: 10px;
                margin:0；
            }
            .plan-card:hover .plan-add-button,
            .plan-card:hover .weekly-title {
                background: linear-gradient(45deg, #F07B07, #FFA500);
                /* Example gradient colors */
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

            .error-close{
                position: absolute;
                top:10px;
                right:10px;
                font-size:35px;
                color:rgb(200, 0, 0);
            }
            .error-close:hover{
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
                z-index: 9;
                background-color: rgba(0, 0, 0, .8);
            }
        </style>
    </style>
</head>
<body>
    <%
        String mealDetailUrl = request.getContextPath() + "/MainController?action=mealDetailPage";
        String customerDetailControllerUrl = request.getContextPath() + "/MainController?action=customerPlan";
        String deleteDetailUrl = request.getContextPath() + "/MainController?action=customerPlan";
        String detailURL = request.getContextPath() + "/MainController?action=mealDetailPage";
        String searchMealUrl = request.getContextPath() + "/MainController?action=customerPlanUpdate";
        String addCustomerDayPlan = request.getContextPath() + "/MainController?action=AddToCustomerDayPlan";
        String customerPlanIdStr = request.getParameter("customerPlanId");
        String deleteUrl = request.getContextPath() + "/MainController?action=deleteCustomerDayPlan";

        String customerDayPlanDetailUrl = request.getContextPath() + "/MainController?action=customerDayPlanDetailPage";
        if (customerPlanIdStr == null || customerPlanIdStr.isEmpty()) {
            response.sendRedirect(customerDetailControllerUrl);
            return;
        }

        int customerPlanId = Integer.parseInt(customerPlanIdStr.trim());
        CustomerPlanDAO cpdao = new CustomerPlanDAO();
        CustomerPlan customerPlan = cpdao.getCustomerPlanById(customerPlanId);
        if (customerPlan == null) {
            response.sendRedirect(customerDetailControllerUrl);
            return;
        }
        int weekNum = customerPlan.getWeek();
        String week = weekNum + "";
        Object attribute = session.getAttribute("searchingMealList");
        List<Meal> list = new ArrayList<>();
        if (attribute instanceof List) {
            try {
                list = (List<Meal>) attribute; // Safe cast after checking instance
            } catch (ClassCastException e) {
                // Handle the exception or log it
                e.printStackTrace();
            }
        }
    %>
    <%@include file="../header.jsp" %>
    <<div class="mt-5 py-5" style="background-color: rgb(245,245,245)">
        <div class="container bg-white" style="min-width:90vw;">
            <div class="row w-100">
                <h1 class="my-4 w-100 text-center" style="justify-content: center;color: #07F07B;">Edit Weekly Meal Plan</h1>
            </div>
            <hr class="py-1" style="border-top: 1.5px solid grey">
            <div class="row w-100　d-flex justify-content-center ">
                <div class="col-md-7">
                    <div class="row d-flex justify-content-center">
                        <div class="plan-card row my-4 bg-white " style="position: relative;">
                            <div class="plan-card-img-container col-xl-6 p-0">
                                <a href="">
                                    <img src="<%= customerPlan.getImgUrl()%>" alt="<%= customerPlan.getName()%>" class="plan-card-img-top">
                                </a>
                            </div>
                            <div class="col-xl-6 p-3 d-flex flex-column">
                                <div style="height:50%;">
                                    <h5><%= customerPlan.getName()%></h5>
                                    <p><%= customerPlan.getType()%></p>
                                </div>
                                <div class="d-flex flex-column p-0 justify-content-around">

                                    <form class="w-100" id="deleteForm" action="<%= deleteDetailUrl%>" method="post" onsubmit="return confirmDelete(event)">
                                        <input type="hidden" name="deleteId" value="<%= customerPlan.getId()%>">
                                        <input type="hidden" name="deleteWeek" value="<%= weekNum%>">
                                        <button type="submit" class="w-100 plan-delete-button btn btn-lg btn-danger text-center text-light d-flex justify-content-center align-items-center">
                                            <span class="p-0 m-0">DELETE</span>
                                        </button>
                                    </form>
                                </div>  
                            </div>

                            <div class="chosen-weekly weekly-title"
                                 style="font-weight: bolder; position: absolute; top: -40px; left: 0; right:0; height:50px; ">
                                <a href="" style="text-decoration:none; color:white;"> Week <%=weekNum%><a/>
                            </div>
                        </div>
                    </div>          
                </div>

            </div>
            <div class="row">
                <div class="meal-plan w-100">
                    <%
                        // Assuming daysInWeek array is predefined
                        String[] daysInWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

                        // Retrieve the CustomerPlan for the current week
                        List<CustomerDayPlan> dayPlans = new ArrayList<>();
                        if (customerPlan != null && customerPlan.getContainsCustomerPlan() != null) {
                            for (Object obj : customerPlan.getContainsCustomerPlan()) {
                                if (obj instanceof CustomerDayPlan) {
                                    dayPlans.add((CustomerDayPlan) obj);
                                }
                            }
                        }
                        for (int i = 0; i < 7; i++) {
                            // Get the corresponding day for the loop
                            String dayName = daysInWeek[i];
                            // Find the CustomerDayPlan for the current day
                            CustomerDayPlan dayPlan = null;
                            for (CustomerDayPlan dp : dayPlans) {
                                if (dp.getDayInWeek() == i) {
                                    dayPlan = dp;
                                    break;
                                }
                            }
                            Meal meal = dayPlan != null ? dayPlan.getMeal() : null;
                    %>
                    <!-- Render Day Plan -->    
                    <div class="col">
                        <div class="card" style="position:relative;">
                            <% if (meal != null) {%>
                            <%  if (meal.getStatus().equalsIgnoreCase("disable")) { %>
                            <div class="card-body">
                                <h4 class="text-danger">Not Available</h3>
                            </div>
                            <% } else {%>
                            <!-- If there is a meal plan for the day -->
                            <a href="<%= mealDetailUrl%>&productId=<%= meal.getId()%>" class="card-img-container">
                                <img src="<%= meal.getImageURL() != null ? meal.getImageURL() : "default.jpg"%>" class="card-img-top" alt="<%= meal.getName() != null ? meal.getName() : "No Meal Name"%>">
                            </a>
                            <div class="card-body">
                                <h5 class="card-title"><%= meal.getName() != null ? meal.getName() : "No Meal Name"%></h5>
                                <a href="<%= mealDetailUrl%>&productId=<%= meal.getId()%>" class="buy-button btn btn-lg mb-2">DETAIL</a>
                            </div>

                            <% }%>
                            <% } else {
                                if (customerPlan != null) {
                            %>
                            <!-- If there is no meal plan for the day -->   
                            <div class="card-body">
                                <a href="#search" class="add-button btn btn-lg mb-2">ADD</a>
                            </div>
                            <% }
                                }%>
                            <div class="daily-title buy-button" style="font-weight: bolder; position: absolute; top: 0; left: 0;">
                                <%= dayName%>
                            </div>
                        </div>
                        <% if (meal != null) {%>  
                        <div class=""
                             style="background-color:white;position: absolute; top: -5px; right: -5px; z-index: 1; height:30px;width:30px; border-radius: 50%;">
                        </div>
                        <form id="deleteForm" method="POST" action="<%= deleteUrl%>" onsubmit="confirmDelete(event)" method="POST">
                            <input type="hidden" name="deleteDay" value="<%= i%>">
                            <input type="hidden" name="customerPlanId" value="<%= customerPlanId%>">
                            <button type="submit" class="text-danger btn btn-white"
                                    style="position: absolute; top: -20px; right: -20px; z-index: 1; font-size:40px;">
                                <i class='bx bxs-x-circle'></i>
                            </button>
                        </form>
                        <% }%>
                    </div>
                    <%
                        }
                    %>
                </div>

            </div>               
        </div>
        <hr class="py-1" style="border-top: 1.5px solid grey">
        <div class="container bg-white" style="min-width:90vw;">
            <div class="row w-100 p-3">
                <h1 class=" w-100 text-start " style="justify-content: center;color: #F07B07;">Search For Meal To Add In Meal Plan</h1>
            </div>
            <div class="col-12">
                <form action="<%=searchMealUrl%>" method="POST" class="w-100 mx-auto d-flex">
                    <input type="hidden" name="customerPlanId" value="<%= customerPlanId%>" >
                    <input type="search" name="txtsearch" id="search" class="form-control p-3" placeholder="Search for Meal Plan" value="${sessionScope.localSearchScheduled}" aria-describedby="search-icon-1">
                    <button type="submit" id="search-icon-1" class="input-group-text p-3"><i class="fa fa-search"></i></button>
                </form>
            </div>
            <div class="row g-4 justify-content-center"> 
                <%
                    if (list != null && !list.isEmpty()) {
                        int i = 0;
                        // Iterate over the list of meals
                        for (Meal item : list) {
                            if (i > 5) {
                                break;
                            } else {
                                i++;
                            }
                            if (item != null) {
                                // Extract attributes of the meal with null checks
                                String mealId = (item.getId() != null) ? String.valueOf(item.getId()) : "";
                                String imgUrl = (item.getImageURL() != null) ? request.getContextPath() + "/" + item.getImageURL() : "";
                                String name = (item.getName() != null) ? item.getName() : "No Name";
                                String description = (item.getDescription() != null) ? item.getDescription() : "No Description";
                                double price = item.getPrice();
                                double priceAfterDiscount = item.getPriceAfterDiscount();
                                boolean onSale = item.isOnSale();
                %>
                <div class="col-xl-3">
                    <div class="card mb-5">
                        <a href="<%=detailURL%>&productId=<%=mealId%>" class="card-img-container">
                            <img src="<%= imgUrl%>" class="card-img-top" alt="meal Img">
                        </a>
                        <div class="card-body">
                            <h5 class="card-title"><%= name%></h5>
                            <p class="card-text"><%= description%></p>
                            <div class="d-flex justify-content-between flex-lg-wrap">
                                <% if (onSale) {%>
                                <p class="text-danger fs-5 fw-bold mb-0" style="font-size:1.4rem;"><%=String.format("%.2f", priceAfterDiscount)%>$</p>
                                <p class="text-dark text-decoration-line-through" style="text-decoration-line: line-through;"><%= price%>$</p>
                                <% } else {%>
                                <p class="text-success fs-5 fw-bold mb-3" style="font-size:1.4rem;"><%=String.format("%.2f", price)%>$</p>
                                <% }%>
                            </div>
                            <form class="d-flex flex-columns justify-content-around align-items-center" action="<%= addCustomerDayPlan%>&customerPlanId=<%=customerPlanId%>" style="width:100;" method="POST">
                                <select class="form-control" name="addDay" id="day" required>
                                    <option value="">Set Day</option>
                                    <option value="0">Monday</option>
                                    <option value="1">Tuesday</option>
                                    <option value="2">Wednesday</option>
                                    <option value="3">Thursday</option>
                                    <option value="4">Friday</option>
                                    <option value="5">Saturday</option>
                                    <option value="6">Sunday</option>
                                </select>
                                </br>
                                <input type="hidden" value="<%=mealId%>" name="mealId">
                                <button type="submit" class="plan-add-button btn btn-lg text-center d-flex justify-content-center align-items-center w-100">
                                    <span class="p-0 m-0">ADD</span>
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                <%
                        }
                    }
                } else {
                %>
                <p class="h4 " style="height:20vh;">No meals available.</p>
                <%
                    }
                %>

            </div>
        </div>
    </div>
    <%
        String error = (String) request.getAttribute("ADDERROR");
        if (error != null) {
    %>
    <div class="popup-error">
        <i class='error-close bx bxs-x-circle' onclick="closeError()"></i>
        <p class="error-text text-danger"><%= error%></p>
    </div>
    <div class="overlay"></div>
    <%
            request.removeAttribute("ADDERROR");
        }
    %>
    <script>
        function confirmDelete(event) {
            event.preventDefault(); // Prevent the default action
            var result = confirm("Are you sure you want to delete this item?");
            if (result) {
                // User clicked OK, redirect to the URL
                event.target.submit();
            } else {
                // User clicked Cancel, do nothing
                console.log("Deletion cancelled");
            }
        }

        function closeError() {
            document.querySelector('.popup-error').style.display = 'none';
            document.querySelector('.overlay').style.display = 'none';
        }
        function confirmDeleteUrl(event, url) {
            // Prevent the default action of the link
            event.preventDefault();

            // Display a confirmation dialog
            var result = confirm("Are you sure you want to delete this item?");

            // If the user confirms, redirect to the URL
            if (result) {
                window.location.href = url;
            } else {
                // If the user cancels, do nothing
                console.log("Deletion cancelled");
            }
        }

    </script>

    <%@include file="../mainFooter.jsp" %>
    <%@include file="../../../jsAdder.jsp" %>

</body>
</html>
