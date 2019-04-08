<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>You're Registered!</title>
</head>
<body>
    <br><b>User ID: </b><%= request.getParameter("userID")%>
    <br><b>Username: </b><%= request.getParameter("username")%>
    <br><b>Theorem: </b><%= request.getParameter("emailAddress")%>
    <br><b>First Name: </b><%= request.getParameter("firstName")%>
    <br><b>Last Name: </b><%= request.getParameter("lastName")%>
</body>
</html>
