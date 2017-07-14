<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>admMemberCreate</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css" />
<style>
</style>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
<script type="text/javascript">

function changeMemberRank()
{
  oFormObject = document.forms['myForm'];

  var myin = document.createElement('input');
  myin.type='hidden';
  myin.name='changeMemberRank';
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

    <h2 style="color:Cyan">Register Member:</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="800">
    <tbody align="left" style="color:purple; background-color:white}">
    <tr>
    <td width="30%"><font color="#33ff36" size="2">Username:</font></td>
    <td width="70%"><input type="text" style='width:20em' name="command.username"
              value="${memberForm.inCompleteCommand.username}"/></td>
    </tr>
    <tr>
      <td width="30%"><font color="#33ff36" size="2">Password:</font></td>
      <td width="70%"><input type="password" style='width:20em' name="command.password"
                value="${memberForm.inCompleteCommand.password}"/></td>
    <tr>
    </tr>
      <td width="30%"><font color="#33ff36" size="2">Verfiy Password:</font></td>
      <td width="70%"><input type="password" style='width:20em' name="command.vPassword"
                value="${memberForm.inCompleteCommand.password}"/></td>
    </tr>
    <tr>
      <td width="30%"><font color="#33ff36" size="2">WeChat Name:</font></td>
    <td width="70%"><input type="text" style='width:20em' name="command.weChatName" value="${memberForm.inCompleteCommand.weChatName}" /></td>
    </tr>
    <tr>
        <td width="30%"><font color="#33ff36" size="2">Member Rank:</font></td>
        <td width="70%">
         <form:select path="command.memberRank" style='width:20em' onchange="changeMemberRank();">
              <option value="ROLE_SMA" ${memberForm.inCompleteCommand.memberRank eq 'ROLE_SMA' ? 'selected' : ''}>SMA</option>
              <c:if test="${memberForm.adminOnly == false}">
                <option value="ROLE_MA" ${memberForm.inCompleteCommand.memberRank eq 'ROLE_MA' ? 'selected' : ''}>MA</option>
                <option value="ROLE_AGENT" ${memberForm.inCompleteCommand.memberRank eq 'ROLE_AGENT' ? 'selected' : ''}>AGENT</option>
                <option value="ROLE_PLAY" ${memberForm.inCompleteCommand.memberRank eq 'ROLE_PLAY' ? 'selected' : ''}>PLAYER</option>
              </c:if>
          </form:select>
       </td>
     </tr>
     <tr>
      <td width="30%"><font color="#33ff36" size="2">Member Superior:</font></td>
      <td width="70%">
          <form:select path="command.superiorCode"  style='width:20em'>
            <c:forEach items="${memberForm.upstreamMembers}" var="member" >
               <option value="${member.code}"  ${memberForm.inCompleteCommand.superiorCode eq member.code ? 'selected' : ''}>${member.contact}</option>
            </c:forEach>
           </form:select>
      </td>
     </tr>
    </tbody>
    </table>
    <tr><td><font color="red" size="3">${memberForm.errMsg}</font></td></tr>
    <br/>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0" width="600">
    <tbody align="left" style="color:purple;">
    </br>
    <tr>
    <td><input type="submit" name="memberRegister" value="Register" class="button" style="height:23px;"/></td>
    <td><input type="submit" name="memberCancel" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    </tbody>
    </table>
    </form:form>
  </div>

</body>
</html>
