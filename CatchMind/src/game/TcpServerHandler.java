package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.framework.TcpApplication;

/**
 * AppServer로부터 전달 받은 소켓을 이용하여
 * 클라이언트의 접속 정보 및 송수신 기능을 관리한다.
 * (AppServer에서 받은 소켓들을 처리하는 곳)
 */

public class TcpServerHandler implements Runnable {
	/*
	 * 클라이언트 ID를 키(K)로 하는 송신(V)을 위한 맵 자료구조
	 */
	// 전역변수.. 클라이언트 한명에 TcpServerHandler 하나가 생기는데 그 정보 하나가 Map.Entry로 들어감
	public static HashMap<String, PrintWriter> sendMap = new HashMap<>();
	
	// 클라이언트와 연결된 소켓 객체
	private Socket sock;
	
	// 클라이언트 ip주소
	private String cAddr;
	
	// 클라이언트 id
	private String id;
	
	
	/*
	 * 생성자
	 * 받아 온 소켓을 맵에 저장
	 */
	public TcpServerHandler(Socket cSocket) {
		this.sock = cSocket; //아이디별로 소켓정보 저장
		this.cAddr = sock.getInetAddress().getHostAddress(); // 소켓을 받아서 주소 찍기
	}
	
	/** 
	 * 참여자 입/퇴실 관리
	* 브로드 캐스팅(클라이언트에게 받은 스트링을 모든 참여인원에게 뿌리는 것)
   * 참여자 송수신 관리
	 */
	
	@Override
	public void run() {
		try {
			// 1. 송신 String 얻기
			PrintWriter pw = new PrintWriter(
							 new OutputStreamWriter(
							 sock.getOutputStream())); // 직접적으로 스트림 연결하기
			// 2. 수신 String 얻기
			BufferedReader keyBoard = new BufferedReader(
								new InputStreamReader(
								sock.getInputStream()));
			
			
			// 3. 클라이언트 접속정보 저장
			//여기서부터 시작이야(!?)
			id = keyBoard.readLine();
			TcpServerHandler.sendMap.put(id,pw); // map 이 static이어서 클래스. 이름 으로 호출
			
			// 4. 클라이언트 입장정보를 브로드캐스팅
			TcpServerHandler.broadCast("(" + TcpApplication.timeStamp() + ")" + 
					"[" + id + "] 님이 들어오셨습니다.");
			System.out.println(TcpApplication.timeStamp() + cAddr + " <= connected");
			System.out.println(TcpApplication.timeStamp() + "참여인원: " + sendMap.size() + "명");
			
			// 5. 수신/송신
			String line = null;
			while((line = keyBoard.readLine()) != null) {
				// 퇴장하는 경우
				if(line.equalsIgnoreCase("/quit")) {
					// 퇴장정보 브로드캐스팅
					TcpServerHandler.broadCast(TcpApplication.timeStamp() +
							"[" + id + "] 님이 나가셨습니다.");
					break;
				}
				// 귓속말하는 경우
				else if(line.indexOf("/to") > -1 ) { // -1보다 크면 /to 값이 존재한다는 것
					// 귓속말 메소드 호출
					whisper(id, line);
				}
				// 일반 메세지 전송
				else {
					String msg = "[" + id + "]" + " " + line;
					TcpServerHandler.broadCast(msg);
				}
			} 
			// 퇴장하는 경우 처리
			System.out.println(TcpApplication.timeStamp() + cAddr + " -> disconnected");
			// 맵 삭제
			TcpServerHandler.sendMap.remove(id);
			
			System.out.println(TcpApplication.timeStamp() + "참여인원: " + sendMap.size() + "명");
			
			pw.close();
			keyBoard.close();
			sock.close();
			
		}catch(IOException e) { 
			e.printStackTrace();
		} finally {
			
		}
		
	}
	
	/*
	 * 귓속말 메소드
	 *  
	 *  문자열의 인덱스로 /to를 구별
	 *  name: 보내는 클라이언트id
	 *  msg: 보낼 메세지(/to IU 뭐하니?)
	 */
	private void whisper(String name, String msg) {
		int start = msg.indexOf(" ") +1; // 시작 위치는 첫 공백의 +1
		//to 이후를 시작 위치로 잡을 수 없나?혹은 @
		int end = msg.indexOf(" ",start); //start위치부터 다음 공백이 나오는 위치
		
		if(end != -1) {
//			toId= 받는사람
			String toId	  = msg.substring(start, end); //substring 으로 첫점과 끝점 알려주기
			String secret = msg.substring(end + 1); //substring 으로 시작지점만 알려주고 (시작지점부터)나머지지점 나타내기
			
			// sendMap으로부터 키<id>에 해당하는 PrintWriter객체를 얻어온다.
			PrintWriter pw = TcpServerHandler.sendMap.get(toId);
			// 메세지 전송
			if(pw != null) {
				pw.println(name + "님의 귓속말[^.^]: " + secret);
				pw.flush();
			}
		}
		
	}
	
	/*
	 * 브로드 캐스팅 
	 * (모든 참여자에게 일괄적으로 전송)
	 */
	public static void broadCast(String message) {
		// sendMap에 여러 스레드가 접근하므로 동기화synchronized 처리가 필요
		synchronized (sendMap) {
			
		// 접속한 모든 클라이언트에게 메세지를 전송 
		for(PrintWriter cpw : TcpServerHandler.sendMap.values()) {
			cpw.println(message);
			cpw.flush();
		}
	}
}
	
	

}
