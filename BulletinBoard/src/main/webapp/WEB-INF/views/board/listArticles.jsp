<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${ pageContext.request.contextPath }"/>
<c:set  var="articlesList"  value="${articlesList}" />
<c:set  var="totArticles"  value="${totArticles}" />
<c:set  var="section"  value="${section}" />
<c:set  var="pageNum"  value="${pageNum}" />
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
	<style>
	   .no-uline {text-decoration:none; color:black; font-size:20px;}
	   .sel-page{text-decoration:none; color:black; font-size:20px;}
	   .cls1 {text-decoration:none;}
	   .cls2{text-align:center; color:black; font-size:30px;}
   </style>
<meta charset="UTF-8">
<title>글목록창</title>
	<script type="text/javascript">
		function fn_articleForm(isLogOn, articleForm, loginForm) {
			if(isLogOn != '' && isLogOn != 'false'){
				location.href = articleForm;
			} else{
				alert("로그인 후 글쓰기가 가능합니다.");
				location.href = loginForm + '?action=/board/articleForm.do';
			}
		}
	</script>
</head>
<body>
	<table align="center" border="1" width="80%">
		<tr height="10" align="center" bgcolor="lightgreen">
			<td>번호</td>
			<td>작성자</td>
			<td>제목</td>
			<td>작성일</td>
			<td>조회수</td>
		</tr>
		<c:choose>
			<c:when test="${ articlesList == null }">
				<tr height="10">
					<td colspan="4">
						<p align="center">
							<b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b>
						</p>
					</td>
				</tr>
			</c:when>
			<c:when test="${ articlesList != null }">
				<c:forEach var="article" items="${ articlesList }" varStatus="articleNum">
					<tr height="10">
						<td width="5%">${ articleNum.count }</td>
						<td width="10%">${ article.id }</td>
						<td align="left" width="35%">
							<span style="padding-right:30px"></span>
							<c:choose>
								<c:when test="${ article.lvl > 1 }">
									<c:forEach begin="1" end="${ article.lvl }" step="1">
										<span style="padding-left:20px"></span>
									</c:forEach>
									<span style="font-size:12px;">[답변]</span>
									<a class="cls1" href="${ contextPath }/board/viewArticle.do?
												articleNO=${ article.articleNO }&
												section=${ section }&
												pageNum=${ pageNum }">${ article.title }</a>
								</c:when>
								<c:otherwise>
									<a class="cls1" href="${ contextPath }/board/viewArticle.do?
												articleNO=${ article.articleNO }&
												section=${ section }&
												pageNum=${ pageNum }">${ article.title }</a>
								</c:otherwise>
							</c:choose>
						</td>
						<td width="10%">
							<fmt:formatDate value="${ article.writeDate }"/>
						</td>
						<td width="5%">${ article.viewCount }</td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
	<br>
	<div class="cls2">
	 <c:if test="${toArticles != null }" >
	      <c:choose>
	        <c:when test="${toArticles >100 }">  <!-- 글 개수가 100 초과인경우 -->
		      <c:forEach   var="page" begin="1" end="10" step="1" >
		         <c:if test="${section >1 && page==1 }">
		          <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section-1}&pageNum=${(section-1)*10 +1 }">&nbsp; pre </a>
		         </c:if>
		          <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${(section-1)*10 +page } </a>
		         <c:if test="${page ==10 }">
		          <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section+1}&pageNum=${section*10+1}">&nbsp; next</a>
		         </c:if>
		      </c:forEach>
	        </c:when>
	        <c:when test="${toArticles ==100 }" >  <!--등록된 글 개수가 100개인경우  -->
		      <c:forEach   var="page" begin="1" end="10" step="1" >
		        <a class="no-uline"  href="#">${page } </a>
		      </c:forEach>
	        </c:when>
	        
	        <c:when test="${toArticles< 100 }" >   <!--등록된 글 개수가 100개 미만인 경우  -->
		      <c:forEach   var="page" begin="1" end="${toArticles/10 +1}" step="1" >
		         <c:choose>
		           <c:when test="${page==pageNum }">
		            <a class="sel-page"  href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${page } </a>
		          </c:when>
		          <c:otherwise>
		            <a class="no-uline"  href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${page } </a>
		          </c:otherwise>
		        </c:choose>
		      </c:forEach>
	        </c:when>
	      </c:choose>
	    </c:if>
	</div>    
	<br>
	<a class="cls1" href="javascript:fn_articleForm('${isLogOn}', '${contextPath}/board/articleForm.do', '${contextPath}/member/loginForm.do')">
		<p class="cls2">글쓰기</p>
	</a>
</body>
</html>