<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %><html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All trucks</title>
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
                    <h2>List of Trucks</h2>
                </div>
                <div class="meta">
                    <time class="published" datetime="">${date}</time>
                    <a href="<c:url value="/manager/listOrders"/>" class="author"><span class="name">${user}</span></a>
                </div>
            </header>
        <table class="table table-hover">
            <a href="<c:url value='/manager/newTruck' />" class="btn btn-success custom-width">Add New Truck</a>
            <br/>
            <br/>
    <tr>
        <td>REG NUMBER</td><td>CONDITION</td><td>MAX NUMBER OF DRIVERS</td><td>CAPACITY (TON)</td><td>CITY</td><td></td><td></td>
    </tr>
    <c:forEach items="${trucks}" var="truck">
        <tr>
            <td>${truck.regNumber}</td>
            <td>${truck.condition}</td>
            <td>${truck.shiftPeriod}</td>
            <td>${truck.capacityTon}</td>
            <td>${truck.city.name}</td>
            <c:choose>
                <c:when test="${truck.order!=null}"></c:when>
                <c:otherwise>
                    <td><a href="<c:url value='/manager/edit-${truck.id}-truck' />" class="btn btn-success custom-width">
                        edit</a></td>
                    <td><a href="<c:url value='/manager/delete-${truck.regNumber}-truck' />" class="btn btn-danger custom-width">
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
                <p>Would you like to create <a href="<c:url value='/manager/newTruck' />">A NEW TRUCK</a>?</p>
            </header>
        </section>
    </section>
</div>
</body>
</html>