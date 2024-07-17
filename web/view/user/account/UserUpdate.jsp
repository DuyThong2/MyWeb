<%@page import="dto.account.User"%>
<%@page import="dao.account.UserDAO"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Update User Information</title>
        <!-- Bootstrap CSS -->
        <%@include file="../../../cssAdder.jsp" %>
    </head>
    <body>
        <%@include file="../../user/header.jsp" %>

        <%            // Retrieve the user data from the database
            String loginURL = request.getContextPath() + "/MainController?action=login";
            User user = (User) session.getAttribute("LoginedUser");
            if (user == null) {
                response.sendRedirect(loginURL);
                return;
            }
            String error = (String) request.getAttribute("errorMessage");


        %>

        <% if (error != null) {%>
        <div class="popup-error">
            <p class="error-close" onclick="closeMessage()">x<p>
            <p class='error-text'>
                <%= error%>
            </p>
        </div>
        <div class="overlay"></div>

        <% }
        %>
        <c:set var="item" value="<%= user%>" />

        <div class="container  p-4" style="min-width:100vw; background-color:rgb(235,235,235);">
            <div class="container p-4 bg-white" style="min-width:85vw">          
                <div class="row bg-white">
                    <!-- Left side: Display old information -->

                    <div class="row col-12">
                        <h3 class="w-100 text-center text-success">Update User Details</h4>
                    </div>

                    <!-- Right side: Form to input new values -->
                    <div class="row w-100">
                        <div class="col d-flex justify-content-center" >
                            <form class="w-75" id="userForm" action="<%= request.getContextPath() + "/MainController?action=userUpdate"%>" method="post" enctype="multipart/form-data">
                                <input type="hidden" class="form-control" id="userId" name="id" value="<%= user.getId()%>">
                                <div class="form-group">
                                    <label for="userName">Name</label>
                                    <input type="text" class="form-control" id="userName" name="name" value="${item.getName()}" placeholder="Enter user name" required="">
                                </div>
                                <div class="form-group">
                                    <label for="userEmail">Email</label>
                                    <input type="email" class="form-control" id="userEmail" name="email" value="${item.getEmail()}" placeholder="Enter user email" required="">
                                </div>
                                <div class="form-group">
                                    <label for="userPhone">Phone</label>
                                    <input type="text" class="form-control" id="userPhone" name="phone" value="${item.getPhone()}" placeholder="Enter user phone" required="">
                                </div>


                                <div class="form-group">
                                    <label for="userCity">City</label>
                                    <input type="text" class="form-control" id="userCity"  name="city" value="${item.address.city}" placeholder="Enter city" required="">
                                </div>
                                <div class="form-group">
                                    <label for="userDistrict">District</label>
                                    <input type="text" class="form-control" id="userDistrict" value="${item.address.district}" name="district" value="" placeholder="Enter district" required="">
                                </div>
                                <div class="form-group">
                                    <label for="userWard">Ward</label>
                                    <input type="text" class="form-control" id="userWard" name="ward" value="${item.address.ward}" placeholder="Enter ward" required="">
                                </div>
                                <div class="form-group">
                                    <label for="userStreet">Street</label>
                                    <input type="text" class="form-control" id="userStreet" name="street" value="${item.address.street}" placeholder="Enter street" required="">
                                </div>
                                <div class="form-group row">
                                    <label for="imgURL" class="col-sm-2 col-form-label">Image File</label>
                                    <div class="col-sm-10">
                                        <input type="file" class="form-control-file" id="imgURL" name="imgURL">
                                    </div>
                                </div>
                                <div class="text-center w-100 justify-content-center d-flex align-items-center">
                                    <button type="submit" class="btn btn-lg btn-primary">Update User</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="../mainFooter.jsp" %>
        <%request.removeAttribute("errorMessage");%>
        <script>
            function closeMessage() {
                const message = document.querySelector('.popup-error');
                message.style.top = "-100%";
                const overlay = document.querySelector('.overlay');
                overlay.classList.remove('overlay');
            }
        </script>
        <!-- Bootstrap JS and dependencies -->
        <%@include file="../../../jsAdder.jsp" %>
    </body>
</html>
