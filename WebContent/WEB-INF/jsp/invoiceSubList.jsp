<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>invoiceSubList</title>
    <link href="../../css/style.css" rel="stylesheet" type="text/css" />
<style>
</style>
</head>

<body>
  <div class="main">
  <div class="headerPic">
    <img width="50" height="50"  src='../../img/${currAccountUser.role.shortCode}.png' border='0'>
  </div>
  <div class="headerWelcome">
    <h2 style="color:#aaa; font-weight:700;">
      Downstream Member Invoices for : #${invoiceListForm.invoice.id}
    </h2>
  </div>
  <div class="navHeading">
    <a href="processAccount?cancelInvoiceDetails"><img src="../../img/back2.png" width="30" height="30"></a>
  </div>

ls
<div style="width:100%; float:left;">
<form:form method="post" action="processPlayer" modelAttribute="invoiceListForm">
<div style="width:100%; background-color:#000; color:#fff; font-size:16px; float:left;">
  Member Invoices for ${invoiceListForm.role.desc} ${invoiceListForm.code} - ${currAccountUser.contact} Invoice: #${invoiceListForm.invoice.id}
		  Amount: <fmt:formatNumber value="${invoiceListForm.invoice.netAmount}" type="number" maxFractionDigits="2" minFractionDigits="2"/>
		Issue Date: <fmt:formatDate value="${invoiceListForm.invoice.timestamp}" pattern="dd-MMM-yy"/>
      <div class="lozengeButton" style="float:right; width:70px; height:18px; line-height:18px; background-color:blue; font-size:12px; margin-right:0px;">
            <a href="processAccount?emailXls&invoiceId=${invoiceListForm.invoice.id}"" style="text-decoration:none;"
                onclick="return confirm('An Excel Spreadsheet Workbook for this Invoice Breakdown will be forwarded to your email account: ${currBaseUser.email} ')">
                Email XLS</a>
      </div>
      <br/>
      <tr><td><font color="red" size="3">${invoiceListForm.errMsg}</font></td></tr>
      <tr><td><font color="blue" size="3">${invoiceListForm.infoMsg}</font></td></tr>
		</div>



<div style="width:100%; height:400px; overflow:auto; float:left;">
<table border="0" style="width:100%;" align="left">
	  <colgroup>
    		<col span="1" style="width: 8%;">
        <col span="1" style="width: 12%;">
        <col span="1" style="width: 12%;">
    		<col span="1" style="width: 10%;">
        <col span="1" style="width: 12%;">
        <col span="1" style="width: 12%;">
    		<col span="1" style="width: 12%;">	  </colgroup>
	  <tr style="color:purple; background-color:#aaa;">
		<td>Invoice</td>
		<td>Payer</td>
    <td>Pay To</td>
		<td>Issue Date</td>
		<td>Amount</td>
		<td>Royalty</td>
		<td>Net Amount</td>
	  </tr>
	  <c:forEach items="${invoiceListForm.displayList}" var="invoice" varStatus="status">
    <tr bgcolor="#ccc">
      		<td><a href="processAccount?invoiceDetails&invoiceId=${invoice.id}">#${invoice.id}</td>
      		<td>${invoice.payer}</td>          <td>${invoice.payee}</td>      		<td><fmt:formatDate value="${invoice.timestamp}" pattern="dd-MMM-yy HH:mm"/></td>
      		<td><fmt:formatNumber value="${invoice.amount}" type="number" maxFractionDigits="2" minFractionDigits="2"/></td>
      		<td><fmt:formatNumber value="${invoice.royalty}" type="number" maxFractionDigits="2" minFractionDigits="2"/></td>
      		<td><fmt:formatNumber value="${invoice.netAmount}" type="number" maxFractionDigits="2" minFractionDigits="2"/></td>
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
