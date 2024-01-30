<%@page import="antlr.collections.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<body>
	<h1>ROXI MAIN</h1>
	<c:if test="${not empty LoginMember}">
		사용자: ${LoginMember.name}
	</c:if>
		<form action="/boot/insert" method="post">
    이름 : <input name="name" type="text">
    <br>
    성별 : <input name="gender" type="text">
    <br>
    나이 : <input name="age" type="text">
    <br>
    병동 : <input name="ward" type="text">
    <br>    
    혈액형 : <input name="bloodtype" type="text">
    <br>
    현재상태 : <input name="sepsisslevel" type="text">
    <br>
    담당의 : <input name="physician" type="text">
    <br>
    제출 : 
    <input type="submit">
    
</form>
		<br>
<hr>
<h3>환자 목록</h3>
<table border="1">
	<tr>
	    <th>num</th>
		<th>name</th>
		<th>gender</th>
		<th>age</th>
		<th>room</th>
		<th>indate</th>
		<th>blood</th>
		<th>status</th>
		<th>denger</th>
        <th>doctor</th>
        <th>상세보기</th>
	</tr>
	<c:forEach var="list" items="${list}">
	<tr>
	    <td>${list.patinum}</td>
	    <td>${list.name}</td>
	    <td>${list.gender}</td>
	    <td>${list.age}</td>
	    <td>${list.ward}</td>
	    <td>${list.hpdate}</td>
	    <td>${list.bloodtype}</td>
	    <td>${list.sepsisslevel}</td>
        <td>${list.sepsisscore}</td>
        <td>${list.physician}</td>
        <td><a href="/boot/detail?patinum=${list.patinum}">상세보기</a></td>
	</tr>
</c:forEach>
</table>
</body>
</html>