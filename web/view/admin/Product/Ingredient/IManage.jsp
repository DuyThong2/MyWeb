<%-- 
    Document   : ProductManage
    Created on : May 20, 2024, 8:45:33 AM
    Author     : Admin
--%>

<%@page import="dto.product.Ingredient"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="Utility.Tool"%>
<%@page import="java.util.ArrayList"%>

<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <title>Ingredient Manage</title>
        <%@include file="../../adminCssAdder.jsp" %>
    </head>
    <body>
        <header>
            <!-- Your header content here -->
        </header>
        <%  
            String error = (String) request.getAttribute("errorMessage");
            String manageUrl = request.getContextPath() + "/AMainController?action=ingredientManage";
            String updateUrl = request.getContextPath()+ "/AMainController?action=IngredientUpdatePage";
            String insertUrl = request.getContextPath() + "/AMainController?action=IngredientInsertPage";
            
            List<Ingredient> iList = (List<Ingredient>) session.getAttribute("ingredientList");

            if (iList == null) {

                response.sendRedirect(manageUrl);
                return;
            }
            
            
            
            List<List<Ingredient>> pages = Tool.splitToPage(iList, 10);;

            Object numString = session.getAttribute("numPage");
            int pageNum = 1;
            if (numString != null) {
                pageNum = (int) numString;
                if (pageNum < 1 || pageNum > pages.size()) {
                    pageNum = 1;
                }
            }
            int realPage = pageNum - 1;
            List<Ingredient> list = new ArrayList();
            if (!pages.isEmpty()){
                list = pages.get(realPage);
            }

            request.setAttribute("table", list);


        %>
        <%@include file="../../AdminHeader.jsp" %>
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
        <div class="container" >
            <div class="row">
                <!-- Table section taking 80% of the row -->
                <div id="table" class="col-md-10">
                    <!-- Table Name and Form in the same row -->
                    <div class="row align-items-center">
                        <div class="col-md-6">
                            <h1>Ingredient</h1>
                        </div>
                        <div class="col-md-6">

                        </div>
                    </div>
                    
                    <div>
                        <table class="table table-striped mt-4">
                            <thead>
                                <tr>
                                    <th>Images</th>
                                    <th>Id</th>
                                    <th>Ingredient Name</th>
                                    <th>Price</th>
                                    <th>Unit</th>
                                    <th>Option</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${table}" var="item">
                                    <tr>    
                                        <td>

                                            <img src="${pageContext.request.contextPath}/${item.getImgURL()}" alt="${request.contextPath}" width="100" height="100">
                                        </td>

                                        <td>${item.getId()}</td>
                                        <td>${item.getName()}</td>
                                        <td>${item.getPrice()}</td>
                                        <td>${item.getUnit()}</td>
                                        <td>
                                            
                                            <a href="<%=updateUrl%>&id=${item.getId()}" class="btn btn-sm btn-warning">Edit</a>
                                        </td>
                                    </tr>
                                    
                                    
                                </c:forEach>

                            </tbody>
                        </table>
                    </div>

                    <!-- Search bar and insert buttons in the same row -->
                    <div class="row mt-4 fixed-search-bar">
                        <div class="col-md-2 d-flex align-items-center">
                            <form action="<%=manageUrl%>" method="POST" class="form-inline">
                                <input type="hidden" name="numPage" value="<%=pageNum - 1%>">
                                <button type="submit" class="btn btn-secondary mr-2">&lt;</button>
                            </form>

                            <form action="<%=manageUrl%>" method="POST" class="form-inline">
                                <input name="numPage" type="number" value="<%=pageNum%>" class="form-control page-number"  min="1" max="<%=pages.size()%>">
                                
                            </form>

                            <span class="ml-2 ">/</span>
                            <span class="total-pages"><%=pages.size()%></span>

                            <form action="<%=manageUrl%>" method="POST" class="form-inline">
                                <input type="hidden" name="numPage" value="<%=pageNum + 1%>">
                                <button type="submit" class="btn btn-secondary ml-2">&gt;</button>
                            </form>
                        </div>
                                
                        <div class="col-md-6 ml-2">
                            <form action="<%=manageUrl%>" method="POST" class="form-inline w-100">
                                <input name="searching" type="text" class="form-control flex-grow-1" placeholder="Search for name">
                                <input type="submit" value="Search" class="btn btn-primary ml-2">
                            </form>
                        </div>
                        <div class="col-md-4 d-flex justify-content-end">

                            <button class="btn btn-secondary w-100" onclick="location.href='<%=insertUrl%>'">Insert Ingredient</button>

                        </div>
                    </div>
                </div>

                <!-- Category section taking 20% of the row -->
                <div id="category" class="col-md-2 fixed-category">
                    <form action="<%=manageUrl%>" method="POST">
                        <div class="form-check">
                            <input name="sort" type="radio" id="category-checkbox" value="max" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Show max</label>
                            <input name="sort" type="radio" id="category-checkbox" value="min" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Show min</label>

                            <hr>
                            <input name="cate" type="radio" id="category-checkbox" value="price" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Price</label>
                            <br>

                            <input name="cate" type="radio" id="category-checkbox" value="id" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">ID</label>
                            <br>

                            <input name="cate" type="radio" id="category-checkbox" value="unit" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Unit</label>
                            <br>



                        </div>
                        <input type="submit" value="Sort" class="btn btn-primary mt-2">
                    </form>
                </div>
            </div>
        </div>

        <footer>
            <!-- Your footer content here -->
        </footer>

                    <%@include file="../../adminJs.jsp" %>
    </body>
</html>
