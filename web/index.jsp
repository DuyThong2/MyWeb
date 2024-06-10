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
        <div class="wrapper">
            <form action='AMainController'>
                <h1>Login</h1>
                <div class="input-box">
                    <input type="text" name="txtEmail" placeholder="Email" required>
                    <i class='bx bxs-user'></i>
                </div>
                <div class="input-box">
                    <input type="password" name ="txtPassword" placeholder="Password" required>
                    <i class='bx bxs-lock' ></i>
                </div>
                <button type="submit" value="login" class="btn" name='action'>login</button>
                <div class="register-link">
                    <p>Don't have an account?
                        <a href="AMainController?action=register">Sign up</a>
                    </p>
                </div>
            </form>
        </div>
    </body>
</html>