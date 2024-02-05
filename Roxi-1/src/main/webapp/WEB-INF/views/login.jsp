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
		
		    <script>
		        alert("${insertError}");
		    </script>
	<c:if test="${not empty LoginMember}">
		사용자: ${LoginMember.name}
	</c:if>
	<form action="/boot/loginMember" method="post">
		id <input name="id"> <br>
		pw <input name="pw"><br> 
		<input type="submit" value="로그인">
		<a href="/boot/logout">로그아웃</a>
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
			<th>이름</th>
			<th>아이디</th>
			<th>직급</th>
			<th>입사일<th>
			<th>마지막 로그인시간</th>
			<th>마지막 로그아웃 시간</th>
			<th>연락처</th>
			<th>퇴사</th>
		</tr>
	<c:forEach var="member" items="${allMember}">
		<tr>
			<td>${member.name}</td>
			<td>${member.id}</td>
			<td>${member.rank}</td>
			<td>${member.date}</td>
			<td>${member.logintime}</td>
			<td>${member.logouttime}</td>
			<td>${member.tell}</td>
			<td><a href="/boot/deleteMember?membernum=${member.membernum}">삭제</a></td>
		</tr>
	</c:forEach>
	</table>
	<hr>
	
	<c:if test="${not empty LoginMember}">
		<form action="/boot/updataMember" method="post">
			<table border="1">
				<tr>
					<td>아이디</td>
					<td><input name="id" value="${LoginMember.id}" readonly="readonly"></td>
				</tr>
				<tr>
					<td>이름</td>
					<td><input name="name" value="${LoginMember.name}"></td>
				</tr>
				<tr>
					<td>비밀번호</td>
					<td><input name="pw" value="${LoginMember.pw}"></td>
				</tr>
				<tr>
					<td>직급</td>
					<td><input name="rank" value="${LoginMember.rank}"></td>
				</tr>
				<tr>
					<td>연락처</td>
					<td><input name="tell" value="${LoginMember.tell}"></td>
				</tr>
			</table>
			<input type="submit" value="정보수정">
			
		</form>
	</c:if>

</body>
</html>