<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<%
  request.setCharacterEncoding("UTF-8");
%> 
<head>
<meta charset="UTF-8">
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">

  function backToArticle(){
	  location.href = "${contextPath}/board/viewArticle.do?articleNO=${parentNO}";
  }
 
  function readURL(input) {
      if (input.files && input.files[0]) {
          var reader = new FileReader();
          reader.onload = function (e) {
              $('#preview').attr('src', e.target.result);
          }
          reader.readAsDataURL(input.files[0]);
      }
  }
  
  function checkValidate(obj) {
		var title = obj.title.value;
		var content = obj.content.value;
		if(title.length == 0 || title == ""){
			alert("제목을 입력해주세요.");
		} else if(content.length == 0 || content == ""){
			alert("내용을 입력해주세요.");
		} else{
			obj.submit();
		}
   }
</script> 
<title>답글쓰기 페이지</title>
</head>
<body>
 <h1 style="text-align:center">답글쓰기</h1>
  <form name="frmReply" method="post"  action="${contextPath}/board/addReply.do?parentNO=${parentNO}"   enctype="multipart/form-data">
    <table border="1" align="center">
    	<tr>
			<td bgcolor="#FF9933">답변자</td>
			<td colspan="2" align="left">
				<textarea name="id" rows="1" cols="65" maxlength="500" disabled >${member.name}</textarea> 
			</td>
		</tr>
		<tr>
			<td bgcolor="#FF9933">글제목</td>
			<td colspan="2">
				<textarea name="title" rows="1" cols="65" maxlength="500"></textarea>
			</td>
		</tr>
		<tr>
			<td bgcolor="#FF9933">글내용 </td>
			<td colspan="2"><textarea name="content" rows="10" cols="65" maxlength="4000"> </textarea> </td>
		</tr>
		<tr height="230">
			<td bgcolor="#FF9933">이미지 추가</td>
			<td> <input type="file" name="imageFileName"  onchange="readURL(this);" /></td>
            <td><img  id="preview" src="${contextPath}/resources/image/default.JPG"   width=200 height=200/></td>
		</tr>
		<tr>
			<td colspan="3" height="50">
				<input type="button" value="답글반영하기" onClick="checkValidate(this.form)"/>
				<input type="button" value="취소" onClick="backToArticle()" />
			</td>
		</tr>
    </table>
  </form>
</body>
</html>