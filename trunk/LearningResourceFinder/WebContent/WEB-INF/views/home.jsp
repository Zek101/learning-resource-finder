<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<html>
<head>
	<title>ToujoursPlus.be - Home</title>
</head>
<body>

<%---------------------------------- COUNTER ----------------------------------%>
<%@include file="/WEB-INF/includes/counter.jsp" %>

<%----------------------------------- SLIDER ----------------------------------%>
<%-- TODO... --%>



<%------------------------------------ SEARCH ---------------------------------%>
<div style="background-color:#17A7D6;">
<div class="container" style="padding:20px 20px;">
<div class="row">
  <div class="col-md-8 col-md-offset-2">
  		<form role="search" method="get" action="/search">
  				   <div class="form-group">
					<div class="input-group" >
						<input name="searchphrase"  type="text" class="form-control input-lg" placeholder="mot(s) clef(s)  ex: fraction, Bonaparte, abeille,..." required>
						<span class="input-group-btn">
							<button class="btn btn-default btn-lg" type="submit">Rechercher</button>
						</span>
					</div>
				  </div>
		</form>
  </div>
</div>  
</div>
</div>



<%------------------------------------ YEARS ---------------------------------%>
<div class="container" style="padding-top:20px; padding-bottom:40px;">
	<div class="row">
	  <div class="col-sm-4 col-sm-offset-2">
	      <h3>primaire</h3><br/>
	      <a style="width:170px;" class="btn btn-primary btn-lg" href="<c:url value='/cycle?id=300'/>">1<sup>�re</sup> &nbsp;&nbsp;-&nbsp;&nbsp; 2<sup>�me</sup></a><br/>
	      <br/>
	      <a style="width:170px;" class="btn btn-primary btn-lg" href="<c:url value='/cycle?id=301'/>">3<sup>�me</sup> &nbsp;&nbsp;-&nbsp;&nbsp; 4<sup>�me</sup></a><br/>
	      <br/>
	      <a style="width:170px;" class="btn btn-primary btn-lg" href="<c:url value='/cycle?id=302'/>">5<sup>�me</sup> &nbsp;&nbsp;-&nbsp;&nbsp; 6<sup>�me</sup></a><br/>
	  </div>
	  <div class="col-sm-4 col-sm-offset-2">
	      <h3>secondaire</h3><br/>
	      <a style="width:170px;" class="btn btn-primary btn-lg" href="<c:url value='/cycle?id=304'/>">1<sup>�re</sup> &nbsp;&nbsp;-&nbsp;&nbsp; 2<sup>�me</sup></a><br/>
	      <br/>
	      <a style="width:170px;" class="btn btn-primary btn-lg" href="<c:url value='/cycle?id=305'/>">3<sup>�me</sup> &nbsp;&nbsp;-&nbsp;&nbsp; 6<sup>�me</sup></a><br/>
	  </div>
	</div>
</div>  

<%------------------------------------ TESTIMONIALS ---------------------------------%>
<div style="background-color:#84BB04;">
<div class="container" style="padding:20px 20px; color:white;">
	<%@include file="/WEB-INF/includes/testimonials.jsp" %>
</div>
</div>

<%------------------------------------ BENEFITS -------------------------------------%>
<div style="background-color:#17A7D6;">
<div class="container" style="padding:20px 20px; color:white;">
	<%@include file="/WEB-INF/includes/benefits.jsp" %>
</div>
</div>

<%------------------------------------ PURPOSES -------------------------------------%>
<div style="background-color:white;">
<div class="container" style="padding:20px 20px; color:white;">
	<%@include file="/WEB-INF/includes/purposes.jsp" %>
</div>
</div>

<%------------------------------------ SUPPORT --------------------------------------%>
<div style="background-color:white;">
<div class="container" style="padding:20px 20px; color:black;">
	<%@include file="/WEB-INF/includes/support.jsp" %>
</div>
</div>

</body>
</html>