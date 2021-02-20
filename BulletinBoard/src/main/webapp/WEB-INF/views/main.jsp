<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"   isELIgnored="false"  %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<c:set var="result" value="${param.result }" />

<%
  request.setCharacterEncoding("UTF-8");
%>    

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>메인 페이지</title>
  <c:if test="${ result == 'insertSuccessed' }">
	<script>
	    window.onload=function(){
	      alert("회원가입 성공!!");
	    }
	</script>
  </c:if>
  <script src="http://code.jquery.com/jquery-latest.js"></script>
</head>
<body>
       
    <h2>1. 주제</h2>
    <h3>답변형 게시판</h3>
        
    <h2>2. 목적</h2>
    <h3>이 페이지는 기본적인 CRUD를 연습하기 위한 홈페이지입니다.</h3>
      
    
    <h2>3. 기능</h2>
    <h3>
       1) 회원가입, 로그인, 로그아웃 <br />
       2) 관리자의 경우 회원관리 가능 <br />
       3) 글 목록보기, 페이징, 조회수 <br />
       4) 글 쓰기, 수정, 삭제, 답변, 상세보기<br />
       5) 이미지 파일 첨부 가능
    </h3>

    <h2>4. 사용 기술</h2>
    <h3>
       1) JSP <br />
       2) 스프링(타일즈, 마이바티스, MVC 패턴) <br />
       3) 오라클 데이터베이스     
    </h3>
        
</body>
</html>