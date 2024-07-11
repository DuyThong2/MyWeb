<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="dto.account.User"%>
<%@page import="dto.account.Staff"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Fruitables - Vegetable Website Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <c:choose>
            <c:when test="${not empty admin}">
                <%@include file="admin/adminCssAdder.jsp" %>
            </c:when>
            <c:otherwise>
                <%@include file="../cssAdder.jsp"%>
            </c:otherwise>
        </c:choose>
    </head>

    <%
        Staff staff = (Staff) session.getAttribute("admin");

        String redirectPage = "";
        if (staff != null) {
            redirectPage = request.getContextPath() + "/AMainController?action=adminmainpage";
        } else {
            redirectPage = request.getContextPath() + "/AMainController?action=mainpage";

        }


    %>

    <body>

        <!-- Spinner Start -->

        <!-- Spinner End -->


        <!-- Navbar start -->
        <c:choose>
            <c:when test="${not empty admin}">
                <%@include file="admin/AdminHeader.jsp" %>
            </c:when>
            <c:otherwise>
                <%@include file="user/header.jsp"%>
            </c:otherwise>
        </c:choose>
        <!-- Navbar End -->


        <!-- Modal Search Start -->

        <!-- Modal Search End -->


        <!-- Single Page Header start -->

        <!-- Single Page Header End -->


        <!-- 404 Start -->
        <div class="container py-5 bg-white">
            <div class="container py-5 text-center">
                <div class="row justify-content-center">
                    <div class="col-lg-6">
                        <i class="bi bi-exclamation-triangle display-1 text-secondary"></i>
                        <h1 class="display-1">Error</h1>
                        <h1 class="mb-4">Something gone wrong</h1>
                        <p class="mb-4">You make some Error! Maybe go to our home page or try to use a search?</p>
                        <p class="mb-4">${errorMessage}</p>
                        <a class="btn border-secondary rounded-pill py-3 px-5" href="<%=redirectPage%>">Go Back To Home</a>
                    </div>
                </div>
            </div>
        </div>
        <!-- 404 End -->


        <!-- Footer Start -->

        <!-- Footer End -->

        <!-- Copyright Start -->

        <!-- Copyright End -->
        <c:choose>
            <c:when test="${not empty admin}">
                <%@include file="admin/adminJs.jsp" %>
            </c:when>
            <c:otherwise>
                <%@include file="../jsAdder.jsp" %>
            </c:otherwise>
        </c:choose>


        <!-- Back to Top -->
        <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top"><i class="fa fa-arrow-up"></i></a>   



    </body>

</html>