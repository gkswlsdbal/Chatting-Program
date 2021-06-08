package Tcp;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import db.login.DB;

public class tcp_chat extends JFrame implements ActionListener {

	private ImageIcon loginbackground;
	private JPanel background;
	private JScrollPane scrollPane;
	private JTextField idText;
	private JPasswordField pwText;
	private JLabel idLabel, pwLabel;
	private JButton loginButton;

	public tcp_chat() {
		setTitle("채팅 상담 프로그램");
		setSize(Main_Server.SCREEN_WIDTH, Main_Server.SCREEN_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(1000, 300);
		setLayout(null);
		// setLocationRelativeTo(this);

		loginbackground = new ImageIcon("image/snow.png");
		background = new JPanel() {
			public void paintComponent(Graphics g) {

				g.drawImage(loginbackground.getImage(), 0, 0, 1400, 770, null);

				setOpaque(false);
				super.paintComponent(g);
			}
		};
		background.setLayout(null);
		scrollPane = new JScrollPane(background);
		setContentPane(scrollPane);

		idText = new JTextField(20);
		pwText = new JPasswordField(20);
		idLabel = new JLabel("ID : ");
		pwLabel = new JLabel("PW : ");
		loginButton = new JButton("로그인");

		idLabel.setBounds(60, 130, 160, 30);
		pwLabel.setBounds(60, 180, 160, 30);

		idText.setBounds(110, 130, 140, 30);
		pwText.setBounds(110, 180, 140, 30);

		loginButton.setBounds(280, 150, 80, 30);

		loginButton.addActionListener(this);

		idText.setText("hi");
		pwText.setText("1234");

		background.add(idLabel);
		background.add(pwLabel);
		background.add(idText);
		background.add(pwText);
		background.add(loginButton);

		setVisible(true);
	}
	
	public static void main(String[] args) {
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == loginButton) {
			String id = idText.getText();
			String pw = new String(pwText.getPassword());
			
			boolean check=DBInit(id,pw);
		
			if (check) {
				this.dispose();
				Runnable s = new Server("Server",450,700);
				Thread t = new Thread(s);
				t.start();
			}else {
				System.out.println("로그인 오류");
				idText.setText("");
				pwText.setText("");
				idText.requestFocus();
			}
		}
	}

	public boolean DBInit(String id2,String pw2) {
         
		boolean flag=false;
		String id=null,pw = null,name=null;
	 try{
		 Class.forName("com.mysql.jdbc.Driver");
		 Connection conn=DriverManager.getConnection(
				 "jdbc:mysql://localhost/user_db", "root", "1234");
		 Statement stmt=conn.createStatement();
		 ResultSet rs=stmt.executeQuery("SELECT * FROM users");
		 
		 rs.first();
			 
			 id=rs.getString("id");
			 pw=rs.getString("pw");
		 if(id.equals(id2)&&pw.equals(pw2)) {		 
			 flag=true;
		 }
		 
	 }
	 catch(Exception e){
		 e.printStackTrace();
	 }
	return flag;
	}
}
