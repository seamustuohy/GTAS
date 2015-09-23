<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	 <title><tiles:insertAttribute name="title" ignore="true" /></title>
	      
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
	
	<title>San Andreas</title>
	
	<c:set scope="request" value="${pageContext.request.contextPath}/" var="baseLocation" />
    <c:set scope="request" value="${pageContext.request.contextPath}/resources/img/" var="baseImageLocation" />
    <c:set scope="request" value="${pageContext.request.contextPath}/resources/js/" var="baseJSLocation" />
    <c:set scope="request" value="${pageContext.request.contextPath}/resources/css/" var="baseCssLocation" />
	
<!-- Custom CSS -->
    <style>
	    body {
	        padding-left: 75px;
	        background:#f4f4f4;
	        /* Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. */
	    }
	        
		.fixed {
		  margin: 10px 5px 5px 50px ;
		  width: 1000px;
		  background-color: white;
		}
	        
        
    </style>


	<script type="text/javascript" src="resources/js/jquery.js"></script>
    <script type="text/javascript" src="resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="resources/js/angular.js"></script>
    <script type="text/javascript" src="resources/js/angular-route.js"></script>
    <script type="text/javascript" src="resources/app/app.js"></script>
    <script type="text/javascript" src="resources/js/moment.js"></script>

    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap-theme.css">
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
     
     
     
</head>
<body <tiles:insertAttribute name="bodyEventHandlerName"  ignore="true"/>>
<table>    
	<tbody>
		<tr>
	        <td><tiles:insertAttribute name="header"/></td>
	    </tr>
	    <tr>
			
		</tr>
	    <%-- <tr>
	        <td><tiles:insertAttribute name="menu"/></td>
	    </tr> --%>
	    <tr>
	         <td><tiles:insertAttribute name="body"/></td>
	    </tr>
	    <tr>
	        <td><tiles:insertAttribute name="footer"/></td>
	    </tr>
	</tbody>
</table>
</body>
</html>
