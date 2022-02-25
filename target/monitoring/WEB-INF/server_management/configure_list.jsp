<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="btns_bar fixed">
	<div class="left">
		<select id="sel_configure"></select>
	</div>
	<div class="right">
	<span class="selected_info"></span>
	</div>
</div>

<!-- #content -->
<div id="content">
	<div class="rd_xmlviewer">
		<XMP>
		</XMP>
	</div>
</div>
<!-- //#content -->

<script src="/monitoring/js/server_management/configure_list.js"></script>
<script>
var configurelist = {
		'swpes.posco.net:7091/pic/Feeds/Feeder_DB_Connection_Info.xml' : '[가동계]패밀리망 Feed 설정',
		'swpes.posco.net:7091/pic/system_query/elastic_query.xml' : '[가동계]e-HR 설정 xml',
		'swpes.posco.net:7091/pic/system_query/elastic_query_PROP.xml' : '[가동계]PROP 설정 xml',
		'swpes.posco.net:7091/pic/system_query/elastic_query_CITIZEN.xml' : '[가동계]CITIZEN 설정 xml',
		'swpes.posco.net:7091/pic/system_query/elastic_query_ECATALOG.xml' : '[가동계]ECATALOG  설정 xml',
		'swpes.posco.net:7091/pic/system_query/elastic_query_PATT.xml' : '[가동계]PATT 설정 xml',
		'swpes.posco.net:7091/pic/system_query/elastic_query_POSAPIA.xml' : '[가동계]POSAPIA  설정 xml',
		'swpes.posco.net:7091/pic/config/u40a_menu_codetable.xml' : '[가동계]WAS U40A menu xml',
		'swpes.posco.net:7091/pic/config/u40a_map_menu_codetable.xml' : '[가동계]WAS U40A 분류맵 포함 menu xml',
		'uswpes.posco.net:7091/pic/Feeds/Feeder_DB_Connection_Info.xml' : '[테스트계]패밀리망 Feed 설정',
		'uswpes.posco.net:7091/pic/system_query/elastic_query.xml' : '[테스트계]e-HR 설정 xml',
		'uswpes.posco.net:7091/pic/system_query/elastic_query_PROP.xml' : '[테스트계]PROP 설정 xml',
		'uswpes.posco.net:7091/pic/system_query/elastic_query_CITIZEN.xml' : '[테스트계]CITIZEN 설정 xml',
		'uswpes.posco.net:7091/pic/system_query/elastic_query_ECATALOG.xml' : '[테스트계]ECATALOG  설정 xml',
		'uswpes.posco.net:7091/pic/system_query/elastic_query_PATT.xml' : '[테스트계]PATT 설정 xml',
		'uswpes.posco.net:7091/pic/system_query/elastic_query_POSAPIA.xml' : '[테스트계]POSAPIA  설정 xml',
		'uswpes.posco.net:7091/pic/config/u40a_menu_codetable.xml' : '[테스트계]WAS U40A menu xml',
		'uswpes.posco.net:7091/pic/config/u40a_map_menu_codetable.xml' : '[테스트계]WAS U40A 분류맵 포함 menu xml',
		'dswpes.posco.net:7091/pic/config/u40a_menu_codetable.xml' : '[개발계]WAS U40A menu xml',
		'dswpes.posco.net:7091/pic/config/u40a_map_menu_codetable.xml' : '[개발계]WAS U40A 분류맵 포함 menu xml',
}

$( document ).ready(function() {
	drawSelect( $('#sel_configure'), configurelist, 'conf');
});


$('#sel_configure').change(function(){
	getHTTPXMLData($('#sel_configure').val())
});

</script>