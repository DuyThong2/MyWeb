<%-- 
    Document   : DayPlanInsert
    Created on : Jul 4, 2024, 7:18:15 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>${requestScope.ERROR}</p>
        <p>${requestScope.url2}</p>
        <p>${requestScope.url}</p>
        <img src="${requestScope.url}" alt="${requestScope.fileName}">
        
    </body>
</html>
