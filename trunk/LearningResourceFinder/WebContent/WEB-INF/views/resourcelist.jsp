<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib tagdir="/WEB-INF/tags/lrftag/" prefix="lrftag"%>
<html>
<head>
<title>Catalog</title>
</head>
<body>
	<c:choose>
		<c:when test="${topic != null}">
			<lrftag:breadcrumb linkactive="${topic.description}">
			<lrftag:breadcrumbelement label="Home" link="/home" />
			<lrftag:breadcrumbelement label="Contributions" link="/contribution" />
			</lrftag:breadcrumb>
		</c:when>
		<c:otherwise>
			<lrftag:breadcrumb linkactive="Ressources">
			<lrftag:breadcrumbelement label="Home" link="/home" />
			<lrftag:breadcrumbelement label="${user.fullName}" link="/user/${user.userName}" />
			</lrftag:breadcrumb>
		</c:otherwise>
	</c:choose>
	
	<div class="container">
		<c:choose>
			 <c:when test="${topic != null}">
				<h1>${topic.description}: ${problemTitle}</h1>
			 </c:when>
			 <c:otherwise>
			 	<h1>Ressources : <a href="/user/${user.userName}">${user.fullName}</a></h1>
			 </c:otherwise>
		</c:choose>
	    <section id="resourcelist">
						<c:forEach items="${resourceList}" var="resource">
								<lrftag:resource resource="${resource}"/>
						</c:forEach>
	    </section>
	</div>
</body>
</html>