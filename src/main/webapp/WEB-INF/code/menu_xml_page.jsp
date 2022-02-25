<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="btns_bar fixed">
	<div class="left">
		<button class="btn1" onclick="loadCenterPage('code/menu_xml_list', 'N');"><span>메뉴속성검색</span></button>
		<button class="btn1" onclick="loadCenterPage('code/menu_xml_update', 'N');"><span>재설정</span></button>
		<button class="btn1" onclick="loadCenterPage('code/menu_xml_insert', 'N');"><span>추가</span></button>
		<button class="btn1" onclick="loadCenterPage('code/menu_xml_delete', 'N');"><span>삭제</span></button>
	</div>
</div>

<!-- #content -->
<div id="content">
<!-- 	<h2>30(포스코) - doc</h2> -->
	
	<table class="rd_info">
		<colgroup>
			<col style="width:1px;">
			<col>
		</colgroup>
		<tbody>
		</tbody>
	</table>
	
	<div class="rd_json">
		<pre>
	    </pre>
	</div>
</div>
<!-- //#content -->

<script>
$( document ).ready(function() {
	getMenuData();
});


</script>