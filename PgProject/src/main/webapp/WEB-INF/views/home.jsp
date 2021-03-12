<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page session="false"%>
<link rel="stylesheet" href="/resources/css/style.css">

<%@include file="includes/header.jsp"%>
<div id="slider">
    <ul class="sbox">
      <li class="0"><img src="/resources/img/photo1.jpg" alt="1"></li>
      <li class="1"><img src="/resources/img/photo2.jpg" alt="2"></li>
      <li class="2"><img src="/resources/img/photo3.jpg" alt="3"></li>
    </ul>
    <div class="directionBtn">
      <button class="prev"><!--이전--> &lt; </button>  
      <button class="next"><!--다음--> &gt; </button>  
    </div>
    <div class="dot">
      <a href="#" class="on">1</a>  
      <a href="#">2</a>  
      <a href="#">3</a>  
    </div>
</div>
  
<%@include file="includes/footer.jsp"%>

</body>
</html>
<script type="text/javascript">
// 홈 화면 갤러리 스타일 구현 위한 기능
$(function(){

	var ul = $(".sbox"); //ul박스의 위치를 변수에 담음
	
    //버튼 클릭 슬라이더
    var w = $(".sbox>li").width(); //이미지 너비
    var l = $(".sbox>li").length; //이미지 개수
    ul.width(w*l); //ul박스의 위치에 너비*개수=총너비를 넣음 -> float: left작업 
       
  	//다음버튼 클릭하면 동작
    function next() { 
        $(".sbox").stop(true).animate({left: -w}, 1000, function(){
            $(".sbox>li:eq(0)").appendTo(".sbox");
            $(".sbox").css({left: 0});
        });
        var num = $(".sbox>li:first").next().attr("class");
        $(".dot>a").removeClass("on");
        $(".dot>a").eq(num).addClass("on");

    }
    $(".next").on("click", next);
    
 	 //이전버튼 클릭하면 동작
    function prev() { 
       $(".sbox>li:last").prependTo(".sbox"); //마지막이미지 찾아서 맨앞으로 보내기
        $(".sbox").css({left: -w}); 
        $(".sbox").stop(true).animate({left: 0}, 1000); //버튼을 클릭했을 때 제자리로 오도록 함
        
        var num = $(".sbox>li:first").attr("class");
        $(".dot>a").removeClass("on");
        $(".dot>a").eq(num).addClass("on");
    } 
    $(".prev").on("click",prev);
});
</script>