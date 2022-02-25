/**
 * 서버관리>미리보기
 */
//http url을 호출하여 가동계 xml데이타를 조회한다. 
function getHTTPXMLData(param){
	var params = {};
	params['url'] = param;
	
	 $.ajax({
	 type : 'POST',
	         url:'getConfigData',
	         data : JSON.stringify(params),
	         contentType: 'application/json',
	         dataType:'text',
             beforeSend: function(){
	        	 $('.selected_info').text('');
            	 $('.rd_xmlviewer XMP').text('');
            	 showProgress();
             },
	         success:function(data){
	        	 $('.selected_info').text('URL:'+param)
	        	 $('.rd_xmlviewer XMP').text(data);//XMP AREA
	        },
	        error : function(e){ 
	        },
	        complete : function() {
	        	hideProgress();    
	        }
     });
	
}



/**
 * XML 파일 열기
 * pop_xml_view _blank 팝업에서 호출 할 경우 ajax 호출 url은 절대경로로 입력한다.
 */
//xml 파일경로에서 파일 오픈 후 내용을 받아온다.
function getFileXMLData(param){
	var params = {};
	params['file_path'] = param;
	
	 $.ajax({
	 type : 'POST',
	         url:'/monitoring/getConfigDataAsXml',
	         data : JSON.stringify(params),
	         contentType: 'application/json',
	         dataType:'text',
             beforeSend: function(){
	        	 $('.selected_info').text('');
            	 $('.rd_xmlviewer XMP').text('');
             },
	         success:function(data){
	        	 $('.selected_info').text('FILE 경로 : '+param)
	        	 $('.rd_xmlviewer XMP').text(data);//XMP AREA
	        },
	        error : function(e){ 
	        },
	        complete : function() {
	        }
     });
	
}
