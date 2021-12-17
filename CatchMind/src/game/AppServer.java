package game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.framework.TcpApplication;

/**
 * 설정데이터를 읽어와서 구현
 * @author bitcamp
 *
 */
public class AppServer extends TcpApplication {
	/*
	 * 어플리케이션 초기화
	 */
	@Override
	public void init() {
		super.init();
	}
	/*
	 * 어플리케이션 실행
	 */
	@Override
	public void start() {
		System.out.println(TcpApplication.timeStamp());
		System.out.println("TCP/IP 서버프로그램을 시작합니다.");
		System.out.println("SERVER START >>>");
		
		ServerSocket server = null;
		Socket cSocket 		= null;
//		Thread th 			= null;
		
		try {
			// 1. 서버소켓 생성
			server = new ServerSocket(TcpApplication.PORT);
			
			// 2. 클라이언트와 연결된 소켓 관리!
			while(true) {
				cSocket = server.accept(); // 서버가 받아온 클라이언트의 소켓
				System.out.println(cSocket + "(" + TcpApplication.timeStamp() + ")"); //접속자가 들어왔을 때 시간 찍어주기
				System.out.println("클라이언트 접속 대기중...");
				
				/*  접속한 클라이언트 송수신을 담당할 스레드를 생성하여 
				 *  			소켓(cSocket)을 전달한다.
				 *  접속자 수 만큼 스레드가 생성된다.
				 */
				Thread th = new Thread(new TcpServerHandler(cSocket));
				
				th.start();
			}
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}

}
