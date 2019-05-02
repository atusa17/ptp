<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>You're Registered!</title>
</head>
<body>
    Thank you for joining!
    <br><b>Username: </b><%= request.getParameter("username")%>
    <br><b>Email Address: </b><%= request.getParameter("emailAddress")%>
</body>
</html>
