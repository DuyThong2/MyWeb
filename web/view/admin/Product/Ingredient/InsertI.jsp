<%@page import="java.nio.file.Paths"%>

<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.FileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.apache.commons.fileupload.FileUploadException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Insert New Ingredient</title>
        <%@include file="../../adminCssAdder.jsp" %>
    </head>
    <body>
        <%
            String url = "/AMainController?action=IngredientInsert";
            String error = (String) request.getAttribute("errorMessage");
            System.out.println(error);
        %>
        <%@include file="../../AdminHeader.jsp" %>
        <% if (error != null) {%>
        <div class="popup-error">
            <p class="error-close" onclick="closeMessage()">x<p>
            <p class='error-text'>
                <%=error%>
            </p>
        </div>
        <div class="overlay"></div>

        <% }
        %>
        <h1>Insert New Ingredient</h1>
        <div class="container ">
            <form action="<%=request.getContextPath() + url%>" method="post" enctype="multipart/form-data">
                <div class="form-group row">
                    <label for="price" class="col-sm-2 col-form-label">ID</label>
                    <div class="col-sm-10">
                        <input type="number" step="0.01" class="form-control" id="price" name="id" placeholder="Enter id" required>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="ingredientName" class="col-sm-2 col-form-label">Ingredient Name</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="ingredientName" name="ingredientName" placeholder="Enter Ingredient Name" required>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="price" class="col-sm-2 col-form-label">Price</label>
                    <div class="col-sm-10">
                        <input type="number" step="0.01" class="form-control" id="price" name="price" placeholder="Enter Price" required>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="unit" class="col-sm-2 col-form-label">Unit</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="unit" name="unit" placeholder="Enter Unit" required>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="imgURL" class="col-sm-2 col-form-label">Image File</label>
                    <div class="col-sm-10">
                        <input type="file" class="form-control-file" id="imgURL" name="imgURL" required>
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-10 offset-sm-2">
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </div>
                </div>
            </form>
            <h2> ${error} </h2>
        </div>

        <footer class="bg-light text-center py-3 mt-5">
            <p>&copy; 2024 Your Company</p>
        </footer>

        <%
            request.removeAttribute("errorMessage");
            
        %>
        <script>
            function closeMessage() {
                const message = document.querySelector('.popup-error');
                message.style.top = "-100%";
                const overlay = document.querySelector('.overlay');
                overlay.classList.remove('overlay');
            }

        </script>

        <%@include file="../../adminJs.jsp" %>
    </body>
</html>