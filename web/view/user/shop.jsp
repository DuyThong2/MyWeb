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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Bootstrap 5 -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css">

        <title>Shop</title>

        <style>
            .vh-100 {
                height: 100vh !important;
            }
            .btn-search {
                padding: 0.75rem 1rem;
            }
            .btn-md-square {
                width: 40px;
                height: 40px;
                padding: 0;
            }
            .navbar-brand h1 {
                margin: 0;
            }
            .rounded-pill {
                border-radius: 50rem !important;
            }
            .list-unstyled {
                padding-left: 0;
                list-style: none;
            }
            .fruite-item img {
                height: 250px;
                object-fit: cover;
            }
            .back-to-top {
                position: fixed;
                bottom: 20px;
                right: 20px;
                display: none;
            }
        </style>
    </head>

    <%
        String addToCartURL = request.getContextPath() + "/MainController?action=addToCart";
        String redirectUrl = request.getContextPath() + "/MainController?action=shop";
        String cartURL = request.getContextPath() + "/MainController?action=cartDisplayPage";
        String detailURL = request.getContextPath() + "/MainController?action=mealDetailPage";

        // only for testing without user
        if (session.getAttribute("mealList") == null) {
            UserDAO userDao = new UserDAO();

            User user = userDao.getUserById(1);
            session.setAttribute("user", user);
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

        <!-- Navbar End -->

        <!-- Modal Search Start -->

        <!-- Modal Search End -->

        <!-- Single Page Header start -->

        <!-- Single Page Header End -->

        <!-- Fruits Shop Start-->
        <div class="container-fluid py-5">
            <div class="container py-5">
                <h1 class="mb-4">Fresh Fruits Shop</h1>
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
                                        <div class="mb-3">
                                            <h4>Sort Option</h4>
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
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="cate" id="expired" value="expired">
                                                    <label class="form-check-label" for="expired">Expired</label>
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

                                    <!-- show expensive and beautiful meal -->
                                    <div class="col-lg-12">
                                        <h4 class="mb-3">Featured products</h4>
                                        <div class="d-flex align-items-center justify-content-start">
                                            <div class="rounded me-4" style="width: 100px; height: 100px;">
                                                <img src="img/featur-1.jpg" class="img-fluid rounded" alt="">
                                            </div>
                                            <div>
                                                <h6 class="mb-2">Big Banana</h6>
                                                <div class="d-flex mb-2">
                                                    <i class="fa fa-star text-secondary"></i>
                                                    <i class="fa fa-star text-secondary"></i>
                                                    <i class="fa fa-star text-secondary"></i>
                                                    <i class="fa fa-star text-secondary"></i>
                                                    <i class="fa fa-star"></i>
                                                </div>
                                                <div class="d-flex mb-2">
                                                    <h5 class="fw-bold me-2">2.99 $</h5>
                                                    <h5 class="text-danger text-decoration-line-through">4.11 $</h5>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="d-flex justify-content-center my-4">
                                            <a href="#" class="btn border border-secondary px-4 py-3 rounded-pill text-primary w-100">View More</a>
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
                                </div>
                            </div>
                            <div class="col-lg-9">
                                <div class="row g-4 justify-content-center">
                                    <!-- for loop product -->
                                    <%
                                        for (Meal item : list) {
                                    %>
                                    <div class="col-md-6 col-lg-6 col-xl-4">
                                        <div class="rounded position-relative fruite-item">

                                            <div class="fruite-img">
                                                <a href="<%=detailURL%>">
                                                    <img src="${pageContext.request.contextPath}/<%= item.getImageURL()%>" class="img-fluid w-100 rounded-top" alt="meal Img">
                                                </a>
                                            </div>

                                            <div class="text-white bg-secondary px-3 py-1 rounded position-absolute" style="top: 10px; left: 10px;">Meal</div>
                                            <div class="p-4 border border-secondary border-top-0 rounded-bottom">
                                                <h4><%= item.getName()%></h4>
                                                <p><%= item.getDescription()%></p>
                                                <div class="d-flex justify-content-between flex-lg-wrap">

                                                    <%if (item.isOnSale()) {%>
                                                    <p class="text-dark fs-5 fw-bold mb-0"><%=String.format("%.2f", item.getPriceAfterDiscount())%>$</p>
                                                    <p class="text-danger text-decoration-line-through"><%= item.getPrice()%>$</p>

                                                    <%
                                                    } else {%>
                                                    <p class="text-dark fs-5 fw-bold mb-0"><%=String.format("%.2f", item.getPrice())%>$</p>

                                                    <%
                                                        }
                                                    %>

                                                    <a href="<%= addToCartURL%>&productId=<%= item.getId()%>" class="btn border border-secondary rounded-pill px-3 text-primary"><i class="fa fa-shopping-bag me-2 text-primary"></i> Add to cart</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <%
                                        }
                                    %>

                                </div>
                            </div>
                            <!-- page changing -->
                            <div class="col-md-2 d-flex align-items-center">
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
                                    <input type="hidden" name="numPage" value="<%= pageNum + 1%>">
                                    <button type="submit" class="btn btn-secondary ml-2">&gt;</button>
                                </form>
                            </div>
                            <a href="<%= cartURL%>">here</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Fruits Shop End-->

        <!-- Footer End -->

        <!-- JavaScript Libraries -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
