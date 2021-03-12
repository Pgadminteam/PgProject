<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<script src="/resources/vendor/jquery/jquery.min.js"></script>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<base href="/">
<title>Register</title>

<!-- Custom fonts for this template-->
<link href="/resources/vendor/fontawesome-free/css/all.min.css"
	rel="stylesheet" type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">

<!-- Custom styles for this template-->
<link href="/resources/css/sb-admin-2.css" rel="stylesheet">

</head>
<style>
header { margin: 50px auto 0px; }
.logo { text-align: center; }
.logo a { text-decoration: none; color: #1e385a; }
</style>

<body class="bg-gradient-primary">
	<header>
		<h1 class="logo">
			<a href="/"><strong>Personal Gym</strong></a>
		</h1>
	</header>

	<div class="container">

		<div class="card o-hidden border-0 shadow-lg my-5">
			<div class="card-body p-0">
				<!-- Nested Row within Card Body -->
				<div class="row">
					<div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
					<div class="col-lg-7">
						<div class="p-5">
							<div class="text-center">
								<h1 class="h4 text-gray-900 mb-4">Create an Account!</h1>
							</div>
							<form id="frmParty" action="/party/registerParty" method="post"
								class="user">
								<input type='hidden' name='${_csrf.parameterName}'
									value='${_csrf.token}'>
									<%@include file="./include/partyCommon.jsp"%>
								<hr>
								<button id='btnRegistParty' type="submit"
									class="btn btn-primary btn-user btn-block">Register
									Account</button>
								<button type="reset" class="btn btn-dark btn-user btn-block">
									Clear all entered values</button>
							</form>
							<hr>
							<div class="text-center">
								<a href="/customLogin">Already have an
									account? Login!</a>
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
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfToken = "${_csrf.token}";

		//계정 이름 입력창을 벗어나는 순간.... 
		//AJAX방식으로 유일성을 검사하고 통과하지 못하면 경고 이후 다시 이곳으로 focus를 옮길 것임 
		$("#inputLoginId").on("focusout", function(e){
			var loginId2ChkDup = $(this).val();
			// 정규 표현식 글자수 6~30 숫자,기호(.),영어 3가지 사용
			var idPattern = /^[a-z0-9~.]{6,30}$/;
			if (isEmpty(loginId2ChkDup))
				return;
			else if(idPattern.test(loginId2ChkDup) != true){
				alert("ID can only be used with 6~30 characters");
				return;
			}

			$.ajax({
				url:'/party/chkIdDup/' + loginId2ChkDup,
				beforeSend: function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfToken);
				},
				type:'get',
				dataType:'json',	//결과를 json으로 받습니다.
				success:function(isDup) {
					if (isDup) {
						alert("Duplicate ID Please re-enter");
						$("#inputLoginId").focus();
					}
				},
	            error: function(xhr, status, erMsg){
					if (cbError) {
						cbError(erMsg);
					}
	            }
			});
			
		});

		//닉네임 입력창을 벗어나는 순간.... 
		//AJAX방식으로 유일성을 검사하고 통과하지 못하면 경고 이후 다시 이곳으로 focus를 옮길 것임 
		$("#inputNickname").on("focusout", function(e){
			var loginNick2ChkDup = $(this).val();
			// 정규 표현식 글자수 2~10 숫자,영어 2가지 사용
			var nickPattern = /^[a-zA-Z0-9]{2,10}$/;
			if (isEmpty(loginNick2ChkDup))
				return;
			else if(nickPattern.test(loginNick2ChkDup) != true){
			      alert("Nickname can only be used with 2~10 characters");
		    }

			$.ajax({
				url:'/party/chkNickDup/' + loginNick2ChkDup,
				beforeSend: function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfToken);
				},
				type:'get',
				dataType:'json',	//결과를 json으로 받습니다.
				success:function(isDup) {
					if (isDup) {
						alert("Duplicate Nickname Enter another Nickname");
						$("#inputNickname").focus();
					}
				},
	            error: function(xhr, status, erMsg){
					if (cbError) {
						cbError(erMsg);
					}
	            }
			});
		});

		$("#pw2").on("focusout", function isSame() {
		    var pw1 = $("#pw1").val();
		    var pw2 = $("#pw2").val();
			// 정규 표현식 숫자, 특문 각 1회 이상, 영문은 2개 이상 사용
		    var pwPattern = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,15}$/; //글자수 8~15
			
		    if (pw1.length < 8 || pw1.length > 15) {
		        window.alert('The password can only be used with 8~15 characters.');
		        document.getElementById('pw1').value=document.getElementById('pw2').value='';
		        $("#pw1").focus();
		    }
		    if(document.getElementById('pw1').value!='' && document.getElementById('pw2').value!='') {
		        if(document.getElementById('pw1').value!=document.getElementById('pw2').value) {
		        	window.alert('Passwords do not match');
		        	$("#pw2").focus();
		        }
		    }
		    if(pwPattern.test(document.getElementById('pw1').value) != true){
			    alert("Password doesn't fit the format");
		        document.getElementById('pw1').value=document.getElementById('pw2').value='';
		        $("#pw1").focus();
			}
		});

		var frmParty = $("#frmParty");
		$("#btnRegistParty").on("click", function(e){
			e.preventDefault();
			frmParty.submit();
		});
	})
	
 
// 넘어온 값이 빈값인지 체크합니다.
// !value 하면 생기는 논리적 오류를 제거하기 위해
// 명시적으로 value == 사용
// [], {} 도 빈값으로 처리
var isEmpty = function(value){
  if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
    return true
  }else{
    return false
  }
};

//영문만 사용가능하게 하는 함수
function onlyAlphabet(ele) {
	  ele.value = ele.value.replace(/[^\\!-z]/gi,"");
}
</script>
