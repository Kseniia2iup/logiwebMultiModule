<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Login page</title>

    <!--[if lte IE 8]><script src="/static/js/ie/html5shiv.js"></script><![endif]-->
    <link href="<c:url value="/static/css/main.css"  />" rel="stylesheet" />

</head>

<body>
<div id="wrapper">
    <!-- Main -->
    <div id="main">
    <div class="login-container">
        <h2 style="text-align: center">LogiWeb</h2>
        <div class="login-card">
            <div class="login-form">
                <c:url var="loginUrl" value="/login" />
                <form action="${loginUrl}" method="post" class="form-horizontal">
                    <c:if test="${param.error != null}">
                        <div class="alert alert-danger">
                            <p>Invalid username or password.</p>
                        </div>
                    </c:if>
                    <c:if test="${param.logout != null}">
                        <div class="alert alert-success">
                            <p>You have been logged out successfully.</p>
                        </div>
                    </c:if>

                    <br />
                    <div class="input-group input-sm">
                        <input type="text" class="form-control" id="username" name="login" placeholder="Enter Username" required>
                    </div>
                    <br />
                    <div class="input-group input-sm">
                        <input type="password" class="form-control" id="password" name="password" placeholder="Enter Password" required>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}" />
                    <br />
                    <div class="form-actions">
                        <input type="submit"
                               class="btn-login" value="Log in">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>