<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- style css -->
        <link rel="stylesheet" href="view/css/SharedStyle/shared.css">
        <link rel="stylesheet" href="view/css/LoginAndRegister/LoginAndRegister.css">
        <link rel="stylesheet" href="view/css/LoginAndRegister/RegisterStyle.css">

        <!-- boxicons -->
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <title>Document</title>
        <style>
            html, body {
                height: 100%;
                margin: 0;
                padding: 0;
            }
        </style>
    </head>

    <body>
        <div class="wrapper">
            <form action='AMainController' method='POST'>
                <h1>Register</h1>
                <div class="input-box">
                    <input type="text" name="txtUserName" placeholder="Enter Your Full Name" required value="<%= request.getAttribute("userName") != null ? request.getAttribute("userName") : ""%>">
                    <i class='bx bxs-user'></i>
                </div>
                <div class="input-box">
                    <input type="text" name="txtPhone" placeholder="Enter Your Phone Number" required minlength='10' value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : ""%>">
                    <i class='bx bxs-phone'></i>
                </div>
                <div class="input-box">
                    <input type="text" name="txtEmail" placeholder="Enter Email" required value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : ""%>">
                    <i class='bx bxs-envelope'></i>
                </div>
                <div class="input-box">
                    <input type="password" name="txtPassword" placeholder="Enter Password" required minlength='8'>
                    <i class='bx bx-lock'></i>
                </div>
                <div class="input-box">
                    <input type="password" name="txtPasswordConfirm" placeholder="Confirm Password" required minlength='8'>
                    <i class='bx bx-lock-open'></i>
                </div>
                <button type="submit" value="register" name='action' class="btn">Register</button>
                <div class="register-link">
                    <p> Have an account already?
                        <a href="AMainController?action="> Back to Login</a>
                    </p>
                </div>
            </form>
        </div>
        <%
            String printMessage = "";
            String messageType = "error"; // Default message type
            String errorMessage = (String) session.getAttribute("REGISTER_ERROR");
            String successMessage = (String) session.getAttribute("REGISTER_SUCCESS");
            if (errorMessage != null || successMessage != null) {
                if (errorMessage != null) {
                    switch (errorMessage) {
                        case "Empty":
                            printMessage = "All fields must be filled!";
                            break;
                        case "Email":
                            printMessage = "Invalid email format!";
                            break;
                        case "Password":
                            printMessage = "Passwords do not match!";
                            break;
                        case "banned":
                            printMessage = "Your account is banned!";
                            break;
                        case "FoundEmail":
                            printMessage = "Your email has already been signed up";
                            break;
                        default:
                            printMessage = "Registration error! Please try again.";
                            break;
                    }
                } else if (successMessage != null) {
                    printMessage = "Registration successful! You can now log in.";
                    messageType = "success"; // Change message type to success
                }
        %>
        <div class="popup-error">
            <i class='error-close bx bxs-x-circle' onclick="closeMessage()"></i>
            <p class='<%=messageType%>-text'>
                <%= printMessage%>
            </p>
        </div>
        <div class="overlay"></div>
        <%
                session.removeAttribute("REGISTER_ERROR");
                session.removeAttribute("REGISTER_SUCCESS");
            }
        %>
        <script>
            function closeMessage() {
                var messageType = '<%= messageType%>';
                const message = document.querySelector('.popup-error, .popup-success');
                message.style.top = "-100%";
                const overlay = document.querySelector('.overlay');
                overlay.classList.remove('overlay');
                if (messageType === "success") {
                    window.location.href = "AMainController";
                }
            }
        </script>
    </body>
</html>
