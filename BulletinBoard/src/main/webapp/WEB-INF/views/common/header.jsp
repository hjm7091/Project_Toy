<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
   .no-underline{
      text-decoration:none;
   }
</style>
<title>상단부</title>
</head>
<body>
<table border=0 width="100%">
	<tr>
		<td width="20%">
		</td>
		<td style="center" width="60%">
			<a href="${ contextPath }/main.do" class="no-underline">
				<h1><font size=30>게시판 연습</font></h1>
			</a>
		</td>
		<td width="20%">
			<c:choose>
	          <c:when test="${isLogOn == true  && member!= null}">
	            <h3>환영합니다. ${member.name }님!</h3>
	            <a href="${contextPath}/member/logout.do"><h3>로그아웃</h3></a>
	          </c:when>
	          <c:otherwise>
		        <a href="${contextPath}/member/loginForm.do"><h3>로그인</h3></a>
		      </c:otherwise>
		   </c:choose>
		   <a href="${contextPath}/member/insertForm.do"><h3>회원가입</h3></a>
	    </td> 
	</tr>
</table>
</body>
</html>