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
        </style>
    </head>
    <%

        String addToCartURL = request.getContextPath() + "/MainController?action=addToCart";
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
        request.setAttribute("packet", packet);
        request.setAttribute("meal", meal);
    %>
    <body>
        <%@include file="../header.jsp" %>
        <div style="min-witdth:100vw;" class=" mt-5 py-5" style="background-color: rgb(245,245,245)">
            <div class="container bg-white  " style="min-width:90vw;">
                <div class="container-fluid py-5">
                    <div class="row g-4 mb-5">
                        <div class="col-lg-8 col-xl-9">
                            <div class="row g-4">
                                <div class="col-lg-6">
                                    <div class="border rounded">
                                        <a href="#">
                                            <img src="${pageContext.request.contextPath}/<%= meal.getImageURL()%>" class="img-fluid rounded" alt="Image">
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
                                        <button type="button" id="buyMealBtn" class="btn border border-secondary rounded-pill px-4 py-2 mb-4 text-primary" onclick="submitForm('<%=addToCartURL%>&productId=<%= meal.getId()%>')">
                                            <i class="fa fa-shopping-bag me-2 text-primary"></i> Buy Meal
                                        </button>
                                        <c:choose>
                                            <c:when test="${not empty packet}">
                                                <button type="button" id="buyPacketBtn" class="btn border border-secondary rounded-pill px-4 py-2 mb-4 text-primary" onclick="submitForm('<%=addToCartURL%>&productId=<%= "P" + meal.getId().substring(1)%>')">
                                                    <i class="fa fa-shopping-bag me-2 text-primary"></i> Buy Packet
                                                </button>
                                            </c:when>
                                        </c:choose>
                                        <%
                                            System.out.println(addToCartURL + "&productId=" + "P" + meal.getId().substring(1));
                                        %>
                                    </form>
                                </div>
                                <div class="col-lg-12">
                                    <nav>
                                        <div class="nav nav-tabs mb-3">
                                            <button class="nav-link border-white border-bottom-0" type="button" role="tab" id="nav-mission-tab" data-bs-toggle="tab" data-bs-target="#nav-mission" aria-controls="nav-mission" aria-selected="false">Reviews</button>
                                        </div>
                                    </nav>
                                    <div class="tab-content mb-5">
                                        <div class="tab-pane active" id="nav-about" role="tabpanel" aria-labelledby="nav-about-tab">
                                            <p> <%= meal.getContent()%> </p>
                                            <div class="px-2">
                                                <div class="row g-4">
                                                    <div class="col-6">
                                                        <c:choose>
                                                            <c:when test="${not empty packet}">
                                                                <c:forEach items="${packet.getContains()}" var="entry">
                                                                    <div class="row bg-light align-items-center text-center justify-content-center py-2">
                                                                        <div class="col-6">
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
                                    <div class="position-relative">
                                        <img src="img/banner-fruits.jpg" class="img-fluid w-100 rounded" alt="">
                                        <div class="position-absolute" style="top: 50%; right: 10px; transform: translateY(-50%);">
                                            <h3 class="text-secondary fw-bold">Fresh <br> Fruits <br> Banner</h3>
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
                <% List<Meal> relatedList = dao.getCustomerMealListByCategory(meal.getCategory(), 4);
                    Collections.shuffle(relatedList);
                    for (Meal item : relatedList) {%>
                <div class="owl-carousel vegetable-carousel justify-content-center">
                    <div class="border border-primary rounded position-relative vesitable-item">
                        <div class="row no-gutters">
                            <div class="col-auto">
                                <a href="<%=detailURL%>&productId=<%=item.getId()%>"> 
                                    <img src="${pageContext.request.contextPath}/<%= item.getImageURL()%>" class="img-fluid fixed-size-img" alt="<%=item.getName()%>">
                                </a>
                            </div>
                            <div class="col">
                                <div class="p-4 pb-0 rounded-bottom">
                                    <div class="text-white bg-primary px-3 py-1 rounded position-absolute" style="top: 10px; right: 10px;"><%=item.getCategory()%></div>
                                    <h4><%=item.getName()%></h4>
                                    <p><%= item.getDescription()%></p>
                                    <div class="d-flex justify-content-between flex-lg-wrap">
                                        <% if (item.isOnSale()) {%>
                                        <p style="color: red" class="text-dark fs-5 fw-bold">$ <%=String.format("%.2f", item.getPriceAfterDiscount())%> </p>
                                        <% } else {%>
                                        <p class="text-dark fs-5 fw-bold">$ <%=String.format("%.2f", item.getPrice())%> </p>
                                        <% }%>
                                        <a href="<%=addToCartURL%>&productId=<%= item.getId()%>" class="btn border border-secondary rounded-pill px-3 py-1 mb-4 text-primary"><i class="fa fa-shopping-bag me-2 text-primary"></i> Add to cart</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <% }%>
                <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top"><i class="fa fa-arrow-up"></i></a>

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