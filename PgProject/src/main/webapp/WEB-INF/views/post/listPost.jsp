<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false"%>
<%@ page import="www.dream.com.framework.display.*"%>
<%@ page import="www.dream.com.board.model.*"%>

<!-- header 삽입 -->
<%@include file="../includes/header.jsp"%>




<!-- Begin Page Content -->
<div class="container-fluid">

	<!-- Page Heading -->
	<h1 class="h3 mb-2 text-gray-800"></h1>

	<!-- DataTales Example -->
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<!-- 현재 사용자가 인증된 상태면 게시글 등록 버튼 보여줌 -->
			<sec:authentication property="principal" var="curUser" />
			<sec:authorize access="isAuthenticated()">
				<button id='btnRegisterPost' type="button" class="btn btn-primary">Register New Post</button>
			</sec:authorize>
		</div>
		<div class="card-body">

			<!-- Search -->
			<form id='frmPaging' action='/post/listPost' method="get"
				class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
				<div class="input-group">
					<input type="text" name="search" value="${criteria.search}"
						class="form-control bg-light border-0 small"
						placeholder="Search for..." aria-label="Search">
						<input type="hidden" name="boardId" value="${boardId}">
						<input type="hidden" name="pageNum" value="${criteria.pageNum}">
					<div class="input-group-append">
						<button class="btn btn-primary" type="submit">
							<i class="fas fa-search fa-sm"></i>
						</button>
					</div>
				</div>
			</form>

			<!-- forEach 통해 게시글 목록 출력 -->
			<div class="table-responsive">
				<table class="table table-bordered" id="dataTable" width="100%"
					cellspacing="0" data-order=''>
					<thead>
						<%=TableDisplayer.displayHeader(PostVO.class)%>
					</thead>
					<tbody>
						<c:forEach var="post" items="${listPost}">
							<tr>
								<td><a class='move2PostDetail' href='${post.id}'>${post.title}</a></td>
								<td>${post.likeCount}</td>
								<td>${post.dislikeCount}</td>
								<td><fmt:formatNumber groupingUsed="{true}"
										value="${post.viewcnt}" /></td>
								<td>${post.writer.nickname}</td>
								<td><fmt:formatDate pattern="yyyy-MM-dd"
										value="${post.regDate}" /></td>
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

				<!-- Modal -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title" id="myModalLabel">Success</h4>
							</div>
							<div class="modal-body"></div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">Close</button>
							</div>
						</div>
						<!-- /.modal-content -->
					</div>
					<!-- /.modal-dialog -->
				</div>
				<!-- /.modal -->

			</div>
		</div>
	</div>

</div>
<!-- /.container-fluid -->

</div>
<!-- End of Main Content -->
<!-- footer 삽입 -->
<%@include file="../includes/footer.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		var result = '<c:out value="${result}"/>';

		checkModal(result);

		history.replaceState({}, null, null);

		// 게시글의 싱테 변동이 일어나면 목록으로 돌아오는데 modal 이용해 상태 변경 완료 알림
		function checkModal(result) {
			if (result === '' || history.state) {
				return;
			}
			$(".modal-body").html("Back to the list.");
			$("#myModal").modal("show");
		}

		var frmPaging = $("#frmPaging");

		// 게시글 등록버튼 누르면 form에 /post/registerPost로 이동할 수 있게 action 추가해준 후 submit
		$("#btnRegisterPost").on("click", function(e) {
			e.stopImmediatePropagation();
			e.preventDefault();
			frmPaging.attr("action", "/post/registerPost");
			frmPaging.submit();
		});

		// 게시글 페이지가 a태그로 있는데 이를 클릭 시 해당 페이지로 들어갈 수 있게 href 추가해줌
		$("#ulPagination a").on("click", function(e) {
			e.preventDefault();
			$("input[name='pageNum']").val($(this).attr("href"));
			frmPaging.submit();
		});

		// 게시글 제목을 누르면 해당 게시글의 상세 조회로 연결되게 함 hidden으로 게시글의 id를 같이 넣어줌
		$(".move2PostDetail").on("click", function(e) {
			e.preventDefault();
			frmPaging.append("<input type='hidden' name='id' value='" + $(this).attr("href") + "'>");
			frmPaging.attr("action", "/post/postDetail");
			frmPaging.submit();
		});

		// 공지사항 게시판이고 현재 사용자 id가 1(admin)이 아닌 경우 공지사항 게시판에서 게시글 등록 버튼 나타나지 않게 함
		if ( ${boardId} == 2 && !(${curUser.loginUser.id} == 1)) {
		    $("#btnRegisterPost").hide();
		}
		
	})
</script>
