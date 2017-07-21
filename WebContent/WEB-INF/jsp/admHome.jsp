<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page trimDirectiveWhitespaces="true" %>

<html>
<head>
    <title>admHome</title>
<style>
th{
text-align:center;

}

body{
font: 20px Arial, sans-serif;
}

.container{
    display: flex;
}
.fixed1{
    width: 300px;
}
.fixed2{
    width: 600px;
}

</style>
</head>


<body>

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>

<form:form method="post" id="myForm" action="exec" modelAttribute="admForm">

<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

<h2 style="background-color:${currUser.role.color}">
    Hi ${currUser.role.desc}&nbsp; ${currUser.contact}
</h2>
<div class="container">
<div class="fixed1">
<table border="0" cellpadding="3" cellspacing="0" width="300">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr>
  <td><a href="../adm/registerMember">Register Member</a></td>
</tr>
<tr>
  <td><a href="../adm/placementMember">Placement Setting</a></td>
</tr>
<tr>
  <td><a href="../adm/memberTree">Member Tree</a></td>
</tr>
<tr>
  <td><a href="../adm/modifyProfile">Modify Profile</a></td>
</tr>
</tbody>
</table>
</div>
<div class="fixed2">
<table border="0" cellpadding="3" cellspacing="0" width="600">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
  <c:set var="tabs" value="0" scope="request"/>
  <fmt:parseNumber var="tab" type="number" value="${tabs}" />
  <c:set var="sm" value="${admForm.memberSummary}" scope="request"/>
	<jsp:include page="_includeAll/memberDisplay.jsp"/>
</tbody>
</table>
</div>
</div>
<br/>
<tr><td><font color="red">${admForm.errMsg}</font></td></tr>
<tr><td><font color="blue">${admForm.infoMsg}</font></td></tr>
</br>
</br>

</br>
<tr>
<td><a href="../logon/signin">Logoff</a></td>
</tr>


</form:form>
</body>
</html>
