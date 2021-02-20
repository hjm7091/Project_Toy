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
<title>로그인창</title>
	<c:choose>
		<c:when test="${result=='loginFailed' }">
		  <script>
		    window.onload=function(){
		      alert("아이디나 비밀번호가 틀립니다.다시 로그인 하세요!");
		    }
		  </script>
		</c:when>
	</c:choose>
	<script type="text/javascript">
		function checkValidate() {
			var frmLogin = document.frmLogin;
			var id = frmLogin.id.value;
			var pwd = frmLogin.pwd.value;
			if(id.length == 0 || id == "" || pwd.length == 0 || pwd == ""){
				alert("아이디와 비밀번호는 필수입니다.");
			} else{
				frmLogin.submit();
			}
		}
	</script>  
</head>

<body>
<form name="frmLogin" method="post"  action="${contextPath}/member/login.do">
   <table border="1"  width="60%" align="center" >
      <tr align="center">
         <td>아이디</td>
         <td>비밀번호</td>
      </tr>
      <tr align="center">
         <td>
	    <input type="text" name="id" value="" size="20">
	 </td>
         <td>
	    <input type="password" name="pwd" value="" size="20">
	 </td>
      </tr>
      <tr align="center">
         <td colspan="2" height="50">
            <input type="button" value="로그인" onclick="checkValidate()" > 
            <input type="reset"  value="다시입력" > 
         </td>
      </tr>
   </table>
</form>
</body>
</html>
