/**
 * SQL 실행
 */

var sql_table;

var checkedIndices =[];
var excel = false;

$( document ).ready(function() {
	
	$('#sel_host').on('change',function(){
		getConfigDataAsJson('sel_host_idx');
	});
	
	sql_table = $('#sql_table').DataTable( {
      "serverSide": true,
      "paging": false,
      ordering : false,
      columns : [
			{
				data : 'INFO_TITLE',
				render: function (data, type, full, meta) {
					return '<a href="#" onclick="setSqlData(&quot;'+meta.row+'&quot;);">'+data+'</a>';
				}
			},
			{
				data : 'OBJECT_ID',
				visible : false
			}
		],
		searching:false,
		language: {
		    zeroRecords: "검색 결과가 없습니다.",
		  },
		info:false,
		columnDefs:[
			{targets:[0], orderable:false},
		],
		scrollX: false,
		scrollY: "300px",
	    scrollCollapse: true,
//		order:[0,'asc'],
		"ajax": {
			type : 'POST',
          url: "selectSqlList",
          contentType : 'application/json',
          dataType:'json',
          data: function ( d ) {
        	  d['order'] = [{column: 0, dir: "asc"}];
          	if(excel){
          		d['start']  = 0;
          		d['length'] = sql_table.settings()[0]._iRecordsDisplay;
          	} else {
          		if(d['draw']!=1)
          			d['start'] = sql_table.settings()[0]._iDisplayStart;
          	}
          	d['host_info'] = $('#sel_host_idx option:selected').text().split(' (')[0];
          	return JSON.stringify(d);
          }
      },
      "drawCallback": function( settings ) {
    	  if($('#sel_host option:selected').text()!=='선택' && $('#sel_host option:selected').text()!==''){
    		  $('#sql_table > tbody').prepend('<tr role="row" class="odd"><td class="sorting_1"><a href="#" onclick="switchState(&quot;user&quot;);">직접입력</a></td></tr>');
    	  }
      }
  });
	
	$('#SQL_CONT').keydown(function (e) {
		
	  if (e.ctrlKey && e.keyCode == 13) {
		excuteSql();
	  }

	});
	
	$('#btn_delete').click(function(){
		$('#param_table>tbody>tr.row_selected').remove();
		$('#check-all-menu').prop('checked', false);
		deleteBtnStatus();
	});
	
	$('#btn_host_search').click(function(){
		refreshSqlTable();
	});
		
	$('#check-all-menu').on('click', function(){
		var tr = $('input[name="ROWDEL"]','#param_table>tbody>tr');
		tr.prop('checked', this.checked);
	    if(this.checked){
	    	tr.closest('tr').addClass('row_selected');
	    }else {
	    	tr.closest('tr').removeClass('row_selected');
	    }
	    deleteBtnStatus();
	});
	
	$('.panel2').on('click', 'input[name="ROWDEL"]', function(){
        if ( $(this).closest('tr').hasClass('row_selected') ) {
            $(this).closest('tr').removeClass('row_selected');
        }
        else {
            $(this).closest('tr').addClass('row_selected');
        }
        deleteBtnStatus();
	});
	
	getConfigDataAsJson('sel_host');
});


function refreshSqlTable(){
	switchState('sql');
	sql_table.ajax.reload();
}

