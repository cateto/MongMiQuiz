package mongmi.client;

import java.awt.*;
import java.awt.Toolkit;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*; 
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.*;
import java.util.*;


class MyScore extends JFrame  implements ActionListener         
{
	
	JPanel contentPane, panel1 ;
	JLabel backLabel, resultScore;
	JButton button;
	
	QuizPage qp;
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	String id;
	int score;
	
	String firstPlace_id;
	int firstPlace_score;
	String secondPlace_id;
	int secondPlace_score;
	String thirdPlace_id;
	int thirdPlace_score;

	MyScore(QuizPage qp, Socket s){

		this.qp = qp;
		this.s = s;
		this.dis = qp.dis;
		this.dos = qp.dos;
		this.id = qp.id;
		this.score = qp.score;

		contentPane = new JPanel();
		panel1 = new JPanel();
		button = new JButton(new ImageIcon("img/button/RankB.png"));
		resultScore = new JLabel(Integer.toString(score)+"점", JLabel.CENTER);
		resultScore.setFont(new Font("배달의민족 주아", Font.PLAIN, 50));
		playSound("eff_grow_wow.wav");

		try{
			boolean serverOn = true;
			dos.writeBoolean(serverOn);
		}catch(IOException ie){}
		
	}

	void init(){
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.setOpaque(true);
		
		panel1.setOpaque(true);		
		contentPane.add(panel1);
		panel1.setLayout(null);

		backLabel = new JLabel(new ImageIcon("img/background/MyScore.png"));
		backLabel.setBounds(0, 0, 350, 550);
		backLabel.setOpaque(true);
		panel1.add(backLabel);

		resultScore.setBounds(69, 221, 221, 100);
		backLabel.add(resultScore);
		//resultScore.setText(dis.writeUTF()); 고쳐야함!!!!!!!
 		

		button.setBounds(45, 407, 270, 79);
		backLabel.add(button);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setPressedIcon(new ImageIcon("img/button/rankBC.png"));
		//button.setOpaque(false);
		button.addActionListener(this);


		setUI();
	}

	void setUI(){
		setTitle("몽미 초성퀴즈");
		setIconImage(new ImageIcon("img/icon/ICon.png").getImage());
		setSize(360, 585);
		//setSize(357, 570);
		setVisible(true);
		setLocation(600,150);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	static void playSound(String filename){ 
		File file = new File("bgm/" + filename);
		if(file.exists()){ 
			try{
				AudioInputStream stream = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
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
			button.setEnabled(false);
			setVisible(false);
			dispose();
			RankIn();
			new RankingPage(this, s).init(); //＃마이스코어에서 버튼(랭킹) 누르면 ranking 페이지로 이동
		}else{}
	}

	void RankIn(){
		try{
			firstPlace_id = dis.readUTF();
			firstPlace_score = dis.readByte();
			secondPlace_id = dis.readUTF();
			secondPlace_score = dis.readByte();
			thirdPlace_id = dis.readUTF();
			thirdPlace_score = dis.readByte();
		}catch(NumberFormatException ne){
			ne.printStackTrace();
		}catch(IOException ie){
			ie.printStackTrace();
		}
	}


}

