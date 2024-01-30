<%@page import="java.util.List"%>
<%@page import="com.smhrd.roxi.entity.Roxi_comment"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<table>
	<th>작성 시간</th>
	<th>내용</th>
	<th>작성자</th>
	<c:forEach var="list" items="${list}">
	<tr>
		<td>${list.inputdate}</td>
		<td>${list.contents}</td>
		<td>${list.membernum}</td>
	</tr>
	</c:forEach>
</table>

<form action="/boot/insertComment">
	<input name="insertComment" type="text">
	<input name="patinum" type="hidden" value="${pNum}">
	<input type="submit">
</form>
<br>
<a href="/boot/">메인으로 돌아가기</a>
</body>
</html>