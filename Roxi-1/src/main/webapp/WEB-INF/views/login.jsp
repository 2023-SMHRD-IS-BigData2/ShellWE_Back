<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>

	<h1>로그인</h1>
	
	<c:if test="${not empty LoginMember}">
		사용자: ${LoginMember.name}
	</c:if>
	<form action="/boot/login" method="post">
		id <input name="membernum"> <br>
		pw <input name="pw"> <br>
		<input type="submit">
	</form >

</body>
</html>