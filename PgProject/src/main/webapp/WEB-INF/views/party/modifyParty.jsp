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
		<h1 class="h3 mb-2 text-gray-800">Modify Profile</h1>
	</div>
	<!-- DataTales Example -->
	<div class="card shadow mb-4" style="width: 1000px; margin: 0 auto;">
		<div class="card-body p-0">
			<div class="row">
				<div class="col-lg-5 d-none d-lg-block bg-modify-image"></div>
				<div class="col-lg-7">
					<div class="p-5">
                            <form id="frmModifyParty" action="/party/modifyParty" method="post" class="user">
                            <input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'>
                            	<%@include file="./include/partyCommon.jsp"%>
                            	 <p style="font-size: 20px;">If you modify your information, You log in again.</p>
                                <button id='btnModifyParty' type="submit" class="btn btn-primary">
                                	Modify Account</button>
                                <button type="reset" class="btn btn-dark">
                                	Reset to original value</button>
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
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfToken = "${_csrf.token}";
		var partyId = "${newbie.id}";
		
		//create read update remove + change
		setOprationMode("update");

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
				url:'/party/chkNickDupInModify/'+ partyId +'/'+loginNick2ChkDup,
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
				}
				,error:function(request,status,error){
				    alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);}

			});
		});

		var frmModifyParty = $("#frmModifyParty");
		$("#btnModifyParty").on("click", function(e){
			e.preventDefault();
			frmModifyParty.submit();
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
