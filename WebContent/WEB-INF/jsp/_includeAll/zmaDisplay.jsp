<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

		<c:set var="editable" value="false" scope="request"/>
		
		<c:set var="smas" value="${zma.smas}" scope="request"/>
		<jsp:include page="smasDisplay.jsp"/>
		<c:set var="mas" value="${zma.mas}" scope="request"/>
		<jsp:include page="masDisplay.jsp"/>
		<c:set var="agents" value="${zma.agents}" scope="request"/>
		<jsp:include page="agentsDisplay.jsp"/>
		<c:set var="players" value="${zma.players}" scope="request"/>
		<jsp:include page="playersDisplay.jsp"/>

    