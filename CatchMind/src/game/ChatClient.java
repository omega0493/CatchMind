package game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ChatClient {
	
	

 JTextArea incoming; // 서버로부터 읽어온 메시지를 표시할 영역
 JTextField outgoing; // 서버로보낼 메시지를 입력할 영역
 BufferedReader reader; // 서버로부터 온 메시지를 읽어드릴 읽기버퍼
 PrintWriter writer; // 메시지를 소켓 스트림에 써줄 쓰기 스트림
 Socket sock; // 소켓
 
 public static void main(String[] args) {
  ChatClient client = new ChatClient();
  client.go();
 }
 
 public void go() {
  // 채팅창 UI
  JFrame frame = new JFrame("Ludicrously Simple Chat Client");
  JPanel mainPanel = new JPanel();
  incoming = new JTextArea(15,40);
  incoming.setLineWrap(true); //자동 개행
  incoming.setWrapStyleWord(true); // 행을 넘길 때 행의 마지막 단어가 두행에 걸쳐 나뉘지 않도록 하기
  incoming.setEditable(false);
  incoming.setVisible(true);
  JScrollPane qScroller = new JScrollPane(incoming);
  qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); //수직 스크롤바 표시 정책 : 항상 보여주기
  qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);// 수평 스크롤바 표시 정책 : 항상 보여주기
  outgoing = new JTextField(20);
  JButton sendButton = new JButton("Send");
  sendButton.addActionListener(new SendButtonListener());
  JButton exitButton = new JButton("Exit");
  exitButton.addActionListener(new ExitButtonListener());
  mainPanel.add(qScroller);
  mainPanel.add(outgoing);
  mainPanel.add(sendButton);
  mainPanel.add(exitButton);
  incoming.append("notice : UI 초기화\n");
  
  // 소켓 통신 초기화
  setUpNetWorking();
  incoming.append("notice : 소켓 통신 초기화\n");
  incoming.append("notice : 채팅에 입장하셨습니다.\n\n");
  
  // 읽기 쓰레드 생성, 시작
  Thread readerThread = new Thread(new IncomingReader());
  readerThread.start();
  
  // UI visual(정렬,크기) 설정
  frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
  frame.setSize(500, 500);
  frame.setVisible(true);
 }
 
 private void setUpNetWorking() { // 클라이언트와 서버간 통신할 준비
  try {
   
   // 소켓 객체 생성
   sock = new Socket("localhost", 5000);
   
   // 버퍼리더 + 입력스트림리더 + 소켓입력스트림 체이닝
   InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
   reader = new BufferedReader(streamReader);
   
   // printwriter + 소켓출력스트림 체이닝
   writer = new PrintWriter(sock.getOutputStream());
   
   System.out.println("networking established");
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
 
 // Send 버튼 리스너
 public class SendButtonListener implements ActionListener {
  
  // 버튼 눌리면
  public void actionPerformed(ActionEvent ev) {
   try {
    writer.println(outgoing.getText()); // outgoing 영역의 텍스트를 가져와 writer에 쓴다.
    writer.flush(); // 버터링 하짐 말고 바로 flush
   } catch(Exception ex) {
    ex.printStackTrace();
   }
   outgoing.setText(""); //쓰고 나서 outgoing 영역을 비운다.
   outgoing.requestFocus(); //포커스를 텍스트창에 놓는다.
  }
 }
 
 public class ExitButtonListener implements ActionListener {
  public void actionPerformed(ActionEvent ev) {
   try {
    System.exit(0);
   } catch(Exception e) {
    e.printStackTrace();
   }
  }
 }
 
 public class IncomingReader implements Runnable {
  public void run() {
   String message;
   try {
    while ((message = reader.readLine()) != null) {
     System.out.println("read " + message);
     incoming.append(message + "\n");
    }    
   } catch (Exception e) {e.printStackTrace();}
  }
 }
}

