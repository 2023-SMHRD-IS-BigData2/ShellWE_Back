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
	<form action="/boot/loginMember" method="post">
		id <input name="membernum"> <br>
		pw <input name="pw"><br> 
		<input type="submit">
	</form>
	<hr>
	<h1>의료진 등록</h1>
	<form action="/boot/insertMember" method="post">
		이름 : <input name="name"> <br> 
		id : <input name="id"><br>
		pw : <input name="pw"><br> 
		직급 : <input name="rank"><br> 
		연락처 : <input name="tell"><br> 
		<input type="submit">
	</form>
	<hr>
	<table border="1">
		<tr>
			<th>의료인번호</th>
			<th>이름</th>
			<th>아이디</th>
			<th>직급</th>
			<th>마지막 로그인시간</th>
			<th>연락처</th>
		</tr>
	<c:forEach var="member" items="${allMember}">
		<tr>
			<td>${member.membernum}</td>
			<td>${member.name}</td>
			<td>${member.id}</td>
			<td>${member.rank}</td>
			<td>${member.date}</td>
			<td>${member.tell}</td>
			<td><a href="/boot/deleteMember?membernum=${member.membernum}">삭제</a></td>
		</tr>
	</c:forEach>
	</table>

</body>
</html>