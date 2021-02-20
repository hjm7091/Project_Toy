<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>

<html>
<head>
 <style>
   .no-underline{
      text-decoration:none;
   }
 </style>
  <meta charset="UTF-8">
  <title>사이드 메뉴</title>
</head>
<body>
	<h1>
		<c:if test="${isLogOn == true  && member.id == 'admin'}">
			<a href="${ contextPath }/member/listMembers.do"  class="no-underline">회원관리</a><br><br>
		</c:if>
	    <a href="${ contextPath }/board/listArticles.do?section=1&pageNum=1"  class="no-underline">자유게시판</a><br><br>
	    <a href="#"  class="no-underline">상품게시판</a><br>
    </h1>
	<%-- 
	<h1>
		<a href="${contextPath}/member/listMembers.do"  class="no-underline">회원관리</a><br>
		<a href="${contextPath}/board/listArticles.do"  class="no-underline">게시판관리</a><br>
		<a href="#"  class="no-underline">상품관리</a><br>
	</h1>
	 --%>
</body>
</html>