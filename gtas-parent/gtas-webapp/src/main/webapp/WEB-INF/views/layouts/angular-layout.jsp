<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html class="no-js" lang="en"  id="ng-app" ng-app="<tiles:insertAttribute name="angularApplicationName"/>" xmlns:ng="http://angularjs.org/">

<head>
	
	<title><tiles:insertAttribute name="title" ignore="true" /></title>
	
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	
	<tiles:insertAttribute name="style" ignore="true"/>
	
    <script type="text/javascript" src="<c:url value="/resources/js/angular.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/angular-resource.js"/>"></script>
   	<script type="text/javascript" src="<c:url value="/resources/js/angular-route.js"/>"></script>
   	<script type="text/javascript" src="<c:url value="/resources/js/ui-utils.js"/>"></script>
   	<script type="text/javascript" src="<c:url value="/resources/js/ng-table.js"/>"></script>
   	<script type="text/javascript" src="<c:url value="/resources/js/ui-bootstrap-tpls-0.11.0.js"/>"></script>
   	<script type="text/javascript" src="<c:url value="/resources/js/app.js"/>"></script>
   	<script type="text/javascript" src="<c:url value="/resources/js/controller.js"/>"></script>
      
	<tiles:insertAttribute name="javascript" ignore="true"/>
</head>


<body>

<table style="width: 1020px;">    
	<tbody>
		<tr>
	        <td><tiles:insertAttribute name="header"/></td>
	    </tr>
	    <tr>
	        <td><tiles:insertAttribute name="menu"/></td>
	    </tr>
	    <tr>
	         <td>
	         	<div>
	         		<tiles:insertAttribute name="body"/>
	         	</div>
	         </td>
	    </tr>
	    <tr>
	        <td><tiles:insertAttribute name="footer"/></td>
	    </tr>
	</tbody>
</table>
</body>
</html>
