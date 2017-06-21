<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page trimDirectiveWhitespaces="true" %>

<html>
<head>
    <title>adminHome</title>
<style>
th{
text-align:center;

}

body{
font: 20px Arial, sans-serif;
}
</style>
</head>


<body>

  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
  <script type="text/javascript">


  function isNumber(s , checkFloat, checkNegative)
  {
     var found = false;
     var i;
     var dCheck = false;
     for (i = 0; i < s.length; i++)
     {
         var c = s.charAt(i);
         if((c == "-") && (i == 0) && (s.length > 0)) {
           if(checkNegative == false) {
             found = true
           }
         }
         else {
           if( ((c == ".") && (checkFloat == true) && (dCheck == false)))
           {
           dCheck = true
           }
           else if (((c < "0") || (c > "9")))
           {
               found = true
           }
       }
     }
     if( s.length == 0)
     {
         found = true
     }

     return found==false;
   }

  function modifyGameDuration(gameId,index)
  {

    oFormObject = document.forms['myForm'];
    var field = 'command.durations[' + index + ']' ;
    oformElement = oFormObject.elements[field];
    isValid = isNumber(oformElement.value,false,false);
    if (!isValid)
    {
      alert("Invalid Duration");
      return false;
    }

    alert('Duration will be modified to : ' + oformElement.value + ' from next game played.');

    var field = 'command.gameId' ;
    oformElement = oFormObject.elements[field];
    oformElement.value = gameId;


    var field = 'command.durationIndex' ;
    oformElement = oFormObject.elements[field];
    oformElement.value = index;

    var myin = document.createElement('input');
    myin.type='hidden';
    myin.name='modifyDuration';
    myin.value='MaHa';
    oFormObject.appendChild(myin);
    oFormObject.submit();

    return false;
  }

  function showGameStatus(status)
  {
      alert("Game State : " + status);
      return false;
  }

  </script>


<form:form method="post" id="myForm" action="exec" modelAttribute="adminForm">

  <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />

<h2 style="background-color:${currUser.role.color}">Hi ${currUser.role.desc}&nbsp; ${currUser.contact}

&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.png' border='0'>
</h2>

  <input type="hidden" name="command.gameId"  value="" />
  <input type="hidden" name="command.durationIndex"  value="-1" />


<table border="0" cellpadding="3" cellspacing="0" width="600">
<tbody align="left" style="font-family:verdana; color:purple; background-color:LightCyan">
<tr><td width="30%">Logon Id (email):</td><td width="70%">${currUser.email}</td></tr>
<tr><td width="30%">Contact:</td>
  <td width="30%"><input name="command.profile.contact" type="text" value="${currUser.contact}" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr><td width="30%">Password:</td>
  <td width="30%"><input name="command.profile.password" type="password" value="" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr><td width="30%">Verify Password:</td>
  <td width="30%"><input name="command.verifyPassword" type="password" value="" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr>
  <td width="30%">Phone:</td><td width="30%">
  <input name="command.profile.phone" type="phone" value="${currUser.phone}" size="40" maxlength="64"
    style="width:380px; height:28px; font-family:inherit; font-size: 18px;" />
  </td>
</tr>
<tr>
  <td width="30%">
    <input type="submit" name="modifyAdmin" value="Modify Profile" class="button" style="height:23px;"/>
  </td>
</tr>
</tbody>
</table>
<br/>
<tr><td><font color="red">${adminForm.errMsg}</font></td></tr>
</br>
</br>
<tr>
  <td><a href="../agnt/viewCompanies">Maintain Companies</a></td>
</tr>

</br>
<tr>
<td><a href="../logon/signin">Logoff</a></td>
</tr>


</form:form>
</body>
</html>
