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

            .add-to-cart-button {
                background-color: #F07B07; /* Primary button color */
                border: none;
                color: white;
                padding: 10px 20px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 1.3rem;
                transition: background 0.3s ease;
            }

            .add-to-cart-button:hover {
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
                font-size: 1.3rem;
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
            .sort-section{
                width:100%;
            }

            .banner{
                opacity:0.8;
                border-radius:20px;
                width:100%;
                height:100%;
                object-fit:cover;
                z-index:0;
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
        String redirectUrl = request.getContextPath() + "/MainController?action=shop";
        String cartURL = request.getContextPath() + "/MainController?action=cartDisplayPage";
        String detailURL = request.getContextPath() + "/MainController?action=mealDetailPage";

        // only for testing without user
        if (session.getAttribute("mealList") == null) {
            response.sendRedirect(redirectUrl);
            return;
        }

        List<Meal> mList = (List<Meal>) session.getAttribute("mealList");
        List<List<Meal>> pages = Tool.splitToPage(mList, 12);

        Object numString = session.getAttribute("numPage");
        int pageNum = 1;
        if (numString != null) {
            pageNum = (int) numString;
            if (pageNum < 1 || pageNum > pages.size()) {
                pageNum = 1;
            }
        }
        int realPage = pageNum - 1;
        List<Meal> list = new ArrayList<>();
        if (!pages.isEmpty()) {
            list = pages.get(realPage);
        }

    %>

    <body>
        <!-- Spinner Start -->

        <!-- Spinner End -->

        <!-- Navbar start -->
        <%@include file="header.jsp" %>

        <!-- Navbar End -->

        <!-- Modal Search Start -->

        <!-- Modal Search End -->

        <!-- Single Page Header start -->

        <!-- Single Page Header End -->

        <!-- Fruits Shop Start-->
        <div style="min-witdth:100vw;" class="bg-white mt-5">
            <div class="container bg-white py-5" style="min-width:90vw;">
                <div class="py-5">
                    <h1 class="mb-4" style="justify-content: center;color: orange">Shop:</h1>
                    <div class="row g-4">
                        <div class="col-lg-12">
                            <div class="row g-4">
                                <div class="col-xl-3">
                                    <form action="<%=redirectUrl%>" method="POST" class="w-100 mx-auto d-flex">
                                        <input type="search" name="searching" class="form-control p-3" placeholder="Search For Meal" aria-describedby="search-icon-1">
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
                                <div class="col-lg-9">
                                    <div class="row g-4 justify-content-center">
                                        <!-- for loop product -->
                                        <%
                                            for (Meal item : list) {
                                        %>
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
                                                    <a href="<%= detailURL%>&productId=<%= item.getId()%>" class="detail-button btn btn-lg">DETAIL</a>
                                                </div>
                                            </div>
                                        </div>
                                        <%
                                            }
                                        %>
                                    </div>
                                    <!-- page changing -->
                                    <div class="col-md-12 d-flex align-items-center justify-content-center">
                                        <form action="<%= redirectUrl%>" method="POST" class="form-inline">
                                            <input type="hidden" name="numPage" value="<%= pageNum - 1%>">
                                            <button type="submit" class="btn btn-secondary mr-2">&lt;</button>
                                        </form>

                                        <form action="<%= redirectUrl%>" method="POST" class="form-inline">
                                            <input  name="numPage" type="number" value="<%= pageNum%>" class="form-control page-number"  min="1" max="<%= pages.size()%>">
                                        </form>

                                        <span class="ml-2 mr-2">/</span>
                                        <span class="total-pages"><%= pages.size()%></span>

                                        <form action="<%= redirectUrl%>" method="POST" class="form-inline">
                                            <input type="hidden" name="numPage" value="<%= Math.min(pageNum + 1, pages.size())%>">
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

        <!-- Fruits Shop End-->

        <!-- Footer End -->

        <!-- JavaScript Libraries -->
        <%@include file="mainFooter.jsp" %>

        <%@include file="../../jsAdder.jsp" %>
    </body>

</html>
