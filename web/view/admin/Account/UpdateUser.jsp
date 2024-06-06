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
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <%
        // Retrieve the user data from the database
        UserDAO dao = new UserDAO();
        if (request.getParameter("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/AMainController?action=userManagePage");
            return;
        }
        int id = Integer.parseInt(request.getParameter("userId"));
        User user = dao.getUserWithOrders(id);
    %>
    <div class="container mt-5">
        <div class="row">
            <!-- Left side: Display old information -->
            <div class="col-md-6">
                <h3>Old Information</h3>
                <c:set var="item" value="<%= user %>" />
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
                <form id="userForm" action="<%= request.getContextPath() + "/AMainController?action=userUpdate" %>" method="post" enctype="multipart/form-data">
                    <input type="hidden" class="form-control" id="userId" name="id" value="<%= user.getId() %>">
                    <div class="form-group">
                        <label for="userName">Name</label>
                        <input type="text" class="form-control" id="userName" name="name" value="<%= user.getName() %>" placeholder="Enter user name">
                    </div>
                    <div class="form-group">
                        <label for="userEmail">Email</label>
                        <input type="email" class="form-control" id="userEmail" name="email" value="<%= user.getEmail() %>" placeholder="Enter user email">
                    </div>
                    <div class="form-group">
                        <label for="userPhone">Phone</label>
                        <input type="text" class="form-control" id="userPhone" name="phone" value="<%= user.getPhone() %>" placeholder="Enter user phone">
                    </div>
                    <div class="form-group">
                        <label for="userAddress">Address</label>
                        <input type="text" class="form-control" id="userAddress" name="address" value="<%= user.getAddress() %>" placeholder="Enter user address">
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

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
