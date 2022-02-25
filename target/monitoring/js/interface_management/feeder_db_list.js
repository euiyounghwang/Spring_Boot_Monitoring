/**
 * sql 관리
 */

var table;
var checkedIndices =[];
var excel = false;

$( document ).ready(function() {	
	table = $('#center .grid_table').DataTable( {
//        "processing": true,
        "serverSide": true,
        "paging": true,
        columns : [
        	{
	        	data : '',
	            render: function (data, type, full, meta) {
		                return '<input type="checkbox" onclick="checkIndex('+meta.row+');">';
	            }
			},
			{data : 'id',
				render: function (data, type, full, meta) {
					var rtnval;
					if($('#feededitYN').val()==='Y')
						rtnval = '<a href="#" title="수정" onclick="setInputbox(&quot;'+meta.row+'&quot;); return false;">'+data+'</a>';
					else
						rtnval = data;
					return rtnval;
				}
			},
			{data : 'desc'},
			{data : 'type'},
			{data : 'feed_sub_sysid'},
			{data : 'feed_db_type'},
			{data : 'feed_db_driver'},
			{data : 'feed_db_url'},
			{data : 'feed_db_user'},
			{data : 'feed_db_pwd'},
			{data : 'feed_db_schema'},
        	{
	        	data : 'recordsTotalIdTypes',
	            render: function (data, type, full, meta) {
	            	if(data!==null && data !=='')
		                return '<input type="hidden" id="recordsTotalIdTypes" value="'+data+'">';
	            	else
	            		return'';
	            }
			}
			
		],
		searching:true,
		language: {
		    search: "System ID 검색 : ",
		    lengthMenu: "결과 _MENU_ 개 씩 보기",
		    zeroRecords: "검색 결과가 없습니다.",
		    info : '조회현황 : _TOTAL_ 건'
		  },
		info:true,
		columnDefs:[
			{targets:[0,2,3,4,5,6,7,8,9,10,11], orderable:false},
			{targets:[0], className:'dt-body-center'},
		],
		order:[1,'asc'],
		"ajax": {
			type : 'POST',
            url: "getXmlFileInfo",
            contentType : 'application/json',
            dataType:'json',
            beforeSend: function(){
                showProgress();
            },
            data: function ( d ) {
            	$('#check-all-menu').prop('checked', false);
            	checkedIndices = [];
            	deleteBtnStatus();
            	if(excel){
            		d['start']  = 0;
            		d['length'] = table.settings()[0]._iRecordsDisplay;
            	} else {
            		if(d['draw']!=1)
            			d['start'] = table.settings()[0]._iDisplayStart;
            	}
//            	d['order'][0].column = 1;
            	return JSON.stringify(d);
            }
        },
        "drawCallback": function( settings ) {
            hideProgress();
        },
        lengthMenu: [[10, 25, -1], [10, 25, "전체"]],
//        lengthChange: false,
        dom: 'lft<"bottom"i>p',
        buttons: []
    });
	
	$('#btn_refresh').click(function(){
		refreshMenu();
	});
	
	$('#btn_delete').click(function(){
		if(checkedIndices.length>0){
			if(confirm('선택한 Feeder System을 삭제하시겠습니까?')){
				deleteSystem();
			}
		} else {
			alert('삭제할 Feeder System을 선택하세요.');
		}
	});
		
	$('#check-all-menu').on('click', function(){
	    var rows = table.rows({ 'search': 'applied' }).nodes();
	    $('input[type="checkbox"]', rows).prop('checked', this.checked);
	    
	    checkedIndices = [];
	    if(this.checked){
	    	for(var i=0; i<rows.length; i++){
	    		checkedIndices[i] = i;
	    	} 
	    }
	    deleteBtnStatus();
	});
});

function deleteSystem(){
	var params ={};
	params['id_type'] = '';
	
	for(var i=0; i <checkedIndices.length; i++){
		params['id_type'] += table.rows().data()[checkedIndices[i]]['id'] + "|" + table.rows().data()[checkedIndices[i]]['type'] + ",";
	}
	params['id_type'] = params['id_type'].substr(0, params['id_type'].length-1);
	$.ajax({
		type : 'POST',
        url:'setXmlFileDelete',
        data : JSON.stringify(params),
        contentType: 'application/json',
        success:function(result){
        	alert('Feeder System 삭제를 완료했습니다.');
        	refreshMenu();
        },
        error : function(e){ 
        	alert('Feeder System 삭제 중 에러가 발생했습니다.');
        }
     });
}



function setInputbox(index){
	var row = table.rows().data()[index];
	$('.write_box :input').val(''); 
	
	Object.keys(row).forEach(function(key) {
		if(row[key]!=='' && row[key]!==undefined && row[key]!==null){
			var id = key;
			console.log(id+":"+row[key]);
			$('#'+id).val(row[key]);
		}
	});
	$('#id').prop('readonly', true);
	$('#type').prop('readonly', true);
	openInsertDiv();
}

function closeInsertDiv(){
	$('#id').prop('readonly', false);
	$('#type').prop('readonly', false);
	$('.write_box :input').val(''); 
	$('#data_insert').hide();
}

function openInsertDiv(){
	$('#data_insert').show();
}

