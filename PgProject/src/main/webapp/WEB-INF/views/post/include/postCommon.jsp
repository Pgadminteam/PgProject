<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="www.dream.com.board.model.*" %>
	
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!-- int, long 같은 것은 null 이면 문제가 발생함 그러니까 숫자로 들어가는거는 null 안들어가도록 default값 아무거나 넣기 -->
<!-- post에 관련된 form -->
<div id="divIdentifier" class="form-group">
	<input type="hidden" name="id" value="${post != null ? post.id : 0}" readonly="readonly" class="form-control" >
</div>
<div class="form-group">
	<label>Title</label>
	<input id="title" name="title" value="${post.title}" class="form-control" >
</div>
<div class="form-group">
	<label>Content</label>
	<textarea id="txacontent" name="content" class="form-control" rows=3 >${post.content}</textarea>
</div>
<div class="form-group">
	<label>HashTag</label>
	<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'>
	<button id="btnExtractHashTag" type="button" class="btn-circle btn-sm">
	<i class="fas fa-hashtag"></i></button>
	<p id="clickTxt">( Click the left button to create a hashtag )</p>
	<input id="hashTag"	name="hashTag" value="${post.hashTagAsString}" class="form-control" >
</div>
<div id="divWriter" class="form-group">
	<label>Writer</label>
<% 
	PostVO post = (PostVO) request.getAttribute("post");
	if (post == null) { //post가 없으면 현 사용자의 닉네임을 writer에 넣어줌
%>		
		<input value='<sec:authentication property="principal.loginUser.nickname"/>' readonly="readonly" class="form-control" >
		<input type='hidden' name="writer.id" value='<sec:authentication property="principal.loginUser.id"/>'>
<%
	} else { // post가 있으면 post 작성자의 닉네임을 writer에 넣어줌
%>		
		<input value='${post.writer.nickname}' readonly="readonly" class="form-control" >
		<input type='hidden' name="writer.id" value='${post.writer.id}'>
<%
	}
%>
</div>
<div class="form-group">
	<button id="btnLike" type="button" class="btn">
		<i class="far fa-thumbs-up"></i>
		<p id="likeCount">${post.likeCount}</p>
	</button>
	<button id="btnDislike" type="button" class="btn">
		<i class="far fa-thumbs-down"></i>
		<p id="dislikeCount">${post.dislikeCount}</p>
	</button>
</div>


<%@include file="/WEB-INF/views/includes/attachUploadCommon.jsp"%>

<script type="text/javascript">
	// CRU에 대한 기능 readonly, show, hide로 제어함
	//create read update
	function setOprationMode(oprationMode) {
		if ("read" === oprationMode) {
			$("#title").attr("readonly", true);
			$("#txacontent").attr("readonly", true);
			$("#hashTag").attr("readonly", true);
			//btn: extractHashTag
			$("#btnExtractHashTag").hide();
			$("#clickTxt").hide();
			//btn: file select, file upload
			$("#uploadFileLbl").hide();
			$("#uploadFile").hide();
			$("#btnUpload").hide();
			$("#clickAttachTxt").hide();
		} else if ("create" === oprationMode) {
			$("#divIdentifier").hide();
			$("#btnLike").hide();
			$("#btnDislike").hide();
		} else if ("update" === oprationMode) {
			$("#btnLike").hide();
			$("#btnDislike").hide();
			}
	}

	$(document).ready(function() {
		var csrfHeaderName = "${_csrf.headerName}";
		var csrfToken = "${_csrf.token}";
		var likeToggle = 1;
		var dislikeToggle = 1;
		var postId = ${post != null ? post.id : 0};

		// 해시태그 추출 버튼 누르면 해시태그 input area에 추출된 해시태그 넣어줌
		$("#btnExtractHashTag").on("click", function(e) {
			e.preventDefault();
			var postBody = {title:$("#title").val(), content:$('#txacontent').val()};

			// 해시태그 추출 ajax로 제어함 for문 이용해 해시태그 넣음
			$.ajax({
	            type : 'post',
	            url : '/hashtag/extractHashTag',
				beforeSend: function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfToken);
				},
	            data : postBody,
	            dataType : 'json',	//결과에 대한 데이터 타입
	            success : function(listHashTag){
		            var candidateHashTag = "";
	    			for (var i = 0; i < listHashTag.length; i++) {
	    				candidateHashTag += listHashTag[i] + " ";
	    			}
	    			$("#hashTag").val(candidateHashTag);
	            },
	            error: function(xhr, status, error){
	                alert(error);
	            }
	        });
		});

		// 좋아요 버튼 클릭
		$("#btnLike").on("click", function(e) {
			e.preventDefault();
			
			$.ajax({
	            type : 'get',
	            url : '/postlike/like/' + postId + '/' + likeToggle,
				beforeSend: function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfToken);
				},
	            dataType : "text",
	            success : function(result){
		            $("#likeCount").html(result);
	            },
	            error: function(xhr, status, error){
	                alert(error);
	            }
	        });
	        likeToggle *= -1;
	    });

		// 싫어요 버튼 클릭
		$("#btnDislike").on("click", function(e) {
			e.preventDefault();

			$.ajax({
	            type : 'get',
	            url : '/postlike/dislike/' + postId + '/' + dislikeToggle,
				beforeSend: function(xhr) {
					xhr.setRequestHeader(csrfHeaderName, csrfToken);
				},
	            dataType : "text",
	            success : function(result){
		            $("#dislikeCount").html(result);
	            },
	            error: function(xhr, status, error){
	                alert(error);
	            }
	        });
	        dislikeToggle *= -1;

	    });
	    
	})
</script>