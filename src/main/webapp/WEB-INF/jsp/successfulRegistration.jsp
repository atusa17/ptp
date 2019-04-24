<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>You're Registered!</title>
</head>
<body>
    <br><b>Username: </b><%= request.getParameter("username")%>
    <br><b>Theorem: </b><%= request.getParameter("emailAddress")%>
</body>
</html>
