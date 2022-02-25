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

	
	<h2>메뉴 속성 검색</h2>
	
	
	<div id="c1">
					<!-- .dialog_item_box -->
				<div class="dialog_item_box">
					<div class="head">
						<table style="width:100%;">
							<tbody>
								<tr>
									<td><span class="label">검색조건 선택</span></td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- .body -->
					<div class="body">
					  <div class="type">
						<table style="width:100%;">
							<colgroup>
								<col style="width:1px;">
								<col style="width:1px;">
								<col>
							</colgroup>
							<tbody>
								<tr>
									<td><span class="label">속성</span></td>
									<td>
											<select id="sel_metas" style="width:250px;"/>
									</td>
									<td><span class="label" id="meta_info1"></span></td>
								</tr>
								<tr>
									<td><span class="label">회사코드</span></td>
									<td>
											<select id="sel_company" style="width:250px;"/>
									</td>
									<td></td>
								</tr>
								<tr>
									<td><span class="label">속성값</span></td>
									<td><input type="text" id = "input_metaValue" style="width:100%;" placeholder="검색할 속성값을 입력해 주십시오."></td>
 								  <td><button class="btn2" id="btn_searchMeta"><span>검색</span></button></td>
								</tr>
								<tr>
								
								</tr>
							</tbody>
						</table>
						
						<h4>검색결과</h4>
						
          	<table class="grid_table" id="grid_meta_search">
	          	<colgroup>
	      			<col style="width:55px;">
	      			<col style="width:80px;">
	      			<col style="width:55px;">
	      			<col style="width:80px;">
	      			<col style="width:80px;">
	      			<col style="width:300px;">
	      		</colgroup>
          		<thead>
          			<tr>
						<th>회사코드</td>
						<th>MenuID</td>
						<th>메뉴코드</td>
						<th>메뉴명</td>
						<th>속성</td>
						<th>속성값</td>
          			</tr>
          		</thead>
          		<tbody>
				</tbody>
			</table>
						
		</div>
	</div>
					<!-- //.body -->
</div>
				<!-- //.dialog_item_box -->	
</div>
	<!-- //필수 Parameter -->
	
	
</div>
<!-- //#content -->
	<script>
	$('#grid_meta_search').DataTable({
		scrollY:0,//'0'이면 세로로 꽉 차는 스크롤 테이블
		searching:true,
		paging:false,
		info:false,
		ordering:false,
		columns: [
	        { data: 'COMPANY_CODE' },
	        { data: 'MENUID' },
	        { data: 'CD_TP' },
	        { data: 'KOR_NAME' },
	        { data: 'META' },
	        { data: 'CD_TP_MEANING' }
	    ],
	    columnDefs:[
	    	{"className": "dt-center", "targets": [0,1,2,3,4]},
	    	{"className": "dt-left", "targets": [5]},
    	],
   		"drawCallback": function (settings) {
   			$('div.dataTables_scrollBody').scrollTop(0);
   		}
	});
	$( document ).ready(function() {
		selectCommonMetas();
		selectCommonCompanys('select');
		
		$('#btn_searchMeta').click(function(){
			selectMetaListByMetas();
		});
		
		$('#sel_metas').change(function(){
			$('#meta_info1').empty().text($('#sel_metas option:selected').attr('tag'));
		});
		
	});
	</script>