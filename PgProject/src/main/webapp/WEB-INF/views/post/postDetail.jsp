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
	<h1 class="h3 mb-2 text-gray-800">Post Detail</h1>

	<!-- DataTales Example -->
	<div class="card shadow mb-4">
		<div class="card-body">
			<!-- postCommon 삽입 -->
			<%@include file="./include/postCommon.jsp"%>
			<br>
			
		
			<!-- data : 요소에 추가적으로 변수와 정보를 마음대로 넣을 수 있는 장치. *** 주의사항:웹은 대소문자 구분 없어 -->
			<!-- 현재 사용자가 인증된 상태고, 현재 사용자의 id와 게시글 작성자의 id가 같은지 if로 확인 후 같다면 modify 버튼 보여줌 -->
			<sec:authentication property="principal" var="curUser"/>
			<sec:authorize access="isAuthenticated()">
				<c:if test="${curUser.loginUser.id eq post.writer.id}">
					<button id="modify" data-id="${post.id}" class="btn btn-warning">Modify</button>
				</c:if>
			</sec:authorize>
			<!-- paging처리 위해서 pagingCommon 삽입 -->
			<%@include file="./include/pagingCommon.jsp"%>
		</div>
	</div>
	
	<!-- 댓글 목록 표시 구간 -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<i class="fa fa-comment-dots fa-fw">Reply</i>
				</div>
				<div class="panel-body">
					<ul id="listReply" data-originalid="${post.id}" data-page_num="1" data-countofreply="${post.countOfReply}">
						View ${post.countOfReply} replies
						<sec:authorize access="isAuthenticated()">
							<button class="btnAddReply" class="btn btn-primary btn-xs fa-pull-right">Comment</button>
						</sec:authorize>
						<!-- 인증된 사용자면 댓글 달기 버튼 보여줌 -->
						<br>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!-- End of 댓글 목록 표시 구간 -->
</div>
<!-- /.container-fluid -->

<!-- 댓글 상세조회, 입력, 수정, 삭제 모달창 -->
<div id="modalReply" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        		<h4 class="modal-title" id="myModalLabel">REPLY MODAL</h4>
			</div>	<!-- End of modal-header -->
			<div class="modal-body">
				<div class="form-group">
					<label>Reply</label> 
					<input class="form-control" name='replyContent' value='New Reply!!!!'>
				</div>
				<div class="form-group">
					<input class="form-control" id='replyer' value='replyer' type="hidden">
				</div>
				<div class="form-group">
					<label>Reply Date</label> 
					<input class="form-control" name='replyDate' value=''>
				</div>
				
				<input id='replyerId' type="hidden">
				
			</div>	<!-- End of modal-body -->
			<div class="modal-footer">
				<button id='btnModifyReply' type="button" class="btn btn-warning">Modify</button>
				<button id='btnRemoveReply' type="button" class="btn btn-danger">Remove</button>
				<button id='btnRegisterReply' type="button" class="btn btn-primary">Register</button>
				<button id='btnCloseModal' type="button" class="btn btn-secondary">Close</button>
			</div>	<!-- End of modal-footer -->
		</div>	<!-- End of modal-content -->
	</div>	<!-- End of modal-dialog -->
