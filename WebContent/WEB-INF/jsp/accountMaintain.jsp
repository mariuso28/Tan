<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <title>accountMaintain</title>
  <style>
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
      {
          return true;
      }

  	 }

       	function test() {
  		if (!testNumber('command.dwAmount','Deposit/Withdrawl Amount',true))
  		    validated=false;
  		else
  		   validated=true;
         return validated;
         }

  	    function validate(theForm)
  	    {
  		// alert('VALIDATED ' + validated);
  		return validated;
  	    }

        function setDefaultDepWith()
        {
          oFormObject = document.forms['myForm'];
          var field = 'command.dwAmount';
          oformElement = oFormObject.elements[field];

          isValid = isNumber(oformElement.value,true,false);
          if (!isValid)
            oformElement.value = 0.0;
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

        function checkPayFlag(checkBox,cnt)
        {
        //  alert('In');
            oFormObject = document.forms['myForm'];
            var field = 'command.payFlags[' + cnt + ']';
            oformElement = oFormObject.elements[field];
            if (checkBox.checked)
              oformElement.value = 'on';
            else
              oformElement.value = 'off';
        //    alert('Value : ' +  oformElement.value);
        }

  </script>

</head>


<body>
  <div class="main">
  <form:form method="post" id="myForm" action="processAccount"
  		modelAttribute="accountDetailsForm">
  <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
  <div class="headerPic">
    <img width="50" height="50"  src='../../img/${currUser.role.shortCode}.png' border='0'>
  </div>
  <div class="headerWelcome">
    <h2 style="color:#aaa; font-weight:700;">
      ${currUser.role.desc}&nbsp; <font color="${currUser.role.color}">${currUser.contact}</font> - Update ${currAccountUser.role.desc} - ${currAccountUser.contact}
    </h2>
  </div>
  <div class="navHeading">
    <a href="../agnt/backtoMemberHome"><img src="../../img/back2.png" width="30" height="30"></a>
    <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
  </div>

  <table cellspacing="0" cellpadding="0" border="0"
   bgcolor="white" id="shell" height="200" width="100%">
     <tr height="50" align="left">
       <td valign="top" style="color:purple; background-color:${currAccountUser.role.color}">
        <table border="0" cellpadding="3" cellspacing="0"  width="50%">
          <colgroup>
            <col span="1" style="width: 50%;">
            <col span="1" style="width: 50%;">
      	  </colgroup>
  	<tr>
  		<c:set var="label" value="Modify Account for ${currAccountUser.role.shortCode} "/>
  		<td><input type="submit" name="modifyAccount"
  			value="${label}" class="button" style="height:30px; width:220px; font-size:14px; line-height:26px;"/></td>
  		<c:choose>
    		<c:when test="${currAccountUser.enabled==true}">
    		<td><input type="submit" name="disableMember"
    			value="Disable" class="button" style="height:30px; font-size:14px; background-color:red;"
    			onclick="return confirm('!! THIS WILL DISABLE ALL SUBMEMBERS (AGENTS AND PLAYERS) - YOU BETTER BE DAMN SURE!! WELL??')"/></td>
    		</c:when>
    		<c:otherwise>
      		<c:choose>
      			<c:when test="${currUser.enabled==false}">
      				<td>Disabled Upstream</td>
      			</c:when>
      			<c:otherwise>
              <c:if test="${currAccountUser.enabled!=true}">
        				<td><input type="submit" name="enableMember"
        					value="Enable" class="button" style="height:30px;"/>
        				</td>
              </c:if>
      			</c:otherwise>
      		</c:choose>
    		</c:otherwise>
  		</c:choose>
  	<tr>
  	<tr><td>&nbsp</td></tr>

  	</table>
       	</td>
  	<td valign="top" style="color:purple; background-color:${currAccountUser.role.color}">
        	<table border="0" cellpadding="3" cellspacing="0">
  		        <colgroup>
                <col span="1" style="width: 50%;">
                <col span="1" style="width: 50%;">
      		    </colgroup>

          		<tr><td>Email (Logon Id):</td><td>${currAccountUser.email}</td></tr>
          		<tr><td>Contact:</td><td>${currAccountUser.contact}</td></tr>
          	  </br>
             </br>
              <tr>
          	  <td>Royalty For Player</td>
              <td><fmt:formatNumber value="${currAccountUser.account.playerRoyalty}"
                 type="number" maxFractionDigits="2" minFractionDigits="2"/>
              </td>
              </tr>

              <tr>
              <td>Royalty For Banker:</td>
              <td><fmt:formatNumber value="${currAccountUser.account.bankerRoyalty}"
                 type="number" maxFractionDigits="2" minFractionDigits="2"/>
              </td>
              </tr>

          </table>

  	</td>
  	</tr>
  	</table>
  	<tr><td><font color="red" size="3">${accountDetailsForm.message}</font></td></tr>
  	<tr><td><font color="blue" size="3">${accountDetailsForm.info}</font></td></tr>
  	<br/>
  	<tr>
  	<td>
  		<font color="#ABEBC6" size="4">
  		Outstanding Invoices for: ${currAccountUser.role.desc} -  ${currAccountUser.contact}
  	</td>
  	<c:choose>
  	<c:when test="${accountDetailsForm.outstandingInvoicesTotal<0.0}">
  	<td>
  		<strong>Total Owed: <fmt:formatNumber value="${accountDetailsForm.outstandingInvoicesTotal*-1.0}"  type="number" maxFractionDigits="2" minFractionDigits="2" />
    </strong></td>
  	</c:when>
  	<c:otherwise>
  	<td>
  		Total Owing: <fmt:formatNumber value="${accountDetailsForm.outstandingInvoicesTotal}"  type="number" maxFractionDigits="2" minFractionDigits="2" />
  	</td>
  	</c:otherwise>
  	</c:choose>

  	</tr>
   	<tr height="400px" valign="top" align="left" >
      <td>
      <div style="height:400px; overflow:auto">
      <table border="0" style="width:100%;" align="left" cellspacing="0">
  	  <colgroup>
    		<col span="1" style="width: 8%;">
        <col span="1" style="width: 10%;">
        <col span="1" style="width: 6%;">
        <col span="1" style="width: 10%;">
    		<col span="1" style="width: 8%;">
        <col span="1" style="width: 10%;">
        <col span="1" style="width: 20%;">
      	<col span="1" style="width: 10%;">
    		<col span="1" style="width: 10%;">
    		<col span="1" style="width: 10%;">
  		</colgroup>
  	    <tr style="color:FFF; background-color:B5B5B5">
  		<td>Invoice</td>
  	  <td>Issue Date</td>
      <td align="center">Type</td>
  		<td align="right">Amount</td>
      <td align="right">Royalty</td>
      <td align="right">Pay</td>
  		<c:choose>
  		<c:when test="${fn:length(accountDetailsForm.outstandingInvoices)>0}">
  		<td align="center">
        <input type="submit" name="invoicePay" value="Pay To" onclick="return setDefaultDepWith();" style="width:100px; height:24px;"/>
      </td>
      <td><input type="submit" name="checkAll" value="ALL" style="width:50px; height:24px;" /></td>
  		<td><a href="processAccount?invoicePDF" target="_blank">
  			<link rel="xyz" type="text/css" href="color.css">
                <div class="lozengeButton" style="width:70px; height:24px; background-color:orange; font-size:12px;">
  							View PDF
              </div>
                 			</link>
  						</a>
  			</td>
  			<td><a href="processAccount?invoiceEmailPDF">
  				<link rel="xyz" type="text/css" href="color.css">
            <div class="lozengeButton" style="width:70px; height:24px; background-color:blue; font-size:12px; margin-right:0px;">
  							Email PDF
              </div>
                 			</link>
  						</a>
  			</td>
  		</tr>
  		</c:when>
  		</c:choose>
  	  <c:forEach items="${accountDetailsForm.outstandingInvoices}" var="invoice" varStatus="status">
      		<c:choose>
      		<c:when test="${invoice.payee == currAccountUser.email}">
      			<c:set var="color" value="${currAccountUser.role.color}"/>
      		</c:when>
      		<c:otherwise>
      			<c:set var="color" value="${currUser.role.color}"/>
      		</c:otherwise>
      		</c:choose>
  	      <tr style="background-color:${color}"/>
    		<td><a href="processAccount?invoiceDetails&invoiceId=${invoice.id}">#${invoice.id}</td>
    		<td><fmt:formatDate value="${invoice.timestamp}" pattern="dd-MMM-yy HH:mm"/></td>
    		<c:set var="linkColor" value="green" scope="page" />
        <td align="center">${invoice.invoiceType}</td>
    		<c:choose>
    		<c:when test="${accountDetailsForm.overDueFlags[status.index]=='true'}">
    			<c:set var="linkColor" value="red" scope="page"/>
    		</c:when>
    		</c:choose>
  		  <td align="right"><fmt:formatNumber value="${invoice.amount}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
        <td align="right"><fmt:formatNumber value="${invoice.royalty}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
        <td align="right"><fmt:formatNumber value="${invoice.netAmount}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
        <td align="center">${invoice.payee}</td>
  		  <td align="center">
  		      <input type="hidden" name="command.invoiceIds[${status.index}]"
  			                           value="${invoice.id}" />
  		      <input type="hidden" name="command.payFlags[${status.index}]"
  			                           value="${accountDetailsForm.payFlags[status.index]}" />
             <c:choose>
          		<c:when test="${accountDetailsForm.payFlags[status.index]=='on'}">
               <input type="checkbox" size="4" onclick="checkPayFlag(this,${status.index})";
     			         checked />
          		</c:when>
             <c:otherwise>
               <input type="checkbox" size="4" onclick="checkPayFlag(this,${status.index})";
                   />
             </c:otherwise>
          		</c:choose>
  	    </td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
  	    </tr>
  	    </c:forEach>
  	</table>
  	</div>
  </table>
  </form:form>

</div>  <!--  main -->
</body>
</html>
