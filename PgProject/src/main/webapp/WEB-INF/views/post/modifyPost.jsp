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
	<h1 class="h3 mb-2 text-gray-800">Modify Post</h1>

	<!-- DataTales Example -->
	<div class="card shadow mb-4">
		<div class="card-body">
			<form id="frm_modify_post" action="/post/modifyPost" method="post">
			<!-- postCommon 삽입 -->
				<%@include file="./include/postCommon.jsp"%>
				<br>
				<!-- data : 요소에 추가적으로 변수와 정보를 마음대로 넣을 수 있는 장치. *** 주의사항:웹은 대소문자 구분 없어 -->
				<!-- 현재 사용자가 인증된 상태고, 현재 사용자의 id와 게시글 작성자의 id가 같은지 if로 확인 후 같다면 modify 버튼 보여줌 -->
				<sec:authentication property="principal" var="curUser"/>
				<sec:authorize access="isAuthenticated()">
					<c:if test="${curUser.loginUser.id eq post.writer.id}">
						<button type="submit" data-oper="modify" class="btn btn-warning btnControl">Modify</button>
						<button type="submit" data-oper="remove" class="btn btn-danger btnControl">Delete</button>
					</c:if>
				</sec:authorize>
			</form>
			<!-- paging처리 위해서 pagingCommon 삽입 -->
			<%@include file="./include/pagingCommon.jsp"%>
			
		</div>
	</div>
</div>
<!-- /.container-fluid -->
<!-- footer 삽입 -->
<%@include file="../includes/footer.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		// postCommon에 있는 oprationMode의 update 기능 사용함
		//create read update
		setOprationMode("update");

		var postId = "${post.id}";
		var ownerType = "${post.ownerType}";
		//최 하단에 책에서 제시하는 개발 방식은 모듈화가 약해요. 따라서 이렇게 부르더라도 문서가 로드된 직후에 자동 호출합니다.
		//즉시 실행 함수와 같은 역할을 하는 것이지요!
		showAttachFileList({ownerId:postId, ownerType:ownerType});
		
		var frmModifyPost = $("#frm_modify_post");
		var frmPaging = $("#frmPaging");

		// 버튼 클릭 시 data-oper의 값을 읽어서 modify / remove 기능을 if로 제어해 사용함
		$(".btnControl").on("click", function(e) {
			e.preventDefault();

			var oper = $(this).data("oper");
			
			addAttachToFrmAsHidden(frmModifyPost);
			
			if (oper === "remove") {
				frmModifyPost.attr("action", "/post/removePost");
			}
			frmModifyPost.append(frmPaging.children());
			frmModifyPost.submit();
		});

	})
</script>
