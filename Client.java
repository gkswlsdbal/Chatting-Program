package Tcp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener {

	private JPanel panCenter, panBottom;
	private JTextArea area;
	private JTextField tf;
	private JButton btn, sf_btn;
	private ImageIcon file_image;
	private JButton file_btn;
	private Image background = new ImageIcon("image/snow.png").getImage();
	private static String name;
	DataOutputStream dataOutputStream;
	OutputStream out;
	BufferedReader in;
	Socket socket = null;

	public String dt;
	public String file = null;

	public Client(String title, int w, int h) {
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
		btn.addActionListener(this);

		file_image = new ImageIcon("image/file.png");
		file_btn = new JButton(file_image);
		file_btn.addActionListener(this);

		sf_btn = new JButton("파일전송");
		sf_btn.addActionListener(this);
		
		panBottom.add(tf);
		panBottom.add(btn);
		panBottom.add(sf_btn);
		panBottom.add(file_btn);

	}

	private void setPanCenter() {
		// TODO Auto-generated method stub
		panCenter = new JPanel();
		panCenter.setLayout(new BorderLayout());
		
		area = new JTextArea(7, 20) {
			{setOpaque(false);}
			public void paintComponent(Graphics g) {
				g.drawImage(background,0,0,this.getWidth(),this.getHeight(),this);
				super.paintComponent(g);
			}
		};
		area.setFont(new Font("SansSerif", Font.BOLD, 15));
		area.append("\n");
		area.setEditable(false);
		JScrollPane sp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panCenter.add(sp);
	}

	public void client() throws IOException {

		try {
			socket = new Socket("localhost", 8888);
			out = socket.getOutputStream();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch (IOException e) {
			System.out.println("포트번호연결불가");
			System.exit(1);
		}

		String fromServer;

		while ((fromServer = in.readLine()) != null) {
			String s = "상담사 : " + fromServer + "\n\n";
			area.append(s);
			if (out.equals("q")) {
				break;
			}
			
		}

		
		in.close();
		socket.close();

	}
	
	public static void main(String[] args) throws IOException {
		name = JOptionPane.showInputDialog("사용하고 싶은 닉네임을 알려주세요", "이름");
		
		Client c = new Client("client", 450, 700);
		c.client();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if (obj == btn || obj == tf) {
			String str = name + " : " + tf.getText();
			if (!str.equals("")) {
				area.append(name + " : " + tf.getText() + "\n\n");
				try {
					out.write((str + "\n").getBytes());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("클라이언트 접속 끊음");
				}
				tf.setText("");
			}
		}

		if (obj == sf_btn) {
			try {
				Socket s = new Socket("localhost", 9999); // 주어지는 ip주소와 포트번호로 접속하는 소켓생성.
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				System.out.println("파일명 : " + file);
				bw.write(file + "\n");
				bw.flush();

				DataInputStream dis = new DataInputStream(new FileInputStream(new File(tf.getText())));
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				System.out.println("바이트 단위로 쓰겠습니다..!!");
				
				
				int i = 0;
				while ((i = dis.read()) != -1) {
					System.out.print("1");
					dos.writeByte(i);
					dos.flush();
				}
				
				System.out.println("이햐~ 다 썼네요..!!");

				// 자원정리
				dis.close();
				dos.close();
				
                tf.setText("");
				System.out.println("시스템 정리 후 파일 전송 종료하께요^^");
				socket = new Socket("localhost", 8888);
	
			} catch (Exception e2) {
				System.out.println(e2 + "접속하는 데 실패하셨습니다..!!");
			}
		}

		if (obj == file_btn) {

			FileDialog fd = new FileDialog(this, "", FileDialog.LOAD); // 다이얼로그창 생성
			fd.setVisible(true);


			String directory = fd.getDirectory(); // 다이얼로그창에서 선택된 디렉토리를..

			file = fd.getFile(); // 파일을...
			tf.setText(directory + file); 
            
		}
	}
}
