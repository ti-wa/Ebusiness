<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>eBusiness Framework Demo - Import</title>
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	<%@ include file="error.jsp"%>
	<%@ include file="authentication.jsp"%>
	<%@ include file="navigation.jspfragment"%>
	<script type="text/javascript">
		function validateUpload() {
			if (document.getElementById("xmlFile").value !== "") {
				document.getElementById("upload").submit();
			} else {
				alert("No File attached. Please select your XMLCatalog!");
			}
		}
	</script>
	<h1>IMPORT - Import Product Catalog</h1>
	<form id="upload" name="upload"
		action="controllerservlet?action=import" enctype="multipart/form-data"
		method="POST">
		<label>Selected File:</label> <input type="file" name="xmlFile"
			id="xmlFile" /> <br> <input type="button" value="Upload"
			onclick="validateUpload()" />
	</form>
	</br>
	<c:forEach var="info" items="${sessionScope.info}">
		<jsp:useBean id="info" type="java.lang.String" />
		<span class="error"> <%=info%>
		</span>
	</c:forEach>
	<c:if test="${not empty sessionScope.missingProducts}">
		</br>
    Not Imported Products:
    	<ul>
			<c:forEach var="product" items="${sessionScope.missingProducts}">
				<jsp:useBean id="product" type="java.lang.String" />
				<span class="error">
					<li><%=product%></li>
				</span>
			</c:forEach>
		</ul>
	</c:if>
	<p>
		<input type=button name=go-back value=" back "
			onclick="javascript:history.back()">
	</p>
</body>
</html>