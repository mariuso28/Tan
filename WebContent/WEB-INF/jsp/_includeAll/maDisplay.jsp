<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		
	
		<c:set var="agents" value="${ma.agents}" scope="request"/>
		<jsp:include page="agentsDisplay.jsp"/>
		<c:set var="players" value="${ma.players}" scope="request"/>
		<jsp:include page="playersDisplay.jsp"/>

	    