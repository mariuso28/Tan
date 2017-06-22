<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <title>accDetails</title>
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <style>
  </style>
</head>

<body>
  <div class="main">

    <form:form method="post" action="processPlayer" modelAttribute="transactionForm">
      <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
      <div class="headerPic">
  			<img width="50" height="50"  src='../../img/${transactionForm.role.shortCode}.png' border='0'>
  		</div>
  		<div class="headerWelcome">
  			<h2 style="font-weight:700;">
          ${transactionForm.role.desc} <font color="${transactionForm.role.color}">${transactionForm.contact}</font> - Your Account Details
  			</h2>
  		</div>
      <div class="navHeading">
        <a href="processAccount?method=backToLastRollup"><img src="../../img/back2.png" width="30" height="30"></a>
      </div>


    <div style="height:300px;">
      <table align="left" style="margin:0; padding:10px; width:100%; background: #f8f8f8; color:#000;">
        <colgroup>
            <col span="1" style="width: 10%;">
            <col span="1" style="width: 10%;">
            <col span="1" style="width: 10%;">
            <col span="1" style="width: 14%;">
            <col span="1" style="width: 6%;">
            <col span="1" style="width: 10%;">
            <col span="1" style="width: 10%;">
            <col span="1" style="width: 10%;">
            <col span="1" style="width: 10%;">
        </colgroup>
      	<tr style="color:purple; background-color:#555;">
      		<td align="center"><strong><font color="#FFF" style="bold" size="2">Id</font></strong></td>
      		<td align="center"><strong><font color="#FFF" style="bold" size="2">Date</font></strong></td>
      		<td align="center"><strong><font color="#FFF" style="bold" size="2">Type</font></strong></td>
      		<td align="center"><strong><font color="#FFF" style="bold" size="2">C/P</font></strong></td>
          <td align="center"><strong><font color="#FFF" style="bold" size="2">Action</font></strong></td>
      		<td align="right"><strong><font color="#FFF" style="bold" size="2">Amount</font></strong></td>
          <td align="right"><strong><font color="#FFF" style="bold" size="2">Royalty</font></strong></td>
          <td align="right"><strong><font color="#FFF" style="bold" size="2">Net Amount</font></strong></td>
      		<td align="center"><strong><font color="#FFF" style="bold" size="2">Pay Date</font></strong></td>
      		</tr>
      	<c:forEach items="${transactionForm.txList}" var="tx" varStatus="status">
      	<tr>
      		<td align="center">#${tx.id}</td>
      		<td align="center"><fmt:formatDate value="${tx.timestamp}" pattern="dd-MMM-yy" /></td>
            <td align="center">${tx.invoiceType}</td>
            <c:if test="${currUser.email == tx.payer}">
                <td align="center">${tx.payee}</td>
                <td align="center">Pay</td>
            </c:if>
            <c:if test="${currUser.email == tx.payee}">
              <td align="center">${tx.payer}</td>
              <td align="center">Collect</td>
            </c:if>
              <td align="right"><fmt:formatNumber value="${tx.amount}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
            <td align="right"><fmt:formatNumber value="${tx.royalty}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
            <td align="right"><fmt:formatNumber value="${tx.netAmount}"  type="number" maxFractionDigits="2" minFractionDigits="2" /></td>
            <td align="center"><fmt:formatDate value="${tx.paymentDate}" pattern="dd-MMM-yy" /></td>

      	</tr>
      	</c:forEach>

      </table>
    </div>

    </br>
    <tr>
    <c:choose>
    <c:when test="${transactionForm.currentPage>1}">
    <td><a href="processAccount?method=accDetailsLast"><img src="../../img/monthBackward_normal.gif"/></a></td>
    </c:when>
    </c:choose>
    <c:choose>
    <c:when test="${transactionForm.currentPage<transactionForm.lastPage}">
    <td><a href="processAccount?method=accDetailsNext"><img src="../../img/monthForward_normal.gif"/></a></td>
    </c:when>
    </c:choose>
    </tr>
    </br>

    </form:form>
  </div>
</body>
</html>
