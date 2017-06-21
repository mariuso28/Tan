<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		
		
		<c:set var="mas" value="${sma.mas}" scope="request"/>
		<jsp:include page="masDisplay.jsp"/>
		<c:set var="agents" value="${sma.agents}" scope="request"/>
		<jsp:include page="agentsDisplay.jsp"/>
		<c:set var="players" value="${sma.players}" scope="request"/>
		<jsp:include page="playersDisplay.jsp"/>