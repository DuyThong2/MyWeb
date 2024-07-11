<%-- 
    Document   : adminCssAdder
    Created on : Jun 27, 2024, 8:50:48 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">

<!-- box icons -->
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
<!-- Google Web Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap" rel="stylesheet">

<!-- Icon Font Stylesheet -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css"/>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
<style>
    .popup-error {
                position: fixed;
                top: 50%;
                left: 50%; 
                transform: translate(-50%,-50%);
                background-color: white;
                width: 600px;
                height: 250px;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 10px;
                z-index: 10;
            }
            .error-close {
                position: absolute;
                top: 0px;
                right: 15px;
                font-size: 35px;
                color: rgb(200, 0, 0);
                cursor: pointer;
            }
            .error-close:hover {
                opacity: 0.8;
            }
            .error-text {
                font-size: 22px;
                text-align: center;
            }
            .overlay {
                position: fixed;
                top: 0;
                bottom: 0;
                left: 0;
                right: 0;
                z-index: 8;
                background-color: rgba(0, 0, 0, .8);
            }
</style>
