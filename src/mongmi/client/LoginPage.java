package mongmi.client;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*; 
import java.net.*;


class LoginPage extends JFrame implements ActionListener
{
	
	JPanel contentPane, panel1 ;
	JLabel backLabel;
	JButton button;
	JTextField userIPIn;
	Clip clip;
	Font font = new Font("배달의민족 주아", Font.PLAIN, 18);
	public static String ip;
	int port = 7000;
	DataInputStream dis;
	DataOutputStream dos;
	

	Socket s;
	
	LoginPage(){
		contentPane = new JPanel();
		panel1 = new JPanel();
		button = new JButton(new ImageIcon("img/button/blank.png"));
		userIPIn = new JTextField("127.0.0.1");
		
	}

	void init(){
		playSound("bgm.wav"); 
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.setOpaque(true);
		
		panel1.setOpaque(true);		
		contentPane.add(panel1);
		panel1.setLayout(null);

		backLabel = new JLabel(new ImageIcon("img/background/Login.png"));
		backLabel.setBounds(0, 0, 350, 550);
		backLabel.setOpaque(true);
		panel1.add(backLabel);

		
		button.setBounds(97, 399, 164, 49);
		button.setPressedIcon(new ImageIcon("img/button/LoginB.png"));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		backLabel.add(button);
		button.addActionListener(this);
		
		
		userIPIn.setBounds(101, 346, 158, 33);
		//userIPIn.setBorder(BorderFactory.createEmptyBorder());
		userIPIn.setFont(font);
		panel1.add(userIPIn);
		userIPIn.addActionListener(this);
		
		

		setUI();
	}

	void setUI(){
		setTitle("몽미 초성퀴즈");
		setIconImage(new ImageIcon("img/icon/ICon.png").getImage());
		setSize(360, 585);
		//setSize(357, 570);
		setVisible(true);
		setLocation(600,150);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	void playSound(String filename){ 
		File file = new File("bgm/" + filename);
		if(file.exists()){ 
			try{
				AudioInputStream stream = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip();
				clip.open(stream);
				clip.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{ 
			System.out.println("File Not Found!");
		}
	}	

	@Override
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		
		if(obj == button){
			if(userIPIn.getText().equals("")){
				JOptionPane.showMessageDialog(null, "IP 주소를 입력해 주세요!", "ERROR!", JOptionPane.WARNING_MESSAGE);
				
			}else{
				
					String temp = userIPIn.getText();
					String serverIP = "";
					if(temp != null) serverIP = temp.trim();
					if(serverIP.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")){ 
						MainPage mp = null;
						try{
							ip = serverIP;
							s = new Socket(ip, port);
							mp = new MainPage(this, s);
						}catch(UnknownHostException uh){
							JOptionPane.showMessageDialog(null, "IP 주소를 정확하게 입력해 주세요! ('ㅁ') ", "ERROR!", JOptionPane.WARNING_MESSAGE);
							return;
						}catch(IOException ie){
							JOptionPane.showMessageDialog(null, "현재 서버와 접속할 수 없습니다!(_ _@@) ", "ERROR!", JOptionPane.WARNING_MESSAGE);
							return;
						}
							clip.stop();
							JOptionPane.showMessageDialog(null, "             로그인 성공!", "몽미 초성퀴즈 LOGIN", JOptionPane.INFORMATION_MESSAGE);
							button.setEnabled(false);
							userIPIn.setEnabled(false);
							setVisible(false);
							dispose();
							mp.init(); 
				}else{
					JOptionPane.showMessageDialog(null, "IP 주소를 정확하게 입력해 주세요! ", "ERROR!", JOptionPane.WARNING_MESSAGE);
				}
			}
		}else if(obj == userIPIn){
		}else{}
	
	}


	public static void main(String []args){
		 LoginPage lg = new LoginPage();
		 lg.init();

	}
}
