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
        <%@include file="../../adminCssAdder.jsp" %>
        <title>JSP Page</title>
        <style>
            .main-container{
                padding:20px;
                position:relative;
            }
            .insert-button{
                position:absolute;
                top:0px;
                right: 0;
            }
            .description-col{
                width:400px;
            }
        </style>
    </head>
    <body>
        <%
            String switchPageUrl = request.getContextPath() + "/AMainController?action=MealPlan";
            String searchPageUrl = request.getContextPath() + "/AMainController";
            String detailPageUrl = request.getContextPath() + "/AMainController?action=MealPlanDetail";
            String insertPageUrl = request.getContextPath() + "/AMainController?action=MealPlanInsertPage";
            List<MealPlan> mealPlanList = (ArrayList<MealPlan>) request.getAttribute("MealPlanList");
            if (mealPlanList == null) {
                response.sendRedirect(switchPageUrl);
                return;
            }
            ArrayList<MealPlan> currentList = new ArrayList<>();
            List<List<MealPlan>> paginationList = Tool.splitToPage(mealPlanList, 10);
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
        <%@include file="../../AdminHeader.jsp" %>

        <div class="container main-container bg-white border border-warning p-3" style="border-radius:10px;">
            <div class="row  mb-2 w-100 d-flex justify-content-center">
                <h1 class=" text-center text-dark">Meal Plan Manage</h1>
                <div class="d-flex justify-content-center insert-button">
                    <a class="btn btn-success" href="<%= insertPageUrl%>"><span class="h5">Insert</span></a>
                </div>
            </div>

            <hr>
           


            <div class="row search-bar p-0 w-100">
                <form action="<%=searchPageUrl%>" class="w-100" method="POST">
                    <div class="row form-group form-inline">
                        <label for="search-bar" class="col-md-3" style=" font-size:1.6rem;">Search Meal Plan</label>
                        <input  type="text" name="txtsearch" id="search-bar" class="form-control col-md-7" value="${sessionScope.localSearch}" placeholder="Search for meal plan">
                        <input type="submit" name="action" value="MealPlan"  class="btn btn-primary btn-md btn-warning col-md-1">
                    </div>
                </form>
            </div>
            <div class="row table ml-1">
                <table class="table table-striped mt-4">
                    <thead class="thead-dark">
                        <tr>
                            <th>Images</th>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th class="description-col">Description</th>
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
                            <td class="d-flex flex-column justify-content-space-between w-100">
                                <a href="<%= detailPageUrl%>&id=<%=mealPlan.getId()%>" class="btn btn-md btn-primary mb-2">Detail</a>
                                <% if (mealPlan.getStatus() == 1) {
                                %>
                                <a href='<%=switchPageUrl%>&id=<%= mealPlan.getId()%>' onclick="" class="btn btn-md btn-danger mb-2">Disable</a>

                                <%
                                } else {
                                %>
                                <a href='<%=switchPageUrl%>&id=<%= mealPlan.getId()%>' class="btn btn-md btn-success mb-2">Enable</a>
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
                        <button type="submit" class="btn btn-dark">&lt;</button>
                    </form>
                    <form action="<%= switchPageUrl%>" method="POST" class="form-check">
                        <input name="NumPage" class="form-control page-number" value="<%=currentNumPage%>"type="number" min="1" max="<%=paginationList.size()%>">
                    </form>
                    <span style="font-size: 1.5rem; "class="align-items-end ml-1"> /<%=paginationList.size()%></span>
                    <form action="<%=switchPageUrl%>" method="Post" class="form-check">
                        <input type="hidden" name="NumPage" value="<%= Math.min(currentNumPage + 1, paginationList.size())%>">
                        <button type="submit" class="btn btn-dark">&gt;</button>
                    </form> 

                </div>
            </div>
                        
            <div class="row group-btn-sort ">
                <div class="col-md-6">
                    <form action="<%= switchPageUrl%>" method="POST">
                        <div class="form-check d-flex justify-content-around align-items-center">
                            <div>
                                <p style=" font-size:1.6rem;">Sort By</p>
                            </div>
                            <div>
                                <select name="cate" id="" class="form-select" required>
                                    <option value=""> Choose Category</option>
                                    <option value="id">ID</option>
                                    <option value="name">Name</option>
                                    <option value="type">Type</option>
                                    <option value="status">Status</option>
                                </select>
                                <div>
                                    <input name="sort" type="radio" id="category-checkbox" value="max" class="form-check-input">
                                    <label for="category-checkbox" class="form-check-label">Ascending</label>
                                </div>
                                <div>
                                    <input name="sort" type="radio" id="category-checkbox" value="min" class="form-check-input">
                                    <label for="category-checkbox" class="form-check-label">Descending</label>
                                </div>
                            </div> 
                            <div>
                                <input type="submit"  value="sort" class="btn btn-primary mt-2">

                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-6">
                    <form action="<%= switchPageUrl%>" method="POST">
                        <div class="form-check d-flex justify-content-around align-items-center">
                            <div>
                                <p style=" font-size:1.6rem;">Type</p>
                            </div>
                            <div>
                                <div>
                                    <input name="type" type="radio" id="type-checkbox" value="vegan" class="form-check-input">
                                    <label for="type-checkbox" class="form-check-label">Vegan</label>
                                </div>
                                <div>
                                    <input name="type" type="radio" id="type-checkbox" value="vegetarian"class="form-check-input">
                                    <label for="type-checkbox" class="form-check-label">Vegetarian</label> 
                                </div>
                                <div>
                                    <input name="type" type="radio" id="type-checkbox" value="meat" class="form-check-input">
                                    <label for="type-checkbox" class="form-check-label">Meat</label>
                                </div>
                            </div>
                            <div>
                                <input type="submit" class="btn btn-primary mt-2">

                            </div>
                        </div>
                    </form>
                </div>
            </div>

        </div>
        <%@include file="../../adminJs.jsp" %>
    </body>
</html>
