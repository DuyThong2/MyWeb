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
</head>

<body>
     <div class="wrapper">
          <form action="">
               <h1>Register</h1>
               <div class="input-box">
                    <input type="text" name="txtEmail" placeholder="Enter Email" required>
                    <i class='bx bxs-envelope'></i>
               </div>
               <div class="input-box">
                    <input type="text" name="txtUserName" placeholder="Enter User Name" required>
                    <i class='bx bxs-user'></i>
               </div>
               <div class="input-box">
                    <input type="password" name="txtPassword" placeholder="Enter Password" required>
                    <i class='bx bx-lock'></i>
               </div>
               <div class="input-box">
                    <input type="password" name="txtPasswordConfirm" placeholder="Confirm Password" required>
                    <i class='bx bx-lock-open'></i>
               </div>
               <button type="submit" value="login" class="btn">Register</button>
               <div class="register-link">
                    <p> Have account already?
                         <a href="index.jsp"> Back to Login</a>
                    </p>
               </div>
          </form>
     </div>
</body>
</html>
