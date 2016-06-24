<%-- 
    Document   : login
    Created on : Jun 24, 2016, 9:58:15 AM
    Author     : song
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>


        <form action="j_security_check" method=post>
            <div id="loginBox">
                <p><strong>username:</strong>
                    <input type="text" size="20" name="j_username"></p>

                <p><strong>password:</strong>
                    <input type="password" size="20" name="j_password"></p>

                <p><input type="submit" value="submit"></p>
            </div>
        </form>

    </body>
</html>
