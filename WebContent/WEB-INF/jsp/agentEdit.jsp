<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <title>agentEdit</title>
  <style>
  </style>
</head>

<body>
  <div class="main">
    <form:form method="post" action="processAgent.html" modelAttribute="agentEditForm">
      <div class="headerPic">
  			<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.png' border='0'>
  		</div>
  		<div class="headerWelcome">
  			<h2 style="color:#aaa; font-weight:700;">
  				${currUser.role.desc}&nbsp; <font color="${currUser.role.color}">${currUser.contact}</font> - Edit Profile
  			</h2>
  		</div>
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
      <table border="0" cellpadding="3" cellspacing="0" width="100%">
        <tbody align="left" style="color:purple; background-color:LightYellow">
          <tr>
            <td width="30%" style="text-align:right;">Contact:</td>
            <td width="70%"><input type="text" name="profile.contact" value="${agentEditForm.inCompleteProfile.contact}" /></td>
          </tr>
          <tr>
            <td width="30%" style="text-align:right;">Password:</td>
            <td width="70%"><input type="password" style='width:20em' name="profile.password" value=""/></td>
          </tr>
          <tr>
            <td width="30%" style="text-align:right;">Verify Password:</td>
            <td width="70%"><input type="password" style='width:20em' name="vPassword" value=""/></td>
          </tr>
          <tr>
            <td width="30%" style="text-align:right;">Phone:</td>
            <td width="70%"><input type="text" name="profile.phone" value="${agentEditForm.inCompleteProfile.phone}"/></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td align="right">
              <input type="submit" name="saveAgent" value="Save Profile" class="button" style="height:23px; background-color: #00BB00;"/>
              <input type="submit" name="memberCancel" value="Cancel" class="button" style="height:23px; background-color: #BB0000;"/>
            </td>
          </tr>
        </tbody>
      </table>
      <br/>
      <font color="red">${agentEditForm.errMsg}</font>
      <br/>
      <table border="0" cellpadding="3" cellspacing="0" width="600">
        <tbody align="left" style="color:purple; background-color:White">
        </tbody>
      </table>
    </form:form>
  </div>
</body>
</html>
