<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
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
<title>Personal Gym Login</title>
<script type="text/javascript">
// 권한이 없거나, 로그아웃 시 alert으로 내용을 띄우기 위해 사용
if(!"${error}" && !"${logout}") {
} else if("${error}") {
	alert("${error}");
} else if("${logout}") {
	alert("${logout}");
}
</script>

<!-- Custom fonts for this template-->
<link href="\resources\vendor\fontawesome-free\css\all.min.css"
	rel="stylesheet" type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">

<!-- Custom styles for this template-->
<link href="\resources\css\sb-admin-2.css" rel="stylesheet">

<style>
body { color: #b0caec; }
header { margin: 50px auto 0px; }
.logo { text-align: center; }
.logo a { text-decoration: none; color: #1e385a; }
</style>
</head>
<body>
	<header>
		<h1 class="logo">
			<a href="/"><strong>Personal Gym</strong></a>
		</h1>
	</header>
	<div class="container">

		<!-- Outer Row -->
		<div class="row justify-content-center">

			<div class="col-xl-10 col-lg-12 col-md-9">

				<div class="card o-hidden border-0 shadow-lg my-5">
					<div class="card-body p-0">
						<!-- Nested Row within Card Body -->
						<div class="row">
							<div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
							<div class="col-lg-6">
								<div class="p-5">
									<div class="text-center">
										<h1 class="h4 text-gray-900 mb-4">Welcome!</h1>
									</div>
									<form class="user" method="post" action="/login" role="form">
										<fieldset>
											<div class="form-group">
												<input type="text" name='username' class="form-control form-control-user"
													id="exampleInputEmail" aria-describedby="emailHelp"
													placeholder="Enter ID" autofocus>
											</div>
											<div class="form-group">
												<input type="password"
													class="form-control form-control-user"
													id="exampleInputPassword" placeholder="Password"
													name='password'>
											</div>

											<div class="form-group checkbox">
												<div class="custom-control custom-checkbox small">
													<input type="checkbox" class="custom-control-input"
														id="customCheck" name='remember-me'> <label
														class="custom-control-label" for="customCheck">
														Remember Me</label>
												</div>
											</div>

										</fieldset>
										<a href="/" class="btn btn-primary btn-user btn-block"> Login </a>
										<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'> 
										<hr>
										<a href="/party/openRegisterParty"
											class="btn btn-facebook btn-user btn-block">Register
										</a>

									</form>
									
									<hr>
									<div class="text-center">
										<a href="/party/openFindPassword" title="findPassword" id="findPassword"> Forgot Password? </a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>

		</div>

	</div>

	<!-- Bootstrap core JavaScript-->
	<script src="/resources/vendor/jquery/jquery.min.js"></script>
	<script src="/resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

	<!-- Core plugin JavaScript-->
	<script src="/resources/vendor/jquery-easing/jquery.easing.min.js"></script>

	<!-- Custom scripts for all pages-->
	<script src="/resources/js/sb-admin-2.min.js"></script>


</body>
</html>

<script type="text/javascript">
$(document).ready(function() {
	// 로그인 버튼 클릭 시 form에 입력된 input data submit
	$(".btn-primary").on("click", function(e) {
		e.preventDefault();
		$("form").submit();
	});
})
</script>