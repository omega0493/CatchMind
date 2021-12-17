package game;

import java.util.Scanner;

import com.framework.TcpApplication;

public class TcpServer {
	public static void main(String[] args) {
	// 출력전에 화면 정리하기(구글링해봐요~)
		for(int i=0; i<15; i++) {
			System.out.println("");
		}
				
		Scanner sc = new Scanner(System.in);
		
		showMenu();
        System.out.printf("            >");
        int select = sc.nextInt();
        
		switch (select) {
		case 1 : 
	        System.out.printf("          1 → Start                   \n");
	        System.out.println();
	        break;
		case 0 :
	        System.out.printf("프로그램을 종료합니다.");
	        System.exit(0);
		}
		
		/*
		 *  TCP/IP 어플리케이션 생성
		 */
		TcpApplication app = new AppServer();
		
		// 설정 초기화
		app.init();
		
		// 어플리케이션 실행
		app.start();
		
		sc.close();
}

	private static void showMenu() {
	        System.out.printf("                                      \n");
	        System.out.printf("          ■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n");
	        System.out.printf("                                      \n");
	        System.out.printf("            [SERVER version 1.0.1®]   \n");
	        System.out.printf("                                      \n");
	        System.out.printf("          ■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n");
	        System.out.printf("                                      \n");
	        System.out.printf("                                      \n");
	        System.out.printf("                                      \n");
	        
	        System.out.printf("          1 → Start                   \n");
	        System.out.printf("          0 → End                     \n");
	        System.out.printf("                                      \n");
	        System.out.printf("                                      \n");
	    }
		
	}


