<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Set status</title>
    <meta charset="utf-8" />

    <!--[if lte IE 8]><script src="/static/js/ie/html5shiv.js"></script><![endif]-->
    <link href="<c:url value="/static/css/main.css"  />" rel="stylesheet" />
</head>
<body>
<div id="wrapper">
    <!-- Header -->
    <header id="header">
        <h1><a href="<c:url value="/driver"/>">LogiWeb</a></h1>
        <nav class="links">
            <ul>
                <li><a href="<c:url value="/driver"/> ">Home</a></li>
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
                    <h2>Set New Status Of Work</h2>
                </div>
                <div class="meta">
                    <time class="published" datetime="">${date}</time>
                    <a href="<c:url value="/driver"/>" class="author"><span class="name">${user}</span></a>
                </div>
            </header>
        <form:form method="POST" modelAttribute="driver">

            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3 control-lable" for="status">Choose truck</label>
                    <div class="col-md-7">
                        <form:select path="status" items="${driverStatuses}" class="form-control input-sm"/>
                    </div>
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="form-actions floatRight">
                    <input type="submit" class="btn btn-primary btn-sm" value="Set new Status"/>
                    <br/>
                    <br/>
                    <a href="<c:url value="/driver"/> " class="btn btn-danger custom-width">
                        Return</a>
                </div>
            </div>
        </form:form>
        </article>
    </div>

    <!-- Sidebar -->
    <section id="sidebar">

        <!-- Intro -->
        <section id="intro">
            <a href="<c:url value="/driver"/>" class="logo">
                <img src="<c:url value="/static/images/logo.jpg"/>" alt="" /></a>
            <header>
                <h2>LogiWeb</h2>
                <p>Hello, Dear Driver!
                    <br/>
                    We hope you are doing well and wish you a happy journey!</p>
            </header>
        </section>
    </section>
</div>
</body>
</html>