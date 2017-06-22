<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>transactionList</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css" />
    <style>
    </style>
</head>

<body>
  <div class="main">
  <div class="headerPic">
    <img width="50" height="50"  src='../../img/${currUser.role.shortCode}.png' border='0'>
  </div>
  <div class="headerWelcome">
    <h2 style="color:#aaa; font-weight:700;">
      Hi, ${currUser.role.desc}&nbsp; <font color="${currUser.role.color}">${currUser.contact}</font>!
    </h2>
  </div>
  <div class="navHeading">
    <a href="processAccount?cancelInvoiceDetails"><img src="../../img/back2.png" width="30" height="30"></a>
  </div>

<div style="width:100%; float:left;">
<form:form method="post" action="processPlayer" modelAttribute="transactionListForm">
<div style="width:100%; background-color:#000; color:#fff; font-size:16px;">
  Member bets for ${currAccountUser.role.desc} ${currAccountUser.code} ${currAccountUser.contact}  ${transactionListForm.role.desc} ${transactionListForm.code} - ${transactionListForm.email} trans: #${transactionListForm.invoice.id}
		  Amount: <fmt:formatNumber value="${transactionListForm.invoice.netAmount}"  type="number" maxFractionDigits="2" minFractionDigits="2"/>
		  Date: <fmt:formatDate value="${transactionListForm.invoice.timestamp}"
			pattern="dd-MMM-yyyy"/>

		</div>
<div style="height:400px; overflow:auto">
<table border="0" style="width:100%;" align="left">
	    <colgroup>
		      <col span="1" style="width: 5%;">
       		<col span="1" style="width: 20%;">
       		<col span="1" style="width: 20%;">
		      <col span="1" style="width: 20%;">
       		<col span="1" style="width: 5%;">
      		<col span="1" style="width: 10%;">		      <col span="1" style="width: 30%;">
	   </colgroup>
	  <tr style="color:purple; background-color:#aaa">
    <td>#</td>
    <td>Made</td>
    <td>Payer</td>
		<td>Payee</td>
		<td>Type</td>
    <td>Amount</td>
    <td>Source</td>
	  </tr>
	  <c:forEach items="${transactionListForm.displayList}" var="tx" varStatus="status">
    <tr style="color:#333; background-color:#ccc">
          <td>${tx.id}</td>
          <td><fmt:formatDate value="${tx.timestamp}" pattern="dd-MMM-yy HH:mm:ss"/></td>
          <td>${tx.payer}</td>
          <td>${tx.payee}</td>
          <td>${tx.type}</td>
      	  <td><fmt:formatNumber value="${tx.amount}" type="number" maxFractionDigits="2" minFractionDigits="2"/></td>
          <td>${tx.source}</td>
	  </tr>
	  </c:forEach>
</table>
</div>
</br>
</br>

</form:form>
</div>
</div>
</body>
</html>