</div>
<!-- End of 댓글 입력 모달창 -->
<!-- footer 삽입 -->
<%@include file="../includes/footer.jsp"%>
<!-- 날짜, 댓글 js 삽입 -->
<script type="text/javascript" src="\resources\js\util\dateGapDisplayService.js"></script>
<script type="text/javascript" src="\resources\js\reply\replyService.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		// postCommon에 있는 oprationMode의 read 기능 사용함
		//create read update
		setOprationMode("read");

		var frmPaging = $("#frmPaging");
		
		//게시글(PostVo) 수정
		$("#modify").on("click", function(e) {
			e.preventDefault();

			frmPaging.append("<input type='hidden' name='id' value='" + $(this).data("id") + "'>");
			frmPaging.attr("action", "/post/modifyPost");
			frmPaging.submit();
		});

		var postId = "${post.id}";
		var ownerType = "${post.ownerType}";
		//최 하단에 책에서 제시하는 개발 방식은 모듈화가 약해요. 따라서 이렇게 부르더라도 문서가 로드된 직후에 자동 호출합니다.
		//즉시 실행 함수와 같은 역할을 하는 것이지요!
		showAttachFileList({ownerId:postId, ownerType:ownerType});

		/** 댓글 목록 조회 및 출력 */
		var listReplyUL = $("#listReply");
		showReplyListAnchor(listReplyUL);
		
		function showReplyListAnchor(parentUl) {
			var replyCnt = parentUl.data("countofreply");
			if (replyCnt > 0) {
				var strShowReply = "<a>Show More</a>";
				parentUl.append(strShowReply);
			}
		}

		//댓글 또는 대댓글 펼치기
		$("#listReply").on("click", "a", function(e){
			//게시글에 댓글에 대댓글에...에서 펼치기가 일어나면 상위 게시글의 펼치기는 막혀야 합니다.
			//Anchor를 감싸는 Anchor가 없기 때문에 사실은 stop 처리 안해도 되지만... 그냥 넣어줬음. 구조 언제 바뀔지 모르잖아!
			e.stopImmediatePropagation();

			var choosenAnchor = $(this);
			var choosenUl = $(this).closest("ul");
			choosenAnchor.remove();
			var originalId = choosenUl.data("originalid");
			var pageNum = choosenUl.data("page_num");
			showReplyList(pageNum, originalId, choosenAnchor, choosenUl);
			//page_num을 1 올려줍시다. 맨 마지막에 anchor를 다시 달아줍시다.
		});

		// 댓글목록 보여주는 기능 for문 통해 li += 이용해 댓글 출력함
		function showReplyList(pageNum, originalId, anchorOfShowMoreReply, choosenUl) {
			replyService.listReply(
				{originalId:originalId, page:(pageNum || 1)},
				function(pairOfCriteriaListReply){
					var listReply = pairOfCriteriaListReply.second;
					var strReplyLi = "";
					for (var i = 0, len = listReply.length || 0; i < len; i++) {
						strReplyLi += "<li class='left clearfix' data-id='" + listReply[i].id + "'>";
						strReplyLi += "<div><div class='header'><strong class='primary-font'>" 
							+ listReply[i].writer.nickname 
							+ "</strong>";
						strReplyLi += "<small class='fa-pull-right text-muted'>" 
							+ dateGapDisplayService.displayTime(listReply[i].updateDate) 
							+ "</small>";
						strReplyLi += "</div>";
						strReplyLi += "<p>" + listReply[i].content + "</p>";

						strReplyLi += "<ul data-originalid='" + listReply[i].id + 
						              "' data-page_num='1' data-countofreply='" +  listReply[i].countOfReply +
						              "'>Replies " + listReply[i].countOfReply + "&nbsp;";
			      		<sec:authorize access="isAuthenticated()">
							strReplyLi += "<button class='btnAddReply' class='btn btn-primary btn-xs fa-pull-right'>Reply</button>" + "&nbsp;"; 
						</sec:authorize>
						if (listReply[i].countOfReply > 0) {
							//대댓글 펼치기 anchor를 넣겠습니다.
							strReplyLi += "<a>Show More</a>";
						}
						strReplyLi += "</ul>";
							
						strReplyLi += "</div></li>";
					}
					choosenUl.append(strReplyLi);

					//특정 게시글에 달린 댓글 펼치기를 연속적으로 수행할 수 있도록 제어하기
					var pageCriteria = pairOfCriteriaListReply.first;
					if (pageCriteria.pageNum < pageCriteria.endPage || pageCriteria.hasNext) {
						choosenUl.append(anchorOfShowMoreReply);
						choosenUl.data('page_num', pageNum + 1); 
					}
				}, // error 발생 시 alert으로 알림
				function(erMsg){
					alert(erMsg);
				}
			);
		}

		/** 댓글 관련 이벤트 처리 영역 */
		var modalReply = $("#modalReply");
		var replyDate = modalReply.find("input[name='replyDate']");
		var btnModifyReply = $("#btnModifyReply");
		var btnRemoveReply = $("#btnRemoveReply");
		var btnRegisterReply = $("#btnRegisterReply");
		var btnCloseModal = $("#btnCloseModal");

		//댓글 작성자 정보는 서버의 principal활용할 것이다. 
		//이 아래 부분은 단지 현 사용자 이름을 출력하기위한 용도이지 서버로 post할 정보는 아닐껄
		var replyerName = null;
		var curUserId = null;
		<sec:authorize access="isAuthenticated()">
			replyerName = '<sec:authentication property="principal.loginUser.nickname"/>';
			curUserId = '<sec:authentication property="principal.loginUser.id"/>';
		</sec:authorize>
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfToken = "${_csrf.token}";

		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(csrfHeaderName, csrfToken);
		});

		var ulOfModalControl;
		var liOfModalControl;
		//댓글 상세 조회할거야
		$("#listReply").on("click", "li p", function(e){
			ulOfModalControl = $(this).closest("ul");
			liOfModalControl = $(this).closest("li");
			var replyId = liOfModalControl.data("id");
			replyService.showReply(
				replyId,
				function(reply){
					modalReply.find("input[name='replyContent']").val(reply.content);
					$("#replyer").val(reply.writer.nickname);
					modalReply.find("input[name='replyDate']")
						.val(dateGapDisplayService.displayTime(reply.updateDate))
						.attr("readonly", "readonly");
					//수정 또는 삭제 시 댓글 아이디 전달 용도로 사용
					modalReply.data("id", replyId);

					//버튼 활성화 관리. 댓글 수정 삭제 기능은 댓글 작성자가 현 사용자일 때 활성화 시켜준다. 
					if (curUserId == reply.writer.id) {
						$("#replyerId").val(reply.writer.id);
						
						btnModifyReply.show();
						btnRemoveReply.show();
					} else {
						$("#replyerId").val(reply.writer.id);
						btnModifyReply.hide();
						btnRemoveReply.hide();
					}
					btnRegisterReply.hide();
					btnCloseModal.show();

					modalReply.modal("show");
				},
				function(erMsg){
					alert(erMsg);
				}
			);
		});

		//댓글 등록할 수 있도록 화면 열어 주세요
		$("ul").on("click", ".btnAddReply", function(e) {
			//Nested li에서 일어난 이벤트가 이를 감싸고 있는 요소(부모 요소)에 할당된 위임 이벤트가 연속해서 처리되는 것을 막을 것이야.
			e.stopImmediatePropagation();

			ulOfModalControl = $(this).closest("ul");
			var originalid = ulOfModalControl.data("originalid");
			openModal(originalid);
		});
		function openModal(originalid) {
			//신규 댓글 모달창 띄우기 전에 기존에 입력된 값 청소
			modalReply.find("input").val("");
			$("#replyer").val(replyerName);

			//신규 댓글 등록 시 작성일자는 현재 시간이 DB에서 자동으로 채워지니까 입력 받을 필요는 없지요
			replyDate.closest("div").hide();

			btnModifyReply.hide();
			btnRemoveReply.hide();
			btnRegisterReply.data("originalid", originalid);
			btnRegisterReply.show();
			btnCloseModal.show();

			modalReply.modal("show");
		}
		
		//댓글 창 닫기
		btnCloseModal.on("click", function(e) {
			modalReply.modal("hide");
		});
		
		//댓글 등록합니다
		btnRegisterReply.on("click", function(e) {
			e.preventDefault();

			var originalid = btnRegisterReply.data("originalid");

			var reply = {originalId:originalid,
					content:modalReply.find("input[name='replyContent']").val()};
			modalReply.modal("hide");

			replyService.registerReply(reply, function(result) {
				alert("The comment has been posted");

				var anchorForListReplyOfReply = ulOfModalControl.children('a')[0];
				//지금 막 달은 댓글은 첫페이지 최 상단에 나타나야합니다.
				//이에 기존 UL에 담긴 LI들은 모두 청소하고
				var listReply = ulOfModalControl.find('li');
				listReply.remove();
				
				//1Page를 조회하여 보여주도록 제어합니다.
				//만약에 댓글의 개수가 페이지당 출력 개수를 초과할 경우에
				var countOfReply = result.second;
				ulOfModalControl.data("countofreply", countOfReply);
				var pageSize = result.third;
				
				if (countOfReply > pageSize) {
					// 다음 펼치기는 2쪽(page_num = 2)으로 설정해 주어야합니다.
					ulOfModalControl.data("page_num", 2);
				}
				 
				showReplyList(1, originalid, anchorForListReplyOfReply, ulOfModalControl);
			});
		});
		
		//댓글 수정합니다
		btnModifyReply.on("click", function(e) {
			e.preventDefault();

			var reply = {id:modalReply.data("id"),
					content:modalReply.find("input[name='replyContent']").val(),
					writer:{id:$("#replyerId").val()}};
			replyService.updateReply(reply, function(result){
				alert("The comment has been modified");
				modalReply.modal("hide");

				var replyContent = liOfModalControl.find('p')[0];
				replyContent.textContent = reply.content;
			});
		});

		//댓글 삭제 이벤트 처리.....            댓글 작성자 ID를 서버로 넘겨라
		btnRemoveReply.on("click", function(e) {
			//Delete method에는 Body를 던질 수 없걸랑
			e.preventDefault();

			replyService.deleteReply(modalReply.data("id"), $("#replyerId").val(), function(result){
				alert("The comment has been deleted");
				modalReply.modal("hide");

				liOfModalControl.remove();
			});
		});

	})
</script>

<script type="text/javascript">
	$(document).ready(function() {(
		function(){
			//즉시 실행 함수라고 한다.
		}
	)();})
</script>
