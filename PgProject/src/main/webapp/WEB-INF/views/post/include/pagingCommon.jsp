<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<form id='frmPaging' action='/post/listPost' method="get">
	<button id="btnGotoList" class="btn btn-info">List</button>
	<input type="hidden" name="boardId" value="${boardId}">
	<input type="hidden" name="pageNum" value="${criteria.pageNum}">
	<input type="hidden" name="search" value="${criteria.search}">
</form>

<script type="text/javascript">
	$(document).ready(function() {
		var frmPaging = $("#frmPaging");
		// 목록 버튼 클릭시 목록으로 돌아갈 수 있도록 form submit
		$("#btnGotoList").on("click", function(e) {
			e.preventDefault();
			frmPaging.submit();
		});
	})
</script>