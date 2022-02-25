/**
 * 서버별 Cronjob 관리
 */

var table;

$( document ).ready(function() {	
	selectCodeType('sel_infra_type','INFRA_TYPE','CONNECTOR','','','selCode');
	selectCodeType('sel_host','HOST_NAME','CONNECTOR','','','selCode');
	
	table = $('#center .grid_table').DataTable( {
        "serverSide": true, // Ajax 서버통신을 통해서 데이터를 가져올지 여부
        "paging": false, // 페이징 기능 사용여부
        columns : [ //jsp에 정의한 <table> 에 들어갈 필드들. <th>로 jsp에 정의필수. 매칭 필수.
			{data : 'SERVER_TYPE'},
			{data : 'INFRA_TYPE'},
			{data : 'HOST_NAME'},
			{data : 'SERVER_NAME'},
			{data : 'POSCO_NAT_IP'},
			{data : 'REAL_IP'},
			{data : 'SERVICE_IP'},
			{data : 'USER_ID'},
			{data : 'USER_PW'},
			{data : 'DESCRIPTION'},
			{
	        	data : '',
	            render: function (data, type, full, meta) {
		                return '<button class="btn1" onclick="loadCenterPageAjax(&quot;server_management/cronjob_list_detail&quot;, &quot;Y&quot;, &quot;'+meta.row+'&quot;);"><span>CronJob 상세 조회</span></button>';
		                
	            }
			}
		],
		searching:false, //검색 기능 사용여부. 쿼리에서 해당 값으로 where 정의 해줘야함.
		language: { //default display 언어를 변경하는 기능
		    zeroRecords: "검색 결과가 없습니다.",
		    info : '조회현황 : _TOTAL_ 건'
		  },
		info:true, // 테이블의 정보 표기여부
		columnDefs:[ // 컬럼들의 속성 지정. 컬럼들의 index로 정의
			{targets:[0,1,3,4,6,7,8,9], orderable:false}, //orderable : 해당 컬럼으로 sort 할지. 쿼리에서 해당 값으로 order by 재정의 필요.
			{targets:[0,1,2,3,4,5,6,7,8,9], className:'dt-body-center'}, //className : 해당 컬럼들의 class 지정
		],
		order:[2,'asc'], //default sort 값. 쿼리에서 해당 값으로 order by 정의 필요.
		"ajax": { //data를 가져올 ajax 통신
			type : 'POST',
            url: "findServerList",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
                showProgress();
            },
            data: function ( d ) { //통신 전 보낼 data 재정의. d: dataTables 기본 데이터 format.
            	d['EVENT_ID'] = 'findServerList';
            	d['P_SERVER_TYPE'] = 'CONNECTOR';
            	if($('#sel_infra_type').val()!=''  && $('#sel_infra_type option:selected').text()!='선택') d['P_INFRA_TYPE'] = $('#sel_infra_type option:selected').val();
            	if($('#sel_host').val()!='' && $('#sel_host option:selected').text()!='선택') d['P_HOST_NAME'] = $('#sel_host option:selected').val();
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) { // 테이블 그리기가 끝난 후 호출되는 함수.
            hideProgress();
        },
        dom: '<"top"li>t<"bottom"p><"clear">'
    });
	
	$('#sel_infra_type').on('change', function() {
		if($('#sel_infra_type').val()!=''  && $('#sel_infra_type option:selected').text()!='전체')
			selectCodeType('sel_host','HOST_NAME','CONNECTOR',$('#sel_infra_type option:selected').text().trim(),'', 'selCode');
	});

	
	$('#btn_refresh').click(function(){
		search();
	});
});



function search(){
	table.ajax.reload();
}

