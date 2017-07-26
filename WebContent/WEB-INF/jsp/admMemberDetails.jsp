<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <title>admMemberDetails</title>
  <style>
  </style>
</head>

<body>
  <div class="main">
    <form:form method="post" action="processAdmMember" modelAttribute="admMemberForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
      <div class="headerPic">
  			<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.png' border='0'>
  		</div>
  		<div class="headerWelcome">
  			<h2 style="color:#aaa; font-weight:700;">
  				${currUser.role.desc}&nbsp; <font color="${currUser.role.color}">${currUser.contact}</font>
  			</h2>
  		</div>
      <table border="0" cellpadding="3" cellspacing="0" width="100%">
        <tbody align="left" style="color:purple; background-color:LightYellow">
          <tr>
            <td width="30%" style="text-align:right;">WeChat Name:</td>
            <td width="70%">${currUser.contact}</td>
          </tr>
          <tr>
            <td width="30%" style="text-align:right;">Member Rank:</td>
            <td width="70%">${currUser.role.shortCode}</td>
          </tr>
          <tr>
            <td width="30%" style="text-align:right;">Password:</td>
            <td width="70%"><input type="password" style='width:20em' name="password" value=""/></td>
          </tr>
          <tr>
            <td width="30%" style="text-align:right;">Verify Password:</td>
            <td width="70%"><input type="password" style='width:20em' name="vPassword" value=""/></td>
          </tr>
          <tr>
            <td width="30%" style="text-align:right;">Superior WeChat Name:</td>
            <c:if test="${currUser.role == 'ROLE_ADMIN'}">
                <td width="70%">N/A</td>
            </c:if>
            <c:if test="${currUser.role != 'ROLE_ADMIN'}">
                <td width="70%">${currUser.parent.contact}</td>
            </c:if>
          </tr>
          <tr>
            <td width="30%" style="text-align:right;">Commission %:</td>
            <td width="70%">${currUser.account.commission}</td>
          </tr>
          <tr>
            <td></td>
            <td align="right">
              <input type="submit" name="updatePassword" value="Update Password" class="button" style="height:23px; background-color: #00BB00;"/>
              <input type="submit" name="memberCancel" value="Cancel" class="button" style="height:23px; background-color: #BB0000;"/>
            </td>
          </tr>
        </tbody>
      </table>
      <br/>
      <tr><td><font color="red">${admMemberForm.errMsg}</font></td></tr>
      <tr><td><font color="blue">${admMemberForm.infoMsg}</font></td></tr>
      <br/>
    </form:form>
  </div>
</body>
</html>
