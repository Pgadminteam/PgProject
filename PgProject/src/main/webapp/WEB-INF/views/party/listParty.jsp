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
	<h1 class="h3 mb-2 text-gray-800"></h1>

	<!-- DataTales Example -->
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'>
			<button id='btnDelete' type="submit" class="btn btn-danger">
				<i class="fas fa-user-slash"></i>Withdraw Member</button>
		</div>
		<div class="card-body">

			<form id='frmPaging' action='/party/listParty' method="get">
				<input type="hidden" name="pageNum" value="${criteria.pageNum}">
			</form>

			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%"
					cellspacing="0" data-order=''>
					<thead>
						<tr>
							<th>Check</th>
							<th>ID</th>
							<th>Login ID</th>
							<th>Name</th>
							<th>Nickname</th>
							<th>BirthDate</th>
							<th>RegistrationDate</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="party" items="${listParty}">
							<tr>
								<td><input type="checkbox" name="mngTargetParty"
									data-party_id="${party.id}"></td>
								<td>${party.id}</a></td>
								<td>${party.loginId}</a></td>
								<td>${party.name}</a></td>
								<td>${party.nickname}</a></td>
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${party.birthDate}" /></td>
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${party.regDate}" /></td>
							</tr>
						</c:forEach>
					</tbody>

				</table>

				<!-- Pagination -->
				<div class='pull-right'>
					<ul id='ulPagination' class='pagination'>
						<c:if test="${criteria.hasPrev}">
							<li class="page-item previous"><a class='page-link'
								href="${criteria.startPage - 1}">&lt;&lt;</a></li>
						</c:if>
						<c:forEach var="num" begin="${criteria.startPage}"
							end="${criteria.endPage}">
							<li class='page-item ${criteria.pageNum == num ? "active" : ""}'>
								<a class='page-link' href="${num}">${num}</a>
							</li>
						</c:forEach>
						<c:if test="${criteria.hasNext}">
							<li class="page-item next"><a class='page-link'
								href="${criteria.endPage + 1}">&gt;&gt;</a></li>
						</c:if>
					</ul>
				</div>
				<!-- End Pagination -->
			</div>
		</div>
	</div>

</div>
<!-- /.container-fluid -->

</div>
<!-- End of Main Content -->

<%@include file="../includes/footer.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfToken = "${_csrf.token}";

		$("#btnDelete").on("click", function(e) {
			e.preventDefault();

			var listTargetParty = document.getElementsByName("mngTargetParty");
			var ltp_length = listTargetParty.length;

			var arrTargetParty = new Array();
			var checkedAmount = 0;

			for (i = 0; i < ltp_length; i++) {
				if (listTargetParty[i].checked == true) {
					arrTargetParty[checkedAmount++] = $(listTargetParty[i]).data('party_id');
				}
			}
			// ajax 이용해 배열로 받은 탈퇴회원 id 넘김 성공시 alert 함께 화면 reload
			$.ajax({
			    method: 'POST',
				url: '/party/removeAdminParty',
				beforeSend: function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfToken);
				},
				traditional: true,
			    data: {'arrTargetParty': arrTargetParty},
	            success : function(data){
		            alert("The selected member has been withdrawn");
	            	window.location.href = "/party/listParty"
	            }
	        });
			
		});

		var frmPaging = $("#frmPaging");

		$("#ulPagination a").on("click", function(e) {
			e.preventDefault();
			$("input[name='pageNum']").val($(this).attr("href"));
			frmPaging.submit();
		});

	})
</script>
