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
        <%@include file="../adminCssAdder.jsp"%>
    </head>
    <body>
        <%
            String error = (String) request.getAttribute("errorMessage");
            // Retrieve the user data from the database
            UserDAO dao = new UserDAO();
            if (request.getParameter("userId") == null) {
                response.sendRedirect(request.getContextPath() + "/AMainController?action=userManagePage");
                return;
            }
            int id = Integer.parseInt(request.getParameter("userId"));
            User user = dao.getUserById(id);
        %>
        <%@include file="../AdminHeader.jsp" %>
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
        <div class="container">
            <div class="row">
                <!-- Left side: Display old information -->
                <div class="col-md-6">
                    <h3>Old Information</h3>
                    <c:set var="item" value="<%= user%>" />
                    <div class="card">
                        <div class="card-body">
                            <p><strong>ID:</strong> ${item.getId()}</p>
                            <p><strong>Name:</strong> ${item.getName()}</p>
                            <p><strong>Email:</strong> ${item.getEmail()}</p>
                            <p><strong>Phone:</strong> ${item.getPhone()}</p>
                            <p><strong>Address:</strong> ${item.getAddress()}</p>
                            <p><strong>Status:</strong> ${item.getStatus()}</p>
                            <p><strong>Image:</strong></p>
                            <img src="${pageContext.request.contextPath}/${item.getImgURL()}" alt="Old Image" class="img-fluid">
                        </div>
                    </div>
                </div>

                <!-- Right side: Form to input new values -->
                <div class="col-md-6">
                    <h4>Update User Details</h4>
                    <form id="userForm" action="<%= request.getContextPath() + "/AMainController?action=userUpdate"%>" method="post" enctype="multipart/form-data">
                        <input type="hidden" class="form-control" id="userId" name="id" value="<%= user.getId()%>" required="">
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
                        <div class="text-center">
                            <button type="submit" class="btn btn-primary">Update User</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <%
            request.removeAttribute("errorMessage");
            
        %>
        <script>
            function closeMessage() {
                const message = document.querySelector('.popup-error');
                message.style.top = "-100%";
                const overlay = document.querySelector('.overlay');
                overlay.classList.remove('overlay');
            }

        </script>

        <!-- Bootstrap JS and dependencies -->
        <%@include file="../adminJs.jsp" %>
    </body>
</html>
