package com.test.test.spring.ssh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.test.test.spring.constants.WebConstants;
import com.test.test.spring.util.ESUtil;

public class FileMgr {

	//최상위 폴더경로
//	public static String rootFilePath = ".\\CrontabBackup";
	public static String rootFilePath = "";

	//오늘날짜 기준으로 만들어진 폴더명
	public String yyyMMddFilePath ="";

	//오늘 시간 기준으로 만들어진 파일명 + 뒤쪽 파일명 확장자
	public String backupCrontabFileName ="";

	//뒤쪽 백업 파일명과 확장자
	public static String backFileNameAndExtension = "_backupCrontab.txt";

	//뒤쪽 신규 파일명과 확장자
	public static String saveFileNameAndExtension = "_saveCrontab.txt";

	//백업 폴더 경로 /WAS/DATA/ES/Monitoring_NAS/
	 //현재 호스트(10.132.12.75)만  이 폴더 있음.
	public static String backupCrontabPath = WebConstants.BACKUP_CRONTAB_PATH;

	//실제 Crontab 폴더 경로 /var/spool/cron/
	//현재 호스트(10.132.12.75)를 제외한 나머지 다른 호스트에 설정 필요 김종철 과장님에게 인수인계(2017.12.28) 전달했음.
	public static String saveCrontabPath = WebConstants.SAVE_CRONTAB_PATH;

	/**
	 * 서버에 연결하는 호스트 이름
	 */
	public String hostname;

	/**
	 * 서버에 연결하는 호스트 IP 주소
	 */
	public String realip;

	/**
	 * 해당 서버에 있는 사용자 이름/신규 crontab 저장될 파일명 (파라미터로 받은 username 과 동일)
	 */
	public String username;

	/**
	 * 해당 서버에 있는 사용자의 암호
	 */
	public String password;

	/**
	 * 작업구분
	 */
	public String eventType;


	public File saveFile;

	protected Logger logger;

	public FileMgr(String hostname, String realip ,String username, String password,String eventType) {
		logger = LoggerFactory.getLogger(getClass());

		this.hostname = hostname;
		this.realip = realip;
		this.username = username;
		this.password = password;
		this.eventType = eventType;
	}

	public FileMgr()
	{

	}

	/**
	 * WAS 서버에 백업 Crontab 파일 생성함
	 */
	public boolean createBackupCrontabFile(Map<String, Object> params)
	{
		boolean createFileFlag = false;

		//날짜로 파일명 만들기
		long time = System.currentTimeMillis();
		SimpleDateFormat yyyMMddFormat = new SimpleDateFormat("yyyyMMdd");
		String yyyMMdd = yyyMMddFormat.format(new Date(time));
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
		String crontabFileName = dayTime.format(new Date(time));
		logger.info("crontabFileName:"+crontabFileName);

		BufferedWriter out = null;
		FileWriter fw = null;

		try {

//			String  localFilePath =this.rootFilePath +"\\"+yyyMMdd;
			crontabFileName = crontabFileName + this.backFileNameAndExtension;
			String fileFullPath = crontabFileName;
			fw = new FileWriter(crontabFileName,true);
			out = new BufferedWriter(fw);

			//crontab 읽어온 내용을 반복문으로 넣기
			String oneLine = ""; 
			//데이터 읽어서 Crontab 내용 만들기
			@SuppressWarnings("unchecked")
			ArrayList<String>  rawBatchTimeArr = (ArrayList<String>)params.get("RAW_BATCH_TIME");
			@SuppressWarnings("unchecked")
			ArrayList<String>  rawProcessArr = (ArrayList<String>)params.get("RAW_PROCESS");

            int idx = 0 ;

            //null이 아닐때
            if(null != rawBatchTimeArr)
            {
                for(String rawBatchTime : rawBatchTimeArr)
                {
                	oneLine = rawBatchTime.trim() + " " + rawProcessArr.get(idx).trim();
        			out.append(oneLine);
        			out.newLine();
        			idx++;
                }

    			out.flush();
    			out.close();
    			fw.close();

    			//오늘날짜 기준으로 만들어진 폴더명
    			setYyyMMddFilePath(yyyMMdd);

    			//오늘 시간 기준으로 만들어진 파일명
    			setBackupCrontabFileName(crontabFileName);

    			//파일 존재 체크
    			File file = new File(crontabFileName);

    			//TEST 중
    			saveFile = new File(crontabFileName);
    			createFileFlag =   file.exists(); //파일 존재 체크
    			rootFilePath = file.getAbsolutePath();

				logger.info("createBackupCrontabFile 절대경로:" + file.getAbsolutePath());
				logger.info("createBackupCrontabFile 상대경로:" + file.getCanonicalPath());
				logger.info("createBackupCrontabFile 파일존재 체크:" + createFileFlag);

            }
			////////////////////////////////////////////////////////////////
		} catch (IOException e) {
			logger.error(e.getMessage(),e); // 에러가 있다면 메시지 출력
//			System.err.println(e); // 에러가 있다면 메시지 출력
		}
		finally{
			try{
				out.close();
				fw.close();
			}
			catch(Exception e)
			{
				logger.error(e.getMessage(),e);
				 e.printStackTrace();
			}

		}
		return createFileFlag;
	}

