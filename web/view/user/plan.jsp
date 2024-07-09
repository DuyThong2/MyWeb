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
    <body>
        <%  String redirectUrl = request.getContextPath() + "/MainController?action=";

        %>

        <%@include file="header.jsp" %>

        <div style="min-witdth:100vw; min-height:75vh;" class="bg-white mt-5">
            <div class="container bg-white py-5" style="min-width:90vw;">
                <div class="py-5">
                    <h1 class="mb-4" style="justify-content: center;color: orange">Fresh Fruits Shop</h1>
                    <div class="row g-4">
                        <div class="col-lg-12">
                            <div class="row g-4">
                                <div class="col-xl-3">
                                    <form action="<%=redirectUrl%>" method="POST" class="w-100 mx-auto d-flex">
                                        <input type="search" name="searching" class="form-control p-3" placeholder="keywords" aria-describedby="search-icon-1">
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
                                                    <img src="<%= request.getContextPath()%>/images/mealPlan/meatMP3.jpg" class="banner" alt="">
                                                    <div class="position-absolute" style="top: 50%; right: 10px; transform: translateY(-50%);">
                                                        <h3 class="text-secondary fw-bold">Fresh <br> Fruits <br> Banner</h3>
                                                    </div>
                                                </a>
                                            </div>
                                        </div>

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
