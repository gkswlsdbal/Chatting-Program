package Tcp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame implements ActionListener, Runnable {

   private JPanel panCenter, panBottom;
   private JTextArea area;
   private JTextField tf;
   private JButton btn, en_btn;
   
   private Image background = new ImageIcon("image/snow.png").getImage();
   
   ServerSocket serversocket=null;
   Socket socket=null;
   OutputStream out;
   BufferedReader in;
   String inputline;
   
   
   
   public Server(String title, int w, int h) {
	  Server s;
	  
      setTitle(title);
      setSize(w, h);

      setLayout(new BorderLayout());
      setLocation(1000, 300);
      // setLocationRelativeTo(this);

      setPanCenter();
      setPanBottom();
      add(panCenter, BorderLayout.CENTER);
      add(panBottom, BorderLayout.SOUTH);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);

   }

   private void setPanBottom() {
      // TODO Auto-generated method stub
      panBottom = new JPanel();
      
      tf = new JTextField(18);
      tf.addActionListener(this);
      btn = new JButton("입력");
      en_btn = new JButton("저장하기");
      en_btn.addActionListener(this);
      btn.addActionListener(this);
      panBottom.add(tf);
      panBottom.add(btn);
      panBottom.add(en_btn);
      
   }

   private void setPanCenter() {
      // TODO Auto-generated method stub
      panCenter = new JPanel();
      panCenter.setLayout(new BorderLayout());

      area = new JTextArea(7, 20) {{setOpaque(false);}
		public void paintComponent(Graphics g) {
			g.drawImage(background,0,0,this.getWidth(),this.getHeight(),this);
			super.paintComponent(g);
		}
	  };
	  area.append("\n");
	  area.setFont(new Font("SansSerif", Font.BOLD, 15));
      area.setEditable(false);
      JScrollPane sp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      panCenter.add(sp);
   }
   
   public void serverStart() throws IOException{
      System.out.println("서버시작");
      
      try {
         serversocket=new ServerSocket(8888); //서버 준비
      }catch(IOException e) {
         System.out.println("포트번호연결불가");
         System.exit(1);
      }
      
      socket=null;
      try {
         socket=serversocket.accept(); //클라이언트 연결
      }catch (IOException e) {
         System.exit(1);
      }
      
      out=socket.getOutputStream(); //데이터 보낼 준비 
     
      //데이터 받을 준비
      in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
      
      
      
      while((inputline=in.readLine())!=null) {
          String s = inputline+"\n\n";
          area.append(s);
          if(out.equals("q")) {
             break;
          }
       }

      // 시스템정리
      
      out.close();
      out=null;
    
   }

   public static void main(String[] args) throws IOException {
 
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      Object obj = e.getSource();
      if (obj == btn || obj == tf) {
         String str = tf.getText();
         if (!str.equals("")) {
            area.append("상담사 : " + tf.getText() + "\n\n");
            try {
               out.write( (str+"\n").getBytes() );
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               System.out.println("클라이언트 접속 끊음");
            }
            tf.setText("");
         }
   
      }
      
      if(obj==en_btn) {
    	  
    	  String area_en = area.getText();
          
          try {
 			String b = Encrypt(area_en);
 			Decrypt(b);
 		} catch (Exception e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
      }

   }

@Override
public void run() {
	try {
		serverStart();
	}catch(IOException e) {
		e.printStackTrace();
	}
}



public static String Encrypt(String text) throws Exception

{


          Encoder encoder = Base64.getEncoder();
          
          String filePath = "c:\\text\\test.txt";
          
          try {
           FileWriter fileWriter = new FileWriter(filePath);
           
           fileWriter.write(Base64.getEncoder().encodeToString(text.getBytes("UTF-8")));
           fileWriter.flush();
           fileWriter.close();          
           String a = Base64.getEncoder().encodeToString(text.getBytes("UTF-8"));
           System.out.println("a");
           return a;
           
          } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
          }
          return null;

}

public static void Decrypt(String text) throws Exception

{

         
          byte[] bytedecode=Base64.getDecoder().decode(text.getBytes("utf-8"));

          String filepath = "c:\\text\\test2.txt";
 
          try {
           FileWriter fileWriter = new FileWriter(filepath);
           fileWriter.write(new String(bytedecode));
           
           fileWriter.close();
          } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
          }

}

}