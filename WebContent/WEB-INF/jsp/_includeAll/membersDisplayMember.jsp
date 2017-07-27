<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


	<div class="cn">

		<c:if test="${fn:length(tabList)>0}">
			<c:forEach var="i" begin="0" end="${fn:length(tabList)}" step="1">
				<c:if test="${fn:substring(tabList, i, i + 1)=='P'}">
				<div class="innerb2">
							<button  class="buttonVerticalNop" type="button"></button>
				</div>
				</c:if>
				<c:if test="${fn:substring(tabList, i, i + 1)=='N'}">
				<div class="innerb">
	<!--					<img width="20" height="25"  src='../../img/monthForward_normal.gif' border='0'> -->
			  </div>
				</c:if>
				</c:forEach>
		 </c:if>

		<div class="innerb">
			<c:if test="${fn:length(sms.members)!=0}">
				<c:if test="${sms.hasPeer==false}">
					<button id="${sms.weChatName}button" class="buttonExpandCorner" type="button" data-toggle="collapse" data-target="#${sms.weChatName}">
					</button>
				</c:if>
				<c:if test="${sms.hasPeer==true}">
					<button id="${sms.weChatName}button" class="buttonExpand" type="button" data-toggle="collapse" data-target="#${sms.weChatName}">
					</button>
				</c:if>
			</c:if>
			<c:if test="${fn:length(sms.members)==0}">
				<c:if test="${sms.hasPeer==false}">
					<button  class="buttonCornerNop" type="button"></button>
				</c:if>
				<c:if test="${sms.hasPeer==true}">
					<button  class="buttonHorizontalNop" type="button"></button>
				</c:if>
			</c:if>
			</div>
			<div class="inner">
				<c:if test="${sms.parent!=null}">
						<a href="../adm/manageMemberFromTree?name=${sms.userName}">
							<img width="25" height="25"  src='../../img/${sms.rank}.png' border='0'>
								<font color="blue" size="1">${sms.userName} - ${sms.weChatName} (${sms.rankLongName} | ${sms.commission}%)</font>
						</a>
				</c:if>
				<c:if test="${sms.parent==null}">
						<img width="25" height="25"  src='../../img/${sms.rank}.png' border='0'>
								<font color="blue" size="1">${sms.userName} - ${sms.weChatName} (${sms.rankLongName} | ${sms.commission}%)</font>
				</c:if>
			</div>
	</div>
		<div style="margin-left:0px" id="${sms.weChatName}" class="collapse in">
			<c:if test="${sms.hasPeer==true}">
				<script>
				$('#${sms.weChatName}').on('hidden.bs.collapse', function (e) {
					e.stopPropagation();
					$('#${sms.weChatName}button').removeClass("buttonExpand");
					$('#${sms.weChatName}button').addClass("buttonCollapse");
				})
				$('#${sms.weChatName}').on('shown.bs.collapse', function (e) {
					$('#${sms.weChatName}button').removeClass("buttonCollapse");
					$('#${sms.weChatName}button').addClass("buttonExpand");
				})
				</script>
			</c:if>
			<c:if test="${sms.hasPeer==false}">
				<script>
				$('#${sms.weChatName}').on('hidden.bs.collapse', function (e) {
					e.stopPropagation();
					$('#${sms.weChatName}button').removeClass("buttonExpandCorner");
					$('#${sms.weChatName}button').addClass("buttonCollapseCorner");
				})
				$('#${sms.weChatName}').on('shown.bs.collapse', function (e) {
					$('#${sms.weChatName}button').removeClass("buttonCollapseCorner");
					$('#${sms.weChatName}button').addClass("buttonExpandCorner");
				})
				</script>
			</c:if>

			<c:if test="${fn:length(sms.members)>0}">
					<c:set var="sm" value="${sms}" scope="request"/>
					<c:if test="${sms.hasPeer==true}">
						<c:set var="tabList" value="${tabList}P" scope="request"/>
					</c:if>
					<c:if test="${sms.hasPeer==false}">
						<c:set var="tabList" value="${tabList}N" scope="request"/>
					</c:if>
					<jsp:include page="memberDisplay.jsp"/>
					<c:set var="tabList" value="${fn:substring(tabList, 0, fn:length(tabList)-1)}" scope="request"/>
					</c:if>



			</div>
