package com.test.test.spring.ssh;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.javasrc.jolt.component.linux.model.FileSystem;
import com.sshtools.j2ssh.SshException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class ShellCommand {

	protected Logger logger;

	/**
	 * 서버에 연결하는 호스트 이름
	 */
	private String hostname;

	/**
	 * 서버에 연결하는 호스트 IP 주소
	 */
	private String realip;

	/**
	 * 해당 서버에 있는 사용자 이름
	 */
	private String username;

	/**
	 * 해당 서버에 있는 사용자의 암호
	 */
	private String password;

	/**
	 *
	 * 서버 연결 커넥션 객체
	 *
	 */

	private Connection connection;

	/**
	 *
	 * SSHAgent 객체 생성
	 *
	 *
	 *
	 * @param hostname
	 *
	 * @param username
	 *
	 * @param password
	 *
	 */

	public ShellCommand(String hostname, String realip ,String username, String password)
	{

		this.hostname = hostname;

		this.realip = realip;

		this.username = username;

		this.password = password;
		logger = LoggerFactory.getLogger(getClass());

	}

	public ShellCommand() {
		logger = LoggerFactory.getLogger(getClass());
	}

	/**
	 *
	 * 서버에 연결
	 *
	 *
	 *
	 * @return 연결 성공 시 true, 그 외에는 false
	 *
	 */

	public boolean connect() throws Exception
	{
		boolean result = false;

		try
		{
			// 서버 연결 객체 생성
			connection = new Connection(realip);
			connection.connect();
			// 인증
			result = connection.authenticateWithPassword(username,password);
			logger.info("연결 성공 여부 : " +realip + "," +  result);
			return result;
		}
		catch (Exception e)
		{
			logger.error("호스트명 "+ hostname +"에 연결하지 못했습니다. : " + e.getMessage());
//			throw new SshException("호스트 "+ hostname +"에 연결하는 동안 예외가 발생하였습니다 : " + e.getMessage());
		}
		return result;
	}

	/**
	 *
	 * 지정된 명령을 실행하고 서버에서 응답을 반환한다.
	 *
	 *
	 *
	 * @param command
	 *            command 실행
	 *
	 * @return 서버에서 반환하는 응답 (or null)
	 *
	 */

	public String executeCommand(String command) throws SshException
	{
		try
		{

			// 세션 생성
			Session session = connection.openSession();
			// command 실행
			session.execCommand(command);
			// 결과 확인
			StringBuilder sb = new StringBuilder();
			InputStream stdout = new StreamGobbler(session.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
//			BufferedReader br = new BufferedReader(new InputStreamReader(stdout,"UTF-8"));

			//원본
			String line = br.readLine();

			while (line != null)
			{
				sb.append(line + "\n");
				line = br.readLine();
				//System.out.println("while 안에서" + line);
			}

			logger.info("###### executeCommand 결과반환 ###### ");
			logger.info(sb.toString());
			// DEBUG : exit 코드 덤프
			logger.info("ExitCode : " + session.getExitStatus());
			// 세션 종료
			session.close();
			// 호출자에게 결과를 반환
			return sb.toString();

		}
		catch (Exception e)
		{
			logger.error("다음 명령을 실행하는 동안 예외가 발생하였습니다. : " + command + ". Exception = " + e.getMessage());
			throw new SshException("다음 명령을 실행하는 동안 예외가 발생하였습니다. : " + command + ". Exception = " + e.getMessage());
		}

	}

	/**
	 *
	 * 서버에서 로그 아웃
	 *
	 * @throws SSHException
	 *
	 */

	public void logout() throws SshException
	{
		try
		{
			connection.close();
		}
		catch (Exception e)
		{
			logger.error("SSH 연결을 종료하는 동안 예외가 발생했습니다. : " + e.getMessage());
			throw new SshException("SSH 연결을 종료하는 동안 예외가 발생했습니다. : " + e.getMessage());
		}

	}

	/**
	 *
	 * 기본 인증이 완료되면 true를 반환하고 그렇지 않을 경우 false
	 *
	 * @return
	 *
	 */

	public boolean isAuthenticationComplete()
	{
		return connection.isAuthenticationComplete();
	}
}
