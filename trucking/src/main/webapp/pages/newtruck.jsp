<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Truck Registration Form</title>
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
                    <h2>Add Truck From</h2>
                </div>
                <div class="meta">
                    <time class="published" datetime="">${date}</time>
                    <a href="<c:url value="/manager/listOrders"/>" class="author"><span class="name">${user}</span></a>
                </div>
            </header>
<form:form method="POST" modelAttribute="truck" >
    <div class="row">
        <div class="form-group col-md-12">
            <label class="col-md-3 control-lable" for="reg_number">Registration number</label>
            <div class="col-md-7">
                <form:input type="text" path="regNumber" id="reg_number" placeholder="AA00000" class="form-control input-sm"/>
                <div class="has-error">
                    <form:errors path="regNumber" class="help-inline"/>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-md-12">
            <label class="col-md-3 control-lable" for="shift_period">Max number of drivers</label>
            <div class="col-md-7">
                <form:input type="text" path="shiftPeriod" id="shift_period" class="form-control input-sm"/>
                <div class="has-error">
                    <form:errors path="shiftPeriod" class="help-inline"/>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-md-12">
            <label class="col-md-3 control-lable" for="capacity">Capacity (ton)</label>
            <div class="col-md-7">
                <form:input type="text" path="capacityTon" id="capacity" placeholder="ton" class="form-control input-sm"/>
                <div class="has-error">
                    <form:errors path="capacityTon" class="help-inline"/>
                </div>
            </div>
        </div>
    </div>


    <div class="row">
        <div class="form-group col-md-12">
            <label class="col-md-3 control-lable" for="city">City</label>
            <div class="col-md-7">
                <form:select id ="city" path="city" items="${cities}" itemLabel="name" itemValue="id" class="form-control input-sm"
                            />
            </div>
        </div>
    </div>


    <div class="row">
        <div class="form-group col-md-12">
            <label class="col-md-3 control-lable" for="condition">Condition</label>
            <div class="col-md-7">
                <form:select id ="status" path="condition" items="${truckConditions}" class="form-control input-sm"/>
            </div>
        </div>
    </div>

    <br/>
    <div class="row">
        <div class="form-actions floatRight">
            <c:choose>
                <c:when test="${edit}">
                    <input type="submit" class="btn btn-primary btn-sm" value="Update"/>
                </c:when>
                <c:otherwise>
                    <input type="submit" class="btn btn-primary btn-sm" value="Register"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</form:form>
<br/>
<br/>
        <a href="<c:url value='/manager/listTrucks' />" class="btn btn-success custom-width">List of All Trucks</a>
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
                <p>Would you like to create <a href="<c:url value='/manager/newTruck' />">A NEW TRUCK</a>
                    <br/> Or see <a href="<c:url value='/manager/listTrucks' />" >List of All Trucks</a>?</p>
            </header>
        </section>
    </section>
</div>
</body>
</html>