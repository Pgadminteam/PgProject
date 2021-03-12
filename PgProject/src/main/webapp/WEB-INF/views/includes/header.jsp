<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>Personal Gym</title>

<!-- Custom fonts for this template -->
<link href="\resources\vendor\fontawesome-free\css\all.min.css"
	rel="stylesheet" type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="/resources/css/sb-admin-2.css" rel="stylesheet">

<!-- Custom styles for this page -->
<link href="/resources/vendor/datatables/dataTables.bootstrap4.css"
	rel="stylesheet">
	
</head>

<body id="page-top">
<script src="\resources\vendor\jquery\jquery.min.js"></script>
	<!-- Page Wrapper -->
	<div id="wrapper">

		<!-- Sidebar -->
		<ul
			class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion toggled"
			id="accordionSidebar">

			<!-- Sidebar - Brand -->
			<a
				class="sidebar-brand d-flex align-items-center justify-content-center"
				href="/"> <!-- 앱아이콘 만들면 여기에 넣을것 -->
				<div class="sidebar-brand-icon">
					 <img alt="logoImg" src="/resources/img/logoImg.svg" id="logoImg" style="width:40px; height:40px;">
				</div>
			</a>

			<!-- Divider -->
			<hr class="sidebar-divider">

			<!-- Nav Item - routine -->
			<li class="nav-item"><a class="nav-link"
				href="/post/listPost?boardId=1"> <i
					class="fas fa-fw fa-list-ul"></i> <span>Bulletin</span>
			</a></li>

			<!-- Divider -->
			<hr class="sidebar-divider">

			<!-- Nav Item - notice -->
			<li class="nav-item"><a class="nav-link"
				href="/post/listPost?boardId=2"> <i
					class="fas fa-fw fa-bullhorn"></i> <span>Notice</span>
			</a></li>

		</ul>
		<!-- End of Sidebar -->

		<!-- Content Wrapper -->
		<div id="content-wrapper" class="d-flex flex-column">

			<!-- Main Content -->
			<div id="content">

				<!-- Topbar -->
				<nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
					<a href="/" id="logoArea">
						<img alt="logoTxt" src="/resources/img/logotxt.png" id="logoTxt">
					</a>
					<!-- 
					 -->
					<!-- Sidebar Toggle (Topbar) -->
					<form class="form-inline">
						<button id="sidebarToggleTop"
							class="btn btn-link d-md-none rounded-circle mr-3">
							<i class="fa fa-bars"></i>
						</button>
					</form>

					<!-- Topbar Navbar -->
					<ul class="navbar-nav ml-auto" id="loginArea" style="margin-left: 0;">

						<!-- Nav Item - Search Dropdown (Visible Only XS) -->
						<li class="nav-item dropdown no-arrow d-sm-none">
							<!-- Dropdown - Messages -->
							<div
								class="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in"
								aria-labelledby="searchDropdown">
								<form class="form-inline mr-auto w-100 navbar-search">
									<div class="input-group">
										<input type="text"
											class="form-control bg-light border-0 small"
											placeholder="Search for..." aria-label="Search"
											aria-describedby="basic-addon2">
										<div class="input-group-append">
											<button class="btn btn-primary" type="button">
												<i class="fas fa-search fa-sm"></i>
											</button>
										</div>
									</div>
								</form>
							</div>
						</li>


						<div class="topbar-divider d-none d-sm-block"></div>

						<!-- Nav Item - User Information -->
						<li class="nav-item dropdown no-arrow">
								<sec:authorize access="isAnonymous()">
									<a id="customLogin"class="mr-2 d-none d-lg-inline text-lg text-gray-600 " 
									   href="/customLogin">&nbsp;&nbsp;<i class="fas fa-sign-in-alt"></i>
									   &nbsp;Log in</a>
								</sec:authorize>
						<a class="nav-link dropdown-toggle" href="#" id="userDropdown"
							role="button" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="false">
							<span class="mr-2 d-none d-lg-inline text-gray-600" style="font-size:25px;">
								<sec:authorize access="isAuthenticated()">
									<sec:authentication property="principal.loginUser.nickname" />
								</sec:authorize>
							</span>

						</a> <!-- Dropdown - User Information -->
							<div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
								aria-labelledby="userDropdown">
								<sec:authorize access="isAuthenticated()">
									<sec:authorize access="hasRole('ROLE_ADMIN')">
									<a class="dropdown-item" href="/party/listParty" id="listParty">
	                                    <i class="fas fa-list fa-sm fa-fw mr-2 text-gray-400"></i>
	                                    See Member List
	                                </a>
	                                <div class="dropdown-divider"></div>
									</sec:authorize>
									<sec:authorize access="hasRole('ROLE_MEMBER')">
									<a class="dropdown-item" href="/party/partyDetail?id=<sec:authentication property='principal.loginUser.id'></sec:authentication>"
										id="partyDetail">
	                                    <i class="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
	                                    Profile
	                                </a>
									</sec:authorize>
								</sec:authorize>
                                <div class="dropdown-divider"></div>
								<a class="dropdown-item" href="#" data-toggle="modal"
									data-target="#logoutModal"> <i
									class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
									Logout
								</a>
							</div></li>

					</ul>

				</nav>
				<!-- End of Topbar -->		