<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<script src="/resources/vendor/jquery/jquery.min.js"></script>

<title>Issuing temp pwd</title>
<!-- Custom fonts for this template-->
<link href="/resources/vendor/fontawesome-free/css/all.min.css"
	rel="stylesheet" type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">

<!-- Custom styles for this template-->
<link href="/resources/css/sb-admin-2.css" rel="stylesheet">

<style>
header { margin: 50px auto 0px; }
.logo { text-align: center; }
.logo a { text-decoration: none; color: #1e385a; }
.con { width: 1000px; margin: 0 auto; }
</style>
</head>
<body class="bg-gradient-light">
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
							<div class="col-lg-6 d-none d-lg-block bg-password-image"></div>
							<div class="col-lg-6">
								<div class="p-5">
									<div class="text-center">
										<h1 class="h4 text-gray-900 mb-4">Send by e-mail <br> after issuing temporary pwd</h1>
									</div>

									<form name="tmpPwSendMail" id="tmpPwSendMail" class="user"
										action="/party/tmpPwSendMail" method="POST">
										<input type='hidden' name='${_csrf.parameterName}'
											value='${_csrf.token}'>
										<div class="form-group">
											<div>
												<input id="chkName" class="form-control form-control-user"
													name="name" autocomplete="off" type="text"
													required="required" autofocus="autofocus" maxlength="30"
													placeholder="Please enter name" />
											</div>
										</div>
										<div class="form-group">
											<div>
												<input id="chkLoginId"
													class="form-control form-control-user" name="loginId"
													autocomplete="off" type="text" required="required"
													autofocus="autofocus" maxlength="30"
													placeholder="Please enter the login ID" />
											</div>
										</div>

										<div class="form-group">
											<span> If you have entered the information correctly, </span>
											<div>
												<input type="submit" id="tmpPwSendMailBtn"
													class="btn btn-primary btn-user btn-block"
													value="Send by e-mail" />
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

<script type="text/javascript">
	$(document).ready(function() {

		var csrfHeaderName = "${_csrf.headerName}";
		var csrfToken = "${_csrf.token}";

		//계정 이름 입력창을 벗어나는 순간.... 
		//AJAX방식으로 유일성을 검사하고 통과하지 못하면 경고 이후 다시 이곳으로 focus를 옮길 것임 
		$("#chkName").on("focusout", function(e){
			var partyNameChk = $(this).val();
			if (isEmpty(partyNameChk))
				return;
			$.ajax({
				url:'/party/chkNameExist/' + partyNameChk,
				beforeSend: function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfToken);
				},
				type:'get',
				dataType:'json',	//결과를 json으로 받습니다.
				contentType: "application/json; charset=utf-8",
				success:function(isExist) {
					if (!isExist) {
						alert("Please re-enter");
						$("#chkName").focus();
					}
				},
			});	
		});
		//계정 로그인아이디 입력창을 벗어나는 순간.... 
		//AJAX방식으로 유일성을 검사하고 통과하지 못하면 경고 이후 다시 이곳으로 focus를 옮길 것임 
		$("#chkLoginId").on("focusout", function(e){
			var partyLoginIdChk = $(this).val();
			if (isEmpty(partyLoginIdChk))
				return;
			$.ajax({
				url:'/party/chkLoginIdExist/' + partyLoginIdChk,
				beforeSend: function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfToken);
				},
				type:'get',
				dataType:'json',	//결과를 json으로 받습니다.
				contentType: "application/json; charset=utf-8",
				success:function(isExist) {
					if (!isExist) {
						alert("Please re-enter");
						$("#chkLoginId").focus();
					}
				},
			});	
		});

 		var tmpPwSendMail = $("#tmpPwSendMail");
		$("#tmpPwSendMailBtn").on("click", function(e) {
			e.preventDefault();
			tmpPwSendMail.submit();
		});
	})

	// 넘어온 값이 빈값인지 체크합니다.
	// !value 하면 생기는 논리적 오류를 제거하기 위해
	// 명시적으로 value == 사용
	// [], {} 도 빈값으로 처리
	var isEmpty = function(value) {
		if (value == ""
				|| value == null
				|| value == undefined
				|| (value != null && typeof value == "object" && !Object
						.keys(value).length)) {
			return true
		} else {
			return false
		}
	};
</script>
