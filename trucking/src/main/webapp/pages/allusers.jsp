<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %><html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Users List</title>
    <meta charset="utf-8" />

    <!--[if lte IE 8]><script src="/static/js/ie/html5shiv.js"></script><![endif]-->
    <link href="<c:url value="/static/css/main.css"  />" rel="stylesheet" />
</head>
<body>
<div id="wrapper">
    <!-- Header -->
    <header id="header">
        <h1><a href="<c:url value="/admin/listUsers"/>">LogiWeb</a></h1>
        <nav class="links">
            <ul>
                <li><a href="<c:url value='/admin/listUsers' />">Users</a></li>
                <li><a href="<c:url value='/admin/newUser' />">Register User</a></li>
                <li><a href="<c:url value="/logout" />">Logout</a></li>
            </ul>
        </nav>
    </header>

    <!-- Main -->
    <div id="main">
        <!-- Post -->
        <article class="post">
            <header>
                <div class="title">
                    <h2>List of Users</h2>
                </div>
                <div class="meta">
                    <time class="published" datetime="">${date}</time>
                    <a href="<c:url value="/admin/listUsers"/>" class="author"><span class="name">${currentUser}</span></a>
                </div>
            </header>
        <table class="table table-hover">
            <thead>
            <tr>
                <!--<th>Email</th>-->
                <th>LOGIN</th>
                <th>E-MAIL</th>
                <th>ROLE</th>
                <th width="100"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user">
                <tr>
                    <!--<td>$//{user.email}</td>-->
                    <td>${user.login}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <c:choose>
                        <c:when test="${user.driver!=null}"></c:when>
                        <c:otherwise>
                        <td><a href="<c:url value='/admin/delete-user-${user.login}' />" class="btn btn-danger custom-width">
                            delete</a></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    <div class="well">
        <a href="<c:url value='/admin/newUser' />" class="btn btn-success custom-width"> Add New User</a>
    </div>
        </article>
    </div>

    <!-- Sidebar -->
    <section id="sidebar">

        <!-- Intro -->
        <section id="intro">
            <a href="<c:url value="/admin/listUsers"/>" class="logo">
                <img src="<c:url value="/static/images/logo.jpg"/>" alt="" /></a>
            <header>
                <h2>LogiWeb</h2>
                <p>Would you like to create <a href="<c:url value='/admin/newUser' />">A NEW USER</a>?</p>
            </header>
        </section>
    </section>
</div>
</body>
</html>