function refreshMenu(){
	$('input[type="search"]').val('').keyup();
	$('#check-all-menu').prop('checked', false);
	table.ajax.reload();
}

function checkIndex(index){
	if(checkedIndices.indexOf(index)>-1){
		var removeIdx = checkedIndices.indexOf(index);
		checkedIndices.splice(removeIdx, 1);
	}else{
		checkedIndices[checkedIndices.length] = index;
	}
	//console.log('checkedIndices:'+checkedIndices);
	deleteBtnStatus();
}

function deleteBtnStatus(){
	if(checkedIndices.length>0){
		$('#btn_delete').prop('disabled', false);
	} else {
		$('#btn_delete').prop('disabled', true);
	}
}


function insertSystemValidation(url){

	//파라메타 공백제거
	$("#id").val($("#id").val().replace(/(\s*)/g,""));
	$("#type").val($("#type").val().replace(/(\s*)/g,""));
	$("#feed_sub_sysid").val($("#feed_sub_sysid").val().replace(/(\s*)/g,""));
	$("#feed_db_type").val($("#feed_db_type").val().replace(/(\s*)/g,""));
	$("#feed_db_driver").val($("#feed_db_driver").val().replace(/(\s*)/g,""));
	$("#feed_db_url").val($("#feed_db_url").val().replace(/(\s*)/g,""));
	$("#feed_db_user").val($("#feed_db_user").val().replace(/(\s*)/g,""));
	$("#feed_db_pwd").val($("#feed_db_pwd").val().replace(/(\s*)/g,""));
	$("#feed_db_schema").val($("#feed_db_schema").val().replace(/(\s*)/g,""));
	//파라메타 앞뒤공백만 제거
	$("#desc").val($("#desc").val().trim());
	
	
	var params = $('.write_box :input').serializeObject();
	Object.keys(params).forEach(function(key) {
		if(params[key]!='' && params[key]!=undefined && params[key]!=null){
			var id = key.toLowerCase();
			$('#'+id).val(params[key].trim());
		}
	});
	params = $('.write_box :input').serializeObject();
	
	if(params['id']=='' 
		|| params['desc']==''
				|| params['type']==''
					|| params['feed_sub_sysid']==''
						|| params['feed_db_type']==''
							|| params['feed_db_driver']==''
								|| params['feed_db_url']==''
									|| params['feed_db_user']==''
										|| params['feed_db_pwd']==''){
		alert('필수항목을 모두 입력해주세요.');
		return false;
	} else if(url.indexOf('Insert')>-1 && $('#recordsTotalIdTypes').val().indexOf(params['id']+'_'+params['type'])>-1){
		alert('입력한 System ID와 Type이 이미 존재합니다.\nID 와 Type의 조합이 기존항목과 중복되지 않도록 입력해주세요.');
		return false;		
	}else {
		return true;
	}
	
}

function insertSystem(){
	var url = 'setXmlFileInsert';
	var string = 'Feeder DB자원정보를 등록 하시겠습니까?';
	
	if($('#id').prop('readonly')){
		url = 'setXmlFileUpdate';
		string = 'Feeder DB자원정보를 수정 하시겠습니까?';
	}
	
	
	var validate = insertSystemValidation(url);
	
	if(validate){
		if(confirm(string)){
		var params = $('.write_box :input').serializeObject();
		$.ajax({
			type : 'POST',
	        url: url,
	        data : JSON.stringify(params).replace(/"\s+|\s+"/g,'"'),
	        contentType: 'application/json',
	        success:function(result){
	        	if(result.value=="true"){
		        	alert(result.msg);
		        	closeInsertDiv();
		        	refreshMenu();
	        	} else {
	        		alert(result.msg);
	        	}
	        },
	        error : function(e){ 
	        	alert('Feeder DB자원정보 추가를 실패했습니다.');
	        }
	     });
		}
	}
	
}



$('#btn_link').click(function(){
	var newWindow = window.open("about:blank");
	newWindow.location.href = 'loadPage/interface_management/pop_xml_view';

});

var fileTarget = $('.filebox .upload-hidden');

fileTarget.on('change', function(){
    if(window.FileReader){
        var filename = $(this)[0].files[0].name;
    } else {
        var filename = $(this).val().split('/').pop().split('\\').pop();
    }

    $(this).siblings('.upload-name').val(filename);
});

$('#btn_upload').click(function(){
	if(confirm('파일 업로드 하시겠습니까?')){
	showProgress();
	var option = {
      type: "POST"
      ,url : "filexmlUpload"
      ,dataType : "json"
      ,success : function (result){
    	  hideProgress();
    	  if(result.msg=='success'){
    		  $('.upload-name').val('');
    		  alert('파일업로드가 완료되었습니다.');
    	  }else{
    		  $('.upload-name').val('');
    		  alert(result.msg);
    		  
    	  }	      
	  }, error : function(error) {
          alert("파일 업로드에 실패하였습니다.");
          console.log(error);
          console.log(error.status);
	  }
   };
   $("#fileForm").ajaxSubmit(option);
	}
});


/*파일 다운로드
 * */
$('#btn_filedownload').click(function(){
	if(confirm('다운로드 하시겠습니까?')){
		document.fileDownForm.action= '/monitoring/fileDownload';
		document.fileDownForm.target = 'fileDown';
		document.fileDownForm.submit();
	}
});