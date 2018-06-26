<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>List Of Drivers</title>
    <meta charset="utf-8" />

    <!--[if lte IE 8]><script src="/static/js/ie/html5shiv.js"></script><![endif]-->
    <link href="<c:url value="/static/css/main.css"  />" rel="stylesheet" />
</head>
<body>
<div id="wrapper">
    <!-- Header -->
    <header id="header">
        <h1><a href="<c:url value="/manager/listOrders"/>">LogiWeb</a></h1>
        <nav class="links">
            <ul>
                <li><a href="<c:url value="/manager/listDrivers"/> ">Drivers</a></li>
                <li><a href="<c:url value="/manager/listTrucks"/>">Trucks</a></li>
                <li><a href="<c:url value="/manager/listOrders"/>">Orders</a></li>
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
                    <h2>List of Drivers</h2>
                </div>
                <div class="meta">
                    <time class="published" datetime="">${date}</time>
                    <a href="<c:url value="/manager/listOrders"/>" class="author"><span class="name">${user}</span></a>
                </div>
            </header>
        <table class="table table-hover">
            <a href="<c:url value='/manager/newDriver' />"
               class="btn btn-success custom-width">Add New Driver</a>
            <br/>
            <br/>
            <tr>
                <td>NAME</td><td>SURNAME</td><td>E-MAIL</td><td>WORKED THIS MONTH</td><td>STATUS</td><td>CITY</td><td></td><td></td>
            </tr>
            <c:forEach items="${drivers}" var="driver">
                <tr>
                    <td>${driver.name}</td>
                    <td>${driver.surname}</td>
                    <td>${driver.email}</td>
                    <td>${driver.workedThisMonth}</td>
                    <td>${driver.status}</td>
                    <td>${driver.city.name}</td>
                    <c:choose>
                        <c:when test="${driver.currentTruck!=null}"></c:when>
                        <c:otherwise>
                            <td><a href="<c:url value='/manager/edit-${driver.id}-driver' />"class="btn btn-success custom-width">
                                edit</a></td>
                            <td><a href="<c:url value='/manager/delete-${driver.id}-driver'/>"class="btn btn-danger custom-width">
                                delete</a></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
        </table>
        </article>
    </div>

    <!-- Sidebar -->
    <section id="sidebar">

        <!-- Intro -->
        <section id="intro">
            <a href="<c:url value="/manager/listOrders"/>" class="logo">
                <img src="<c:url value="/static/images/logo.jpg"/>" alt="" /></a>
            <header>
                <h2>LogiWeb</h2>
                <p>Would you like to register <a href="<c:url value='/manager/newDriver' />">A NEW DRIVER</a>?</p>
            </header>
        </section>
    </section>
</div>
</body>
</html>