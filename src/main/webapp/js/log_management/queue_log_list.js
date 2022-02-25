/**
 * was 서버 로그 조회
 */

var table;
var excel = false;

$( document ).ready(function() {	
	selectCodeType('sel_server_conf','INFRA_TYPE','CONNECTOR','','','selCode');
	setTimeout(function(){ 
		$("#sel_server_conf option:contains('가동계')").prop('selected', true);
		selectCodeType('sel_host','HOST_NAME','CONNECTOR',$("#sel_server_conf option:selected").text().trim(),'','hostAll'); 
	},700);
	
	selectResultCode();

	$('#startDate').datepicker({
		inline: true,
		changeMonth: true,
		changeYear: true,
		yearRange: "1990:+0",
		dateFormat: "yy-mm-dd"
	});
	$('#startDate').datepicker('setDate', -1);
	
	$('#endDate').datepicker({
		inline: true,
		changeMonth: true,
		changeYear: true,
		yearRange: "1990:+0",
		dateFormat: "yy-mm-dd"
	});
	$('#endDate').datepicker('setDate', new Date());
	
	table = $('#center .grid_table').DataTable( {
		ordering: false,
        "serverSide": true,
        "paging": true,
        "autoWidth": false, //자동 열 너비 계산을 사용 또는 사용하지 않도록 설정한다.(true일 경우 td에 width값이 px단위로 고정되어 window resize 이벤트에 대응 안됨)
        pagingType: "full_numbers_no_ellipses",
        columns : [
			{data : 'COMPANY_CODE'},
			{data : 'INDEX'},
			{data : 'SYSTEM_ID'},
			{data : 'ID'},
			{data : 'QUEUE_ID'},
			{data : 'ACTION'},
			{data : 'S_CODE'},
			{data : 'CREATE_DATE'},
			{data : 'QUEUE_INSERT_DATE'},
			{data : 'RESULT_CODE'},
			{
				data : 'HIGHLIGHT',
				render: function (data, type, full, meta) {
					return '<textarea class="ui-widget" style="width:100%;">'+data+'</textarea>';
				}
			}
		],
		searching:true, 
		language: {
		    search: "검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		    info : '조회현황 : _TOTAL_ 건'
		  },
		info:true,
		columnDefs:[
			{targets:[0,1,2,3,4,5,6,7,8], className:'dt-body-center'},
			{ "orderable": false, "targets": [0,1,2,3,4,5,6,7,8]},
		],
		"rowCallback": function( row, data, index ) {
		    if ( data["RESULT_CODE"] == "CF" || data["RESULT_CODE"] == "UF" ){
		        $('td', row).css('background-color', '#F5BCA9');
		    	}
		    
		    if ( data["ACTION"] == "1" ){
		    	      $('td', row).css('font-weight', '900');
			 	}
		    else if ( data["ACTION"] == "3" ){
		        $('td', row).css('background-color', '#F5BCA9');
		        $('td', row).css('font-weight', '900');
	    		}
		},
		"ajax": {
			type : 'POST',
            url: "findQueueServerLog",
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
            	}
            	d['EVENT_ID'] = 'findQueueServerLog';
            	d['RESULT_CODE'] = $('#result_code option:selected').text()=='전체'?'':$('#result_code option:selected').text().trim();
            	
            	if($('#sel_host option:selected').text()==''){ //before selectbox loaded setting parameters..
            		d['P_HOST_NAME'] = "PLCFCNA1|PLCFCNA2|PLCFCNA3|PLCFCNA4|PLCFCNA5|PLCFCNA6|PLCFCNA7|PLCFCNA8|PLCPCNA1|PLCFSERA1|PLCFSERA2";
            	} else {
            		d['P_HOST_NAME'] = $('#sel_host option:selected').text()=='전체'?$('#sel_host option:selected').val():$('#sel_host option:selected').text().trim();
            	}
            	d['START_DATE'] = $('#startDate').val();
            	d['END_DATE'] = $('#endDate').val();
            	d['P_SERVER_TYPE_CD'] = '30';
            	d['P_SERVER_CONF_CD'] = $('#sel_server_conf option:selected').val();
            	d['search'].value = $('#input_search').val();
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) {
        	hideProgress();
        	//second row add : log message 
        	if(table.rows()[0].length > 0){
	        	var tr = $('#center .grid_table tr');
	        	tr= tr.splice(1, tr.length-1);
        	}
        },
        lengthMenu: [[10, 50, 100], [10, 50, 100]],
        dom: '<"top"li>t<"bottom"p><"clear">'
    });
	
	table.on( 'page.dt', function () {
		$('#center').animate({scrollTop: 0}, 100);
	} );
	
	$('#btn_refresh').click(function(){
		search();
	});
	
	$('#sel_server_conf').on('change', function() {
		if($('#sel_server_conf').val()!='')
			selectCodeType('sel_host','HOST_NAME','CONNECTOR',$("#sel_server_conf option:selected").text().trim(),'','hostAll');

	});

	$('#input_search').on('keypress', function (e) {
	       if(e.which === 13){
    	   		search();
	       }
	});
});

function search(){
	$('input[type="search"]').val('').keyup();
	table.ajax.reload();
}


function selectResultCode(){
	var params = {};
	params['cdTpTp'] = 'RESULT_CODE';
	$.ajax({
		type : 'POST',
        url:'commonCodeList',
        data : JSON.stringify(params),
        dataType:'json',
        contentType: 'application/json',
        success:function(data){
        	var obj = $('#result_code');
    		drawSelect(obj, data, 'commonCodeAll');
        },
        error : function(e){ 
        }
     });
}

