<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <title>admMemberManage</title>
  <style>
  </style>
</head>

<body>
  <div class="main">
    <form:form id="myForm" method="post" action="processAdm" modelAttribute="memberForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

    <h2 style="color:Cyan">Manage Member:</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="1000">
    <tbody align="left" style="color:purple; background-color:white}">
    <tr>
    <td><font color="#33ff36" size="2">Member To Change:</font></td>
    </tr>
     <tr>
     <td width="40%">
         <form:select path="command.memberToChangeCode"  style='width:20em'>
           <c:forEach items="${memberForm.chooseMembers}" var="member" >
              <option value="${member.userName}"  ${memberForm.inCompleteCommand.memberToChangeCode eq member.userName ? 'selected' : ''}>
                       ${member.weChatName} - ${member.rank} to ${member.parent.userName}</option>
           </c:forEach>
          </form:select>
     </td>
     <td width="20%"><input type="text" style='width:20em' name="command.search"
               value="${memberForm.inCompleteCommand.search}"/></td>
     <td width="20%"><input type="submit" name="searchWeChat" value="Search WeChat"class="button" style="height:23px; background-color:blue;"/></td>
     <td width="20%"><input type="submit" name="searchUserName" value="Search UserName"class="button" style="height:23px; background-color:blue;"/></td>
    </tr>
    <tr>
    <td  width="70%"><font color="#33ff36" size="2">New Username  (current : ${memberForm.inCompleteCommand.memberToChangeCode})</font></td>
    </tr>
    <tr>
    <td width="70%"><input type="text" style='width:20em' name="command.username"
              value=""/></td>
    <td><input type="submit" name="memberChangeUsername" value="Update" class="button" style="height:23px;"/></td>
    </tr>
    <tr>
      <td  width="70%"><font color="#33ff36" size="2">New WeChat Name  (current : ${memberForm.inCompleteCommand.weChatName})</font></td>
    </tr>
    <td width="70%"><input type="text" style='width:20em' name="command.weChatName" value="" /></td>
    <td><input type="submit" name="memberChangeWeChat" value="Update" class="button" style="height:23px;"/></td>
    </tr>
    <tr>
      <td  width="70%"><font color="#33ff36" size="2">Reset Password:</font></td>
    </tr>
    <tr>
      <td width="70%"><input type="password" style='width:20em' name="command.password"
                value=""/></td>
      <td><input type="submit" name="memberChangePw" value="Update" class="button" style="height:23px;"/></td>
    <tr>
    <tr>
        <td width="70%"><font color="#33ff36" size="2">Change Commission (Existing :  ${memberForm.inCompleteCommand.commission}%)</font></td>
    </tr>
    <tr>
        <td width="70%"><input type="text" style='width:20em' name="command.commission"
                  value=""/></td>
        <td><input type="submit" name="memberChangeCommission" value="Update" class="button" style="height:23px;"/></td>
    <tr>
    </tbody>
    </table>
    <tr><td><font color="red" size="3">${memberForm.errMsg}</font></td></tr>
    <tr><td><font color="blue" size="3">${memberForm.infoMsg}</font></td></tr>
    <br/>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    </br>
    <tr>
    <td><input type="submit" name="memberCancel" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    </tbody>
    </table>
  </div>
</form:form>
</body>
</html>
