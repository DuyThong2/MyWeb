<%@page import="java.util.Collections"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.List"%>
<%@page import="dao.discount.DiscountDAO"%>
<%@page import="dto.product.Meal"%>
<%@page import="dao.product.MealDAO"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="dto.product.IngredientPacket"%>
<%@page import="dto.product.Ingredient"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Meal Details</title>
        <!-- Bootstrap CSS -->
        <%@include file="../../../cssAdder.jsp" %>
        <style>
            .body{
                position:relative;
            }
            .container {

            }
            .quantity {
                display: flex;
                justify-content: center;
                align-items: center;
            }
            .quantity input {
                text-align: center;
                border: 0;
            }

            .fixed-size-img {
                width: 150px;
                height: 150px;
                object-fit: cover;
            }
            footer {
                position: relative;
                z-index: 10;
                clear: both;
            }
            .add-to-cart-button {
                background-color: #F07B07; /* Primary button color */
                border: none;
                color: white;
                padding: 10px 20px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1.4rem;
                transition: background 0.3s ease;
            }

            .add-to-cart-button:hover {
                background: linear-gradient(45deg, #F07B07, #FFA500);
            }

            .buy-packet-button {
                background-color:  #00C853; 
                border: none;
                color: white;
                padding: 10px 20px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1.4rem;
                transition: background 0.3s ease;
            }

            .buy-packet-button:hover {
                background: linear-gradient(to right, #00C853, #4CAF50, #00C853);
                color: transparent;
                background-clip: text;
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                box-shadow: 0 0 5px #00C853;
            }
            .img-meal{
                height:100%;
                width:100%;
                object-fit:cover;
            }
            .img-meal-container{
                width: 100%; /* Adjust the width to be responsive */
                aspect-ratio: 3/ 4; /* Maintain a 16:9 aspect ratio */
                min-height: 350px;
                max-height: 500px; /* Set a maximum height */
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
            @media(max-width:1500px){
                .card{
                    height:475px;
                }
                .card-img-top,.card-img-container{
                    height: 185px;
                }
            }
            @media (max-width:1300px){
                .card{
                    height:450px;
                }
                .card-img-top,.card-img-container {
                    height: 170px;
                }
            }
            @media(max-width:1100px){
                .card{
                    height:425px;
                }
                .card-img-top,.card-img-container {
                    height: 160px;
                }
            }
        </style>
    </head>
    <%

        String addToCartURL = request.getContextPath() + "/MainController?action=vailon";
        String detailURL = request.getContextPath() + "/MainController?action=mealDetailPage";
        String shopURL = request.getContextPath() + "/MainController?action=shop";
        String mealId = request.getParameter("productId");
        if (mealId == null) {
            response.sendRedirect(request.getContextPath() + "/MainController?action=shop");
            return;
        }   
        MealDAO dao = new MealDAO();
        Meal meal = dao.getMealFullDetailFromId(mealId);
        IngredientPacket packet = meal.getPacket();
        if (packet == null || packet.getContains().isEmpty()){
            packet = null;
        }
        request.setAttribute("packet", packet);
        request.setAttribute("meal", meal);
    %>
    <body>
        <%@include file="../header.jsp" %>
        <div class=" mt-5 py-5" style="background-color: rgb(245,245,245);min-witdth:100vw;">
            <div class="container bg-white  " style="min-width:90vw;">
                <div class="container-fluid py-5">
                    <div class="row g-4 mb-5">
                        <div class="col-lg-8 col-xl-9">
                            <div class="row g-4">
                                <div class="col-lg-6">
                                    <div class="img-meal-container">
                                        <a href="#">
                                            <img src="${pageContext.request.contextPath}/<%= meal.getImageURL()%>" class="img-meal" alt="Image">
                                        </a>
                                    </div>
                                </div>
                                <div class="col-lg-6">
                                    <form id="orderForm" action="#" method="POST">
                                        <h1 style="color: orange" class="fw-bold mb-3"><%=meal.getName()%></h1>
                                        <p class="mb-3"><strong>Category:</strong> <%=meal.getCategory()%></p>
                                        <h5 class="fw-bold mb-3">Meal : <%=String.format("%.2f", meal.getPriceAfterDiscount())%> $</h5>
                                        <c:choose>
                                            <c:when test="${not empty packet}">
                                                <h5 class="fw-bold mb-3">Packet : <%=String.format("%.2f", meal.getPacket().getPriceAfterDiscount())%> $</h5>
                                            </c:when>
                                        </c:choose>
                                        <p class="mb-4"><%=meal.getDescription()%></p>
                                        <div class="input-group quantity mb-5" style="width: 100px;">
                                            <div class="input-group-btn">
                                                <button type="button" class="btn btn-sm btn-minus rounded-circle bg-light border" onclick="updateQuantity(-1)">
                                                    <i class="fa fa-minus"></i>
                                                </button>
                                            </div>
                                            <input type="number" name="quantity" value="1" id="quantityInput" class="form-control form-control-sm text-center border-0" min="1" max="20" oninput="validateQuantity()">
                                            <div class="input-group-btn">
                                                <button type="button" class="btn btn-sm btn-plus rounded-circle bg-light border" onclick="updateQuantity(1)">
                                                    <i class="fa fa-plus"></i>
                                                </button>
                                            </div>
                                        </div>
                                        <div class="w-100 g-4">
                                            <button type="button" id="buyMealBtn" class="add-to-cart-button btn btn-lg mr-2 " onclick="submitForm('<%=addToCartURL%>&productId=<%= meal.getId()%>')">
                                                <i class="fa fa-shopping-bag me-2 text-center"></i> Buy Meal
                                            </button>
                                            <c:choose>
                                                <c:when test="${not empty packet}">
                                                    <button type="button" id="buyPacketBtn" class="btn btn-lg buy-packet-button" onclick="submitForm('<%=addToCartURL%>&productId=<%= "P" + meal.getId().substring(1)%>')">
                                                        <i class="fa fa-shopping-bag me-2 text-center"></i> Buy Packet
                                                    </button>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                        <%
                                            System.out.println(addToCartURL + "&productId=" + "P" + meal.getId().substring(1));
                                        %>
                                    </form>
                                </div>
                                <div class="col-lg-12">

                                    <div class="tab-content mb-5">
                                        <div class="tab-pane active" id="nav-about" role="tabpanel" aria-labelledby="nav-about-tab">
                                            <hr class="py-1" style="border-top: 1.5px solid grey">
                                            <div>
                                                <h3 style="color: orange" class="fw-bold mb-3">Meal Recipe </h3>
                                                <p style="font-size:1.3em;"><%= meal.getContent()%></p>
                                            </div>
                                            <hr class="py-1" style="border-top: 1.5px solid grey">
                                            <h3 style="color: green" class="fw-bold mb-3">Ingredient List</h3>
                                            <div class="px-2">

                                                <div class="row g-4 d-flex justify-content-center">
                                                    <div class="col-10">
                                                        <c:choose>

                                                            <c:when test="${not empty packet}">
                                                                <div class="row bg-secondary align-items-center text-center justify-content-center py-2">
                                                                    <div class="col-6" style="border-right: 1px solid black;">
                                                                        <h4 class="mb-0 text-light">Ingredients</h4>
                                                                    </div>
                                                                    <div class="col-6">
                                                                        <h4 class="mb-0 text-light">Units</h4>
                                                                    </div>
                                                                </div>
                                                                <c:forEach items="${packet.getContains()}" var="entry">
                                                                    <div class="row bg-light align-items-center text-center justify-content-center py-2">
                                                                        <div class="col-6" style="border-right: 1px solid black;">
                                                                            <p class="mb-0">${entry.key.getName()}</p>
                                                                        </div>
                                                                        <div class="col-6">
                                                                            <p class="mb-0">${entry.value} ${entry.key.getUnit()}</p>
                                                                        </div>
                                                                    </div>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div><h2>recipe classify</h2></div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-xl-3">
                            <div class="row g-4 fruite">
                                <div class="col-lg-12">
                                    <form action="<%=shopURL%>" method="POST" class="input-group w-100 mx-auto d-flex mb-4">
                                        <input type="search" name="searching" class="form-control p-3" placeholder="keywords" aria-describedby="search-icon-1">
                                        <button type="submit" id="search-icon-1" class="input-group-text p-3"><i class="fa fa-search"></i></button>
                                    </form>
                                    <div class="mb-4">
                                        <div class="card-header">
                                            <h4>Sort Option</h4>
                                        </div>
                                        <div class="card-body">
                                            <form action="<%=shopURL%>" method="POST">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="cate" id="category" value="category">
                                                    <label class="form-check-label" for="category">Category</label>
                                                </div>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="cate" id="price" value="price">
                                                    <label class="form-check-label" for="price">Price</label>
                                                </div>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="cate" id="isOnSale" value="isOnSale">
                                                    <label class="form-check-label" for="isOnSale">Is On Sale</label>
                                                </div>
                                                <hr>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="sort" id="max" value="max">
                                                    <label class="form-check-label" for="max">Show max (have discount)</label>
                                                </div>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="sort" id="min" value="min">
                                                    <label class="form-check-label" for="min">Show min (no discount)</label>
                                                </div>
                                                <button type="submit" class="btn btn-primary mt-3">Apply</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-lg-12">
                                    <div class="d-flex justify-content-center my-4">
                                        <a href="<%=ShopURL%>" class="btn border border-secondary px-4 py-3 rounded-pill text-primary w-100">View More</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container bg-white mt-5">
                <h1 class="fw-bold mb-0">Related products</h1>
                <div class="row g-4 justify-content-center">
                    <% List<Meal> relatedList = dao.getCustomerMealListByCategory(meal.getCategory(), 4);
                        Collections.shuffle(relatedList);
                        for (Meal item : relatedList) {%>
                    <div class="col-md-6 col-lg-6 col-xl-4">
                        <div class="card mb-5">
                            <a href="<%=detailURL%>&productId=<%=item.getId()%>" class="card-img-container">
                                <img src="${pageContext.request.contextPath}/<%= item.getImageURL()%>" class="card-img-top" alt="meal Img">
                            </a>
                            <div class="card-body">
                                <h5 class="card-title"><%= item.getName()%></h5>
                                <p class="card-text"><%= item.getDescription()%></p>
                                <div class="d-flex justify-content-between flex-lg-wrap">
                                    <% if (item.isOnSale()) {%>
                                    <p class="text-danger fs-5 fw-bold mb-0" style="font-size:1.4rem;"><%=String.format("%.2f", item.getPriceAfterDiscount())%>$</p>
                                    <p class="text-dark text-decoration-line-through" style="text-decoration-line: line-through;"><%= item.getPrice()%>$</p>
                                    <% } else {%>
                                    <p class="text-success fs-5 fw-bold mb-3" style="font-size:1.4rem;"><%=String.format("%.2f", item.getPrice())%>$</p>
                                    <% }%>
                                </div>
                                <a href="<%= addToCartURL%>&productId=<%= item.getId()%>" class="add-to-cart-button btn btn-lg mb-2">ADD TO CART</a>
                            </div>
                        </div>
                    </div>

                    <% }%>
                </div>
            </div>
        </div> 
    </div> 
    <footer>
        <%@include file="../mainFooter.jsp" %>
    </footer>
    <%@include file="../../../jsAdder.jsp" %>
    <script>
        function submitForm(actionURL) {
            const form = document.getElementById('orderForm');
            form.action = actionURL;
            form.submit();
        }
        function validateQuantity() {
            const quantityInput = document.getElementById('quantityInput');
            if (quantityInput.value < 1) {
                quantityInput.value = 1;
            } else if (quantityInput.value > 20) {
                quantityInput.value = 20; // Ensure it doesn't exceed the maximum
            }
        }
        document.addEventListener('DOMContentLoaded', function () {
            document.querySelectorAll('.btn-minus').forEach(function (button) {
                button.addEventListener('click', function () {
                    const input = button.parentElement.nextElementSibling;
                    let value = parseInt(input.value);
                    if (value > 1) {
                        input.value = value - 1;
                    }
                });
            });
            document.querySelectorAll('.btn-plus').forEach(function (button) {
                button.addEventListener('click', function () {
                    const input = button.parentElement.previousElementSibling;
                    let value = parseInt(input.value);
                    input.value = value + 1;
                });
            });
        });
    </script>
</body>
</html>