<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Set Truck</title>
    <meta charset="utf-8" />

    <!--[if lte IE 8]><script src="/static/js/ie/html5shiv.js"></script><![endif]-->
    <link href="<c:url value="/static/css/custom.css"  />" rel="stylesheet" />
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
                    <h2>Add Truck<br/> For Order ${order.id}</h2>
                </div>
                <div class="meta">
                    <time class="published" datetime="">${date}</time>
                    <a href="<c:url value="/manager/listOrders"/>" class="author"><span class="name">${user_}</span></a>
                </div>
            </header>
        <form:form method="POST" modelAttribute="order">

            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="truck">Choose truck</label>
                    <div class="col-md-7">
                        <form:select path="truck" items="${trucks}" class="form-control input-sm"
                                     itemLabel="regNumber" itemValue="id"/>
                    </div>
                </div>
            </div>
            <br />
            <div class="row">
                <div class="form-actions floatRight">
                    <input type="submit" class="btn btn-primary btn-sm" value="Add Truck"/>
                    <br/>
                    <br/>
                    <a href="<c:url value="/manager/listOrders"/> " class="btn btn-success custom-width">
                        All Orders</a>
                </div>
            </div>
            </form:form>
    </div></div>
    </article>
</div>

    </div>
</body>
</html>