function excuteSql(){
	// ; 제거
	// query 생성
	var url = 'excuteSql';
	var string = '실행 하시겠습니까?';
	resultMessage('');
	var validate = excuteSqlValidation();// ?갯수와 파람갯수 비교
	
	if(validate){
		if(confirm(string)){
			var params = {};
			var query = $('#SQL_CONT').val().split('?');
			var queryString = '';
			var ELEMENT_VALUE=[];
			var DATA_TYPE=[];
			var parameter = [];
			var i=0;
			$('input[name="ELEMENT_VALUE"]').each(function() {
				 if(this.value!=''){
					 ELEMENT_VALUE[i] = this.value;
					 if($(this).closest('td').siblings(':last').children().is(":checked")){
						 DATA_TYPE[i] = 'FUNCTION';
					 }else{
						 DATA_TYPE[i] = 'STRING';
					 }
					 i++;
				 }
	       });
	       
			if(query.length == 1){
				queryString = query[0];
			}else {
				for(var i=0; i<query.length; i++){
					if(i!=query.length-1){
						if(DATA_TYPE[i]=="FUNCTION"){
							queryString += query[i] + ELEMENT_VALUE[i];
						}else{
							queryString += query[i] + " '" + ELEMENT_VALUE[i] +"' ";
						}
						
					}else{
						queryString += query[i];
					} 
				}
			}
			queryString = queryString.replace(';','');
			params["QUERY"]=queryString;
			params["host_info"]=$('#sel_host_idx option:selected').val();
			if($.trim(queryString).substr(0, 6).toUpperCase()!='SELECT'){
				$.ajax({
					type : 'POST',
			        url: 'excuteSql',
			        data : JSON.stringify(params),
			        contentType: 'application/json',
			        dataType:'json',
			        beforeSend: function(){
		                showProgress();
		            },
			        success:function(result){
			        	if ('result' in result){
			        		var msg = result.result;
			        		resultMessage(msg);
			        	} else if ('errMsg' in result){
			        		var errMsg = result.errMsg;
			        		resultMessage(errMsg);
			        	}
			        },
			        error : function(e){ 
			        	alert('SQL 실행에 실패했습니다.');
			        },
			        complete : function(){
			        	hideProgress();
			        }
			     });
			} else {
				selectTable(params);
			}
		}
	}
}
var table;
var select_params = {};
var my_columns = [];
function selectTable(params){
	select_params = params;
	
	params['header'] = 'header';
	$.ajax({
		type : 'POST',
        url: 'excuteSql',
        data : JSON.stringify(params),
        contentType: 'application/json',
        dataType:'json',
        beforeSend: function(){
            showProgress();
        },
        success:function(result){
        	 if ('errMsg' in result){
	        		var errMsg = result.errMsg;
	        		resultMessage(errMsg);
	        		hideProgress();
        	 } else if('data' in result){
        		if(result.data.length>0){
        		var data = result.data;
        		my_columns = [];
            	$.each( data[0], function( key, value ) {
        	        var my_item = {};
        	        my_item.data = key;
        	        my_columns.push(my_item);
            	});
            	if(table != null)
            		table.destroy(true);
            	$('#result_table').remove();
            	$('#result_table_wrapper').remove();
        		drawTable();
        		}
        	}
        	 else {
        		resultMessage('조회된 데이터가 없습니다.');
        		hideProgress();
        	}
        },
        error : function(e){
        	alert('SQL 실행에 실패했습니다.');
        }
        
     });
	setTimeout(function(){ 
		$("#result_table_wrapper > .dataTables_scroll > div").css( 'width', ''); 
	},500);//실행결과 테이블 스크롤 버그수정(모니터화면 크기에 상관없이 적용되도록 하기위함)

	
	}

function drawTable(){
	$('#SQL_RESULT').val('');
	$('#SQL_RESULT').hide();
	
	var tag = '<table class="grid_table" id="result_table" style="width:100%;"><thead><tr>';
	for(var i=0; i<my_columns.length; i++)
		tag += '<th>'+my_columns[i].data+'</th>';
	tag += '</tr></thead><tbody></tbody></table>';
	$('#result_div').append(tag);
	
	table = $('#result_table').DataTable({
//		data: data,
	    columns : my_columns,
	    ordering:false,
	    processing: false,
	    serverSide: true,
	    paging: false,
		searching:false,
		info:true,
		scrollX: true,
		scrollable : true,
		scrollY: "280px",
	    scrollCollapse: true,
//	    order: [1, 'asc'],
	    language: {
		    info : '(전체결과건수 : _TOTAL_건) *최대 100건까지 표시됩니다.',
	    	zeroRecords: "검색 결과가 없습니다."
		  },
		  "ajax": {
				type : 'POST',
	            url: 'excuteSql',
	            contentType : 'application/json',
	            dataType:'json',
	            beforeSend: function(){
	                showProgress();
	            },
	            data: function ( d ) {
	            	if(excel){
	            		d['start']  = 0;
	            		d['length'] = table.settings()[0]._iRecordsDisplay;
	            	} else {
	            		if(d['draw']!=1)
	            			d['start'] = table.settings()[0]._iDisplayStart;
	            		d['length'] = 100;
	            	}
	            	d['QUERY'] = select_params['QUERY'];
	            	d['host_info'] = $('#sel_host_idx option:selected').val();
	            	return JSON.stringify(d);
	            }
	        },
        "drawCallback": function( settings ) {
            hideProgress();
        },
	    dom:'Bi<"top">t<"bottom"><"clear">',
	    buttons: [
            {
                extend: 'excel',
                text: 'Export Excel',
                title: 'SQL_EXCUTE',
                className:'btn2',
                action:  function (e, dt, button, config) {
                	var info = table.page.info();
                	if(info.recordsTotal>20000){
                		alert('2만건 이상의 대용량 Export는 관리자에게 문의해주세요.');
                		return false;
                	}
                	
                    var self = this;
                    var oldStart = table.settings()[0]._iDisplayStart;
                    excel = true;
                    table.one('preXhr', function (e, s, data) {
                        // Just this once, load all data from the server...
                        data.start = 0;
                        data.length = 2147483647;
                        excel = false;
                        table.one('preDraw', function (e, settings) {
                            // Call the original action function 
//                            oldExportAction(self, e, dt, button, config);
//                            function (self, e, dt, button, config) {
                            	if (button[0].className.indexOf('buttons-excel') >= 0 || button[0].className.indexOf('buttons-csv') >= 0) {
                                    if ($.fn.dataTable.ext.buttons.excelHtml5.available(table, config)) {
                                        $.fn.dataTable.ext.buttons.excelHtml5.action.call(self, e, table, button, config);
                                    }
                                    else {
                                        $.fn.dataTable.ext.buttons.excelFlash.action.call(self, e, table, button, config);
                                    }
                                } else if (button[0].className.indexOf('buttons-print') >= 0) {
                                    $.fn.dataTable.ext.buttons.print.action(e, table, button, config);
                                }
//                            };

                            	table.one('preXhr', function (e, s, data) {
                                // DataTables thinks the first item displayed is index 0, but we're not drawing that.
                                // Set the property to what it was before exporting.
                                settings._iDisplayStart = oldStart;
                                data.start = oldStart;
                            });
                            // Reload the grid with the original page. Otherwise, API functions like table.cell(this) don't work properly.
                            setTimeout(table.ajax.reload, 0);

                            // Prevent rendering of the full data to the DOM
                            return false;
                        });
                    });
                    // Requery the server with the new one-time export settings
                    table.ajax.reload();
                }
            }
        ]
	  });
}

