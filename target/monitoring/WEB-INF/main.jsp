<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<spring:eval expression="@environment.getProperty('feed_db_xml.http.path')" var="feedHttpPath" /> --%>
<c:set var="ctx">${pageContext.request.contextPath}</c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>검색 모니터링</title>
<script src="${ctx}/js/common/webfontloader.js"></script>
<link rel="stylesheet" href="${ctx}/css/datatables.min.css">
<link rel="stylesheet" href="${ctx}/css/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/css/buttons.dataTables.min.css">
<link rel="stylesheet" href="${ctx}/css/common.css">

<script src="${ctx}/js/common/jquery-1.12.4.min.js"></script>
<script src="${ctx}/js/common/jquery-ui.js"></script>
<script src="${ctx}/js/common/jquery.serializeObject.min.js"></script>

<script src="${ctx}/js/common/chart.js"></script>
<script src="${ctx}/js/common/chart_common.js"></script>


<script src="${ctx}/js/common/datatables.min.js"></script>
<script src="${ctx}/js/common/dataTables.buttons.min.js"></script>
<script src="${ctx}/js/common/full_numbers_no_ellipses.js"></script>

<script src="${ctx}/js/common/jszip.min.js"></script>
<script src="${ctx}/js/common/pdfmake.min.js"></script>
<script src="${ctx}/js/common/vfs_fonts.js"></script>

<script src="${ctx}/js/common/buttons.html5.min.js"></script>

<script src="${ctx}/js/common/jquery.form.js"></script> 

<script src="${ctx}/js/common.js"></script>

