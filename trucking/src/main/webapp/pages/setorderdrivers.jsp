<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Set Drivers</title>
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
                    <h2>Set Drivers <br/> For Order ${order.id}</h2>
                </div>
                <div class="meta">
                    <time class="published" datetime="">${date}</time>
                    <a href="<c:url value="/manager/listOrders"/>" class="author"><span class="name">${user}</span></a>
                </div>
            </header>
            <c:choose>
            <c:when test="${orderDrivers.size()!=0}">
        <table class="table table-hover">
            <tr>
                <td>NAME</td><td>SURNAME</td><td>WORKED THIS MONTH</td>
            </tr>
            <c:forEach items="${orderDrivers}" var="orderDriver">
                <tr>
                    <td>${orderDriver.name}</td>
                    <td>${orderDriver.surname}</td>
                    <td>${orderDriver.workedThisMonth}</td>
                </tr>
            </c:forEach>
        </table>
        <br/>
            </c:when>
            <c:otherwise/>
        </c:choose>
        <h2>Add Drivers Form</h2>
        <form:form method="POST" modelAttribute="driver">
            <c:choose>
            <c:when test="${orderDrivers.size()<maxDrivers}">
            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="id">Choose driver</label>
                    <div class="col-md-7">
                        <form:select path="id" items="${drivers}" class="form-control input-sm"
                                     itemLabel="name" itemValue="id"/>
                    </div>
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="form-actions floatRight">
                    <input type="submit" class="btn btn-primary btn-sm" value="Add Driver"/>
                    </c:when>
                    <c:otherwise>
                    <div class="row">
                        <div class="form-actions floatRight">

                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${orderDrivers.size()!=0}">
                        <br/>
                        <br/>
                        <a href="<c:url value="/manager/${order.id}/complete"/> " class="btn btn-success custom-width">
                            Complete</a>
                            </c:when>
                            <c:otherwise/>
                        </c:choose>
                        <br/>
                        <br/>
                        <a href="<c:url value='/manager/${order.id}/setOrderTruck' />" class="btn btn-success custom-width">
                            Change Truck</a>
                        <br/>
                        <br/>
                        <a href="<c:url value="/manager/listOrders"/> " class="btn btn-success custom-width">
                            All Orders</a>
                    </div>
                </div>
                </div>
            </div>
            </form:form>
    </article>
    </div>

</div>
</body>
</html>