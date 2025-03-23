<%-- 
    Document   : footer
    Created on : Feb 23, 2025, 7:34:49 PM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Footer Page</title>
        <!-- Nếu muốn dùng Font Awesome cho icon -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            .footer h1 {
                font-size: 20px;
                padding-bottom: 5%;
            }
            /* 
               Sử dụng gradient xanh dương mô phỏng tông màu trong ảnh.
               Nếu muốn đổi sang màu khác, thay đổi mã màu bên dưới.
            */
            .footer {
                margin-top: 2rem;
                /* Màu nền chuyển từ #3169C6 sang #3D8BCE */
                background: #1478D3;
                padding: 40px 20px;
                color: white;
            }
            .footer-container {
                display: flex;
                justify-content: space-between;
                padding-left: 5%;
            }
            .footer-section {
                flex: 1;
                width: 33%;
            }
            .about-us {
                padding-right: 7%;
            }
            .latest-tweet {
                padding-left: 20px;
                width: 20%;
            }
            .quick-link {
                display: flex;
                flex-direction: column;
                padding: 0;
            }
            .quick-link a{
                color: white;
                text-decoration: none;
            }
            
            .quick-link a:hover{
                text-decoration: underline;
            }
            .latest-tweet li {
                padding: 5px;
                display: block;
            }
            .phone-number, .email {
                padding-top: 3%;
                font-weight: bolder;
            }
            .email-form {
                margin-top: 10px;
                padding-bottom: 5%;
            }
            .email-form input {
                display: flex;
                padding: 2%;
                border: none;
                border-radius: 3px;
                margin: 2% 0;
                width: 70%;
            }
            /* Giữ màu cam cho nút gửi, bạn có thể đổi thành màu xanh nếu muốn */
            .email-form button {
                width: 70%;
                background: linear-gradient(90deg, #020024, #ff4c4c 0, #ff3030 25%);
                color: #fff;
                border: none;
                padding: 10px 20px;
                cursor: pointer;
                border-radius: 3px;
            }

        </style>
    </head>
    <body>
        <footer class="footer">
            <div class="footer-container">
                <!-- About Us -->
                <div class="footer-section about-us">
                    <h1>About Us</h1>
                    <p>
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                        Laboriosam itaque unde facere repellendus, odio et iste
                        voluptatum aspernatur ratione.
                    </p>
                    <div class="contact-info">
                        <p class="phone-number">
                            <i class="fa fa-phone"></i> +1 291 3912 329
                        </p>
                        <p class="email">
                            <i class="fa fa-envelope"></i> info@gmail.com
                        </p>
                    </div>
                </div>

                <!-- Quick Links -->
                <div class="footer-section latest-tweet">
                    <h1>Quick Links</h1>
                    <ul class="quick-link">
                        <li><a href="#">Our doctors</a></li>
                        <li><a href="#">Scheduled</a></li>
                        <li><a href="#">Medical History</a></li>
                    </ul>
                </div>

                <!-- Contact Us -->
                <div class="footer-section">
                    <h1>Contact Us</h1>
                    <form class="email-form">
                        <input type="email" placeholder="Email" required />
                        <input type="text" placeholder="Message" required />
                        <button type="submit">Send</button>
                    </form>
                </div>
            </div>

        </footer>
    </body>
</html>
