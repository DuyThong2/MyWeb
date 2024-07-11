<%-- 
    Document   : success.jsp
    Created on : May 21, 2024, 10:46:26 AM
    Author     : Admin
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="dto.account.User"%>
<%@page import="dto.account.Staff"%>
<!DOCTYPE html>

<html>
  <head>
    <meta charset="utf-8">
        <title>Fruitables - Vegetable Website Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <c:choose>
            <c:when test="${not empty admin}">
                <%@include file="admin/adminCssAdder.jsp" %>
            </c:when>
            <c:otherwise>
                <%@include file="../cssAdder.jsp"%>
            </c:otherwise>
        </c:choose>
  </head>
    <style>
      body {
        text-align: center;
        padding: 40px 0;
        background: #EBF0F5;
      }
        card h1 {
          color: #88B04B;
          font-family: "Nunito Sans", "Helvetica Neue", sans-serif;
          font-weight: 900;
          font-size: 40px;
          margin-bottom: 10px;
        }
        p {
          color: #404F5E;
          font-family: "Nunito Sans", "Helvetica Neue", sans-serif;
          font-size:20px;
          margin: 0;
        }
      i {
        color: #9ABC66;
        font-size: 100px;
        line-height: 200px;
        margin-left:-15px;
      }
      .card {
        background: white;
        padding: 60px;
        border-radius: 4px;
        box-shadow: 0 2px 3px #C8D0D8;
        display: inline-block;
        margin: 0 auto;
      }
    </style>
    
    <%
        Staff staff = (Staff) session.getAttribute("admin");

        String redirectPage = "";
        if (staff != null) {
            redirectPage = request.getContextPath() + "/AMainController?action=adminmainpage";
        } else {
            redirectPage = request.getContextPath() + "/AMainController?action=mainpage";

        }


    %>
    <body>
        <!-- Navbar start -->
        <c:choose>
            <c:when test="${not empty admin}">
                <%@include file="admin/AdminHeader.jsp" %>
            </c:when>
            <c:otherwise>
                <%@include file="user/header.jsp"%>
            </c:otherwise>
        </c:choose>
        <!-- Navbar End -->

      <div class="card container">
      <div style="border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;">
        <i class="checkmark">✓</i>
      </div>
        <h1>Success</h1> 
        <p>We received your purchase request;<br/> we'll be in touch shortly!</p>
        <br>
        <a class="btn border-secondary rounded-pill py-3 px-5" href="<%=redirectPage%>">Go Back To Home</a>
      </div>
        
        <c:choose>
            <c:when test="${not empty admin}">
                <%@include file="admin/adminJs.jsp" %>
            </c:when>
            <c:otherwise>
                <%@include file="../jsAdder.jsp" %>
            </c:otherwise>
        </c:choose>
    </body>
</html>
