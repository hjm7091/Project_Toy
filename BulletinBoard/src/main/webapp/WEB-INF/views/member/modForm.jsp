<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" 
    isELIgnored="false"  %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />

<%
  request.setCharacterEncoding("UTF-8");
%>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정창</title>
<style>
   .text_center{
     text-align:center;
   }
</style>
<script type="text/javascript">
	function checkValidate(obj) {
		var pwd = obj.pwd.value;
		var name = obj.name.value;
		var email = obj.email.value;
		if(pwd.length === 0 || pwd === ""){
			alert("비밀번호는 필수입니다.");
		} else if(name.length === 0 || name === ""){
			alert("이름은 필수입니다.");
		} else{
			obj.submit();
		}
	}
</script>
</head>
<body>
	<form name="frmMod" method="post"   action="${contextPath}/member/modMember.do?id=${memInfo.id}">
	<h1  class="text_center">회원 정보 수정창</h1>
	<table border="1" align="center">
	   <tr>
	      <td width="200"><p align="right">아이디</td>
	      <td width="400"><input type="text" name="id" value="${memInfo.id}" disabled></td>
	   </tr>
	   <tr>
	      <td width="200"><p align="right">비밀번호</td>
	      <td width="400"><input type="password" name="pwd" value="${memInfo.pwd}"></td>
	    </tr>
	    <tr>
	       <td width="200"><p align="right">이름</td>
	       <td width="400"><p><input type="text" name="name" value="${memInfo.name}"></td>
	    </tr>
	    <tr>
	       <td width="200"><p align="right">이메일</td>
	       <td width="400"><p><input type="text" name="email" value="${memInfo.email}"></td>
	    </tr>
	    <tr>
	       <td colspan="2" height="50">
	       		<input type="button" value="수정하기" onClick="checkValidate(this.form)">
	       		<input type="reset" value="다시입력">
	       	</td>
	    </tr>
	</table>
	</form>
</body>
</html>