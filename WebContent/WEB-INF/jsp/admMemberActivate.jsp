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

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
  <script type="text/javascript">

  function changeMemberActive()
  {
    oFormObject = document.forms['myForm'];

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='changeMemberActive';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();
  }

  </script>

</head>

<body>
  <div class="main">
    <form:form id="myForm" method="post" action="processAdm" modelAttribute="memberForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

    <h2 style="color:Cyan">Manage Member:</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="800">
    <tbody align="left" style="color:purple; background-color:white}">
    <tr>
    <td><font color="#33ff36" size="2">Member To Change:</font></td>
    </tr>
     <tr>
     <td width="40%">
         <form:select path="command.memberToChangeCode"  style='width:20em' onchange="return changeMemberActive();">
           <c:forEach items="${memberForm.chooseMembers}" var="member" >
              <option value="${member.userName}" ${memberForm.inCompleteCommand.memberToChangeCode eq member.userName ? 'selected' : ''}>
                       ${member.weChatName} - ${member.rank} to ${member.parent.userName}</option>
           </c:forEach>
          </form:select>
     </td>
     <td width="20%"><input type="text" style='width:20em' name="command.search"
               value="${memberForm.inCompleteCommand.search}"/></td>
     <td width="20%"><input type="submit" name="searchWeChat3" value="Search WeChat"class="button" style="height:23px; background-color:blue;"/></td>
     <td width="20%"><input type="submit" name="searchUserName3" value="Search UserName"class="button" style="height:23px; background-color:blue;"/></td>
    </tr>
    <tr>
      <c:if test="${memberForm.inCompleteCommand.enabled == true}">
        <td><font color="#33ff36" size="2">${memberForm.inCompleteCommand.memberToChangeCode} is Activated</font></td>
        <td><input type="submit" name="deactivateMember" value="Deactivate" class="button" style="height:23px;"/></td>
      </c:if>
      <c:if test="${memberForm.inCompleteCommand.enabled == false}">
        <td><font color="#33ff36" size="2">${memberForm.inCompleteCommand.memberToChangeCode} is Deactivated</font></td>
        <td><input type="submit" name="activateMember" value="Activate" class="button" style="height:23px;"/></td>
      </c:if>
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
