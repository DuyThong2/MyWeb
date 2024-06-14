<%-- 
    Document   : ProductManage
    Created on : May 20, 2024, 8:45:33 AM
    Author     : Admin
--%>

<%@page import="dto.account.User"%>

<%@page import="java.nio.file.Paths"%>
<%@page import="Utility.Tool"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="style.css" rel="stylesheet">
        <title>Document</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <header>
            <!-- Your header content here -->
        </header>
        <%  
            String manageUrl = request.getContextPath() + "/AMainController?action=userManage";
            String detailUrl = request.getContextPath() + "/AMainController?action=userDetail";
            

            List<User> iList = (List<User>) session.getAttribute("userList");

            if (iList == null) {
                response.sendRedirect(manageUrl);
                return;
            }

            List<List<User>> pages = Tool.splitToPage(iList, 20);

            Object numString = session.getAttribute("numPage");
            int pageNum = 1;
            if (numString != null) {
                pageNum = (int) numString;
                if (pageNum < 1 || pageNum > pages.size()) {
                    pageNum = 1;
                }
            }
            int realPage = pageNum - 1;
            List<User> list = new ArrayList();
            if (!pages.isEmpty()){
                list = pages.get(realPage);
            }
            
            request.setAttribute("table", list);
            
            
        %>
        <div class="container">
            <div class="row">
                <!-- Table section taking 80% of the row -->
                <div id="table" class="col-md-10">
                    <!-- Table Name and Form in the same row -->
                    <div class="row align-items-center">
                        <div class="col-md-6">
                            <h1>User table:</h1>
                        </div>
                        <div class="col-md-6">
                            <!-- Optionally, add some controls here -->
                        </div>
                    </div>
                    
                    <div>
                        <table class="table table-striped mt-4">
                            <thead>
                                <tr>
                                    <th>Images</th>
                                    <th>Name</th>
                                    <th>Phone</th>
                                    <th>Email</th>
                                    <th>Address</th>
                                    <th>Option</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    for (User user : list) { 
                                %>
                                    <tr>
                                        <td>
                                            <img src="<%=request.getContextPath() %>/<%= user.getImgURL() %>" alt="<%= request.getContextPath() %>" width="100" height="100">
                                        </td>
                                        <td><%= user.getName() %></td>
                                        <td><%= user.getPhone() %></td>
                                        <td><%= user.getEmail() %></td>
                                        <td><%= user.getAddress() %></td>
                                        <td>
                                            
                                            <a href="<%=detailUrl%>&userId=<%=user.getId()%>" class="btn btn-sm btn-info">Detail</a>
                                            
                                            <%
                                            String disableUrl = manageUrl + "&status=";
                                            String status = user.getStatus();
                                            int userId = user.getId();
                                            String disableLink = "";
                                            String disableBtnClass = status.equals("active") ? "btn-danger" : "btn-success";
                                            String disableBtnText = status.equals("active") ? "Disable" : "Enable";

                                            disableLink = "<a href='" + disableUrl + (status.equals("active") ? "disable" : "active") + "&deleteUserId=" + userId + "' class='btn btn-sm " + disableBtnClass + " '>" + disableBtnText + "</a>";
                                        %>
                                        <%= disableLink %>
                                        </td>
                                    </tr>
                                <% 
                                    } 
                                %>
                            </tbody>
                        </table>
                    </div>

                    <!-- Search bar and insert buttons in the same row -->
                    <div class="row mt-4 fixed-search-bar">
                        <div class="col-md-2 d-flex align-items-center">
                            <form action="<%= manageUrl %>" method="POST" class="form-inline">
                                <input type="hidden" name="numPage" value="<%= pageNum - 1 %>">
                                <button type="submit" class="btn btn-secondary mr-2">&lt;</button>
                            </form>

                            <form action="<%= manageUrl %>" method="POST" class="form-inline">
                                <input name="numPage" type="number" value="<%= pageNum %>" class="form-control page-number" min="1" max="<%= pages.size() %>">
                                
                            </form>

                            <span class="ml-2 mr-2">/</span>
                            <span class="total-pages"><%= pages.size() %></span>

                            <form action="<%= manageUrl %>" method="POST" class="form-inline">
                                <input type="hidden" name="numPage" value="<%= pageNum + 1 %>">
                                <button type="submit" class="btn btn-secondary ml-2">&gt;</button>
                            </form>
                        </div>

                        
                        
                    </div>
                </div>

                <!-- Category section taking 20% of the row -->
                <div id="category" class="col-md-2 fixed-category">
                    <h1> Options: </h1>
                    <form action="<%= manageUrl %>" method="POST">
                        <div class="form-group">
                            <label for="searchCriteria">Search By:</label>
                            <select name="searchCriteria" id="searchCriteria" class="form-control">
                                <option value="address">Destination</option>
                                <option value="name">Name</option>
                                <option value="phone">Phone</option>
                                <option value="email">Email</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="searchValue">Search For:</label>
                            <input type="text" name="searchValue" id="searchValue" class="form-control" placeholder="Enter search term">
                        </div>
                        <input type="submit" value="Search" class="btn btn-primary mt-2">
                    </form>
                        <hr>
                        
                </div>
            </div>
        </div>

        <footer>
            <!-- Your footer content here -->
        </footer>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>