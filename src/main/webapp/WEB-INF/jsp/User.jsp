<%@ taglib prefix="th" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Creation</title>
        <style type="text/css">
            input {
                padding: 0.25em 0.5em;
                border: 0.125em solid hsl(30, 76%, 10%);
                outline: none;
            }

            /* Show green borders when valid */
            input[data-state="valid"] {
                border-color: hsl(120, 76%, 50%);
            }

            /* Show red borders when filled, but invalid */
            input[data-state="invalid"] {
                border-color: hsl(0, 76%, 50%);
            }

            .error-messages {
                display:none;
            }
            .error-messages[data-state="valid"] {
                display:none;
            }
            .error-messages[data-state="invalid"] {
                display:inline;
                color: hsl(0, 76%, 50%);
            }
        </style>

        <script type="text/javascript">
            document.getElementById("submit").disabled = true;

                function checkIDBeginsWith900(){
                var userID = document.getElementById("userID");
                var error = document.getElementById("errorUserID");

                var IDValue = userID.value;

                if(IDValue - 900000000 > 0) {
                    userID.dataset.state = 'valid';
                    error.dataset.state = 'valid';
                } else {
                    userID.dataset.state = 'invalid';
                    error.dataset.state = 'invalid';
                }
            }

            function checkValidUsername(){
                var username = document.getElementById("username");
                var error = document.getElementById("errorUsername");

                var re = /^\w+$/;

                if(re.test(username.value)){
                    username.dataset.state = 'valid';
                    error.dataset.state = 'valid';
                } else {
                    username.dataset.state = 'invalid';
                    error.dataset.state = 'invalid';
                }
            }

            function checkValidPassword(){
                var password = document.getElementById("password");
                var error = document.getElementById("errorPassword");

                var re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;

                if(re.test(password.value)){
                    password.dataset.state = 'valid';
                    error.dataset.state = 'valid';
                } else {
                    password.dataset.state = 'invalid';
                    error.dataset.state = 'invalid';
                }
            }

            function checkPasswordsMatch(){
                var confirmPassword = document.getElementById("confirmPassword");
                var password = document.getElementById("password");
                var error = document.getElementById("errorConfirmPassword");

                if(password.value === confirmPassword.value){
                    confirmPassword.dataset.state = 'valid';
                    error.dataset.state = 'valid';
                } else {
                    confirmPassword.dataset.state = 'invalid';
                    error.dataset.state = 'invalid';
                }
            }

            function checkAgreedToTerms(){
                var agreedToTerms = document.getElementById("agreedToTerms");
                var error = document.getElementById("errorAgreedToTerms");

                if(agreedToTerms.checked === true){
                    error.dataset.state = 'valid';
                    document.getElementById("submit").disabled = false;
                    return true;
                } else {
                    error.dataset.state = 'invalid';
                    document.getElementById("submit").disabled = true;
                    return false;
                }
            }
        </script>
    </head>
    <body>
        New User Registration:
        <form method="post" action="" enctype="utf8">
            <label for="userID">User ID:
                <input type="text" name="userID" id="userID" onchange="checkIDBeginsWith900()"/>
                <div class="error-messages" id="errorUserID">Invalid MSU student ID</div>
            </label>
            <br>
            <label for="username">Username:
                <input type="text" name="username" id="username" onchange="checkValidUsername()"/>
                <div class="error-messages" id="errorUsername">Username must contain only letters, numbers, and underscores</div>
            </label>
            <br>
            <label for="password">Password:
                <input type="text" name="password" id="password" onchange="checkValidPassword()"/>
                <div class="error-messages" id="errorPassword">Password must be at least 8 characters long, including numbers, uppercase and lowercase letters.</div>
            </label>
            <br>
            <label for="confirmPassword">Confirm Password:
                <input type="text" name="confirmPassword" id="confirmPassword" onchange="checkPasswordsMatch()"/>
                <div class="error-messages" id="errorConfirmPassword">Passwords do not match</div>
            </label>
            <br>
            <label for="emailAddress">Email Address:
                <input type="text" name="emailAddress" id="emailAddress" />
                <div class="error-messages" id="errorEmailAddress">Invalid email address</div>
            </label>
            <br>
            <label for="firstName">First Name:
                <input type="text" name="firstName" id="firstName"/>
                <div class="error-messages"></div>
            </label>
            <br>
            <label for="lastName">Last Name:
                <input type="text" name="lastName" id="lastName"/>
                <div class="error-messages"></div>
            </label>
            <br>
            <label for="referrer">Referrer:
                <input type="text" name="referrer" id="referrer" />
                <div class="error-messages" id="errorReferrer">Username must contain only letters, numbers, and underscores, and cannot be your own!</div>
            </label>
            <br>
            <label for="agreedToTerms">I agree to the terms and conditions.
                <input type="checkbox" name="agreedToTerms" id="agreedToTerms" onclick="checkAgreedToTerms()"/>
                <div class="error-messages" id="errorAgreedToTerms">You must agree to the terms and conditions in order to register.</div>
            </label>
            <br>
                <input type="submit" id="submit" value="Submit" disabled="disabled"/>
        </form>
    </body>
</html>