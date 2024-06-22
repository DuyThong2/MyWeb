<%-- 
    Document   : header
    Created on : Jun 6, 2024, 3:11:14 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<!-- Nav Bar -->
<div class="header-container">
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="navbar navbar-light bg-white  navbar-container text-center">
            <!-- logo -->
            <p class="logo col-lg-4 col-md-5 col-sm-6 "><a>HappiCook<i class='bow-logo bx bxs-bowl-hot'></i></a></p>
            <!-- central-links -->
            <div class="central-links col-lg-4 col-md-2 col-sm-1 container-fluid">
                <ul class="navbar-nav">
                    <li><a href="#">Home</a></li>
                    <li><a href="#">Blog</a></li>
                    <li><a href="#">Products</a></li>
                    <li><a href="#">About</a></li>
                    <label class="menu-button" for="">
                        <svg xmlns="http://www.w3.org/2000/svg" height="32px"
                             viewBox="0 -960 960 960" width="32px" fill="#000000">
                        <path
                            d="M120-240v-80h720v80H120Zm0-200v-80h720v80H120Zm0-200v-80h720v80H120Z" />
                        </svg>
                    </label>
                </ul>
            </div>
            <!-- search -->
            <div class="search-user-cart col-lg-4  col-md-5 col-sm-5 ">
                <label class="search-open-button search" for="search-active">
                    <svg xmlns="http://www.w3.org/2000/svg" height="32px" viewBox="0 -960 960 960"
                         width="32px" fill="#000000">
                    <path
                        d="M792-120.67 532.67-380q-30 25.33-69.64 39.67Q423.39-326 378.67-326q-108.44 0-183.56-75.17Q120-476.33 120-583.33t75.17-182.17q75.16-75.17 182.5-75.17 107.33 0 182.16 75.17 74.84 75.17 74.84 182.27 0 43.23-14 82.9-14 39.66-40.67 73l260 258.66-48 48Zm-414-272q79.17 0 134.58-55.83Q568-504.33 568-583.33q0-79-55.42-134.84Q457.17-774 378-774q-79.72 0-135.53 55.83-55.8 55.84-55.8 134.84t55.8 134.83q55.81 55.83 135.53 55.83Z" />
                    </svg>
                </label>
                <a href="#">
                    <svg xmlns="http://www.w3.org/2000/svg" height="32px" viewBox="0 -960 960 960"
                         width="32px" fill="#000000">
                    <path
                        d="M280-80q-33 0-56.5-23.5T200-160q0-33 23.5-56.5T280-240q33 0 56.5 23.5T360-160q0 33-23.5 56.5T280-80Zm400 0q-33 0-56.5-23.5T600-160q0-33 23.5-56.5T680-240q33 0 56.5 23.5T760-160q0 33-23.5 56.5T680-80ZM246-720l96 200h280l110-200H246Zm-38-80h590q23 0 35 20.5t1 41.5L692-482q-11 20-29.5 31T622-440H324l-44 80h480v80H280q-45 0-68-39.5t-2-78.5l54-98-144-304H40v-80h130l38 80Zm134 280h280-280Z" />
                    </svg>
                </a>
                <a href="#">
                    <svg xmlns="http://www.w3.org/2000/svg" height="40px" viewBox="0 -960 960 960"
                         width="40px" fill="#000000">
                    <path
                        d="M480-480.67q-66 0-109.67-43.66Q326.67-568 326.67-634t43.66-109.67Q414-787.33 480-787.33t109.67 43.66Q633.33-700 633.33-634t-43.66 109.67Q546-480.67 480-480.67ZM160-160v-100q0-36.67 18.5-64.17T226.67-366q65.33-30.33 127.66-45.5 62.34-15.17 125.67-15.17t125.33 15.5q62 15.5 127.28 45.3 30.54 14.42 48.96 41.81Q800-296.67 800-260v100H160Zm66.67-66.67h506.66V-260q0-14.33-8.16-27-8.17-12.67-20.5-19-60.67-29.67-114.34-41.83Q536.67-360 480-360t-111 12.17Q314.67-335.67 254.67-306q-12.34 6.33-20.17 19-7.83 12.67-7.83 27v33.33ZM480-547.33q37 0 61.83-24.84Q566.67-597 566.67-634t-24.84-61.83Q517-720.67 480-720.67t-61.83 24.84Q393.33-671 393.33-634t24.84 61.83Q443-547.33 480-547.33Zm0-86.67Zm0 407.33Z" />
                    </svg>
                </a>
            </div>
            <!-- sidebar mobile -->
            <!-- <div class="sidebar-container ">
                 <input type="checkbox" id="#sidebar-active">
                 <label class="sidebar-menu-button" for="sidebar-active">
                      <svg xmlns="http://www.w3.org/2000/svg" height="32px" viewBox="0 -960 960 960" width="32px" fill="#000000"><path d="M120-240v-80h720v80H120Zm0-200v-80h720v80H120Zm0-200v-80h720v80H120Z"/></svg>
                 </label>
                 <label class="overlay" for="sidebar-active">
                 </label>
                 <div class="sidebar-links">
                      <label class="sidebar-close-button"for="sidebar-active">
                           <svg xmlns="http://www.w3.org/2000/svg" height="32px" viewBox="0 -960 960 960" width="32px" fill="#000000"><path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/></svg>
                      </label>
                      <a href="#">Home</a>
                      <a href="#">Blog</a>
                      <a href="#">Products</a>
                      <a href="#">About</a>
                 </div>
            </div> -->
        </div>
    </nav>

    <input type="checkbox" id="search-active">
    <label class="popup-search-container" for="search-active">
        <div class="search-bar-container">
            <form action="">
                <input class="search-bar" type="text" class="form-control"
                       placeholder="Search for HappiCook OwO">
                <label class="search-close-button" for="search-active">
                    <svg xmlns="http://www.w3.org/2000/svg" height="40px" viewBox="0 -960 960 960"
                         width="40px" fill="#000000">
                    <path
                        d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z" />
                    </svg>
                </label>
            </form>
        </div>

    </label>
    <label id="search-overlay" for="search-active"></label>
</div>


<!-- POP-UP SEARCH BOX -->


