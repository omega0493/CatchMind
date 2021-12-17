package game;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class ChatServer {
 
 ArrayList clientOutputStreams;
 JTextArea status; // 서버상태를 표시할 영역
 
 public class ClientHandler implements Runnable {
  BufferedReader reader;
  Socket sock;
  
  public ClientHandler(Socket clientSocket) {
   try {
    sock = clientSocket;
    InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
    reader = new BufferedReader(isReader);
    
   } catch (Exception e) {e.printStackTrace();}
  }
  
  public void run() {
   String message;
   try {
    // 메시지가 올때마다 접속한 채팅클라이언트에 모두 전달
    while((message = reader.readLine()) != null) {
     System.out.println("read : " + message);
     tellEveryone(message);
    }
   } catch (Exception e) {e.printStackTrace();}
  }
  
 }
 
 public static void main(String[] args) {
  new ChatServer().start();
 }
  
 public void start() {
  JFrame frame = new JFrame("server status");
  JPanel mainPanel = new JPanel();
  status = new JTextArea(15,40);
  status.setLineWrap(true); //자동 개행
  status.setWrapStyleWord(true); //행을 넘길 때 행의 마지막 단어가 두행에 걸쳐 나뉘지 않도록 하기
  status.setEditable(false);
  status.setVisible(true);
  JButton exitButton = new JButton("Exit");
  exitButton.addActionListener(new ExitButtonListener());
  JScrollPane qScroller = new JScrollPane(status);
  qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); //수직 스크롤바 표시 정책 : 항상 보여주기
  qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);// 수평 스크롤바 표시 정책 : 항상 보여주기
  mainPanel.add(qScroller);
  mainPanel.add(exitButton);
  status.append("Chat Server Start..\n");
  
  frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
  frame.setSize(500, 500);
  frame.setVisible(true);
  
  go();
 }
 
 public void go() {
    
  clientOutputStreams = new ArrayList();
  try {
   ServerSocket serverSock = new ServerSocket(5000);
   
   // 계속 새로운 채팅 클라이언트를 accept
   while(true) {
    Socket clientSocket = serverSock.accept();
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
    clientOutputStreams.add(writer);
    
    Thread t = new Thread(new ClientHandler(clientSocket));
    t.start();
    //System.out.println("got a connection" + clientSocket.getPort());
    status.append("got a connection : " + clientSocket.getPort() + "\n");
   }
  } catch(Exception e) {
   e.printStackTrace();
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
 
 public void tellEveryone(String message) {
  Iterator it = clientOutputStreams.iterator();
  while(it.hasNext()) {
   try {
    PrintWriter writer = (PrintWriter) it.next();
    writer.println(message);
    writer.flush();
   } catch(Exception e) {
    e.printStackTrace();
   }
  }
 }
}

