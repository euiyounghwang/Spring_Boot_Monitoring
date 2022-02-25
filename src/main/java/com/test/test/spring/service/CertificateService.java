package com.test.test.spring.service;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {
	@Autowired
	CodeService codeService;
	
	
	//private static Logger logger = Logger.getLogger(CertificateService.class);
	
	
	public String updateCertPass(HttpServletRequest request, Map<String, Object> pass) {
		String msg = "";
		HttpSession session = request.getSession();
		String securedPassword = (String) pass.get("securedPassword");
		PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
		
		if (privateKey == null || securedPassword==null) {
			return "common/devLogin_certification";
		}
		
		String password = "";
		try {
			password = decryptRsa(privateKey, securedPassword);
		} catch (Exception e) {
			msg = "fail";
		}
		
		password = BCrypt.hashpw(password, BCrypt.gensalt());
		
		//update sql
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("cd_tp_meaning", password);
		params.put("cd_v_explain", "");
		params.put("search_sort_seq", "");
		params.put("fk_cd_tp", "064");
		params.put("cd_tp", "064_0000");
		params.put("company_code", "00");
		codeService.updateCodeSimple(params);
		msg = "success";

		return msg;
	}
	

	
	public String certificateLogin(HttpServletRequest request, Map<String, Object> pass) {
		String msg = "";
		HttpSession session = request.getSession();
		String securedPassword = (String) pass.get("securedPassword");
		PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
		
		if (privateKey == null || securedPassword==null) {
			return "common/devLogin_certification";
		}
		
		String password = "";
		try {
			password = decryptRsa(privateKey, securedPassword);
		} catch (Exception e) {
			
		}
		
		Map<String, String> params = new HashMap<String, String>(); 
		params.put("fk_cd_tp", "064");
		params.put("cd_tp", "064_0000");
		params.put("company_code", "00");
		String real_pass = null;
		List<Map<String, String>> rowset = codeService.selectCodeInfo(params);
		if(rowset.size()>0) {
			for(Map<String, String> row : rowset) {
				real_pass = row.get("CD_TP_MEANING")== null ? ""
						: (String) row.get("CD_TP_MEANING");
			}
		}
		
		boolean matchPassword = BCrypt.checkpw(password, real_pass);

		if (matchPassword) {
			msg = "success";
			session.setAttribute("certification", "Y");
		}else {
			msg = "fail";
			session.setAttribute("certification", "N");
		}
		
		return msg;
	}
	
	public static String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }
	
	/**
     * 16진 문자열을 byte 배열로 변환한다.
     * 
     * @param hex
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }
 
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
	
}

