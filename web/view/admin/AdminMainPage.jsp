<%-- 
    Document   : adminMainPage
    Created on : Jun 18, 2024, 1:00:09 AM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1> Admin Main Page</h1>
<!--        DASH BOARD-->

        <div class="container">
            <div>
                <p><a>Manage Users</a></p>
            </div>
            <div>
                <p><a>Manage Orders</a></p>

            </div>
            <div>
                <p><a href='<%=request.getContextPath()+"/AMainController?action=MealPlan" %>'>Manage Meal Plan</a></p>

            </div>
            <div>
                <p><a>Manage Meal</a></p>
            </div>
            <div>
                <p><a>Manage Ingredient</a></p>
            </div>
        </div>
    </body>
</html>
