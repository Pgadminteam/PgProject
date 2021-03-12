<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false"%>
<%@ page import="www.dream.com.framework.display.*"%>
<%@ page import="www.dream.com.party.model.*"%>

<%@include file="../includes/header.jsp"%>
<!-- Begin Page Content -->
<div class="container-fluid">

	<!-- Page Heading -->
	<div class="text-center">
		<h1 class="h3 mb-2 text-gray-800">Change Password</h1>
	</div>
	<!-- DataTales Example -->
	<div class="card shadow mb-4" style="width: 1000px; margin: 0 auto;">
		<div class="card-body p-0">
			<div class="row">
				<div class="col-lg-5 d-none d-lg-block bg-change-image"></div>
				<div class="col-lg-7">
					<div class="p-5">
						<form id="frmChangePW" action="/party/changePW" method="post" class="user">
							<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'>
							<%@include file="./include/partyCommon.jsp"%>
							<button id='btnChangePW' type="submit" class="btn btn-info">
								Change Password</button>
						 <a href="#" onClick="history.go(-1); return false;" class="btn btn-dark" id="cancel">Cancel</a>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>
<!-- /.container-fluid -->

<%@include file="../includes/footer.jsp"%>
</body>
</html>

<script type="text/javascript">
	$(document).ready(function() {
		//create read update remove + change
		setOprationMode("change");
		
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfToken = "${_csrf.token}";

		$("#pw1").on("focusout", function isSame() {
		    var pw1 = $("#pw1").val();
			// 정규 표현식: 숫자, 특문 각 1회 이상, 영문은 2개 이상 사용, 글자수 8~15제한
		    var pwPattern = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,15}$/;

		    if (isEmpty(pw1))
				return;
		    else if (pw1.length < 8 || pw1.length > 15) {
		        window.alert('The password can only be used with 8~15 characters.');
		        document.getElementById('pw1').value='';
		        $("#pw1").focus();
		    } else if(pwPattern.test(document.getElementById('pw1').value) != true){
			    alert("Password doesn't fit the format");
		        document.getElementById('pw1').value='';
		        $("#pw1").focus();
			}
		});
		
		var frmChangePW = $("#frmChangePW");
		$("#btnChangePW").on("click", function(e) {
			e.preventDefault();
			frmChangePW.submit();
		});
	})
	
// 넘어온 값이 빈값인지 체크합니다.
// !value 하면 생기는 논리오류제거는
// 명시적으로 value == 사용합니다.
// [], {} 도 빈값으로 처리합니다.
var isEmpty = function(value){
  if( value == "" || value == null || value == undefined || 
		  ( value != null && typeof value == "object" && !Object.keys(value).length ) ){
    return true
  }else{
    return false
  }
};
</script>
