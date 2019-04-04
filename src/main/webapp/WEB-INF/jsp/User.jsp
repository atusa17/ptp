<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Creation</title>
    </head>
    <body>
        <form method="post" action="" enctype="utf8">
            <label>Username:
                <input type="text" name="username"/>
                <p th:each="error: ${#fields.errors('userName')}"
                   th:text="${error}">Validation error</p>
            </label>
            <br>
            <label>Password:
                <input type="text" name="password" th:field="*{matchingPassword}"/>
                <p th:each="error : ${#fields.errors('password')}"
                   th:text="${error}">Validation error</p>
            </label>
            <br>
            <label>Confirm Password:
                <input type="text" name="confirmPassword"/>

            </label>
            <br>
            <label>Email Address:
                <input type="text" name="emailAddress"/>
                <p th:each="error : ${#fields.errors('email')}"
                   th:text="${error}">Validation error</p>
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
                <p th:each="error : ${#fields.errors('terms')}"
                   th:text="${error}">Validation error</p>
            </label>
            <br>
                <input type="submit" value="Save">
        </form>
    </body>
</html>