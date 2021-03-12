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
		<h1 class="h3 mb-2 text-gray-800">Withdraw</h1>
	</div>
	<!-- DataTales Example -->
	<div class="card shadow mb-4" style="width: 1000px; margin: 0 auto;">
		<div class="card-body p-0">
			<div class="row">
				<div class="col-lg-5 d-none d-lg-block bg-remove-image"></div>
				<div class="col-lg-7">
					<div class="p-5">
						<form id="frmRemoveParty" action="/party/removeParty" method="post" class="user">
							<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'>
							<%@include file="./include/partyCommon.jsp"%>
							<p style="font-size: 25px;">
							Are you sure to withdraw?<br>All information will be deleted.</p>
							<button id='btnRemoveParty' type="submit" class="btn btn-danger">
								Withdraw</button>
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
		setOprationMode("remove");
		
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfToken = "${_csrf.token}";

		var frmRemoveParty = $("#frmRemoveParty");
		$("#btnRemoveParty").on("click", function(e) {
			e.preventDefault();
			frmRemoveParty.submit();
		});
	})
</script>
