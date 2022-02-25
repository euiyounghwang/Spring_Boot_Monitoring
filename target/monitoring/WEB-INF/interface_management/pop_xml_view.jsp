<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:eval expression="@environment.getProperty('feed_db_xml.server.path')" var="feedServerPath" />
<c:set var="ctx">${pageContext.request.contextPath}</c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>검색 모니터링</title>
<script src="${ctx}/js/common/webfontloader.js"></script>
<link rel="stylesheet" href="${ctx}/css/datatables.min.css">
<link rel="stylesheet" href="${ctx}/css/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/css/common.css">

<script src="${ctx}/js/common/jquery-1.12.4.min.js"></script>
<script src="${ctx}/js/common/jquery-ui.js"></script>
<script src="${ctx}/js/common/jquery.serializeObject.min.js"></script>

<script src="${ctx}/js/common.js"></script>

</head>
<body>
<div class="btns_bar fixed">
	<div class="right">
	<span class="selected_info"></span>
	</div>
</div>
<!-- #wrapper -->
<div id="">
	<!-- #container -->
	<div id="container">		
		<!-- #full -->
		<div id="full" >

<!-- #content -->
<div id="content">
	<div class="rd_xmlviewer">
		<XMP>
		
		</XMP>
	</div>
</div>
<!-- //#content -->

	<input type="hidden" id="file_path" value="${feedServerPath }"/>
<script>
$( document ).ready(function() {

	getFileXMLData($('#file_path').val());
	
	
});



</script>

<script src="/monitoring/js/server_management/configure_list.js"></script>
		</div>
		<!-- //#full -->
	</div>
	<!-- //#container -->
	
</div>

<!-- //#wrapper -->
</body>

</html>

