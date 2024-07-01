<%@ page import="Utility.Tool" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.plan.MealPlan" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            String switchPageUrl = request.getContextPath() + "/AMainController?action=MealPlan";
            String searchPageUrl = request.getContextPath() + "/AMainController";
            List<MealPlan> mealPlanList = (ArrayList<MealPlan>) request.getAttribute("MealPlanList");
            ArrayList<MealPlan> currentList = new ArrayList<>();
            List<List<MealPlan>> paginationList = Tool.splitToPage(mealPlanList, 4);
            Integer numPageInt = (Integer) request.getAttribute("NumPage");
            String numPage = numPageInt != null ? numPageInt.toString() : null;
            int currentNumPage = 1;
            if (numPage != null) {
                currentNumPage = Integer.parseInt(numPage);
                if (currentNumPage < 1 || currentNumPage > paginationList.size()) {
                    currentNumPage = 1;
                }
            }
            if (paginationList != null && !paginationList.isEmpty()) {
                currentList = (ArrayList<MealPlan>) paginationList.get(currentNumPage - 1);
            }

            session.setAttribute("currentList", mealPlanList);
        %>

        <div class="container">
            <div class="row mt-5 mb-5">
                <h1 class="col-md-12 text-center text-success">Meal Plan Manage</h1>
            </div>
            <div class="row meal-plan-btn-container mt-5 mb-5">
                <div class="col-md-12 d-flex justify-content-center">
                    <a class="btn btn-danger" href="#"><span class="h4">Insert Meal Plan</span></a>
                </div>
            </div>
            <hr>
            <div class="row">
                <h2 class="text-info">Meal Plan Table</h2>
            </div>
            <hr>
            <div class="row search bar">
                <form action="<%=searchPageUrl%>" class="container" method="POST">
                    <div class="row form-group form-inline">
                        <label for="search-bar" class="col-md-3" style=" font-size:1.6rem;">Search Name</label>
                        <input type="text" name="txtsearch" id="search-bar" class="form-control col-md-7" value="${sessionScope.localSearch}" placeholder="">
                        <input type="submit" name="action" value="MealPlan" class="btn btn-primary col-md-1">
                    </div>
                </form>
            </div>
            <div class="row group-btn-sort ">
                <div class="col-md-6">
                    <p style=" font-size:1.6rem;">Sort</p>
                    <form action="<%= switchPageUrl%>" method="POST">
                        <div class="form-check">
                            <input name="sort" type="radio" id="category-checkbox" value="max" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Ascending</label>
                            </br>
                            <input name="sort" type="radio" id="category-checkbox" value="min" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Descending</label>
                            <hr>
                            <input name="cate" type="radio" id="category-checkbox" value="id" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">ID</label>
                            <br>
                            <input name="cate" type="radio" id="category-checkbox" value="name" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Name</label>
                            <br>
                            <input name="cate" type="radio" id="category-checkbox" value="type" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Type</label>
                            <br>
                            <input name="cate" type="radio" id="category-checkbox" value="status" class="form-check-input">
                            <label for="category-checkbox" class="form-check-label">Status</label>
                        </div>
                        <input type="submit"  class="btn btn-primary mt-2">
                    </form>
                </div>
                <div class="col-md-6">
                    <p style=" font-size:1.6rem;">Type</p>
                    <form action="<%= switchPageUrl%>" method="POST">
                        <div class="form-check">
                            <input name="type" type="radio" id="type-checkbox" value="vegan" class="form-check-input">
                            <label for="type-checkbox" class="form-check-label">Vegan</label>
                            </br>
                            <input name="type" type="radio" id="type-checkbox" value="vegetarian"class="form-check-input">
                            <label for="type-checkbox" class="form-check-label">Vegetarian</label>
                            </br>
                            <input name="type" type="radio" id="type-checkbox" value="meat" class="form-check-input">
                            <label for="type-checkbox" class="form-check-label">Meat</label>
                            </br> 
                        </div>
                        <input type="submit" class="btn btn-primary mt-2">
                    </form>
                </div>
            </div>
            <div class="row table">
                <table class="table table-striped mt-4">
                    <thead>
                        <tr>
                            <th>Images</th>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Description</th>
                            <th>Option</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%      if (currentList != null) {
                                for (MealPlan mealPlan : currentList) {
                        %>
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/<%= mealPlan.getImgUrl()%>" alt="<%=mealPlan.getName()%>" width="100" height="100">
                            </td>
                            <td><%= mealPlan.getId()%></td>
                            <td><%= mealPlan.getName()%></td>
                            <td><%= mealPlan.getType()%></td>
                            <td><%= mealPlan.getContent()%></td>
                            <td>
                                <a href="" class="btn btn-sm btn-info mb-2">Detail</a>
                                <% if (mealPlan.getStatus() == 1) {

                                %>
                                <a href='<%=switchPageUrl%>&id=<%= mealPlan.getId()%>' onclick="" class="btn btn-sm btn-danger mb-2">Disable</a>

                                <%
                                } else {
                                %>
                                <a href='<%=switchPageUrl%>&id=<%= mealPlan.getId()%>' class="btn btn-sm btn-success mb-2">Enable</a>
                                <%
                                    }
                                %>
                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
            </div>
            <div class="row">
                <div class="col-md-12 d-flex justify-content-center">
                    <form action="<%= switchPageUrl%>" method="POST" class="form-check">
                        <input type="hidden" name="NumPage" value="<%= currentNumPage - 1%>">
                        <button type="summit" class="btn btn-info">&lt;</button>
                    </form>
                    <form action="<%= switchPageUrl%>" method="POST" class="form-check">
                        <input name="NumPage" class="form-control page-number" value="<%=currentNumPage%>"type="number" min="1" max="<%=paginationList.size()%>">
                    </form>
                    <span style="font-size: 1.5rem; "class="align-items-end ml-1"> /<%=paginationList.size()%></span>
                    <form action="<%=switchPageUrl%>" method="Post" class="form-check">
                        <input type="hidden" name="NumPage" value="<%= Math.min(currentNumPage + 1, paginationList.size())%>">
                        <button type="summit" class="btn btn-info">&gt;</button>
                    </form> 

                </div>
            </div>
        </div>
        <footer style="height:500px;">

        </footer>
        <script>

        </script>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
