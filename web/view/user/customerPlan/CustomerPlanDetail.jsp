
<%@page import="dto.plan.MealPlan"%>
<%@page import="dto.product.Meal"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dto.plan.CustomerDayPlan"%>
<%@page import="java.util.TreeMap"%>
<%@page import="dto.plan.CustomerPlan"%>
<%@page import="java.util.List"%>
<%@page import="dto.account.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%@include file="../../../cssAdder.jsp" %>
        <style>
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
                background-color:rgb(235,235,235); 
                border-radius:10px;
                margin-bottom:10px;
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
                background-color:rgb(235,235,235);
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
    </head>
    <body>
        <%
            String loginPageUrl = request.getContextPath() + "/MainController?action=login";
            String customerDetailControllerUrl = request.getContextPath() + "/MainController?action=customerPlan";
            String mealDetailUrl = request.getContextPath() + "/MainController?action=mealDetailPage";
            String deleteDetailUrl = request.getContextPath() + "/MainController?action=customerPlan";
            String searchMealPlanUrl = request.getContextPath() + "/MainController?action=customerPlan";
            String detailMealPlanUrl = request.getContextPath() + "/MainController?action=mealPlanDetailPage";
            String addCustomerPlanUrl = request.getContextPath() + "/MainController?action=addToCustomerPlan";
            String editCustomerPlanUrl = request.getContextPath() + "/MainController?action=customerPlanUpdatePage";
            User user = (User) session.getAttribute("LoginedUser");
            if (user == null) {
                response.sendRedirect(loginPageUrl);
                return;
            }
            TreeMap<Integer, CustomerPlan> customerPlanList = (TreeMap<Integer, CustomerPlan>) request.getAttribute("customerPlanList");
            String currentWeek = request.getParameter("week");
            int weekNumber = 1;
            if (currentWeek != null) {
                weekNumber = Integer.parseInt(currentWeek);
            }
            CustomerPlan plan = customerPlanList != null ? customerPlanList.get(weekNumber) : null;
            if (customerPlanList == null) {
                response.sendRedirect(customerDetailControllerUrl);
                return;
            }
            List<MealPlan> showMealPlan = (List<MealPlan>) session.getAttribute("searchMealPlan");
        %>
        <%@include file="../header.jsp" %>

        <<div class="mt-5 py-5" style="background-color: rgb(245,245,245)">
            <div class="container bg-white" style="min-width:90vw; overflow:hidden;" >
                <div class="row w-100">
                    <h1 class="my-4 w-100 text-center" style="justify-content: center;color: #F07B07;">My Weekly Meal Plan</h1>
                </div>
                <hr class="py-1" style="border-top: 1.5px solid grey">
                <div class="row w-100　d-flex justify-content-center ">
                    <%                    String[] weeks = {"Week 1", "Week 2", "Week 3", "Week 4"};
                        for (int i = 1; i <= 4; i++) {
                            String weekClass = (weekNumber == i) ? "chosen-weekly" : "";
                            String weekClassPlanCard = (weekNumber == i) ? "chosen-week" : "";
                            CustomerPlan getPlan = customerPlanList.get(i);
                            if (getPlan != null) {
                    %>
                    <!-- Not Null CustomerPlan -->
                    <div class="col-md-3" ">
                        <div class="row d-flex justify-content-center <%=weekClassPlanCard%>">
                            <div class="plan-card row my-4 bg-white " style="position: relative;">
                                <div class="plan-card-img-container col-xl-6 p-0">
                                    <a href="<%= customerDetailControllerUrl%>&week=<%= i%>">
                                        <img src="<%= getPlan.getImgUrl()%>" alt="<%= getPlan.getName()%>" class="plan-card-img-top">
                                    </a>
                                </div>
                                <div class="col-xl-6 p-3 d-flex flex-column">
                                    <div style="height:50%;">
                                        <h5><%= getPlan.getName()%></h5>
                                        <p><%= getPlan.getType()%></p>
                                    </div>
                                    <div class="d-flex flex-column p-0 justify-content-around">
                                        <a href="<%= editCustomerPlanUrl%>&customerPlanId=<%=getPlan.getId()%>"
                                           class="plan-add-button btn btn-lg text-center d-flex justify-content-center align-items-center">
                                            <span class="p-0 m-0">EDIT</span>
                                        </a>
                                        <a  onclick="confirmDelete(event, '<%= deleteDetailUrl%>&deleteId=<%= getPlan.getId()%>&deleteWeek=<%= i%>')"
                                            class="plan-delete-button btn btn-lg btn-danger text-center text-light d-flex justify-content-center align-items-center">
                                            <span class="p-0 m-0">DELETE</span>
                                        </a>
                                    </div>  
                                </div>

                                <div class="<%= weekClass%> weekly-title"
                                     style="font-weight: bolder; position: absolute; top: -40px; left: 0; right:0; height:50px; ">
                                    <a href="<%= customerDetailControllerUrl%>&week=<%= i%>" style="text-decoration:none; color:white;"> <%= weeks[i - 1]%><a/>
                                </div>
                            </div>
                        </div>
                        <% if (weekClass.equalsIgnoreCase("chosen-weekly")) {
                        %>
                        <div class="row d-flex justify-content-center">
                            <h2 class="mb-1 w-100 text-center text-black p-3" style="justify-content: center; background-color:rgb(235,235,235);
                                border-radius: 50px 50px 0 0;
                                transform: scale(1.2);"><%=weeks[i - 1]%></h1>
                        </div>
                        <%
                            }
                        %>
                    </div>
                    <%
                    } else {
                    %>
                    <!-- Null CustomerPlan -->
                    <div class="col-md-3 d-flex justify-content-center">
                        <div class="plan-card row my-4" style="position: relative;">
                            <div class="card-body">
                                <a href="#search" class="add-button btn btn-lg mb-5">ADD TEMPLATE</a>
                                <a href="<%= addCustomerPlanUrl%>&mealPlanId=MP999&addWeek=<%= i%>" class="add-button btn btn-lg mb-2">CREATE</a>
                            </div>
                            <div class="<%= weekClass%> weekly-title"
                                 style="font-weight: bolder; position: absolute; top: -40px; left: 0; right:0; height:50px; ">
                                <%= weeks[i - 1]%>
                            </div>
                        </div>

                    </div>
                    <%
                            }
                        }
                    %>
                </div>  
                <div class="row">
                    <div class="meal-plan w-100">
                        <%
                            // Assuming daysInWeek array is predefined
                            String[] daysInWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

                            // Retrieve the CustomerPlan for the current week
                            List<CustomerDayPlan> dayPlans = new ArrayList<>();
                            if (plan != null && plan.getContainsCustomerPlan() != null) {
                                for (Object obj : plan.getContainsCustomerPlan()) {
                                    if (obj instanceof CustomerDayPlan) {
                                        dayPlans.add((CustomerDayPlan) obj);
                                        System.out.println(obj.toString());
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
                                    if (plan != null) {
                                %>
                                <!-- If there is no meal plan for the day -->   
                                <div class="card-body">
                                    <a href="<%= editCustomerPlanUrl%>&customerPlanId=<%=plan.getId()%>&week=<%=i%>" class="add-button btn btn-lg mb-2">ADD</a>
                                </div>
                                <% }
                                    }%>
                                <div class="daily-title buy-button" style="font-weight: bolder; position: absolute; top: 0; left: 0;">
                                    <%= dayName%>
                                </div>
                            </div>
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
                    <h1 class=" w-100 text-start " style="justify-content: center;color: #F07B07;">Search for Customized Meal Plans</h1>
                </div>
                <div class="col-12">
                    <form action="<%= searchMealPlanUrl%>" method="POST" class="w-100 mx-auto d-flex">
                        <input type="search" name="txtsearch" id="search" class="form-control p-3" placeholder="Search for Meal Plan" value="${sessionScope.localSearchScheduled}" aria-describedby="search-icon-1">
                        <button type="submit" id="search-icon-1" class="input-group-text p-3"><i class="fa fa-search"></i></button>
                    </form>
                </div>
                <div class="row g-4 justify-content-center"> 
                    <%
                        if (showMealPlan != null && !showMealPlan.isEmpty()) {
                            int i = 0;
                            // Iterate over the list of meal plans
                            for (MealPlan mealPlan : showMealPlan) {
                                if (i > 4) {
                                    break;
                                } else {
                                    i++;
                                }
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
                        <div class="plan-card row my-4">
                            <div class="plan-card-img-container col-xl-5 col-lg-5 p-0">
                                <a href="<%= detailMealPlanUrl%>&mealPlanId=<%= mealPlanId%>">
                                    <img src="<%= imgUrl%>" alt="<%= name%>" class="plan-card-img-top">
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

                            <div class="col-xl-2 col-lg-3 d-flex align-items-center " style="height:100%;">
                                <form action="<%= addCustomerPlanUrl%>" style="width:100%;" method="POST">
                                    <!-- Hidden Inputs -->
                                    <input type="hidden" name="mealPlanId" value="<%= mealPlanId%>">

                                    <!-- Select Input for Week -->
                                    <select class="form-control" name="addWeek" id="week" required>
                                        <option value="">Select Week</option>
                                        <option value="1">Week 1</option>
                                        <option value="2">Week 2</option>
                                        <option value="3">Week 3</option>
                                        <option value="4">Week 4</option>
                                    </select>  

                                    <!-- Submit Button -->
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
                    <!-- Message to display if no meal plans are available -->
                    <p class="h4 " style="height:20vh;">No meal plans available.</p>
                    <%
                        }
                    %> 
                </div>
            </div>

        </div>
        <%
            String error = (String) session.getAttribute("ADDMPERROR");
            if (error != null) {
        %>
        <div class="popup-error">
            <i class='error-close bx bxs-x-circle' onclick="closeError()"></i>
            <p class="error-text text-danger"><%= error%></p>
        </div>
        <div class="overlay"></div>
        <%
                session.removeAttribute("ADDMPERROR");
            }
        %>
        <script>
            function confirmDelete(event, url) {
                event.preventDefault(); // Prevent the default action
                var result = confirm("Are you sure you want to delete this item?");
                if (result) {
                    // User clicked OK, redirect to the URL
                    window.location.href = url;
                } else {
                    // User clicked Cancel, do nothing
                    console.log("Deletion cancelled");
                }
            }
            function closeError() {
                document.querySelector('.popup-error').style.display = 'none';
                document.querySelector('.overlay').style.display = 'none';
            }
        </script>
        <%@include file="../mainFooter.jsp" %>
        <%@include file="../../../jsAdder.jsp" %>

    </body>
</html>