/**
 * 서버별 Cronjob 관리
 */

var table;
var backuptable;

$( document ).ready(function() {
	findCronJob();
});

function findCronJob(){
	
	table = $('#center .grid_table').DataTable( {
        "serverSide": true, // Ajax 서버통신을 통해서 데이터를 가져올지 여부
        "bDestroy": true, //새로고침시 필요한 옵션
        "paging": false, // 페이징 기능 사용여부
        columns : [ //jsp에 정의한 <table> 에 들어갈 필드들. <th>로 jsp에 정의필수. 매칭 필수.
        	{
	        	data : '',
	            render: function (data, type, full, meta) {
		                return '<input type="checkbox" onclick="checkIndex('+meta.row+')">';
	            }
			},
			{data : 'BATCH_TIME',
	            render: function (data, type, full, meta) {
	                return '<input type="text" value="'+data+'" id="BATCH_TIME" name="BATCH_TIME" style="width:100%"><input type="hidden" value="'+data+'" id="RAW_BATCH_TIME" name="RAW_BATCH_TIME">';
	            }
			},
			{data : 'PROCESS',
	            render: function (data, type, full, meta) {
	                return '<input type="text" value="'+data+'"  id="PROCESS" name="PROCESS" style="width:100%"><input type="hidden" value="'+data+'" id="RAW_PROCESS" name="RAW_PROCESS" >';
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
			{targets:[0,1,2], orderable:false} //orderable : 해당 컬럼으로 sort 할지. 쿼리에서 해당 값으로 order by 재정의 필요.
		],
		order:[], 
		"ajax": { //data를 가져올 ajax 통신
			type : 'POST',
            url: "findCronJob",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
                showProgress();
            },
            data: function ( d ) { //통신 전 보낼 data 재정의. d: dataTables 기본 데이터 format.
            	d['EVENT_ID'] = 'findCronJob';
            	d['HOST_NAME'] = $('#HOST_NAME').val();
            	d['REAL_IP'] = $('#REAL_IP').val();
            	d['USER_ID'] = $('#USER_ID').val();
            	d['USER_PW'] = $('#USER_PW').val();
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) { // 테이블 그리기가 끝난 후 호출되는 함수.
            hideProgress();
        },
        dom: '<"top"li>t<"bottom"p><"clear">'
    });
	
}


function addRow(time, process){
    var my_tbody = document.getElementById('my-tbody');
	var row = my_tbody.insertRow( my_tbody.rows.length );
	row.className="unlearned";
	var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    cell1.innerHTML = '<td><input type="checkbox"></td>';
    cell2.innerHTML = '<input type="text" name="BATCH_TIME" value="'+time+'" style="width:100%"/>';
    cell3.innerHTML = '<input type="text" name="PROCESS" value="'+process+'" style="width:100%"/>';
}

/**
 * 행삭제
 */
function deleteRow()
{

	//체크박스 개수 0일때 패스
	if($("#my-tbody input[type='checkbox']:checked").length == 0)
	{
		alert("선택된 행이 없습니다.");
		return false;
	}

	//삭제시겠습니까? 확인메시지
	var save = confirm("행삭제하시겠습니까? \n실제 Crontab 적용은 ####### 반드시 [저장] 버튼을 클릭 ####### 하셔야 합니다.");

	if(save)
	{
		//체크 박스에 체크된것은 화면에서 삭제 처리함
		$("#my-tbody input[type='checkbox']:checked").each(function(){
				//해당행 삭제
				var idx = $(this).parent().parent().index();
				console.log('tr idx='+idx);
				$("#my-tbody tr:eq("+ (idx)  +")").remove();
		});
	}
}



/**
 * 저장전 CronJob 유효성 체크
 * 1. #으로 시작하지 안고
 * 2. 공백을 기준으로 5개의 단어
 * 3. 패턴 숫자 이거나 * - , / (4개 특수 문자만 가능) /^[0-9*-,/]*$/
*/
function validCheckForSave()
{
	var validFlag = false;
	var batchTime;
	var regShap = /^#/;
	var reg = /^[0\s][0-99\s][*\s][-\s][,\s][/\s]*$/;

	//기존 선택한 tr class 제거
	$('.errormsg').removeClass("errormsg");

	var validCnt = 0;
	$("#my-tbody input[name='BATCH_TIME']").each(function(idx){
		batchTime = $(this).val();
		
		if(!regShap.test(batchTime))
		{
			var batchTimeArr = batchTime.split(" ");
			if( 5 == batchTimeArr.length)
			{
				console.log(reg.test(batchTime));
			}
			else
			{
				console.log("err("+idx+")"+batchTime);
				validCnt++;
				//현재 선택한 tr 의 class 적용
				$(this).parents("tr").addClass("errormsg");
				alert( (idx) + "번째의 CronJob의 배치시간은 5개의 패턴으로 작성되어야 합니다. 입력값을 확인해 주세요.");
			}
		}


	});

	if(validCnt == 0)
	{
		validFlag = true;
	}

	return validFlag;
}



/**
 * 백업 CronJob
 */
function backupCrontab()
{
	var  backupFlag = false; //cronJob 백업 성공 여부

	var paramData = $(".forBackupCrontabFormClass :input").serializeObject() ;

	//console.log(paramData);

	$.ajaxSetup({ async:false }); // 비동기식을 동기식으로 변경 결과 올때까지 대기함
    $.ajaxSetup({ cache:false }); // Cache 가 남아 있어 문제 발생 할수 있어서 Cache fasle 로 설정함.

	$.ajax({
	    type: 'post'
	    , url: 'backupCronJob'
	    , data: JSON.stringify(paramData)
	    , contentType: 'application/json'
	    , success: function(e) {
			if(  e=="success" )
			{
				backupFlag = true;
			}
			else{
				alert(e);
				
			}
	    }
	    , error: function(jqXHR, textStatus, errorThrown) {
			console.log("오류?");
			console.log(jqXHR);
			console.log(textStatus);
	    }
	    , complete: function() {
	    }
		});

	$.ajaxSetup({ async:true }); // 비동기식 원래 값으로 초기화

	return backupFlag;
}


//CronJob 백업 후 화면기준으로 CronJob 수정하여 Connector 서버에 Crontab 반영하기
function saveCronJob()
{
	 //유효성 체크 하기
	if(validCheckForSave())
	{
//	 	CronJob 백업 후 성공하면 다음 CronJob 수정하기
		var backupFlag = backupCrontab();

//	 	CronJob 백업 했을때 실행
		if(backupFlag)
		{
			//서버정보 form(form[name=searchFormCronJob]) + 신규 저장할 Crontab 정보(divResultList2안의 dataTable 테이블ID)
			var paramData = $(".forBackupCrontabFormClass").serializeObject() ;

			//console.log(paramData);


			//잠시주석
			$.ajaxSetup({ async:false }); // 비동기식을 동기식으로 변경 결과 올때까지 대기함
		    $.ajaxSetup({ cache:false }); // Cache 가 남아 있어 문제 발생 할수 있어서 Cache fasle 로 설정함.

			$.ajax({
			    type: 'post'
			    , url: 'saveCronJob'
			    , data: JSON.stringify(paramData)
			    , contentType: 'application/json'
			    , beforeSend: function(){
	                showProgress();
	            }
			    , success: function(e) {
					if(  e!="success" )
					{
						alert(e);
					}
			    }
			    , error: function(jqXHR, textStatus, errorThrown) {
					console.log("오류?");
					console.log(jqXHR);
					console.log(textStatus);
			    	}
			    , complete: function() {
			    	hideProgress();
			    	//조회 호출
			    	findCronJob();
			    	}
				});

			$.ajaxSetup({ async:true }); // 비동기식 원래 값으로 초기화
		}
	}
	

}


//서버에 접속하여 job 실행(비동기)
function runCommand()
{
	var runConfirm = confirm("입력하신 프로세스를 지금 실행하시겠습니까?");

	if(runConfirm)
	{
		//서버정보 
		var paramData = {
			HOST_NAME : $("#HOST_NAME").val(),
			REAL_IP : $("#REAL_IP").val(),
			USER_ID    : $("#USER_ID").val(),
			USER_PW : $("#USER_PW").val(),
			RUN_COMMAND : $("#RUN_COMMAND").val(),
			RUN_MODE : $("#RUN_MODE").val()
		};

		console.log(paramData);


		$.ajax({
		    type: 'post'
		    , url: 'runJob'
		    , data: JSON.stringify(paramData)
		    , contentType: 'application/json'
		    , success: function(e) {
				if(  e!="success" )
				{
					alert(e);
				}
		    }
		    , error: function(jqXHR, textStatus, errorThrown) {
				console.log("오류?");
				console.log(jqXHR);
				console.log(textStatus);
		    	}
			});
		
	}
	

}
