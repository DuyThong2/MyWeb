<%-- 
    Document   : UserDetail
    Created on : Jun 4, 2024, 8:20:34 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <%
        String redirectURL = request.getContextPath()+"/AMainController?action=userManagePage";
        String userIdStr = request.getParameter("id");
        if (userIdStr == null){
            response.sendRedirect();
        }
        
    %>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
