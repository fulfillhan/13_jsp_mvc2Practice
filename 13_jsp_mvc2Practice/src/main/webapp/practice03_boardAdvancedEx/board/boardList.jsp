<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>boardList</title>
<style>
	ul {
	    list-style:none;
	    margin:0;
	    padding:0;
	}
	
	li {
	    margin: 0 0 0 0;
	    padding: 0 0 0 0;
	    border : 0;
	    float: left;
	}
</style>
<script src="jquery/jquery-3.6.1.min.js"></script>
<script>

	
	
</script>
</head>
<body>
	<div id="hi">
	</div>
	<p align="right">
		<a href="boardSetDummy">(히든)테스트 데이터 생성(한번만클릭)</a>
	</p>

	<div align="center" style="padding-top: 100px" >
		<h3> 커뮤니티 게시글 리스트 </h3>
		<table border="1">
			<colgroup>
				<col width="10%">
				<col width="40%">
				<col width="20%">
				<col width="20%">
				<col width="10%">
			</colgroup>
			<tr>
				<td> 
					조회 : <span style="color:red">${allBoardCnt}</span>개
				</td>
				<td colspan="4" align="right" >
					<select id="onePageViewCnt" onchange="getBoardList()" >
						<option>5</option>
						<option>7</option>
						<option>10</option>
					</select>
				</td>
			</tr>
			<tr align="center">
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>조회수</th>
			</tr>
			<c:forEach var="mainBoardDTO" items="${boardList }" }>
			<tr align = "center">
			<!-- 여기서부터 -->
			
			</tr>
			</c:forEach>
				<tr align="right">
				<td colspan="5">
					<input type="button" value="글쓰기" onclick="location.href='boardWrite'">
				</td>
			</tr>
			<tr>
				<td colspan="5" align="center">			
					<select id="searchKeyword">
						<option value="total">전체검색</option>
						<option value="writer">작성자</option>
						<option value="subject">제목</option>
					</select>
					<input type="text" id="searchWord" name="searchWord" value="${searchWord }">
					<input type="button" id="searchBtn" value="검색" onclick="getBoardList()">
				</td>
			</tr>
		</table>
		<div style="display: table; margin-left: auto; margin-right: auto">
			<ul>
			
			</ul>
		</div>
	</div>
	
</body>
</html>