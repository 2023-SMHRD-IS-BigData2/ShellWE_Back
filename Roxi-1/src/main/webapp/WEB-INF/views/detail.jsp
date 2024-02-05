<%@page import="com.smhrd.roxi.entity.Roxi_Patient"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detail Page</title>
</head>
<body>
<%Roxi_Patient p = (Roxi_Patient)request.getAttribute("patient");%>
	<h1>${patient.name} 환자 정보</h1>
	<hr>
	<table>
		<tr>
			<td>이름 : </td>
			<td>${patient.name}</td>
		</tr>
		<tr>
			<td>나이 : </td>
			<td>${patient.age}</td>
		</tr>
		<tr>
			<td>성별 : </td>
			<td>${patient.gender}</td>
		</tr>
		<tr>
			<td>혈액형 : </td>
			<td>${patient.bloodtype}</td>
		</tr>
		<tr>
			<td>예측치 : </td>
			<td>${patient.sepsisscore}</td>
		</tr>
		<tr>
			<td>상태 : </td>
			<td>${patient.sepsisslevel}</td>
		</tr>
		<tr>
			<td>주치의 : </td>
			<td>${patient.physician}</td>
		</tr>
		<tr>
			<td>병동 : </td>
			<td>${patient.ward}</td>
		</tr>
		<tr>
			<td>입원날짜 : </td>
			<td>${patient.hpdate}</td>
		</tr>
		
	</table>

<a href="/boot/comment?patinum=${patient.patinum}">comment</a>
<hr>
<h3>데이터 입력</h3>
<form action="/boot/detailInsert" method="get">
	dbp : <input name="dbp" type="text">
	<br>
	hr : <input name="hr" type="text">
	<br>
	resp : <input name="resp" type="text">
	<br>
	sbp : <input name="sbp" type="text">
	<br>
	spo2 : <input name="spo2" type="text">
	<br>
	temp : <input name="temp" type="text">
	<br>
	<input type="hidden" name="patientnum" value="${patient.patinum}">
	<input type="submit" value="입력">
</form>


<hr>
<br>
날짜 선택

<form action="/boot/selectDate">
	<input name="date" type="date">
	<input type="hidden" name="patinum" value="${patient.patinum}">
	<input type="submit" value="선택">
</form>
<br>
<br>

<table border="1">
	<tr>
		<th>dbp</th>
		<th>hr</th>
		<th>resp</th>
		<th>sbp</th>
		<th>spo2</th>
		<th>temp</th>
		<th>예측치</th>
		<th>입력날짜</th>
	</tr>
	<c:forEach var="list" items="${list}">
	<tr>
	    <td>${list.dbp}</td>
	    <td>${list.hr}</td>
	    <td>${list.resp}</td>
	    <td>${list.sbp}</td>
	    <td>${list.spo2}</td>
	    <td>${list.temp}</td>
	    <td>${list.sepsisscore}</td>
	    <td>${list.sepdate}</td>
	</tr>
</c:forEach>
</table>

<br>
<a href="/boot/delPatient?patinum=${patient.patinum}">환자 삭제</a>

<a href="/boot/">목록으로 돌아가기</a>
<br>
<br>

<a href="/boot/SendAllData?patinum=${patient.patinum}">전체 데이터 파이썬으로 넘기기</a>
</body>
</html>