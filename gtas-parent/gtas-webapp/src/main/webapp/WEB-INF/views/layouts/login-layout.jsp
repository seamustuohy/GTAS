<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	 <title><tiles:insertAttribute name="title" ignore="true" /></title>
	
	<title>San Andreas</title>
	
</head>
<body> 

<tiles:insertAttribute name="body"/>


<%-- <tiles:insertAttribute name="bodyEventHandlerName"  ignore="true"/>>
<table>    
	<tbody>
		<tr>
	        <td><tiles:insertAttribute name="header"/></td>
	    </tr>
	    <tr>
			
		</tr>
	    <tr>
	        <td><tiles:insertAttribute name="menu"/></td>
	    </tr>
	    <tr>
	        <td style="width: 750px;" ></td> <td><tiles:insertAttribute name="body"/></td>
	    </tr>
	    <tr>
	        <td><tiles:insertAttribute name="footer"/></td>
	    </tr>
	</tbody>
</table>
 --%>
 
 </body>
</html>
