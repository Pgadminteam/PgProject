<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<style>
#uploadResult {	width: 100%; background-color: #EAECF4; border-radius: 5px; border: 1px solid #D1D3E2; }
#uploadResult ul{ display:flex; flex-flow: row; justify-content: center; align-items: center;
				  margin-bottom: 0;}
#uploadResult ul li {list-style:none; padding: 10px; align-content: center; text-align: center;}
#uploadResult ul li img{ width: 120px;}
.bigWrapper { position: absolute; display: none; justify-content: center;
			align-items: center; top: 0%; width: 100%; height: 100%; background-color: gray;
			z-index: 100; background:rgba(255,255,255,0.5);}
.bigNested { position: relative; display:flex; justify-content: center; align-items:center;}
.bigNested img {width: 600px;}
.bigNested video {width: 600px;}
</style>

<!-- 첨부 파일과 관련된 영역 -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					
				</div>
				<div class="panel-body">
					<!-- p520. Div clone하고 이를 업로드 성공 시 재 초기화 하면
					!!!!! 문제는 Event Listening 함수 연결이 깨어진다. 이에 한번은 업로드되나 두번째는 막힌다.
					5시간 고생했어. Input만 청소하는 것을 대안으로 찾음 $("#uploadFile").val(null);
					 -->
					 
					<div class="form-group" id="uploadDiv">
					<span>Attached Files</span>
						<div id="uploadBtn">
						 <label id="uploadFileLbl" class="btn">
						 	<i class="fas fa-arrow-circle-up"></i>
						 	<input type='file' id='uploadFile' multiple="multiple" style="display: none;">
						 </label>
						<button id='btnUpload' class="btn">
							<i class="fas fa-check-circle"></i>
						</button>
						</div>
					</div>
						<p id="clickAttachTxt" class="txtInline">( Click the check button after uploading )</p>
					<hr style="height:28pt; visibility:hidden;" />
					<div id='uploadResult'>
						<ul></ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class='bigWrapper'>
		<div class='bigNested'>
		</div>
	</div>

<!-- End of 첨부 파일과 관련된 영역 -->
<script type="text/javascript">
// 첨부된 파일 목록 출력해줌
function showAttachFileList(attachDTO) {
	$.getJSON("/listAttach", attachDTO, function(listAttach){
		var strLi = "";

		//jQuery에서 제공하는 편리한 반복문. $(uploadResultArr).each
		$(listAttach).each(function(index, attachObj){
			strLi += makeLi(attachObj);
		});
		$("#uploadResult ul").append(strLi);
	});
}

// 파일 첨부될 때 li로 출력하고 li를 만드는 기능
function makeLi(attachObj) {
	var strLi = "";
	var attachJson = JSON.stringify(attachObj);
	
	//strLi += obj.displayAsLi;
	if ("image" === attachObj.fileType || "video" === attachObj.fileType || "audio" === attachObj.fileType) {
		//시간적 여유가 있을 때 돌아와서 서버측에서 이부분을 처리할 수 있도록 연구 적용합시다. 2020-12-28
		//p526. 한글이나 공백이 파일 이름에 있을 경우에 대한 대비책
		var fileCallPath = encodeURIComponent(attachObj.fileCallPath);
		var originPath = attachObj.uploadedFileFullPath;
		originPath = originPath.replace(new RegExp(/\\/g), "/");

		strLi += "<li data-attach_info='" + attachJson + "'>";
		//업로드한 원본을 받아서 디스플레이해 줍니다.
		strLi += "<a href=\"javascript:showUploadedFile(\'" 
			+ originPath + "\', \'" + attachObj.fileType + "\')\">";
		if ("audio" === attachObj.fileType) {
			strLi += "<i class='fas fa-headphones'></i> " + attachObj.originalFilename;
			strLi += "<a href='/download?fileName=" + fileCallPath + "'>";
			strLi += "&nbsp;<i class='fas fa-save'></i></a>";
		} else {
			//썸네일을 작게 기본적으로 보여줍니다.
			strLi += "<img src='/display?fileName=" + fileCallPath + "'>";
		}
		strLi += "</a>";
		//객체의 속성을 빼와서 data로 달아 주는 것은 객체 속성 명 바꿀때 이 자리까지 찾아와서 함께 바꾸어 주어야 하는 문제가 있을 껄!!
		//이럴 때 JSON을 그대로 data로 넣어 주고 객체로 변환하여 처리토록하자
		strLi += "&nbsp;<span id='x'><i class='fas fa-times'></i></span>";
		strLi += "</li>";
	} else {
		var fileCallPath = encodeURIComponent(attachObj.fileCallPath);
		strLi += "<li data-attach_info='" + attachJson + "'>";
		strLi += "<i class='fas fa-paperclip'></i> " + attachObj.originalFilename;
		strLi += "<a href='/download?fileName=" + fileCallPath + "'>";
		strLi += "&nbsp;<i class='fas fa-save'></i></a>";
		strLi += "&nbsp;<span id='x'><i class='fas fa-times'></i></span>";
		strLi += "</li>";
	}
	return strLi;
}

