<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<c:set var="result" value="${param.result }" />
<%
   request.setCharacterEncoding("UTF-8");
%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 등록창</title>
<c:if test="${ result == 'insertFailed' }">
	<script>
	    window.onload=function(){
	      alert("중복된 아이디가 존재합니다!!");
	    }
	</script>
</c:if>
<style>
   .text_center{
     text-align:center;
   }
</style>
<script type="text/javascript">
	function checkValidate() {
		var frmLogin = document.frmLogin;
		var id = frmLogin.id.value;
		var pwd = frmLogin.pwd.value;
		var name = frmLogin.name.value;
		if(id.length == 0 || id == "" || pwd.length == 0 || pwd == ""){
			alert("아이디와 비밀번호는 필수입니다.");
		} else if(name.length == 0 || name == ""){
			alert("이름은 필수입니다.");
		} else{
			frmLogin.submit();
		}
	}
</script>  
</head>
<body>
	<form name="frmLogin" method="post" action="${contextPath}/member/insertMember.do">
	<h1  class="text_center">회원 정보 등록창</h1>
	<table border=1 align="center">
	   <tr>
	      <td width="200"><p align="right">아이디</p></td>
	      <td width="400"><input type="text" name="id"></td>
	   </tr>
	   <tr>
	      <td width="200"><p align="right">비밀번호</td>
	      <td width="400"><input type="password" name="pwd"></td>
	    </tr>
	    <tr>
	       <td width="200"><p align="right">이름</td>
	       <td width="400"><p><input type="text" name="name"></td>
	    </tr>
	    <tr>
	       <td width="200"><p align="right">이메일</td>
	       <td width="400"><p><input type="text" name="email"></td>
	    </tr>
	    <tr>
	       <td colspan="2" height="50">
	       		<input type="button" value="가입하기" onclick="checkValidate()">
	       		<input type="reset" value="다시입력">
	       </td>
	    </tr>
	</table>
	</form>
</body>
