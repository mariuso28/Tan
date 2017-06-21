<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

		<c:set var="zmas" value="${comp.zmas}" scope="request"/>
		<jsp:include page="zmasDisplay.jsp"/>
		<c:set var="smas" value="${comp.smas}" scope="request"/>
		<jsp:include page="smasDisplay.jsp"/>
		<c:set var="mas" value="${comp.mas}" scope="request"/>
		<jsp:include page="masDisplay.jsp"/>
		<c:set var="agents" value="${comp.agents}" scope="request"/>
		<jsp:include page="agentsDisplay.jsp"/>
		<c:set var="players" value="${comp.players}" scope="request"/>
		<jsp:include page="playersDisplay.jsp"/>

    