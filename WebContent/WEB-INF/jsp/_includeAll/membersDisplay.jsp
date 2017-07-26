<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<c:forEach items="${members}" var="sms" >
	<div class="cn">
		<div class="innerb">
			<button id="${sms.weChatName}button" class="buttonCollapse" type="button" data-toggle="collapse" data-target="#${sms.weChatName}">
				</button>
			</div>
			<div class="inner">
				${sms.weChatName}
			</div>
	</div>
		<div style="margin-left:20px" id="${sms.weChatName}" class="collapse">
			<c:if test="${fn:length(sms.members)>0}">
					<c:set var="sm" value="${sms}" scope="request"/>
					<jsp:include page="memberDisplay.jsp"/>
			</c:if>
			</div>
			<script>
			$('#${sms.weChatName}').on('hidden.bs.collapse', function () {
				$('#${sms.weChatName}button').removeClass("buttonExpand");
				$('#${sms.weChatName}button').addClass("buttonCollapse");
			})
			$('#${sms.weChatName}').on('shown.bs.collapse', function () {
				$('#${sms.weChatName}button').removeClass("buttonCollapse");
				$('#${sms.weChatName}button').addClass("buttonExpand");
			})
			</script>
</c:forEach>