function resultMessage(msg){
	$('#result_table').hide();
	$('#SQL_RESULT').val(msg);
	$('#SQL_RESULT').show();
}

function switchState(type){
	$('#SQL_RESULT').val('');
	$('#my-tbody>tr').remove();
	if(type=='user'){
		$('.panel2 .btns_bar').show();
		$('#SQL_CONT').prop('readonly', false);
	} else {
		$('.panel2 .btns_bar').hide();
		$('#SQL_CONT').prop('readonly', true);
	}
	$('#INFO_TITLE').html('');
	$('#SQL_CONT').val('');
	$('#SQL_RESULT').val('');
	$('#result_table_wrapper').remove();
	$('#SQL_RESULT').show('');
}

function setSqlData(index){
	switchState('sql');
	
	var row = sql_table.rows().data()[index];
	$.ajax({
		type : 'POST',
        url:'selectSql',
        data : JSON.stringify({'OBJECT_ID' : row.OBJECT_ID}),
        contentType: 'application/json',
        success:function(result){
        	var sql = result.sql;
        	var params = result.params; 
        	$('#INFO_TITLE').html(sql.INFO_TITLE+'&#10;'+sql.DESCRIPTION);
        	// SQL_CONT의 {schema}를 feed.db.schema값으로 교체
			sql.SQL_CONT = sql.SQL_CONT.replace('{schema}',$('#sel_host option:selected').attr("tag"));			
        	$('#SQL_CONT').val(sql.SQL_CONT);
        	
        	if(params.length>0){
        		for(var i=0; i<params.length; i ++){
        			var type='';
        			if(params[i].DATA_TYPE=='FUNCTION') type='checked';
            		addRow(params[i].ELEMENT_NAME, params[i].ELEMENT_VALUE, type);
            	}
        	} else {
        		$('#param_table>tbody>tr').remove();
        	}
        },
        error : function(e){ 
        	alert('SQL자원을 가져오던 중 에러가 발생했습니다.');
        }
     });
}

function addRow(name, value, type){
    var my_tbody = document.getElementById('my-tbody');
	var row = my_tbody.insertRow( my_tbody.rows.length );
	var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
    cell1.innerHTML = '<td><input type="checkbox" name="ROWDEL"></td>';
    cell2.innerHTML = '<input type="text" name="ELEMENT_NAME" value="'+name+'" style="width:100%"/>';
    cell3.innerHTML = '<input type="text" name="ELEMENT_VALUE" value="'+value+'" style="width:100%"/>';
    cell4.innerHTML = '<td><input type="checkbox" name="DATA_TYPE" '+type+'></td>';
}

function excuteSqlValidation(){
	var ELEMENT_NAME = [];
	var ELEMENT_VALUE=[];
	var i=0;
	
	 $('input[name="ELEMENT_NAME"]').each(function() {
         if(this.value!='') {
        	 ELEMENT_NAME[i] = this.value;
        	 i++;
        }
    }); 
	 i=0;
	 $('input[name="ELEMENT_VALUE"]').each(function() {
         if(this.value!='') {
        	 ELEMENT_VALUE[i] = this.value;
        	 i++;
        }
    });
	var cntQustionMark = $('#SQL_CONT').val().split('?');
		
	if($('#SQL_CONT').val()==''){
		alert('SQL 소스를 입력해주세요.');
		return false;
	} else if($('input[name="ELEMENT_NAME"]').length!=ELEMENT_NAME.length){
		alert('변수명을 입력해주세요.');
		return false;
	} else if($('input[name="ELEMENT_VALUE"]').length!=ELEMENT_VALUE.length){
		alert('변수값을 입력해주세요.');
		return false;
	} else if(cntQustionMark.length!=1 && cntQustionMark.length-1 != ELEMENT_VALUE.length){
		alert('SQL 소스의 물음표 갯수와 파라미터 수가 다릅니다.');
		return false;
	} else if($('#sel_host_idx option:selected').text()=='선택'){
		alert('DB자원을 선택 해 주세요.');
		return false;
	} else {
		return true;
	}
}

function fnGetSelected(){
    return $('#param_table>tbody>tr.row_selected');
}

function deleteBtnStatus(){
	var row_selected = $('#param_table>tbody>tr.row_selected');
	if(row_selected.length>0){
		$('#btn_delete').prop('disabled', '');
	} else {
		$('#btn_delete').prop('disabled', 'disabled');
	}
}