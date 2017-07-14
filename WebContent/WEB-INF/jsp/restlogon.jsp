<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>restlogon</title>
<!--<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/main_css.css"/>" />-->
  <link href="../../css/style.css" rel="stylesheet" type="text/css" />
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/dist/css/social-share-kit.css" />" />
  <link rel="stylesheet" href="<c:url value="/resources/font/web fonts/sansation_light_macroman/stylesheet.css"/>" />
  <link rel="stylesheet" href="<c:url value="/resources/font/web fonts/sansation_bold_macroman/stylesheet.css"/>" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"/>

<style>

body {
  background-color: black;
  background-image: url("../../img/all-in2.png");
  background-position:center;
  background-repeat: no-repeat;
}

.heading {
  width:300px;
  margin-right:auto;
  margin-left:auto;
}

.forgot {
  color: #aaa;
  font-size:14px;
  font-weight:700;
  text-shadow: 1px 1px 0px #333;
}
</style>

<script>

function submitForgotPassword()
    {
      email = $('#emailDefault').val();
      if (email=='')
          return false;
      if (!confirm('A reset password will be sent to your email account: ' + email))
        return false;
      window.location.replace("/redpacket/rp/logon/password?user&email="+email);
      return false;
    }

</script>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script>
$(document).ready(function(){
  $("#login").click(function()
  {
    var n = $('#emailDefault').val();
    var p = $('#password').val();
  $.ajax({url: "/Tan/rp/api/a/authorize", type: "POST", data: { username: n, password: p } ,
        success: function(result){
       alert(result.status);
   }});
 });
});
</script>

</head>
<body>
  <div class="main">
      <div id="header">
          <div class="bg"></div>
        </div>
    		<h2 style="padding-top:50px; text-align:center;"><font style="color:white; font-weight:900;">Log In</font></h2>
    		<div class="heading">
    				<table width="100%">
    					<tr>
    						<td><font style="color:#aaa; font-size:14px; font-weight:700;">Email Address:</font></td>
              	<td><input id="emailDefault" class="inTable form-control" type="text"
    							name="email" value="c1@rpco.com" style="height:24px;"/></td>
    					</tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
    					</tr>
    					<tr>
    						<td><font style="color:#aaa;  font-size:14px; font-weight:700;">Password:</font></td>
    						<td><input id="password" class="inTable form-control" type="password"
    							name="password" value='88888888' style="height:24px;"/> </td>
    					</tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
    					</tr>
              <tr>
                <td colspan="2">
                  <span style="color:#00569E;">${logon['infoMsg']}</span>
          			  <span style="color: #ed1c24;">${logon['errMsg']}
                    <c:if test="${(logon['errMsg']=='Authentication Error' || logon['errMsg']=='') && logon['email']!=''}">
                        <a href="" onclick="return submitForgotPassword();">
                          <div class="forgot">
                            Forgot Password?
                          </div>
                        </a>
                    </c:if>
                  </span>
                </td>
    					</tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
    					</tr>
    					<tr>
                <td>&nbsp;</td>
              	<td><button id="login">Login</button></td>
    					</tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
    					</tr>
              <tr>
                <td>&nbsp;</td>
    						<td>

                </td>
    					</tr>
    				</table>
    		</div>
  </div>  <!--  main -->
</body>
</html>
