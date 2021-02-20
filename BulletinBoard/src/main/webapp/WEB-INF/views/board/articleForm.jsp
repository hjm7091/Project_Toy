<%@ page language="java" contentType="text/html; charset=UTF-8"
     pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  /> 
<head>
<meta charset="UTF-8">
<title>글쓰기창</title>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	function readURL(input) {
	      if (input.files && input.files[0]) {
		      var reader = new FileReader();
		      reader.onload = function (e) {
		        $('#preview').attr('src', e.target.result);
	          }
	         reader.readAsDataURL(input.files[0]);
	      }
	}
   
  	function backToList(obj){
    	obj.action="${contextPath}/board/listArticles.do";
    	obj.submit();
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
 <title>새글 쓰기 창</title>
</head>
<body>
<h1 style="text-align:center">새글 쓰기</h1>
  <form name="articleForm" method="post"   action="${contextPath}/board/addNewArticle.do"   enctype="multipart/form-data">
    <table border="1" align="center">
     <tr>
       <td bgcolor="#FF9933">작성자 </td>
       <td colspan="2" align="left">
       	<textarea name="id" rows="1" cols="65" maxlength="500" disabled>${ member.name }</textarea>
       </td>
     </tr>
     <tr>
	   <td bgcolor="#FF9933">글제목 </td>
	   <td colspan="2"><textarea name="title" rows="1" cols="65" maxlength="500"></textarea> </td>
	 </tr>
	 <tr>
		<td bgcolor="#FF9933">글내용 </td>
		<td colspan=2><textarea name="content" rows="10" cols="65" maxlength="4000"></textarea> </td>
     </tr>
     <tr height="230">
         <td bgcolor="#FF9933">이미지 추가</td>
	     <td> <input type="file" name="imageFileName"  onchange="readURL(this);" /></td>
         <td><img  id="preview" src="${contextPath}/resources/image/default.JPG"   width=200 height=200/></td>
	 </tr>
	 <tr>
	    <td colspan="3" height="50">
	       <input type="button" value="글쓰기" onClick="checkValidate(this.form)"/>
	       <input type="button" value="목록보기" onClick="backToList(this.form)" />
	    </td>
     </tr>
    </table>
  </form>
</body>
</html>
