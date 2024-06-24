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
            .card-img-top {
                height: 200px;
                object-fit: cover;
            }

            #carouselExampleSlidesOnly {
                height: 70vh;
            }

            .carousel-inner img {
                height: 70vh;
                width: 100%;
                object-fit: cover;
            }

            .card {
                height: 100%;
            }

            .card-body {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }

            .card-text {
                flex-grow: 1;
            }

            .btn-row {
                display: flex;
                justify-content: space-between;
            }

            .container{
                border: solid orange thick;
                border-radius: 10px;
            }
        </style>
    </head>

    <%

        String cartURL = request.getContextPath() + "/MainController?action=addToCart";
        String detailURL = request.getContextPath() + "/MainController?action=mealDetailPage";
        List<Meal> list = (List<Meal>) session.getAttribute("mealList");
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

        request.setAttribute("carouselItems", carouselShow);
        request.setAttribute("homeShow", homeShow);


    %>
    <body>

        <!-- Navbar -->
        <%@include file="../user/header.jsp" %>

        <!-- Carousel -->
        <div class="container">
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
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
        </div>

        <!-- Main Content -->
        <div class="container mt-5 bg-white p-4">
            <h2 style="color: orange">Featured Meals</h2>
            <div class="row">
                <!-- Meal Card 1 -->
                <%
                    for (Meal item : homeShow) {
                %>
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <a href="<%=detailURL%>&productId=<%=item.getId()%>">
                            <img src="<%=request.getContextPath() + "/" + item.getImageURL()%>" class="card-img-top" alt="<%=item.getName()%>">
                        </a>
                        <div class="card-body">
                            <h5 class="card-title"><%=item.getName()%></h5>
                            <p class="card-text"><%=item.getDescription()%></p>
                            <%if (item.isOnSale()) {%>
                            <p class="text-dark fs-5 fw-bold mb-0"><%=String.format("%.2f", item.getPriceAfterDiscount())%>$</p>
                            <p class="text-danger text-decoration-line-through"><%= item.getPrice()%>$</p>

                            <%
                            } else {%>
                            <p class="text-dark fs-5 fw-bold mb-0"><%=String.format("%.2f", item.getPrice())%>$</p>

                            <%
                                }
                            %>
                            <a href="<%=cartURL%>&productId=<%=item.getId()%>" class="btn btn-primary mb-2">Buy Now</a>
                            <a href="<%=detailURL%>&productId=<%=item.getId()%>" class="btn btn-success">Detail</a>
                        </div>
                    </div>
                </div>
                <%
                    }
                %>

                <!-- Meal Card 2 -->
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="meal-image-2.jpg" class="card-img-top" alt="...">
                        <div class="card-body">
                            <h5 class="card-title">Meal 2</h5>
                            <p class="card-text">Short description of Meal 2.</p>
                            <a href="#" class="btn btn-primary">Buy Now</a>
                            <a href="#" class="btn btn-success">Add to Cart</a>
                        </div>
                    </div>
                </div>

                <!-- Meal Card 3 -->
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <img src="meal-image-3.jpg" class="card-img-top" alt="...">
                        <div class="card-body">
                            <h5 class="card-title">Meal 3</h5>
                            <p class="card-text">Short description of Meal 3.</p>
                            <a href="#" class="btn btn-primary">Buy Now</a>
                            <a href="#" class="btn btn-success">Add to Cart</a>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <!-- Bootstrap JS and jQuery -->
        <%@include file="../../jsAdder.jsp" %>
    </body>
</html>
