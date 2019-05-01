<%--
  Created by IntelliJ IDEA.
  User: tianliu
  Date: 2019-04-24
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Log in with your account</title>
</head>
<body>
<div class="container">
    <form method="post" action="">
        <h2 class="form-heading">Log in</h2>
        <div>
            <input name="username" type="text" class="form-control" placeholder="Username"/><br>
            <input name="password" type="password" class="form-control" placeholder="Password"/><br>
            <span style="Color: red">${error}</span><br>
            <input type="hidden" name="${parameterName}" value="${token}"/>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
            <h4 class="text-center"><a href="/registration">Create an account</a></h4>
        </div>
    </form>
</div>
</body>
</html>