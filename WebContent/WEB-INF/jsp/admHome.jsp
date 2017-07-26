<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page trimDirectiveWhitespaces="true" %>

<html>
<head>
    <title>admHome</title>

      <meta name="viewport" content="width=device-width, initial-scale=1">
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>



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
    width: 500px;
}
.fixed2{
    width: 500px;
}

.buttonCollapse {
  background-image: url(../../img/collapse.gif);
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 20px;
  width: 20px;
  border: none;
}


.buttonExpand {
  background-image: url(../../img/expand.gif);
   background-repeat: no-repeat;
   background-position: 10% 10%;
  /* put the height and width of your image here */
  height: 20px;
  width: 20px;
  border: none;
}

.cn {
   position: relative;
  width: 300px;
  height: 20px;
  vertical-align: middle;
  text-align: center;
}

.inner {
   position: absolute;
  top: 0;
    bottom: 0;
    left: 20px;
  width: 120px; height: 20px;
}

.innerb {
  position: absolute;
 top: 0;
   bottom: 0;
   left: 0;
  width: 20px; height: 20px;
}

</style>



</head>


<body>



<form:form method="post" id="myForm" action="exec" modelAttribute="admMemberForm">

<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

<h2 style="background-color:${currUser.role.color}">
    Hi ${currUser.role.desc}&nbsp; ${currUser.contact}
</h2>
<div class="container">
<div class="fixed1">
<table border="0" cellpadding="3" cellspacing="0" width="300">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr>
  <td><a href="../adm/registerMember">Admin - Register Member</a></td>
</tr>
<tr>
  <td><a href="../adm/manageMember">Admin - Manage Member</a></td>
</tr>
<tr>
  <td><a href="../adm/activateMember">Admin - Activate/Deactivate Member</a></td>
</tr>
<tr>
  <td><a href="../adm/memberTree">Group Management - Structure Tree</a></td>
</tr>
<tr>
  <td><a href="../adm/placementMember">Group Management - Placement Setting</a></td>
</tr>
<tr>
  <td><a href="../adm/commissionSetting">Group Management - Commission Setting</a></td>
</tr>
<tr>
  <td><a href="../admMember/accountDetails">General - Account Details</a></td>
</tr>
<tr>
  <td><a href="../adm/reportCompany">Report - Company</a></td>
</tr>
<tr>
  <td><a href="../adm/reportAdmin">Report - Admin</a></td>
</tr>
</tbody>
</table>
</div>
<div class="fixed2">

  <c:set var="tabs" value="0" scope="request"/>
  <fmt:parseNumber var="tab" type="number" value="${tabs}" />
  <c:set var="sm" value="${admForm.memberSummary}" scope="request"/>
	<jsp:include page="_includeAll/memberDisplay.jsp"/>

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