</head>
<body>
<!-- #wrapper -->
<div id="wrapper">
	<!-- #header -->
	<div id="header">
		<div id="header_top">
			<span class="logo"><a href="#" onclick="$('#a_firstmenu').trigger('click');">통합검색 모니터링 시스템 <span id="taskName"></span></a></span>
			<ul class="info">
				<!-- li><span class="ic_user">홍길동</span></li-->
				<!-- li><a href="#n" class="ic_help">도움말</a></li-->
				<li id="li_start"><a href="javascript:;" onclick="search_engine_start();" id="butt_start"><span class="ui-icon ui-icon-play">Start</span>Start</a></li>
				<li id="li_stop"><a href="javascript:;" onclick="search_engine_stop();" id="butt_stop"><span class="ui-icon ui-icon-pause">Stop</span>Stop</a></li>
			</ul>
		</div>
		<div id="gnb">
			<h1 id="gnb_title"></h1>
			<ul>
				<li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_status_list?version=7.9.0', 'Y','검색엔진 모니터링');"  id="a_firstmenu"><span>검색엔진 모니터링</span></a>
					<ul>
				    	<!--  <li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_indices_list?version=6.5.4', 'Y','검색엔진 Dashboard');"><span>검색엔진 Dashboard</span></a></li>-->
				    	<li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_status_list?version=7.9.0', 'Y','검색엔진상태관리');"><span>가동계 클러스터(가동계 V.7.9.0)</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_status_list?version=7.9.1', 'Y','검색엔진상태관리');"><span>로그 클러스터(가동계 V.7.9.0)</span></a></li>
				    	<!-- <li><a href="#n" onclick="loadCenterPage('search_monitoring/search_engine_status_list?version=5.4.0', 'Y','검색엔진상태관리');"><span>개발계 클러스터(개발계 V.5.4.1)</span></a></li>-->
				   </ul>
				</li>
				<li><a href="#n" onclick="loadCenterPage('chart_management/server_realtime_transaction', 'Y','AI모델');"><span>AI모델 모니터링</span></a>
					<ul>
							<li><a href="#n" onclick="loadCenterPage('chart_management/server_realtime_transaction', 'Y','실시간AI처리상태');"><span>실시간AI처리(제안)</span></a></li>
							<li><a href="#n" onclick="loadCenterPage('log_management/ai_server_log_list', 'Y','AI서버로그');"><span>AI서버로그(공통)</span></a></li>
							<!-- <li><a href="#n" onclick="loadCenterPage('log_management/ai_extract_result_list', 'Y','AI검출결과로그');"><span>AI검출결과로그</span></a></li> -->
							<li><a href="#n" onclick="loadCenterPage('chart_management/daily_usage_transaction', 'Y','일별사용률');"><span>일별AI사용률(공통)</span></a></li>
							<li><a href="#n" onclick="loadCenterPage('chart_management/monthly_usage_transaction', 'Y','월별사용률');"><span>월별AI사용률(공통)</span></a></li>
							<li><a href="#n" onclick="loadCenterPage('chart_management/poscomply_realtime_transaction', 'Y','실시간AI처리상태');"><span>실시간AI처리(약관공정화)</span></a></li>
							<li><a href="#n" onclick="loadCenterPage('interface_management/ai_interface_verify', 'Y','문장검출패턴');"><span>문장검출패턴</span></a></li>
							<!-- <li><a href="#n" onclick="loadCenterPage('chart_management/server_realtime_transaction', 'Y','모델성능');"><span>AI모델성능(약관공정화)</span></a></li>
							<li><a href="#n" onclick="loadCenterPage('log_management/ai_server_log_list', 'Y','WAS서버로그');"><span>WAS서버로그(약관공정화)</span></a></li>
							<li><a href="#n" onclick="loadCenterPage('log_management/ai_server_log_list', 'Y','AI검출결과로그');"><span>AI검출결과로그(약관공정화)</span></a></li> -->
					</ul>
				</li>
				<li><a href="#n" onclick="loadCenterPage('server_management/resource_list', 'Y','서버대상항목관리');"><span>서버관리</span></a>
					<ul>
				    	<li><a href="#n" onclick="loadCenterPage('server_management/resource_list', 'Y','서버대상항목관리');"><span>서버대상항목관리</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('server_management/configure_list', 'Y','미리보기');"><span>미리보기</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('server_management/cronjob_list', 'Y','서버별 CronJob 관리');"><span>서버별 CronJob 관리</span></a></li>
					</ul>
				</li>
				<li><a href="#n" onclick="loadLeftPage('code/menu_xml_manage', '','메뉴xml관리');"><span>코드관리</span></a>
					<ul>
				    	<li><a href="#n" onclick="loadLeftPage('code/menu_xml_manage', '','메뉴xml관리');"><span>메뉴xml관리</span></a></li>
						<li><a href="#n" onclick="loadCenterPage('code/menu_list', 'Y','메뉴코드');"><span>메뉴코드</span></a></li>
						<li><a href="#n" onclick="loadCenterPage('code/menu_attr_list', 'Y','메뉴속성코드');"><span>메뉴속성코드</span></a></li>
						<li><a href="#n" onclick="loadCenterPage('code/log_area_list?cd_tp=ES_SCR_AREA', 'Y','로그영역코드(통합검색)');"><span>로그영역코드(통합검색)</span></a></li>
						<li><a href="#n" onclick="loadCenterPage('code/log_area_list?cd_tp=ES_ADM_AREA', 'Y','로그영역코드(ESAdmin)');"><span>로그영역코드(ESAdmin)</span></a></li>
						<li><a href="#n" onclick="loadCenterPage('code/code_type_list', 'Y','코드유형정보관리');"><span>코드유형정보관리</span></a></li>
					</ul>
				</li>
				<li><a href="#n" onclick="loadCenterPage('log_management/was_log_list', 'Y','WAS로그');"><span>실시간통합검색로그</span></a>
					<ul>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/was_log_list', 'Y','WAS로그');"><span>WAS로그</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/queue_log_list', 'Y');"><span>시스템변경정보로그</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/feed_log_list', 'Y');"><span>서버별색인로그</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/ecm_auth_log_list', 'Y');"><span>문서권한조회로그</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('log_management/feed_server_log_list', 'Y','Feed로그');"><span>Feed로그</span></a></li>
					</ul>
				</li>
				<li><a href="#n" onclick="loadCenterPage('interface_management/interface_verify', 'Y','인터페이스 검증');"><span>인터페이스관리</span></a>
					<ul>
						<li><a href="#n" onclick="loadCenterPage('interface_management/interface_verify', 'Y','인터페이스 검증');"><span>인터페이스 검증</span></a></li>
						<li><a href="#n" onclick="loadCenterPage('interface_management/interface_provideif', 'Y','provideAIF 검증');"><span>provideAIF 검증</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('interface_management/sql_excute', 'Y','SQL 실행');"><span>SQL 실행</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('interface_management/sql_list', 'Y','SQL 관리');"><span>SQL 관리</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('interface_management/feeder_db_list', 'Y','Feeder DB자원 관리');"><span>Feeder DB자원 관리</span></a></li>
				    	<li><a href="#n" onclick="loadCenterPage('interface_management/developer_link', 'Y','운영사이트 관리자접속');"><span>운영사이트 관리자접속</span></a></li>
				    	
					</ul>
				</li>
				<li><a href="#n" onclick="alert('준비중입니다.');"><span>Feed 관리</span></a></li>
				<li><a href="#n" onclick="loadCenterPage('configuration/configure_upsert', 'Y','설정');"><span>환경설정</span></a>
					<ul>
						<li><a href="#n" onclick="loadCenterPage('configuration/configure_upsert', 'Y','설정');"><span>설정</span></a></li>
						<li><a href="#n" onclick="loadCenterPage('configuration/esauthdb_list', 'Y','ES통합권한');"><span>ES통합권한</span></a></li>
					</ul>
				</li>
			</ul>
		</div>
		<div id="util">
		</div>
		<div id="depth2_bar"></div>
	</div>
	<!-- //#header -->
	<%--<input type="hidden" id="tns_info" value="${feedHttpPath }"/> --%>
	<!-- #container -->
	<div id="container">
		
		<!-- #right -->
		<div id="right" style="display:none;">
		</div>
		<!-- //#right -->		
		<!-- #left -->
		<div id="left" style="display:none;">
		</div>
		<!-- //#left -->
		
		<!-- #center -->
		<div id="center">
		</div>
		<!-- //#center -->
		
		
		<!-- #full -->
		<div id="full" style="display:none;">
		</div>
		<!-- //#full -->
	</div>
	<!-- //#container -->
	
	<div id="blank"></div>
</div>
<div id="ajax_load_indicator" style="display:none;">
	<img id='loadingImg' src='/monitoring/img/loding.gif' style="display:none;" />
</div>
<script type="text/javascript">
$( document ).ready(function() {
	//초화면 셋팅
	$('#a_firstmenu').trigger('click');
	
	getApplicationTask();
});
</script>

<!-- //#wrapper -->
</body>

</html>
