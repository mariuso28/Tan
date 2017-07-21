<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>admMemberChangeLevel</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css" />
<style>
</style>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
<script type="text/javascript">

function changeMemberUpline()
{
  oFormObject = document.forms['myForm'];

  var myin = document.createElement('input');
  myin.type='hidden';
  myin.name='changeMemberUpline';
  myin.value='MaHa';
  oFormObject.appendChild(myin);
  oFormObject.submit();
}

function confirmChangeUpline()
{
  oFormObject = document.forms['myForm'];

  var field = "command.memberToChangeUpline";
  oformElement = oFormObject.elements[field];
  member = oformElement.value;

  var field1 = "command.superiorCode";
  oformElement = oFormObject.elements[field1];
  superior = oformElement.value;

  return confirm("Change superior for member : " + member + " to : " + superior);
}

function confirmChangeLevel()
{
  oFormObject = document.forms['myForm'];

  var field = "command.memberToChangeCode";
  oformElement = oFormObject.elements[field];
  member = oformElement.value;

  var field1 = "command.memberRank";
  oformElement = oFormObject.elements[field1];
  rank = oformElement.value;

  return confirm("Change rank of member : " + member + " to : " + rank);
}


</script>

</head>

<body>
  <div class="main">
    <form:form id="myForm" method="post" action="processAdm" modelAttribute="memberForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

    <h2 style="color:Cyan">Change Member Level:</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="1000">
    <tbody align="left" style="color:purple; background-color:white}">
      <tr>
       <td width="20%"><font color="#33ff36" size="2">Member To Change:</font></td>
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
       <td width="10%"><input type="submit" name="searchWeChat" value="Search WeChat"class="button" style="height:23px; background-color:blue;"/></td>
       <td width="10%"><input type="submit" name="searchUserName" value="Search UserName"class="button" style="height:23px; background-color:blue;"/></td>
      </tr>
    <tr>
        <td width="30%"><font color="#33ff36" size="2">Member Rank:</font></td>
        <td width="70%">
         <form:select path="command.memberRank" style='width:20em'>
              <option value="SMA" ${memberForm.inCompleteCommand.memberRank eq 'SMA' ? 'selected' : ''}>SMA</option>
              <c:if test="${memberForm.adminOnly == false}">
                <option value="MA" ${memberForm.inCompleteCommand.memberRank eq 'MA' ? 'selected' : ''}>MA</option>
                <option value="AGENT" ${memberForm.inCompleteCommand.memberRank eq 'AGENT' ? 'selected' : ''}>AGENT</option>
                <option value="PLAY" ${memberForm.inCompleteCommand.memberRank eq 'PLAY' ? 'selected' : ''}>PLAYER</option>
              </c:if>
          </form:select>
       </td>
     </tr>
     <tr>
     <td><input type="submit" name="changeMemberLevel" value="Change Level" class="button" style="height:23px;" onclick="return confirmChangeLevel()"/></td>
     </tr>
    </tbody>
    </table>
    <tr><td><font color="red" size="3">${memberForm.errMsg}</font></td></tr>
    <br/>
    <br/>
    <h2 style="color:Cyan">Change Member Upline:</h2>
    <table border="0" cellpadding="3" cellspacing="0" width="1000">
    <tbody align="left" style="color:purple;">
      <tr>
       <td width="20%"><font color="#33ff36" size="2">Member To Change:</font></td>
       <td width="40%">
           <form:select path="command.memberToChangeUpline"  style='width:20em' onchange="return changeMemberUpline();">
             <c:forEach items="${memberForm.uplineMembers}" var="member" >
                <option value="${member.userName}"  ${memberForm.inCompleteCommand.memberToChangeUpline eq member.userName ? 'selected' : ''}>
                         ${member.weChatName} - ${member.rank} to ${member.parent.userName}</option>
             </c:forEach>
            </form:select>
       </td>
       <td width="20%"><input type="text" style='width:20em' name="command.search1"
                 value="${memberForm.inCompleteCommand.search1}"/></td>
       <td width="10%"><input type="submit" name="searchWeChat1" value="Search WeChat"class="button" style="height:23px; background-color:blue;"/></td>
       <td width="10%"><input type="submit" name="searchUserName1" value="Search UserName"class="button" style="height:23px; background-color:blue;"/></td>
      </tr>
      <tr>
       <td width="30%"><font color="#33ff36" size="2">New Superior:</font></td>
       <td width="70%">
           <form:select path="command.superiorCode"  style='width:20em' >
             <c:forEach items="${memberForm.possibleSuperiors}" var="member" >
                <option value="${member.userName}"  ${memberForm.inCompleteCommand.superiorCode eq member.userName ? 'selected' : ''}>
                         ${member.weChatName} - ${member.rank}</option>
             </c:forEach>
            </form:select>
       </td>
       <td width="20%"><input type="text" style='width:20em' name="command.search2"
                 value="${memberForm.inCompleteCommand.search2}"/></td>
       <td width="10%"><input type="submit" name="filterSuperiorsWeChat" value="Filter WeChat"class="button" style="height:23px; background-color:blue;"/></td>
       <td width="10%"><input type="submit" name="filterSuperiorsUserName" value="Filter UserName"class="button" style="height:23px; background-color:blue;"/></td>
      </tr>
      <tr>
      <td><input type="submit" name="submitMemberUpline" value="Change Upline" class="button" style="height:23px;"
                onclick="return confirmChangeUpline();"/></td>
      </tr>
    </br>
    <tr>
    <td><input type="submit" name="memberCancel" value="Cancel" class="button" style="height:23px; background-color:red;"/></td>
    </tr>
    </tbody>
    </table>
    </form:form>
  </div>

</body>
</html>
