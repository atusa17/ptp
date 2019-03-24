<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Creation</title>
    </head>
    <body>
        <form method="post" action="">
            <label>Username:
                <input type="text" name="username"/>
            </label>
            <br>
            <label>Password:
                <input type="text" name="password"/>
            </label>
            <br>
            <label>Confirm Password:
                <input type="text" name="confirmPassword"/>
            </label>
            <br>
            <label>Email Address:
                <input type="text" name="emailAddress"/>
            </label>
            <br>
            <label>First Name:
                <input type="text" name="firstName"/>
            </label>
            <br>
            <label>Last Name:
                <input type="text" name="lastName"/>
            </label>
            <br>
            <label>I agree to the terms and conditions.
                <input type="checkbox" name="agreedToTerms"/>
            </label>
            <br>
                <input type="submit" value="Save">
        </form>
    </body>
</html>