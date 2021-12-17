package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.framework.TcpApplication;

/**
 * 
 * @author bitcamp
 *
 */
public class AppClient extends TcpApplication {
	
	public static PrintWriter pw = null;
	public static BufferedReader keyBoard = null;
	public static BufferedReader fromServer = null;
	public static Socket serverSocket = null;
	/**
	 * 배고파
	 */
	// 서버 ip 주소
	private static String sAddr = null;
	// 클라이언트 id
	private String id = null;
	

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
		System.out.println("TCP/IP 클라이언트 프로그램을 시작합니다.");
		System.out.println("CLIENT START >>>");
		
		try {
			// 1. 서버연결
			System.out.println(TcpApplication.timeStamp() + "서버에 연결중...");
			serverSocket = new Socket(TcpApplication.IP, TcpApplication.PORT); // 서버에 접속...서버에서 accept하는 것 //클라이언트는 서버쪽 ip와 ..다 가져와야한다
			sAddr = serverSocket.getInetAddress().getHostAddress();
			System.out.println(TcpApplication.timeStamp() + sAddr + " ← connected" );
			
				// 2.1. 송신 스트림 얻기
				pw = new PrintWriter(
					 new OutputStreamWriter(
					 serverSocket.getOutputStream())); // 직접적으로 스트림 연결하기
				// 2.2. 수신 스트림 얻기
				fromServer = new BufferedReader(
							 new InputStreamReader(
							 serverSocket.getInputStream()));
				
				// 2.3. 키보드 수신 스트림 얻기
				keyBoard = new BufferedReader(new InputStreamReader(System.in));
				
				// 3. 클라이언트의 id를 전송한다
				System.out.print("아이디 입력> ");
					// 아이디를 키보드로 입력받기
					id = keyBoard.readLine(); 
					// 입력받은 값 서버에 보내기
					pw.println(id);
					pw.flush();
					
				// 4. 접속한 서버의 데이터 수신을 담당할 스레드를 생성하여 실행(정보 입력하는 스레드와 정보를 전달하는 스레드 분리해서 동시에 진행하도록...)
				Thread thread = new Thread(new TcpClientHandler()); // 서버에서 보내 온 것 읽어들이는 	
				
				thread.start();

				// 5. 접속한 서버에게 키보드 입력 전송( 키보드에 입력된 정보 서버에 전달 )
				String line = null;
					while((line = keyBoard.readLine()) != null ) {
						pw.println(line);
						pw.flush();
						if(line.equalsIgnoreCase("/quit")) {
							break;
						}
					}			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}

}
