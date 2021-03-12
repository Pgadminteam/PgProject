/**
p398/브라우저에 js파일은 다운로드 된다. 
서버에서 js파일을 수정해서 기동할때, 다운로드하면서 안돌아간다면 , 
브라우저에서 js를 다운로드 받아야 하는 행위를 해줘야한다(디버그 주의사항)
*/
var dateGapDisplayService = (function(){
	
	function displayTimeInternal(dateValue) {
		var now = new Date();
		var gap = now.getTime() - dateValue;
		var dateObj = new Date(dateValue);
		
		var strReturn = "";
		
		if (gap < 1000 * 60) {
			/* 일분 안에 등록된 경우 */
			strReturn += parseInt(gap / 1000) + "초 전 등록";
		} else if (gap < 1000 * 60 * 60) {
			/* 한시간 안에 등록된 경우 */
			strReturn += parseInt(gap / 1000 / 60) + "분 전 등록";
		} else if (gap < 1000 * 60 * 60 * 24) {
			/* 하루 안에 등록된 경우 */
			strReturn += parseInt(gap / 1000 / 60 / 60) + "시간 전 등록";
		} else if (gap < 1000 * 60 * 60 * 24 * 30.5) {
			/* 한달안에 등록된 경우 */
			strReturn += parseInt(gap / 1000 / 60 / 60 / 24) + "일 전 등록";
		} else if (gap < 1000 * 60 * 60 * 24 * 365) {
			/* 일년안에 등록된 경우 */
			strReturn += parseInt(gap / 1000 / 60 / 60 / 24 / 30.5) + "달 전 등록";
		} else {
			/* 몇년이 지난 경우 */
			strReturn += parseInt(gap / (1000 * 60 * 60 * 24 * 365)) + "년 전 등록";
		}
		return strReturn;
	}
	
	return {
		displayTime:displayTimeInternal
	};	
})();
