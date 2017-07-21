<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<c:forEach items="${members}" var="sms" >
		<tr><td>${tab}</td><td>${sms.weChatName}</td></tr>
		<c:if test="${fn:length(sms.members)>0}">
				<c:set var="sm" value="${sms}" scope="request"/>
				<c:set var="tab" value="${tab+1}" scope="request"/>
					<jsp:include page="memberDisplay.jsp"/>
				<c:set var="tab" value="${tab-1}" scope="request"/>
		</c:if>
</c:forEach>
