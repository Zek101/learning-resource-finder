<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib tagdir="/WEB-INF/tags/lrftag/" prefix="restag"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="/js/ext/jquery-1.9.0.min.js"></script>
<script src="/js/ext/bootstrap.js"></script>
<link rel="stylesheet" href="css/ext/bootstrap.css" type="text/css"
	media="screen" />
<link rel="stylesheet" href="css/ext/bootstrap-theme.css"
	type="text/css" media="screen" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
label {
	font-weight: normal;
}

.form-platform {
	position: absolute;
	top: 0px;
	left: 13em
}

.form-nature {
	position: absolute;
	top: 0px;
	left: 26em
}

.form-lang {
	position: absolute;
	top: 0px;
	left: 42em
}

.form-pub {
	position: absolute;
	top: 0px;
	left: 55em
}

.form-time {
	position: absolute;
	top: 0px;
	left: 68em
}


.filterSubBlock {
     display:inline-block;
     vertical-align:top;
     padding-right:30px;
}

</style>
<script>

	function addLang() {
		if ($("#monMenu input:checkbox:checked").length > 0) {
			var i = 0;
			$("input[name=language]")
					.each(
							function() {
								i++;
								if (this.checked) {
									document.getElementById('lang' + i).style.display = 'inline';
								} else {
									document.getElementById('lang' + i).style.display = 'none';
								}
								$('#monMenu').hide();
							});
		} else {
			alert('Vous devez s�lectionner au moins une langue');
		}
	}
</script>
<title>Insert title here</title>
</head>
<body>

	<form:form modelAttribute="searchOptions" action="searchresourcesubmit">
		<div class="filterSubBlock">
			<fieldset>
				<legend>Format</legend>
				<c:forEach var="formats" items="${formatEnumAllValues}">
					<label class="checkbox"> <form:checkbox path="format"
							value="${formats}" />${formats.description}
					</label>
				</c:forEach>
			</fieldset>
		</div>


		<div class="filterSubBlock">
			<fieldset>
				<legend>Plateforme</legend>
				<c:forEach var="platform" items="${platformsEnumAllValues}">
					<label class="checkbox"> <form:checkbox path="platform"
							value="${platform}" />${platform.description}
					</label>
				</c:forEach>
			</fieldset>
		</div>

		<div class="filterSubBlock">
			<fieldset>
				<legend>Nature p�dagogique</legend>
				<c:forEach var="nature" items="${natureEnumAllValues}">
					<label class="checkbox"> <form:checkbox path="nature"
							value="${nature}" />${nature.description}
					</label>
				</c:forEach>
			</fieldset>
		</div>

		<div class="filterSubBlock" style="max-width: 200px;">
			<fieldset>
				<legend>Langues</legend>
				<br> 
				<span id="lang1" class="label label-primary" style="display: inline">Fran�ais</span> 
				<span id="lang2" class="label label-primary" style="display: none">N�erlandais</span>
				<span id="lang3" class="label label-primary" style="display: none">Allemand</span>
				<span id="lang4" class="label label-primary" style="display: none">Anglais</span>
				<span id="lang5" class="label label-primary" style="display: none">Russe</span>
				<div class="dropdown">
					<br> <a data-toggle="dropdown" href="#"
						onclick="$('#monMenu').show();">Changer</a>
					<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel"
						id="monMenu">
					
						<c:forEach var="language" items="${languagesEnumAllValues}">
				
							<label class="checkbox" style="margin-left: 1em"> <form:checkbox
									path="language" value="${language}" />${language.description}
							</label>
						</c:forEach>

						<input type="button" id="btnclose" class="btn btn-default"
							data-dismiss="modal" style="margin-left: 1em" onclick="addLang()"
							value="Fermer">
					</ul>
				</div>
			</fieldset>
		</div>
		<div class="filterSubBlock">
			<fieldset>
				<legend>Publicit�</legend>
				<div class="radio">
					<label> <input type="radio" name="advertising" id="optionsRadio1"
						value="1" checked> Oui
					</label>
				</div>
				<div class="radio">
					<label> <input type="radio" name="advertising" id="optionsRadio2"
						value="0"> Non
					</label>
				</div>
			</fieldset>
		</div>

		<div class="filterSubBlock">
			<fieldset>
				<legend>Dur�e</legend>
				Moins de <input type="text" name="maxDuration">
			</fieldset>
		</div>
		<input type="hidden" name="search" value="${search}">
		<button type="submit" class="btn btn-default">Filtrer</button>
	</form:form>
		<c:forEach items="${resourcelist}" var="resource">
		<div style="float: left; position: relative; padding: 10px; margin-top: 10px; width: 210px;">
			<restag:resource resource="${resource}"></restag:resource>
		</div>
	</c:forEach>
	
	<ul class="pagination">
			
  <li><a href="searchresource?search=${search}&page=${param.page-1}">&laquo;</a></li>
  <c:forEach begin="1" end="${numberResource}" varStatus="loop">
    <li><a href="searchresource?search=${search}&page=${loop.index}">${loop.index}</a></li>
</c:forEach>
  <li><a href="searchresource?search=${search}&page=${param.page+1}">&raquo;</a></li>
</ul>
	
</body>
</html>