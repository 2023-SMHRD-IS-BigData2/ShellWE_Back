<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>

	<h1>로그인</h1>
	<form action="/boot/login" method="post">
		id : <input name="id"><br>
		pw : <input name="pw"><br> 
		<input value="로그인" type="submit">
	</form>


</body>
</html>