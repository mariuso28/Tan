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
    width: 400px;
}
.fixed2{
    width: 700px;
}

.buttonExpandLabel {
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 50px;
  width: 300px;
  border: none;
}

.buttonCollapse {
  background-image: url(../../img/expand.gif);
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 25px;
  width: 25px;
  border: none;
}


.buttonExpand {
  background-image: url(../../img/collapse.gif);
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 25px;
  width: 25px;
  border: none;
}

.buttonCollapseCorner {
  background-image: url(../../img/expandcorner.gif);
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 25px;
  width: 25px;
  border: none;
}


.buttonExpandCorner {
  background-image: url(../../img/collapsecorner.gif);
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 25px;
  width: 25px;
  border: none;
}

.buttonCornerNop {
  background-image: url(../../img/corner.gif);
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 25px;
  width: 25px;
  border: none;
}

.buttonHorizontalNop {
  background-image: url(../../img/horizontal.gif);
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 25px;
  width: 25px;
  border: none;
}

.buttonVerticalNop {
  background-image: url(../../img/vertical.gif);
   background-repeat: no-repeat;
   background-position: 0% 0%;
  /* put the height and width of your image here */
  height: 25px;
  width: 25px;
  border: none;
}

.cn {
  display: flex;
  width: 600px;
  height: 25px;
}

.inner {
    height: 25px;
    text-align: left;
    vertical-align: top;
}

.innerb {
  width: 25px; height: 25px;
  text-align: left;
}

.innerb2 {
  width: 25px; height: 25px;
  text-align: left;
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
  <button id="adminButton"  class="buttonExpandLabel" type="button" data-toggle="collapse" data-target="#Admin">
    Administration
  </button>
  <div style="margin-left:0px" id="Admin" class="collapse">
    <a href="../admImport/importData">Import Data</a><br>
    <a href="../adm/registerMember">Register Member</a><br>
    <a href="../adm/manageMember">Manage Member</a><br>
    <a href="../adm/activateMember">Activate/Deactivate Member</a><br>
  </div>
  <button id="gmButton"  class="buttonExpandLabel" type="button" data-toggle="collapse" data-target="#GroupManage">
    Group Manaagement
  </button>
  <div style="margin-left:0px" id="GroupManage" class="collapse">
    <a href="../adm/placementMember">Placement Setting</a></td><br>
    <a href="../adm/manageMember">Commission Setting</a></td><br>
  </div>
  <button id="gButton"  class="buttonExpandLabel" type="button" data-toggle="collapse" data-target="#General">
    General
  </button>
  <div style="margin-left:0px" id="General" class="collapse">
    <a href="../admMember/accountDetails">Account Details</a><br>
  </div>
  <button id="rButton"  class="buttonExpandLabel" type="button" data-toggle="collapse" data-target="#Report">
    Report
  </button>
  <div style="margin-left:0px" id="Report" class="collapse">
    <a href="../adm/reportCompany">Company</a><br>
    <a href="../adm/reportAdmin">Admin</a><br>
  </div>

</div>
<div class="fixed2">

  <c:set var="tabList" value="" scope="request"/>
  <c:set var="sms" value="${admForm.memberSummary}" scope="request"/>
	<jsp:include page="_includeAll/membersDisplayMember.jsp"/>

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
