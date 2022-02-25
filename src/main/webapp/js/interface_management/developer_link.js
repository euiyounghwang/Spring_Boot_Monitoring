/**
 * 관리자화면 인증번호 관리
 */

function closePasswdDiv(){
	$('#data_insert').hide();
	$('#new_passwd').val('');
	$('#new_passwd_re').val('');
}

function validateEncryptedForm(){

	if($('#new_passwd').val()=='' || $('#new_passwd_re').val()==''){
		alert('새 비밀번호를 입력하세요.');
		return;
	}

	if($('#new_passwd').val()!=$('#new_passwd_re').val()){
		alert('새 비밀번호와 비밀번호 확인이 일치하지 않습니다.');
		return;
	}
	if(!confirm('비밀번호를 변경 하시겠습니까?')){
		return false;
	}
	var passNew = $('#new_passwd').val();
	
    try {
        var rsaPublicKeyModulus = $("#rsaPublicKeyModulus").val();
        var rsaPublicKeyExponent = $("#rsaPublicKeyExponent").val();
        checkPass(passNew, rsaPublicKeyModulus, rsaPublicKeyExponent);
    } catch(err) {
        alert(err);
    }
    return;
}

//devLogin 비밀번호 확인
function checkPass(passNew, rsaPublicKeyModulus, rsaPublicKeyExponent){
	var rsa = new RSAKey();
    rsa.setPublic(rsaPublicKeyModulus, rsaPublicKeyExponent);
    changePass(rsa.encrypt(passNew));
 
}

//devLogin 비밀번호 변경
function changePass(securedPassword){	
	$.ajax({
		type : 'POST',
		url : "updateCertPass",     
        data : JSON.stringify({
	    	'securedPassword':securedPassword
	    }),
        contentType: 'application/json',
        async : true,
        success:function(result){
        	if(result=='success'){
        		alert('비밀번호 변경이 완료되었습니다.');
        		closePasswdDiv();
        	}else {
        		alert('ERROR!');
        	}
        },
        error : function(e){ 
        	
        }
     });
}
