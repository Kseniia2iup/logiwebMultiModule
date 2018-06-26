<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User Registration Form</title>
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
					<h2>User Registration From</h2>
				</div>
				<div class="meta">
					<time class="published" datetime="">${date}</time>
					<a href="<c:url value="/admin/listUsers"/>" class="author"><span class="name">${currentUser}</span></a>
				</div>
			</header>
	<form:form method="POST" modelAttribute="user" class="form-horizontal">
		<form:input type="hidden" path="id" id="id"/>

		<div class="row">
			<div class="form-group col-md-12">
				<label class="col-md-3 control-lable" for="login">LOGIN</label>
				<div class="col-md-7">
							<form:input type="text" path="login" id="login" placeholder="min 3 symbols" class="form-control input-sm" />
							<div class="has-error">
								<form:errors path="login" class="help-inline"/>
							</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="form-group col-md-12">
				<label class="col-md-3 control-lable" for="password">PASSWORD</label>
				<div class="col-md-7">
					<form:input type="password" path="password" id="password" placeholder="min 5 symbols" class="form-control input-sm"/>
					<div class="has-error">
						<form:errors path="password" class="help-inline"/>
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="form-group col-md-12">
				<label class="col-md-3 control-lable" for="email">Email</label>
				<div class="col-md-7">
					<form:input type="text" path="email" id="email" class="form-control input-sm" />
					<div class="has-error">
						<form:errors path="email" class="help-inline"/>
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="form-group col-md-12">
				<label class="col-md-3 control-lable" for="role">ROLE</label>
				<div class="col-md-7">
					<form:select path="role" id="role" type="role" items="${adminRegistrationRoles}" class="form-control input-sm" />
					<div class="has-error">
						<form:errors path="role" class="help-inline"/>
					</div>
				</div>
			</div>
		</div>
		<br/>
		<div class="row">
			<div class="form-actions floatRight">
						<input type="submit" value="Register" class="btn btn-primary btn-sm"/> or <a
							href="<c:url value='/admin/listUsers' />" class="btn btn-success custom-width">Cancel</a>
			</div>
		</div>
	</form:form>
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