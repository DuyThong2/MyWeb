<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Document</title>
        <!-- style css -->
        <link rel="stylesheet" href="view/css/SharedStyle/shared.css">
        <link rel="stylesheet" href="view/css/LoginAndRegister/LoginAndRegister.css">     
        <!-- boxicons -->
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>

    </head>
    <body>
        <%
            String returnEmail= (String)session.getAttribute("ReturnedEmail");
            returnEmail = returnEmail==null?"":returnEmail;
            %>
        <div class="wrapper">
            <form action='AMainController' method="POST">
                <h1>Login</h1>
                <div class="input-box">
                    <input type="text" name="txtEmail" value="<%= returnEmail%>" placeholder="Email" required>
                    <i class='bx bxs-user'></i>
                </div>
                <div class="input-box">
                    <input type="password" name ="txtPassword" placeholder="Password" required>
                    <i class='bx bxs-lock' ></i>
                </div>
                <button type="submit" value="login" class="btn" name='action'>login</button>
                <div class="register-link">
                    <p>Don't have an account?
                        <a href="AMainController?action=registerform">Sign up</a>
                    </p>
                </div>
            </form>
        </div>
        <%
            String printMessage="";
            String errorMessage= (String)session.getAttribute("ERROR");
            if(errorMessage!=null){
                if(errorMessage.equalsIgnoreCase("WrongPassword")){
                    printMessage="WRONG PASSWORD! PLEASE ENTER AGAIN!";
                }
                else if(errorMessage.equalsIgnoreCase("banned")){
                    printMessage="YOUR ACCOUNT IS BANNED!";
                }else{
                    printMessage="YOUR ACCOUNT DOES NOT EXIST!</br>PLEASE REGISTER A NEW ACCOUNT.";
                }
            
        %>
        <div class="popup-error">

            <i class='error-close bx bxs-x-circle'  onclick="closeError()"></i>

            <p class="error-text">
                <%= printMessage %>
            </p>
        </div>
        <div class="overlay">
        </div>

        <%
                session.removeAttribute("ERROR");
        }
        %>
        <script>
            function closeError() {
                error = document.querySelector('.popup-error');
                error.style.top = "-100%";
                const overlay = document.querySelector('.overlay');
                overlay.classList.remove('overlay');
            }
        </script>
    </body>
</html>