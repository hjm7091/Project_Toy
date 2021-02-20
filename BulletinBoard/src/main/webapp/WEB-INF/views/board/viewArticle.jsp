<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<c:set var="imgPath" value="${contextPath}/download.do?imageFileName=${article.imageFileName}&articleNO=${article.articleNO }" />
<head>
   <meta charset="UTF-8">
   <title>글보기</title>
   <style type="text/css">
   	.unshowing {
    	display: none;
	}
   </style>
   <script src="http://code.jquery.com/jquery-latest.min.js"></script>
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
		  
	function back_to_List(){
	    location.href = "${contextPath}/board/listArticles.do?section=${section}&pageNum=${pageNum}";
	}
	   
	function fn_modify_article(obj){
 	  	  var title = obj.title.value;
		  var content = obj.content.value;
		  if(title.length === 0 || title === ""){
			  alert("제목을 입력해주세요.");
		  } else if(content.length === 0 || content === ""){
			  alert("내용을 입력해주세요.");
		  } else{
			  obj.action = "${ contextPath }/board/modArticle.do";
			  obj.submit();
		  }
	}
	   
   function toggle_action(){
 	  const i_title = document.getElementById("i_title");
 	  const i_content = document.getElementById("i_content");
 	  const i_imageFileName = document.getElementById("i_imageFileName");
 	  if(i_title.disabled === false && i_content.disabled === false){
 		  i_title.disabled = true;
 		  i_content.disabled = true;
 	  } else{
 		  i_title.disabled = false;
 		  i_content.disabled = false;
 	  }
 	  if(i_imageFileName !== null){
 		  if(i_imageFileName.disabled === true)
 			i_imageFileName.disabled = false;
 		  else{
 		  	i_imageFileName.disabled = true; 
 		  	$('#preview').attr('src', "${imgPath}");
 	  	  }
 	  }
 	  const action1 = document.querySelector(".action1");
       const action2 = document.querySelector(".action2"); 
       if(!action1.classList.contains("unshowing") && action2.classList.contains("unshowing")){
           action1.classList.add("unshowing");
           action2.classList.remove("unshowing");
       } else if(action1.classList.contains("unshowing") && !action2.classList.contains("unshowing")){
           action1.classList.remove("unshowing");
           action2.classList.add("unshowing");
       }
   }
	   
   function fn_remove_article(url,articleNO){
		 var form = document.createElement("form");
		 form.setAttribute("method", "post");
		 form.setAttribute("action", url);
	     var articleNOInput = document.createElement("input");
	     articleNOInput.setAttribute("type","hidden");
	     articleNOInput.setAttribute("name","articleNO");
	     articleNOInput.setAttribute("value", articleNO);
		 
	     form.appendChild(articleNOInput);
	     document.body.appendChild(form);
	     form.submit();
	 }
   
  	function fn_reply_form(isLogOn, url, parentNO){
  		if(isLogOn !== '' && isLogOn !== 'false'){
  			 var form = document.createElement("form");
  			 form.setAttribute("method", "get");
  			 form.setAttribute("action", url);
  		     var parentNOInput = document.createElement("input");
  		     parentNOInput.setAttribute("type","hidden");
  		     parentNOInput.setAttribute("name","parentNO");
  		     parentNOInput.setAttribute("value", parentNO);
  			 
  		     form.appendChild(parentNOInput);
  		     document.body.appendChild(form);
  			 form.submit();	
  		} else{
  			 alert("로그인 후 답변하기가 가능합니다.");
  			 location.href = "${ contextPath }/member/loginForm.do" + "?action=/board/replyForm.do&parentNO=" + parentNO; 
  		}
	 }
   </script>
</head>
<body>
  <form name="frmArticle" method="post"   enctype="multipart/form-data">
  <table  border="1" align="center" >
  <tr>
   <td align="center" bgcolor="#FF9933">
      글번호
   </td>
   <td>
    <textarea rows="1" cols="60" disabled >${ article.articleNO }</textarea>
    <input type="hidden" name="articleNO" value="${article.articleNO}"  />
    <input type="hidden" name="parentNO" value="${article.parentNO}"  />
   </td>
  </tr>
  <tr>
   <td align="center" bgcolor="#FF9933">
      작성자 아이디
   </td>
   <td >
    <textarea rows="1" cols="60" name="id"  disabled >${ article.id }</textarea>
    <input type="hidden" name="articleID" value="${article.id}"  />
   </td>
  </tr>
  <tr>
   <td align="center" bgcolor="#FF9933">
      제목 
   </td>
   <td>
    <textarea rows="1" cols="60" name="title"  id="i_title" disabled >${article.title }</textarea>
   </td>   
  </tr>
  <tr>
   <td align="center" bgcolor="#FF9933">
      내용
   </td>
   <td>
    <textarea rows="15" cols="60"  name="content"  id="i_content"  disabled >${article.content }</textarea>
   </td>  
  </tr>
 
<c:if test="${not empty article.imageFileName && article.imageFileName!='null' }">  
<tr>
   <td align="center" bgcolor="#FF9933"  rowspan="2">
      이미지
   </td>
   <td>
     <input  type= "hidden"   name="originalFileName" value="${article.imageFileName }" />
    <img id="preview" src="${ imgPath }" width=350 height=200 /><br>
       
   </td>   
  </tr>
     
  <tr>
    <td>
       <input width="400" type="file"  name="imageFileName " id="i_imageFileName"   disabled   onchange="readURL(this);" />
    </td>
  </tr>
  
 </c:if>
  <tr>
	   <td align="center" bgcolor="#FF9933">
	      등록일자
	   </td>
	   <td>
	    <textarea rows="1" cols="60" disabled ><fmt:formatDate value="${article.writeDate}" /></textarea>
	   </td>   
  </tr>
  
  <tr class="action2 unshowing" align="center" height="50">
	   <td colspan="2">
	       <input type=button value="수정반영하기"   onClick="fn_modify_article(frmArticle)"  >
           <input type=reset value="취소" onClick="toggle_action()">
	   </td>   
  </tr>
  
  <tr class="action1" align="center" height="50">
   <td colspan="2">
   	<c:if test="${member.id == article.id }">
   	  <input type=button value="수정하기" onClick="toggle_action()"/>
	  <input type=button value="삭제하기" onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNO})"/>
   	</c:if>		
	  <input type=button value="리스트로 돌아가기"  onClick="back_to_List()"/>
	  <input type="button" value="답변하기" onClick="fn_reply_form('${ isLogOn }', '${contextPath}/board/replyForm.do', ${article.articleNO})" >
   	  <!--<input type="button" value="답변하기" onClick="location.href='${ contextPath }/board/replyForm.do?parentNO=${ article.articleNO }'"/>-->
   </td>
  </tr>
  
 </table>
 </form>
</body>
</html>