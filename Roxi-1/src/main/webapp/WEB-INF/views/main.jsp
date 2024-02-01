
<%@page import="java.util.List"%>
<%@page import="com.smhrd.roxi.entity.Roxi_Patient"%>

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
<%List<Roxi_Patient> Plist = (List<Roxi_Patient>)request.getAttribute("list");%>
	<h1>ROXI MAIN</h1>
	<a href="/boot/login">loginpage</a>
	<c:if test="${not empty LoginMember}">
		사용자: ${LoginMember.name}
	</c:if>
	<form action="/boot/insert" method="post">
		이름 : <input name="name" type="text"> <br> 성별 : <input
			name="gender" type="text"> <br> 나이 : <input name="age"
			type="text"> <br> 병동 : <input name="ward" type="text">
		<br> 혈액형 : <input name="bloodtype" type="text"> <br>
		현재상태 : <select name="sepsisslevel">
			<option value="Screening">Screening</option>
			<option value="Observing">Observing</option>
			<option value="None">None</option>
			<!-- 추가 옵션들 -->
		</select> <br> 담당의 : <input name="physician" type="text"> <br>
		제출 : <input type="submit">

	</form>
	<br>
	<hr>
	<h3>환자 목록</h3>
	<a href="/boot/dengerList">위험환자만 출력</a>
	<br>
	<a href="/boot/">전체환자 출력</a>
	<br>
	<form action="/boot/searchPatient">
		<input name="search" type="text"> <input type="submit"
			value="검색">
	</form>
	<br>
	<form action="/boot/searchWard">
		<select name="searchWard">
			<option value="all">--</option>
			<option value="A">A</option>
		<option value="B">B</option>
	
		</select> <input type="submit" value="탐색">
	</form>
	<br>

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
			<th>comment</th>
		</tr>
		<%if(Plist!=null){ %>
		<%for(int i=0; i<Plist.size(); i++){ %>
			<tr>
				<td><%=Plist.get(i).getPatinum()%></td>
				<td><%=Plist.get(i).getName()%></td>
				<td><%=Plist.get(i).getGender()%></td>
				<td><%=Plist.get(i).getAge()%></td>
				<td><%=Plist.get(i).getWard()%></td>
				<td><%=Plist.get(i).getHpdate()%></td>
				<td><%=Plist.get(i).getBloodtype()%></td>
				<form id="myForm" action="/boot/changeSepsisslevel">
				<input name="patinum" type="hidden" value="<%=Plist.get(i).getPatinum()%>">
				<input name="pastStatus" type="hidden" value="<%=Plist.get(i).getSepsisslevel()%>">
				<%if(Plist.get(i).getSepsisslevel().equals("Screening")){ %>
				<td><select name="sepsisslevel" id="mySelect" onchange="this.form.submit()">
						<option value="Screening" selected>Screening</option>
						<option value="Observing">Observing</option>
						<option value="None">None</option>
					</select>
				</td>
				
				<%} else if(Plist.get(i).getSepsisslevel().equals("Observing")){ %>
				<td><select name="sepsisslevel" id="mySelect" onchange="this.form.submit()">
						<option value="Screening">Screening</option>
						<option value="Observing" selected>Observing</option>
						<option value="None">None</option>
						</select>
						</td>
				
				<%}else if(Plist.get(i).getSepsisslevel().equals("None")){ %>
				<td><select name="sepsisslevel" id="mySelect" onchange="this.form.submit()">
						<option value="Screening">Screening</option>
						<option value="Observing">Observing</option>
						<option value="None" selected>None</option>
						</select> </td>
				
				<%} else { %>
				<td><%=Plist.get(i).getSepsisslevel() %></td>
				<%}%>
				</form>
				<td><%=Plist.get(i).getSepsisscore() %></td>
				<td><%=Plist.get(i).getPhysician()%></td>
				<td><a href="/boot/detail?patinum=<%=Plist.get(i).getPatinum()%>">상세보기</a></td>
				<td><a href="/boot/comment?patinum=<%=Plist.get(i).getPatinum()%>">commnet</a></td>
			</tr>
			<%}}else{%>
			<tr>
			</tr>
			<%}%>
			
	</table>
	<script>
	document.getElementById("mySelect").addEventListener("change", function() {
		console.log("do");
  		document.getElementById("myForm").submit();
	});
	</script>
</body>
</html>