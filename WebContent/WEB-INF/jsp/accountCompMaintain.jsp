<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <title>accountCompMaintain</title>
  <style>
    body {
    	font-size: 14px;
    }
  </style>

<script type="text/javascript">

var validated = true;

function isNumber(s , checkFloat, checkNegative) {
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

     function testNumber(testField,fieldName,doubleAllowed) {

		oFormObject = document.forms['myForm'];
   		var field = testField;
   		oformElement = oFormObject.elements[field];
		var value = oformElement.value;

    if (value==null || value=="")
    {
        alert(fieldName + ' has incorrect format');
        if (doubleAllowed)
          oformElement.value = '0.00';
        else
          oformElement.value = '0';
        return false;
    }
		var isValid = isNumber(value,doubleAllowed,false);
                if (!isValid)
		{
                    alert(fieldName + ' has incorrect format');
		    if (doubleAllowed)
			oformElement.value = '0.00';
		    else
			oformElement.value = '0';
		    return false;
                }
		else
		   return true;
	 }

   // Retrieve last key pressed.  Works in IE and Netscape.
         // Returns the numeric key code for the key pressed.
         function getKey(e)
         {
           if (window.event)
              return window.event.keyCode;
           else if (e)
              return e.which;
           else
              return null;
         }
         function restrictChars(e, obj)
         {
           var CHAR_AFTER_DP = 2;  // number of decimal places
           var validList = "0123456789.";  // allowed characters in field
           var key, keyChar;
           key = getKey(e);
           if (key == null) return true;
           // control keys
           // null, backspace, tab, carriage return, escape
           if ( key==0 || key==8 || key==9 || key==13 || key==27 )
              return true;
           // get character
           if (key==190)
             keyChar = ".";
           else
             keyChar = String.fromCharCode(key);
           // check valid characters
           if (validList.indexOf(keyChar) != -1)
           {
             // check for existing decimal point
             var dp = 0;
             if( (dp = obj.value.indexOf( ".")) > -1)
             {
               if( keyChar == ".")
                 return false;  // only one allowed
               else
               {
                 // room for more after decimal point?
                 if( obj.value.length - dp <= CHAR_AFTER_DP)
                   return true;
               }
             }
             else return true;
           }
           // not a valid character
           return false;
         }

     	function test() {
		if (!testNumber('command.dwAmount','Deposit/Withdrawl Amount',true))
		    validated=false;
		else
		   validated=true;
       }

	    function validate(theForm)
	    {
		// alert('VALIDATED ' + validated);
		return validated;
	    }


</script>

</head>


<body>
  <div class="main">
    <form:form method="post" id="myForm" action="processAccount"
    		modelAttribute="accountDetailsForm" onsubmit="return validate(this)">

      <div class="headerPic">
  			<img width="50" height="50"  src='../../img/${currUser.role.shortCode}.png' border='0'>
  		</div>
  		<div class="headerWelcome">
  			<h2 style="color:#aaa; font-weight:700;">
  				Hi, ${currUser.role.desc}&nbsp; <font color="${currUser.role.color}">${currUser.contact}</font> - Update Account
  			</h2>
  		</div>

      <div class="navHeading">
        <a href="../agnt/backtoMemberHome"><img src="../../img/back2.png" width="30" height="30"></a>
        <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
      </div>
      <table cellspacing="0" cellpadding="0" border="0" bgcolor="white" id="shell" height="200" width="100%">
        <tr height="50" align="left">
        	<td valign="top" style="color:purple; background-color:${currUser.role.color}">

           	<table border="0"  cellpadding="3" cellspacing="0" style="width:200px;" align="left">
              <colgroup>
                <col span="1" style="width: 30%;">
                <col span="1" style="width: 30%;">
          		  <col span="1" style="width: 20%;">
              </colgroup>
              <tr>
                <c:set var="linkColor" value="green" scope="page" />
                  <c:choose>
                    <c:when test="${member.account.balance < '0'}">
                      <c:set var="linkColor" value="red" scope="page"/>
                    </c:when>
                  </c:choose>
                  <td colspan="3">
                    <strong>
                      <font color="#555" size="4">
                        Current Balance:
                      </font>
                      <font color=${linkColor} size="4">
                        <fmt:formatNumber value="${currAccountUser.account.balance}" type="number" maxFractionDigits="2" minFractionDigits="2" />
                      </font>
                    </strong>
                  </td>
              </tr>
          	  <tr>
            		<td colspan="3">
                  <font color="#555" size="4">
                    Make:
                  </font>
                </td>
              </tr>
              <tr>
                <td colspan="3">
              		<form:select path="command.dwType">
                     <c:choose>
                     <c:when test="${currAccountUser.account.balance <= 0.0}">
              	        <option value="Deposit">Deposit</option>
                     </c:when>
               			 </c:choose>
              			 <c:choose>
              			 <c:when test="${currAccountUser.account.balance > 0.0}">
              				     <option value="Withdrawl">Withdrawal</option>
              			 </c:when>
              			 </c:choose>
              		</form:select>
            		</td>
              </tr>
              <tr>

            		<td colspan="3">
                  <input type="text" name="command.dwAmount" id="dw" size="11" maxlength="12"
                    style="font-size:16px; font-weight:700;"
                     />
<!--
onkeydown="return restrictChars(event, this);"

                  <input type="text" name="command.dwAmount" size="11" style="font-family:
                    'Roboto Condensed'; font-size: 16px; font-weight: 700;"
                    value="<fmt:formatNumber value="${0.00}" type="number" maxFractionDigits="2" minFractionDigits="2"/>"
-->
            	  </td>
          	  </tr>
          	  <tr>
          		  <td colspan="3"><input type="submit" name="saveWithDepComp" value="Update" onclick="test()" style="height:30px; background-color:#7f4b8e;"/></td>
          	  </tr>
              <tr><td><font color="red" size="4">${accountDetailsForm.message}</td></tr>
          	  <tr><td><font color="blue" size="4">${accountDetailsForm.info}</td></tr>
            </tr>
        	</table>
        </td>
        <td valign="top" style="color:purple; background-color:${currUser.role.color}">
          <table border="0" cellpadding="3" cellspacing="0"  width="300">
      	    <colgroup>
             	<col span="1" style="width: 50%;">
             	<col span="1" style="width: 50%;">
          	</colgroup>
            <tr>
      	       <td>
                 <font color="#555" size="4">
                   Royalty For Player:
                 </font>
               </td>
      	        <td>
                  <font color=${linkColor} size="4">
                  <fmt:formatNumber value="${accountDetailsForm.account.playerRoyalty}"
      		           type="number" maxFractionDigits="2" minFractionDigits="2"/>%
                  </font>
      	        </td>
      	    </tr>
            </br>
            <tr>
      	       <td>
                 <font color="#555" size="4">
                   Royalty For Banker:
                 </font>
               </td>
               <td>
                 <font color=${linkColor} size="4">
                   <fmt:formatNumber value="${accountDetailsForm.account.bankerRoyalty}"
      		           type="number" maxFractionDigits="2" minFractionDigits="2"/>%
                </font>
              </td>
      	    </tr>
            </br>
      	    <tr>
      		      <c:set var="label" value="Modify Account for ${currAccountUser.role.shortCode} "/>
      		      <td><input type="submit" name="modifyAccount"
      			           value="${label}" class="button" style="height:30px; width:220px; font-size:14px;"/></td>

            </tr>
      	  </table>
        </td>
      </table>
    </form:form>
  </div>
</body>
</html>
