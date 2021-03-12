/**
p398/브라우저에 js파일은 다운로드 된다. 
서버에서 js파일을 수정해서 기동할때, 다운로드하면서 안돌아간다면 , 
브라우저에서 js를 다운로드 받아야 하는 행위를 해줘야한다(디버그 주의사항)
*/
var replyService = (function(){
	function listReplyInternal(param, cbSuccess, cbError) {
		var page = param.page || 1;
		$.getJSON(
			"/replies/pages/" + param.originalId + "/" + page + ".json",
			function(result) {
				if (cbSuccess) {
					cbSuccess(result);
				}
			}
		). fail(
			function(xhr, status, erMsg){
				if (cbError) {
					cbError(erMsg);
				}
			}
		);
	}

	function showReplyInternal(id, cbSuccess, cbError) {
		$.get(
			"/replies/" + id + ".json",
			function(reply) {
				if (cbSuccess) {
					cbSuccess(reply);
				}
			}
		). fail(
			function(xhr, status, erMsg){
				if (cbError) {
					cbError(erMsg);
				}
			}
		);
	}

	function registerReplyInternal(reply, cbSuccess, cbError) {
		$.ajax({
            type : 'post',
            url : '/replies/new',
            data : JSON.stringify(reply),
			contentType:"application/json;charset=utf-8", //보내는 데이타의 타입을 지정해 준다
            dataType : 'json',	//결과에 대한 데이터 타입
            success : function(result, status, xhr){
				if (cbSuccess) {
					cbSuccess(result);
				}
            },
            error: function(xhr, status, erMsg){
				if (cbError) {
					cbError(erMsg);
				}
            }
        });
	}
	
	function updateReplyInternal(reply, cbSuccess, cbError) {
		$.ajax({
            type : 'PATCH',
            url : '/replies/' + reply.id,
            data : JSON.stringify(reply),
			contentType:"application/json;charset=utf-8", //보내는 데이타의 타입을 지정해 준다
            success : function(result, status, xhr){
				if (cbSuccess) {
					cbSuccess(result);
				}
            },
            error: function(xhr, status, erMsg){
				if (cbError) {
					cbError(erMsg);
				}
            }
        });
	}
	
	function deleteReplyInternal(replyId, replyerId, cbSuccess, cbError) {
		$.ajax({
            type : 'Delete',
            url : '/replies/' + replyId + '/' + replyerId,
            dataType : "text",	//결과에 대한 데이터 타입
            success : function(result, status, xhr){
				if (cbSuccess) {
					cbSuccess(result);
				}
            },
            error: function(xhr, status, erMsg){
				if (cbError) {
					cbError(erMsg);
				}
            }
        });
	}
	
	return {
		listReply:listReplyInternal,
		showReply:showReplyInternal,
		registerReply:registerReplyInternal,
		updateReply:updateReplyInternal,
		deleteReply:deleteReplyInternal
	};	
})();
