<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
  <head>
    <link href="../../css/style.css" rel="stylesheet" type="text/css" />
    <title>rollup</title>
    <style>
    </style>
  </head>

  <body>
    <div class="main">

      <form:form method="post" action="processPlayer" modelAttribute="rollupForm">
        <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
    		<div class="headerWelcome">
    			<h2 style="color:#aaa; font-weight:700;">
    				${currUser.role.desc}&nbsp; <font color="${currUser.role.color}">${currUser.contact}</font> - Account Summary
    			</h2>
    		</div>
        <div class="navHeading">
          <a href="processAccount?method=cancelRollup"><img src="../../img/back2.png" width="30" height="30"></a>
        </div>
        <div style="float:left; width:100%;">
          <c:choose>
            <c:when test="${rollupForm.role.rank < 5}">
            	<tr>
              	<td>
                  <strong>
                		<font color="green" size="3" weight="900">
                		Total Outstanding Invoices:
                		<fmt:formatNumber value="${rollupForm.outstandingInvoicesTotal}" type="number" maxFractionDigits="2" minFractionDigits="2"/>
                  </strong>
              	</td>
            	</tr>
              <tr height="400px" valign="top" align="left">
                <td>
                  <table border="0" style="width:100%;" align="left">
              	    <colgroup>
              		      <col span="1" style="width: 10%;">
                     		<col span="1" style="width: 10%;">
                        <col span="1" style="width: 10%;">
              		      <col span="1" style="width: 18%;">
              		      <col span="1" style="width: 18%;">
              		      <col span="1" style="width: 12%;">
              		      <col span="1" style="width: 10%;">
                        <col span="1" style="width: 12%;">
                  	</colgroup>
              	    <tr style="color:purple; background-color:#555;">
                  		<td align="center"><strong><font color="#FFF" style="bold" size="2">Invoice</font></strong></td>
                  		<td align="center"><strong><font color="#FFF" style="bold" size="2">Issue Date</font></strong></td>
                      <td align="center"><strong><font color="#FFF" style="bold" size="2">Type</font></strong></td>
                  	  <td align="center"><strong><font color="#FFF" style="bold" size="2">Collect</font></strong></td>
                  		<td align="center"><strong><font color="#FFF" style="bold" size="2">Pay</font></strong></td>
                  		<td align="right"><strong><font color="#FFF" style="bold" size="2">Amount</font></strong></td>
                      <td align="right"><strong><font color="#FFF" style="bold" size="2">Royalty</font></strong></td>
                      <td align="right"><strong><font color="#FFF" style="bold" size="2">Net Amount</font></strong></td>
                      <!--		<td align="right">
                      		<a href="processAccount.html?invoiceIssuerPDF" target="_blank">View PDF</td>
                      		<c:choose>
                      		<c:when test="${(rollupForm.parentEmail!=null) && (fn:length(rollupForm.parentEmail)>0)}">
                      		<td align="right">
                      			<a href="processAccount.html?invoiceEmailIssuerPDF">Email PDF
                      		</td>
                      		</c:when>
                      		</c:choose>
                      -->
                		</tr>
              	    <c:forEach items="${rollupForm.outstandingInvoices}" var="invoice" varStatus="status">
                	    <tr style="color:purple; background-color:#ccc;">
                    		<c:set var="cnt" value="${status.index}" />
                    		<td align="center">#${invoice.id}</td>
                        <td align="center">${invoice.invoiceType}</td>
                    		<td align="center"><fmt:formatDate value="${invoice.timestamp}" pattern="dd-MMM-yy"/></td>
                        <td align="center">${invoice.payer}</td>
                        <td align="center">${invoice.payee}</td>
                    		<td align="right"><fmt:formatNumber value="${invoice.amount}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
                        <td align="right"><fmt:formatNumber value="${invoice.royalty}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
                        <td align="right"><fmt:formatNumber value="${invoice.netAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
                	    </tr>
              	    </c:forEach>
                    <tr><td><font color="red" size="3">${rollupForm.message}</td></tr>
                    <tr><td><font color="blue" size="3">${rollupForm.info}</td></tr>
            	     </table>
                 </td>
               </tr>
            </c:when>
          </c:choose>
        </div>

        <div class="sectionHeading">
          Account Summary
        </div>
        <div class="sectionBody">
          <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
            <colgroup>
                <col span="1" style="width: 12%;">
                <col span="1" style="width: 10%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 6%;">
            </colgroup>
          	<tr style="color:purple; background-color:#555;">
          		<td><strong><font color="#FFF" style="bold" size="2">Member</font></strong></td>
          		<td><strong><font color="#FFF" style="bold" size="2">Type</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Paid In</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Pay Out</font></strong></td>
          	   <td align="right"><strong><font color="#FFF" style="bold" size="2">Owed</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Owing</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Net Owed</font></strong></td>
              <td>&nbsp;</td>
          	</tr>
          	<tr>
          		<td>${rollupForm.rollup.email}</td>
          		<td>${rollupForm.rollup.role}</td>
          		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.paidIn}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.paidOut}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          	  <td align="right"><fmt:formatNumber value="${rollupForm.rollup.owed}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.owing}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<td align="right"><fmt:formatNumber value="${rollupForm.rollup.totalOwed}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<td align="right">
                <div class="lozengeButton" style="float:right; font-size:14px; width:60px; height:20px; line-height:20px; margin:0px;">
                  <a href="processAccount?method=accDetails&code=${rollupForm.rollup.code}">Details</a>
                </div>
              </td>
          	</tr>
          </table>
        </div>
        </br>
        <div class="sectionHeading">
          Member Account Summary
        </div>
        <div>
          <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
            <colgroup>
                <col span="1" style="width: 12%;">
                <col span="1" style="width: 10%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 9%;">
                <col span="1" style="width: 6%;">
            </colgroup>
          	<tr style="color:purple; background-color:#555;">
          		<td><strong><font color="#FFF" style="bold" size="2">Member</font></strong></td>
          		<td><strong><font color="#FFF" style="bold" size="2">Type</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Paid In</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Pay Out</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Owed</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Owing</font></strong></td>
          		<td align="right"><strong><font color="#FFF" style="bold" size="2">Net Owed</font></strong></td>
              <td>&nbsp;</td>
          	</tr>
          	<c:forEach items="${rollupForm.memberRollups}" var="rollup" varStatus="status">
          	<tr>
          		<c:choose>
            		<c:when test="${rollup.role == 'Player'}">
            			<td>${rollup.email}</td>
            		</c:when>
            		<c:otherwise>
            			<td><a href="processAccount?method=subRollup&code=${rollup.code}">${rollup.email}</a></td>
            		</c:otherwise>
          		</c:choose>
          		<td>${rollup.role}</td>
          		<td align="right"><fmt:formatNumber value="${rollup.paidIn}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<td align="right"><fmt:formatNumber value="${rollup.paidOut}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<td align="right"><fmt:formatNumber value="${rollup.owed}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<td align="right"><fmt:formatNumber value="${rollup.owing}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<td align="right"><fmt:formatNumber value="${rollup.totalOwed}" type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
          		<c:choose>
          		<c:when test="${rollup.role != 'Player'}">
          			<td align="right">
                  <a href="processAccount?method=subRollup&code=${rollup.code}">
                    <div class="lozengeButton" style="float:right; font-size:14px; width:60px; height:20px; line-height:20px; margin:0px;">
          			         Details
                     </div>
                   </a>
                 </td>
          		</c:when>
          		</c:choose>
          	</tr>
          	</c:forEach>
          </table>
        </div>
      </form:form>
    </div>  <!--  main -->
  </body>
</html>
