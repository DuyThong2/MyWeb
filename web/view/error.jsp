<%-- 
    Document   : error
    Created on : May 21, 2024, 10:45:43 AM
    Author     : Admin
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>Hello</p>
        <p><%= request.getContextPath()%>/images/mealPlan/blackhole.jpg</p>
        <img src="<%= request.getContextPath()%>/images/mealPlan/blackhole.jpg" alt="hehe">
    </body>
</html>
