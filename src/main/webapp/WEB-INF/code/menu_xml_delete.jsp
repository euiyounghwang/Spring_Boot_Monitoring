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

	
	<h2>메뉴 속성 삭제</h2>
	
	<div id="c1" >
				<!-- .dialog_item_box -->
				<div class="dialog_item_box">
					<div class="head">
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
							</tbody>
						</table>
					</div>
					<!-- .body -->
					<div class="body">
						<div class="type">
  						<table>
				       <colgroup>
            			<col style="width:50%;">
            			<col style="width:50%;">
            		</colgroup>
    						<tr>
      						<td>
			                  	<table class="grid_table" id="grid_table_menu">
			                  		<colgroup>
			                  			<col style="width:10px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:100px;">
			                  			<col style="width:50px;">
			                  		</colgroup>
			                  		<thead>
			                  			<tr>
			                  				<th><input type="checkbox" id="check-all-menu"></th>
			                  				<th>  MenuID</th>
			                  				<th>메뉴코드</th>
			                  				<th>  메뉴명</th>
			                  				<th>Level</th>
			                  			</tr>
			                  		</thead>
			                  		<tbody>
			                  		</tbody>
			                  	</table>
      						</td>
      						<td>
      						  <table class="grid_table"  id="grid_table_company">
		                  		<colgroup>
		                  			<col style="width:20px;">
		                  			<col style="width:120px;">
		                  		</colgroup>
		                  		<thead>
		                  			<tr>
		                  				<th><input type="checkbox" id="check-all-company"></th>
		                  				<th>회사코드</th>
		                  				<th>회사명</th>
		                  			</tr>
		                  		</thead>
		                  		<tbody>
		                  		</tbody>
		                  	</table>
      						</td>
    						</tr>
    						<tr>
      						<td>
                    <button class="btn2" id="btn-submit"><span>삭제</span></button>
      						</td>
    						</tr>
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
	var tableMenu = $('#grid_table_menu').DataTable({
		scrollY:0,
 		searching:false,
 		paging:false,
 		info:false,
 		ordering:false,
 		columns: [
 					{ data: 'checkbox' },
 			        { data: 'MENUID' },
 			        { data: 'MENU_CODE' },
 			        { data: 'CD_TP_MEANING' },
 			        { data: 'DEPTH' }
 			    ],
	    columnDefs: [{
	         'targets': 0,
	         'searchable':false,
	         'orderable':false,
	         'className': 'dt-body-center',
	         'render': function (data, type, full, meta){
	             return '<input type="checkbox">';
	         }
	      },
	      {"className": "dt-center", "targets": "_all"}
	      ],
	      "drawCallback": function (settings) {
// 	   			$('div.dataTables_scrollBody').scrollTop(0);
	   		}
	   });
	
	var tableCompany = $('#grid_table_company').DataTable({
		scrollY:0,
 		searching:false,
 		paging:false,
 		info:false,
 		ordering:false,
 		columns: [
				{ data: 'checkbox' },
		        { data: 'COMPANY_CODE' },
				{ data: 'COMPANY_NAME' }
		    ],
	    columnDefs: [{
	         'targets': 0,
	         'searchable':false,
	         'orderable':false,
	         'render': function (data, type, full, meta){
	             return '<input type="checkbox">';
	         }
	      },
	      {"className": "dt-center", "targets": "_all"}
	      ],
	      "drawCallback": function (settings) {
// 	   			$('div.dataTables_scrollBody').scrollTop(0);
	   		}
	   });
	
	selectCommonMetas();
	
	$('#sel_metas').change(function(){
		$('#meta_info1').empty().text($('#sel_metas option:selected').attr('tag'));
		$('#input_metaValue').val('');
		selectMenucodeByMeta();
	});
	
	$('#check-all-menu').on('click', function(){
		$('#check-all-company').prop('checked', false);
	      var rows = tableMenu.rows({ 'search': 'applied' }).nodes();
	      $('input[type="checkbox"]', rows).prop('checked', this.checked);
	      
			var menu_list = $( tableMenu.$('input[type="checkbox"]').map(function () {
			  return $(this).prop("checked") ? $(this).closest('tr').children('td:eq(2)').text() : null;
			}));
			
			if(menu_list.length > 0){
				selectCompanyByMetaMenu(menu_list);
			} else {
				tableCompany.clear();
				tableCompany.draw();
			}
	});
	
	$('#check-all-company').on('click', function(){
	      var rows = tableCompany.rows({ 'search': 'applied' }).nodes();
	      $('input[type="checkbox"]', rows).prop('checked', this.checked);
	});
	
	$('#grid_table_menu').on('click', 'input[type="checkbox"]', function() {
		var menu_list = $( tableMenu.$('input[type="checkbox"]').map(function () {
		  return $(this).prop("checked") ? $(this).closest('tr').children('td:eq(2)').text() : null;
		}));

		selectCompanyByMetaMenu(menu_list);
	});   
	
	$('#btn-submit').on('click', function(){
		if($('#sel_metas').val()=='선택'){
			alert('속성을 선택하세요.');
			return;
		}
		
		var menu_list = $( tableMenu.$('input[type="checkbox"]').map(function () {
			  return $(this).prop("checked") ? $(this).closest('tr').children('td:eq(2)').text() : null;
			}));
		
		var company_list = $( tableCompany.$('input[type="checkbox"]').map(function () {
			  return $(this).prop("checked") ? $(this).closest('tr').children('td:eq(1)').text() : null;
			}));
		
		if(menu_list.length < 1){
			alert('메뉴를 선택하세요.');
			return;
		} else if(company_list.length < 1){
			alert('회사를 선택하세요.');
			return;
		} else {
			if(confirm('선택하신 메뉴에 속성을 삭제 하시겠습니까?'))
				deleteMetaInMenu(menu_list, company_list);
		}
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
</script>