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
		<h1 class="h3 mb-2 text-gray-800">Profile</h1>
	</div>
	<!-- DataTales Example -->
	<div class="card shadow mb-4" style="width: 1000px; margin: 0 auto;">
		<div class="card-body p-0">
			<div class="row">
				<div class="col-lg-5 d-none d-lg-block bg-detail-image"></div>
				<div class="col-lg-7">
					<div class="p-5">
						<form class="user">
							<%@include file="./include/partyCommon.jsp"%>
						</form>
						<!-- data : 요소에 추가적으로 변수와 정보를 마음대로 넣을 수 있는 장치. *** 주의사항:웹은 대소문자 구분 없어 -->
						<form id='frmParty' method="get" action="/party/modifyParty"
							class="user">
							<button id="modify" data-oper="modify" data-id="${newbie.id}"
								class="btn btn-warning btnControl">Modify</button>
							<button id="withdraw" data-oper="withdraw" data-id="${newbie.id}"
							class="btn btn-danger btnControl">Withdraw</button>
							<button id="change" data-oper="change" data-id="${newbie.id}"
							class="btn btn-info btnControl">Change Password</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /.container-fluid -->

<%@include file="../includes/footer.jsp"%>

<script type="text/javascript"
	src="\resources\js\util\dateGapDisplayService.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		//create read update remove + change
		setOprationMode("read");

		var frmParty = $("#frmParty");

		$(".btnControl").on("click",function(e) {
			e.preventDefault();
			// 버튼 클릭 시 data-oper의 값을 읽어서 withdraw / change 기능을 if로 제어해 사용함
			var oper = $(this).data("oper");
			
			if (oper === "withdraw") {
				frmParty.attr("action", "/party/openRemoveParty");
			} else if (oper === "change") {
				frmParty.attr("action", "/party/openChangePW");
			}
				
			frmParty.append("<input type='hidden' name='id' value='" + $(this).data("id") + "'>");
			frmParty.submit();
		});

	})
</script>