<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>안녕히 가세요</title>
</head>
<!-- logout시 여기를 거쳐감 -->
<body>
	<h1>Logout Page</h1>
	<form method="post" action="/customLogout">
		<button>로그아웃</button>
		<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'>
	</form>
</body>
</html>