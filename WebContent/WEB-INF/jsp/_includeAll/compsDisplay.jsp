<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


		<c:forEach items="${comps}" var="member" varStatus="status">
		<tr style="background-color:${member.role.color}">
			<c:set var="initEditable" value="${editable}" scope="request"/>
			<c:set var="initCompTab" value="${tab}" scope="request"/>

			<c:set var="subMember" value="${currExpandedMembers[member.code]}"/>
			<c:set var="expanded" value="+" />
			<c:choose>
			<c:when test="${subMember != null}">
				<c:set var="expanded" value="-" />
			</c:when>
			</c:choose>
			<c:set var="ecolor" value="red"/>
			<c:choose>
			<c:when test="${member.enabled==true}">
				<c:set var="ecolor" value="green"/>
			</c:when>
			</c:choose>

			<td><a href="processAgent?method=expand&code=${member.code}" style="text-decoration: none">
			<link rel="xyz" type="text/css" href="color.css">
				<font color="${ecolor}" size="3">
               				${tab}${expanded}${member.code}
              			 </font>
               			</link>
			</a></td>


			<td>${member.role.shortCode}</td>
			<td>${member.contact}</td>
			<td>${member.email}</td>

			<td align="right">
               				<fmt:formatNumber value="${member.account.playerRoyalty}"
							type="number" maxFractionDigits="2" minFractionDigits="2" />
			</td>
			<td align="right">
               				<fmt:formatNumber value="${member.account.bankerRoyalty}"
							type="number" maxFractionDigits="2" minFractionDigits="2" />
			</td>


			<c:choose>			<c:when test="${editable==true}">
			<td align="center"><a href="../acc/processAccount?method=update&code=${member.code}" >
			<link rel="xyz" type="text/css" href="color.css">
				<div class="lozengeButton" style="background-color:#7f4b8e; float:right; font-size:14px; width:60px; height:20px; line-height:20px; margin:0px;">
          Update
				</div>
      </link>
			</a></td>
			</c:when>
			</c:choose>

			<c:choose>
			<c:when test="${subMember != null}">
				<c:set var="editable" value="false" scope="request"/>
				<c:set var="tab" value="${tab}&nbsp&nbsp" scope="request"/>
				<c:set var="comp" value="${subMember}" scope="request"/>
				<jsp:include page="compDisplay.jsp"/>
			</c:when>
			</c:choose>
			<c:set var="editable" value="${initEditable}" scope="request"/>
			<c:set var="tab" value="${initCompTab}" scope="request"/>
		</tr>
		</c:forEach>