$(document).ready(function() {
	/** 첨부 파일 처리 관련 영역 */
	var uploadResult = $("#uploadResult ul");
	
	var rexForbiddenExt = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	var MAX_SIZE = 536870912;

	var csrfHeaderName = "${_csrf.headerName}";
	var csrfToken = "${_csrf.token}";
	
	/*
	사용자가 선택한 파일을 가능성 여부를 따진뒤 웹 서버로 Ajax 기술을 활용하여 업로드합니다.
	그리고 그 결과를 uploadResult div의 UL에 나타냅니다.
	*/
	$("#btnUpload").on("click", function(e) {
		e.stopImmediatePropagation();
		e.preventDefault();

		var formData = new FormData();	//프로그램에서 관리할 수 있는 html Form 객체
		
		var inputFile = $("#uploadFile");
		var files = inputFile[0].files;
		for (var i = 0; i < files.length; i++) {
			if (!canUpload(files[i].name, files[i].size)) {
				return;
			}
			formData.append("uploadFiles", files[i]);
		}

		$.ajax({
			url:'/uploadAjaxAction',
			processData : false,
			contentType:false,
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfToken);
			},
			data:formData,
			type:'post',
			dataType:'json',	//결과를 json으로 받습니다.
			success:function(result) {
				var strLi = "";

				//jQuery에서 제공하는 편리한 반복문. $(uploadResultArr).each
				$(result).each(function(index, attachObj){
					strLi += makeLi(attachObj);
				});
				uploadResult.append(strLi);
				//<input type="file" name='uploadFile' 초기화
				$("#uploadFile").val(null);
			}
		});
	});

	// 파일 사이즈나 형식에 따라 업로드 가능한지 체크하는 함수
	function canUpload(fileName, fileSize) {
		if (fileSize > MAX_SIZE) {
			alert("File size exceeded");
			return false;
		}

		if (rexForbiddenExt.test(fileName)) {
			alert("This file cannot be uploaded");
			return false;
		}
		return true;
	}

	$(".bigWrapper").on("click", function(e) {
		$(".bigNested").animate({width:'0%', height:'0%'}, 500);
		setTimeout(function(){
			$(".bigWrapper").hide();
		}, 500);
	});

	//X 를 클릭하면 작성 시 업로드한 첨부파일을 취소한다.
	$("#uploadResult").on("click", "span", function(e) {
		var attachLi = $(this).closest("li");
		var attachInfo= attachLi.data("attach_info");
		$.ajax({
			url:'/deleteFile',
			data:attachInfo,
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfToken);
			},
			dataType:'text',	//결과를 json으로 받습니다.
			type:'post',
			success:function(result) {
				alert("Delete the uploaded file");
				attachLi.remove();
			}
		});
	});
	/** End of 첨부 파일 처리 관련 영역 */
})

// 업로드된 파일 보여주기
function showUploadedFile(fileCallPath, fileType) {
	$(".bigWrapper").css("display", "flex").show();
	var nestedInternal = "";
	if (fileType === "image") {
		nestedInternal = "<img src='/display?fileName=" + encodeURI(fileCallPath) + "'>";
	} else if (fileType === "video") {
		nestedInternal = "<video controls autoplay >";
		//확장자 파악
		var ext = fileCallPath.substring(fileCallPath.lastIndexOf(".") + 1);
		if (ext === "webm") {
			nestedInternal += "<source src='/display?fileName=" + encodeURI(fileCallPath) + "' type='video/webm'>";
		} else if (ext === "mp4") {
			nestedInternal += "<source src='/display?fileName=" + encodeURI(fileCallPath) + "' type='video/mp4'>";
		} else {
			nestedInternal += "Sorry, your browser doesn't support embedded videos.";
		}
		nestedInternal += "</video>";
	} else if(fileType === "audio") {
		nestedInternal = "<audio controls autoplay src='/display?fileName=" + encodeURI(fileCallPath) + "'>";
		nestedInternal += "Your browser does not support the <code>audio</code> element.";
		nestedInternal += "</audio>";
	} else {
		$(".bigWrapper").hide();
		return;
	}
	
	$(".bigNested").html(nestedInternal).animate({width:'100%', height:'100%'}, 500);
}

function addAttachToFrmAsHidden(frmPost) {
	var strAttachVo = "";
	
	$("#uploadResult ul li").each(function(i, liObj){
		//javascript는 대소문자 구분 안해줘요!
		strAttachVo += "<input type='hidden' name='attachJson[" + i + "]' value='"; 
		strAttachVo += JSON.stringify($(liObj).data("attach_info")) + "'>"; 
	});

	frmPost.append(strAttachVo);
}
</script>
