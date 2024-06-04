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
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <%
        String editMealURL = request.getContextPath() + "/AMainController?action=MealUpdatePage";
        String insertPacketURL = request.getContextPath() + "/AMainController?action=PacketInsert";
        String updatePacketURL = request.getContextPath() + "/AMainController?action=PacketUpdate";
        String disableMealURL = request.getContextPath() + "/AMainController?action=ProductDelete";
        String addSaleURL = request.getContextPath() + "/AMainController?action=addSale";
        String mealId = request.getParameter("mealId");
        if (mealId == null) {
            response.sendRedirect(request.getContextPath() + "/AMainController?action=MealManage");
            return;
        }
        MealDAO dao = new MealDAO();
        DiscountDAO discountDao = new DiscountDAO();
        Meal meal = dao.getMealFullDetailFromId(mealId);
        System.out.println(mealId);
        request.setAttribute("meal", meal);
    %>
    <body>
        <div class="container mt-3">
            <!-- Top Row: Meal Image and Info -->
            <div class="row">
                <!-- Meal Image and Basic Info (40%) -->
                <div class="col-md-4">
                    <img src="${pageContext.request.contextPath}/${meal.getImageURL()}" class="img-fluid" alt="${meal.getName()}">
                    <h2>${meal.getName()}</h2>
                    <p><strong>Price:</strong> $${meal.getPrice()}</p>
                    <p><strong>Description:</strong> ${meal.getDescription()}</p>
                    <p><strong>ID:</strong> ${meal.getId()}</p>
                </div>

                <!-- Inserted Ingredients (60%) -->
                <div class="col-md-8">
                    <h3>Ingredients</h3>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Ingredient Name</th>
                                <th>Quantity</th>
                                <th>Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${meal.getPacket().getContains()}" var="entry">
                                <tr>
                                    <td>${entry.key.getName()}</td>
                                    <td>${entry.value} ${entry.key.getUnit()}</td>
                                    <td>${entry.key.getPrice()} per ${entry.key.getUnit()}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    
                    <c:choose>
                        <c:when test="${meal.getPacket() != null}">
                            <p><strong>Packet total price:</strong> $${meal.getPacket().getTotalPrice()}</p>
                            <%
                                int discountId = meal.getPacket().getDiscountID();
                                double discount = discountDao.getDiscountPercentForProduct(discountId);
                                if (meal.getPacket().isOnSale()) {
                                    out.print("<p><strong>Discount: </strong>" + discount + " %</p>");
                                } else {
                                    out.print("<p><strong>Discount: </strong>0% </p>");
                                }
                            %>
                        </c:when>
                        <c:otherwise>
                            <h6> no data </h6>
                        </c:otherwise>
                    </c:choose>

                    <a href="<%= addSaleURL%>&id=P<%= meal.getId().substring(1)%>" class="btn btn-warning">Adjust packet discount</a>
                </div>
            </div>

            <!-- Content Row (70%) -->
            <div class="row mt-3">
                <div class="col-md-8">
                    <h3>Content</h3>
                    <p>${meal.getContent()}</p>
                </div>
                <div class="col-md-4">
                    <div class="btn-group-vertical col-md-12">
                        <a href="<%= editMealURL%>&mealId=${meal.getId()}" class="btn btn-info mb-2">Edit Meal Info</a>
                        <c:choose>
                            <c:when test="${meal.getPacket() != null}">
                                <a href="<%= updatePacketURL%>&packetId=P${meal.getId().substring(1)}" class="btn btn-secondary mb-2">Edit Ingredients</a>
                            </c:when>
                            <c:otherwise>
                                <a href="<%= insertPacketURL%>&packetId=P${meal.getId().substring(1)}" class="btn btn-info mb-2">Add Ingredients</a>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${meal.getStatus() == 'active'}">
                                <a href="<%= disableMealURL%>&status=disable&mealId=${meal.getId()}" class="btn btn-danger mb-2">Disable</a>
                            </c:when>
                            <c:otherwise>
                                <a href="<%= disableMealURL%>&status=active&mealId=${meal.getId()}" class="btn btn-success mb-2">Enable</a>
                            </c:otherwise>
                        </c:choose>
                        <a href="<%= addSaleURL%>&mealId=${meal.getId()}" class="btn btn-warning">Adjust Meal Sale</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