	/**
	 * WAS 서버에 신규 Crontab 파일 생성함
	 */
	public boolean createSaveCrontabFile(Map<String, Object> params)
	{
		boolean createFileFlag = false;

		//날짜로 하위 폴더 만들기
		long time = System.currentTimeMillis();
		SimpleDateFormat yyyMMddFormat = new SimpleDateFormat("yyyyMMdd");
		String yyyMMdd = yyyMMddFormat.format(new Date(time));
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
		String crontabFileName = dayTime.format(new Date(time));
		logger.info("crontabFileName:"+crontabFileName);

		BufferedWriter out = null;
		FileWriter fw = null;

		try {
//			String  localFilePath =this.rootFilePath +"\\"+yyyMMdd;
			crontabFileName = crontabFileName + this.saveFileNameAndExtension;
			String fileFullPath = crontabFileName;
			fw = new FileWriter(crontabFileName,true);
			out = new BufferedWriter(fw);

			//crontab 읽어온 내용을 반복문으로 넣기
			String oneLine = "";

			//데이터 읽어서 Crontab 내용 만들기
			@SuppressWarnings("unchecked")
			ArrayList<String>  batchTimeArr = (ArrayList<String>)params.get("BATCH_TIME");
			@SuppressWarnings("unchecked")
			ArrayList<String>  processArr = (ArrayList<String>)params.get("PROCESS");


			int idx = 0 ;
			int arrSize = 0;

			//null이 아닐때
			if(null != batchTimeArr)
			{
				for(String batchTime : batchTimeArr)
				{
					
					oneLine = batchTime.trim() + " " + processArr.get(idx).trim();
					out.append(oneLine);
					out.newLine();
					idx++;
				}

				out.flush();
				out.close();
				fw.close();

				//오늘날짜 기준으로 만들어진 폴더명
				setYyyMMddFilePath(yyyMMdd);

				//오늘 시간 기준으로 만들어진 파일명
				setBackupCrontabFileName(crontabFileName);

				//파일 존재 체크
				File file = new File(fileFullPath);

    			//TEST 중
    			saveFile = new File(crontabFileName);

				createFileFlag =   file.exists(); //파일 존재 체크

				rootFilePath = file.getAbsolutePath();

				logger.info("createSaveCrontabFile 절대경로:" + file.getAbsolutePath());
				logger.info("createSaveCrontabFile 상대경로:" + file.getCanonicalPath());
				logger.info("createSaveCrontabFile 파일존재 체크:" + createFileFlag);

			}
			////////////////////////////////////////////////////////////////
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			logger.error(e.getMessage(),e);
//			System.exit(1);
		}
		finally{
			try{
				out.close();
				fw.close();
			}
			catch(Exception e)
			{
				logger.error(e.getMessage(),e);
				e.printStackTrace();
			}

		}
		return createFileFlag;
	}

	/**
	 * Connector 서버 crontab 파일 백업
	 */
	public  static  String backupCrontab(String hostname, String realip ,String username, String password,String eventType ,Map<String, Object> params)
	{
		String rtnmsg = "";
		FileMgr fileMgr = new FileMgr(hostname, realip ,username, password,eventType);

		//WAS 서버의 폴더에 임시 파일 생성
		boolean fileExistFlag = fileMgr.createBackupCrontabFile(params);

		//파일존재 할때
		if(fileExistFlag)
		{
			//Connector 서버에 파일 전송
			boolean sendFileFlag  = fileMgr.putFileFTP(params);

			//WAS 서버의 폴더의 파일 삭제
			if(sendFileFlag)
			{
				//파일전송 성공일때만 localFile 삭제하기 추후 쌓여 있으면 자원 낭비
				fileMgr.deleteFile();				
				rtnmsg = "success";
			}
		}
		else
		{
			rtnmsg="backupCrontab >>> CronJob 백업하지 못 했습니다.";
		}
		
		return rtnmsg;
	}

