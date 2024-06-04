<%-- 
    Document   : success.jsp
    Created on : May 21, 2024, 10:46:26 AM
    Author     : Admin
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    

    // Get all attribute names
    Enumeration<String> attributeNames = session.getAttributeNames();

    // Iterate through each attribute name
    while (attributeNames.hasMoreElements()) {
        String attributeName = attributeNames.nextElement();

        // Check if the attribute name is not "user" or "staff"
        if (!attributeName.equals("user") && !attributeName.equals("staff")) {
            // Remove the attribute
            session.removeAttribute(attributeName);
        }
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
