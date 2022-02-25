/*==============================================================================
 *Copyright(c) 2018 Poscoict
 *
 *@ProcessChain : S39
 *
 *@File			: AesUtil.java
 *
 *@FileDesc		: AES 128/256 암복호화 유틸리티 클래스
 *
 *Open Issues	: 해당 사항 없음
 *
 *Change history  
 *@LastModifyDate : 20180601
 *@LastModifier   : 서정진
 *@LastVersion    : 1.0
 *
 * Date           Ver.     Name      Description
 *-----------------------------------------------------------------------------
 * 2018-06-01    V1.0	   서정진	 최초 생성
 * 2018-06-11    V1.1	   서정진	 스페로우 점검으로 인한 랜덤해시 적용
==============================================================================*/

package com.test.test.spring.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class AesUtil {
	private static Logger logger = Logger.getLogger(Base64Utils.class);

	// 자바 기본은 AES128만 지원
	private static final int AES_128BIT_KEY_LENGTH = 16;
	
	// AES256을 지원하기 위해서는 Unlimited Strength Java(TM) Cryptography Extension Policy Files 설치 필요
	private static final int AES_256BIT_KEY_LENGTH = 32;
	
	private static SecretKeySpec secretKey;
	
	
	public static void getInstance(String strKey, byte[] bSalt){
		if(secretKey==null){
			AesUtil.setKey(strKey,bSalt);
		}		
	}
	
	/**
	 * AES 암복호화에 사용할 보안키를 생성한다.
	 * 
	 * @param strKey : AES 암호키 평문 스트링 (128비트 암호화는 16자 스트링, 256비트 암호화는 32자 스트링)
	 */
	public static void setKey(String strKey, byte[] bSalt) {
		try {
			byte[] key = strKey.getBytes("UTF-8");
			int keyLength = strKey.length();

			/* In-Memory 데이터는 랜덤 해쉬 처리, API 데이터는 고정 해쉬 처리
			SecureRandom sr = new SecureRandom();
			byte[] bSalt = new byte[8];
			sr.nextBytes(bSalt);
			*/

			MessageDigest sha = MessageDigest.getInstance("SHA-512");
			sha.reset();
			sha.update(bSalt);
			
			key = sha.digest(key);
			
			if(keyLength == AES_128BIT_KEY_LENGTH) key = Arrays.copyOf(key, AES_128BIT_KEY_LENGTH);	
			else if(keyLength == AES_256BIT_KEY_LENGTH) key = Arrays.copyOf(key, AES_256BIT_KEY_LENGTH);
			else {
				logger.error("Error key length: " + keyLength);
				return;
			}
			
			secretKey = new SecretKeySpec(key, "AES");
			
		} catch (Exception e) {
			logger.error("Error set key: " + e.toString());
		}
	}

	/**
	 * 일반 스트링을 AES로 암호화
	 * 
	 * @param strToEncrypt : 암호화 할 스트링
	 * @return String : 암호화 된 스트링
	 */
	public static String encrypt(String strToEncrypt) {
		try {
			logger.info(secretKey.toString());
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return URLEncoder.encode(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))), "UTF-8");
		} catch (Exception e) {
			logger.error("Error while encrypting: " + e.toString());
		}
		
		return null;
	}

	/**
	 * AES로 암호화 된 스트링을 복호화
	 * 
	 * @param strToDecrypt : 복호화 할 스트링
	 * @return String : 복호화 된 스트링
	 */
	public static String decrypt(String strToDecrypt) {
		try {
			logger.info(secretKey.toString());
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING"); // AES/ECB/PKCS5PADDING | AES/CBC/PKCS5PADDING 
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.decodeBase64(URLDecoder.decode(strToDecrypt, "UTF-8"))));
		} catch (Exception e) {
			logger.error("Error while decrypting: " + e.toString());
		}
		
		return null;
	}

	/**
	 * AES 암복호화 테스트 함수
	 * 
	 * @param strToEncrypt : 암호화 할 스트링
	 * @param strKey : AES 암호키 스트링 (128비트 암호화는 16자 스트링, 256비트 암호화는 32자 스트링)
	 */
	public static void forTest(String strToEncrypt, String strKey, byte[] bSalt) {
		logger.info("Source String: " + strToEncrypt);
		
		// 테스트를 위한 보안키 생성
		// ※ 시스템에 적용 시 보안키를 매번 생성하지 마시오!
		AesUtil.setKey(strKey, bSalt);
		
		String encryptedString = AesUtil.encrypt(strToEncrypt.trim());		
		logger.info("Encrypted: " + encryptedString);
		
		String decryptedString = AesUtil.decrypt(encryptedString.trim());
		logger.info("Decrypted : " + decryptedString);
	}
}