	/**
	 * Connector 서버 crontab 파일 생성
	 */
	public  static String saveCrontab(String hostname, String realip ,String username, String password,String eventType ,Map<String, Object> params)
	{
		String rtnmsg = "";
		FileMgr fileMgr = new FileMgr(hostname, realip ,username, password, eventType );

		//WAS 서버의 폴더에 임시 파일 생성
		boolean fileExistFlag = fileMgr.createSaveCrontabFile(params);

		//파일존재 할때
		if(fileExistFlag)
		{
			//Connector 서버에 파일 전송
			boolean sendFileFlag  = fileMgr.putFileFTP(params);

			//WAS 서버의 폴더의 파일 삭제
			if(sendFileFlag)
			{
				//파일전송 성공일때만 localFile 삭제하기 추후 쌓여 있으면 자원 낭비
				fileMgr.deleteFile();
				rtnmsg= "success";
			}
		}
		else
		{
			rtnmsg= "saveCrontab >>>> CronJob 백업하지 못 했습니다.";
		}
		return rtnmsg;
	}

	/**
	 * local 파일 삭제
	 * @param path
	 * @return
	 */
	public boolean deleteFile() {
		//파일삭제 성공여부
		boolean deleteFileFlag = false;

		String filePath = this.rootFilePath + "\\" + yyyMMddFilePath;
		String fileFullPath = filePath + "\\" + backupCrontabFileName;

		File path = new File(filePath);

		if (!path.exists()) {
			logger.info("삭제하려는 파일의 상위 폴더 존재하지 않습니다.");
			return deleteFileFlag;
		}

		//파일명 있으면 삭제함
		if(!ESUtil.isEmpty(fileFullPath))
		{
			File file = new File(fileFullPath);

			logger.info("기본 파일경로:"+fileFullPath);

			try {
				logger.info("절대파일경로:"+file.getAbsolutePath());
				logger.info("상대파일경로:"+file.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logger.info("파일존재여부:"+file.exists());

			deleteFileFlag = file.delete();

			if(deleteFileFlag)
			{
				logger.info( fileFullPath+"의 파일을 성공적으로 삭제하였습니다.");
				return deleteFileFlag;
			}
			else
			{
				logger.info(fileFullPath+"의 파일 삭제에 실패 하였습니다.");
				return deleteFileFlag;
			}

		}
		return deleteFileFlag;
	}


	/**
	 * FTP 서버 접속해서 파일 넣기
	 */
	public boolean putFileFTP(Map<String, Object> params)
	{

		FTPClient ftpClient = null;
		FileInputStream fis = null;

		//파일전송 성공여부
		boolean putFileFlag = false;
		try
		{
			logger.info("putFileFTP start");

			ftpClient = new FTPClient();
			ftpClient.setControlEncoding("euc-kr");  // 한글파일명 때문에 디폴트 인코딩을 euc-kr로 합니다

			/**
			 * 접속 IP가 있을때만
			 */
			if(!ESUtil.isEmpty(realip))
			{
				//logger.info("putFileFTP realip="+realip);

				ftpClient.connect(realip);  // FTP에 접속합니다

				int reply = ftpClient.getReplyCode(); // 응답코드가 비정상이면 종료합니다
				//logger.info("putFileFTP reply="+reply);
				if (!FTPReply.isPositiveCompletion(reply))
				{
					ftpClient.disconnect();
					logger.info("FTP server refused connection.");
				}
				else
				{
					logger.info(ftpClient.getReplyString());  // 응답 메세지를 찍어봅시다

					//10000 으로 설정됨
					ftpClient.setSoTimeout( WebConstants.SO_TIMEOUT);  // 현재 커넥션 timeout을 millisecond 값으로 입력합니다
					ftpClient.login(username, password); // 로그인 유저명과 비밀번호를 입력 합니다

					ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
					ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
					//방화벽 제외 시키기 추가함 -> 정말 중요함.. 로컬에서는 되었는데 개발계에서는 올려보니 안돼서 추가함.. ㅠㅠ
					//하루 종일 별짓 다해봄. ㅠㅠ
					ftpClient.enterLocalPassiveMode();

					logger.info("FTP에 들어갈 파일 경로:"+rootFilePath);

					//backupCrontab 일때
					if("backupCrontab".equals(eventType))
					{
						ftpClient.changeWorkingDirectory(backupCrontabPath); //서버 이 폴더로 접속
//						fis = new FileInputStream(new File( rootFilePath )); //로컬일때
						//개발계에서는 이렇게 해야함.
						fis = new FileInputStream( saveFile );

						putFileFlag = ftpClient.storeFile(this.backupCrontabFileName, fis); //잠시주석
					}
					//saveCrontab 일때
					else
					{
						ftpClient.changeWorkingDirectory(saveCrontabPath); //서버 이 폴더로 접속
//						fis = new FileInputStream(rootFilePath); //로컬일때
						//개발계에서는 이렇게 해야함.
						fis = new FileInputStream(saveFile);
						//생성자에서 username 셋업되어 있음 ex) tomadmd
						putFileFlag = ftpClient.storeFile(this.username, fis); //잠시주석
					}

					int i=0;
					while((i=fis.read ())!=-1){
						logger.debug(""+(char)i);
					}
					ftpClient.logout();
				}

			}

		} catch (Exception e)
		{
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		finally
		{
			if (ftpClient != null && ftpClient.isConnected())
			{
				try {
					fis.close();
					ftpClient.disconnect();
				} catch (IOException ioe) {
					logger.error(ioe.getMessage(),ioe);
					ioe.printStackTrace();
				}
			}

		}
		return putFileFlag;
	}

	/**
	 *
	 * @param ctx
	 */
	public String readFtpFile(String printWorkingDirectory,String targetName)
	{
		int defaultPort = 21;
		String fileContents ="";

		try {
			// Opening the FTP connection and logging in
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect(realip, defaultPort);
			ftpClient.login(username, password);
			ftpClient.enterLocalPassiveMode();


			// if the file is not in root we need to change directory
			ftpClient.changeWorkingDirectory(printWorkingDirectory+"/");

			InputStream inputStream = ftpClient.retrieveFileStream(targetName);

			Scanner sc = new Scanner(inputStream);
			StringBuffer sb = new StringBuffer(1000);


			/** 이부분 추가
			 *
			 */
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while((line=br.readLine())!=null)
			{
				sb.append(line+"\n");
			}
			br.close();


			fileContents =  sb.toString();
			logger.debug(fileContents);

			// Closing the channels
			sc.close();
			inputStream.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return fileContents;
	}

	/**
	 *파일 저장
	 * @param ctx
	 * @throws IOException
	 */
	public String saveFtpFile(String printWorkingDirectory,String targetName,Map<String, Object> params) throws Exception
	{
		int defaultPort = 21;
		String fileContents ="";
		FTPClient ftpClient = new FTPClient();
		ByteArrayInputStream inputStream = null;

		try {
			// Opening the FTP connection and logging in
			ftpClient.connect(realip, defaultPort);
			ftpClient.login(username, password);
			ftpClient.enterLocalPassiveMode();
			fileContents = ((String[])params.get("FILE_CONTENTS"))[0];

			logger.info("파일 내용 출력:"+fileContents);
			// if the file is not in root we need to change directory
			ftpClient.changeWorkingDirectory(printWorkingDirectory+"/");
			inputStream = new ByteArrayInputStream(fileContents.getBytes("utf-8"));
			ftpClient.storeFile(targetName, inputStream);

			inputStream.close();
			ftpClient.disconnect();
		}

		catch (Exception e) {
			e.printStackTrace();
		}finally {
	        if (ftpClient.isConnected()) {
	        	ftpClient.abort();
	        	ftpClient.disconnect();
	        }
	        if (inputStream != null) {
	        	inputStream.close();
	        }
	    }
		return fileContents;
	}



	public String getYyyMMddFilePath() {
		return yyyMMddFilePath;
	}

	public void setYyyMMddFilePath(String yyyMMddFilePath) {
		this.yyyMMddFilePath = yyyMMddFilePath;
	}

	public String getBackupCrontabFileName() {
		return backupCrontabFileName;
	}

	public void setBackupCrontabFileName(String backupCrontabFileName) {
		this.backupCrontabFileName = backupCrontabFileName;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getRealip() {
		return realip;
	}

	public void setRealip(String realip) {
		this.realip = realip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public static String getSaveCrontabPath() {
		return saveCrontabPath;
	}

	public static void setSaveCrontabPath(String saveCrontabPath) {
		FileMgr.saveCrontabPath = saveCrontabPath;
	}

	public static String getSaveFileNameAndExtension() {
		return saveFileNameAndExtension;
	}

	public static void setSaveFileNameAndExtension(String saveFileNameAndExtension) {
		FileMgr.saveFileNameAndExtension = saveFileNameAndExtension;
	}

	public static String getBackupCrontabPath() {
		return backupCrontabPath;
	}

	public static void setBackupCrontabPath(String backupCrontabPath) {
		FileMgr.backupCrontabPath = backupCrontabPath;
	}

}
