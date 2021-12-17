package game;

import java.io.IOException;

/**
 * AppClient의 소켓을 이용하여
 * 서버의 송/수신 기능을 관리한다.
 * 
 * @author bitcamp
 *
 */
public class TcpClientHandler implements Runnable{
	@Override
	public void run() {		
		try {
			// 서버로부터 메세지 수신
			String line = null;
			while(true) {
				line = AppClient.fromServer.readLine();
				if(line != null) {
					System.out.println(line);
				}
			}
			
		} catch(IOException ec) {
			ec.printStackTrace(); 
		} finally {
			try {
				if(AppClient.keyBoard != null) {AppClient.keyBoard.close();}
				if(AppClient.pw != null) {AppClient.pw.close();}
				if(AppClient.fromServer != null) {AppClient.fromServer.close();}
				if(AppClient.serverSocket != null) {AppClient.serverSocket.close();}
		
			} catch(IOException ec){
				ec.printStackTrace();		
			}
		}
	}
}
