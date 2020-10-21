package mongmi.client;

import java.awt.*;
import java.awt.Toolkit;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*; 
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


class RankingPage extends JFrame implements ActionListener
{
	
	JPanel contentPane, panel1 ;
	JLabel backLabel, firstPlace, secondPlace, thirdPlace;
	JButton button;
	Font font = new Font("배달의민족 주아", Font.BOLD, 23);

	MyScore ms;
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	String firstPlace_id;
	int firstPlace_score;
	String secondPlace_id;
	int secondPlace_score;
	String thirdPlace_id;
	int thirdPlace_score;

	RankingPage(MyScore ms, Socket s){

		this.ms = ms;
		this.s = s;
		this.dis = ms.dis;
		this.dos = ms.dos;
		this.firstPlace_id = ms.firstPlace_id;
		this.firstPlace_score = ms.firstPlace_score;
		this.secondPlace_id = ms.secondPlace_id;
		this.secondPlace_score = ms.secondPlace_score;
		this.thirdPlace_id = ms.thirdPlace_id;
		this.thirdPlace_score = ms.thirdPlace_score;
		
		contentPane = new JPanel();
		panel1 = new JPanel();
		button = new JButton(new ImageIcon("img/button/ExitB.png"));
		firstPlace = new JLabel("");
		secondPlace = new JLabel("");
		thirdPlace = new JLabel("");

		firstPlace.setFont(font);
		secondPlace.setFont(font);
		thirdPlace.setFont(font);
		playSound("eff_grow_shine.wav");
		
	}

	void init(){ 
		
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.setOpaque(true);
		
		panel1.setOpaque(true);		
		contentPane.add(panel1);
		panel1.setLayout(null);

		backLabel = new JLabel(new ImageIcon("img/background/Rank.png"));
		backLabel.setBounds(0, 0, 350, 550);
		backLabel.setOpaque(true);
		panel1.add(backLabel);

		firstPlace.setBounds(140, 215, 182, 61);
		firstPlace.setText(firstPlace_id+" ("+firstPlace_score+"점)");
		backLabel.add(firstPlace);
		
		secondPlace.setBounds(140, 300, 182, 61);
		secondPlace.setText(secondPlace_id+" ("+secondPlace_score+"점)");
		backLabel.add(secondPlace);

		thirdPlace.setBounds(140, 375, 182, 61);
		thirdPlace.setText(thirdPlace_id+" ("+thirdPlace_score+"점)");
		backLabel.add(thirdPlace);
		

		button.setBounds(59, 450, 265, 80);
		backLabel.add(button);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setPressedIcon(new ImageIcon("img/button/ExitB.png"));
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

	void dataIN(){
		try{
			for(int i = 0; i<6; i++){
				String nameT = dis.readUTF();
				if(nameT.startsWith("#name")){
					String str = nameT.trim();
					str = str.substring(nameT.indexOf(" "), str.length());
					if(i==0){
						firstPlace_id = str;
					}else if(i==2){
						secondPlace_id = str;
					}else if(i==4){
						thirdPlace_id = str;
					}
				}else if(nameT.startsWith("#rank")){
					String str = nameT.trim();
					str = str.substring(nameT.indexOf(" "), str.length());
					int tempNum = Integer.parseInt(str);
					if(i==1){
						firstPlace_score = tempNum;
					}else if(i==3){
						secondPlace_score = tempNum;
					}else if(i==5){
						thirdPlace_score = tempNum;
					}
				}
			}
		}catch(NumberFormatException ne){
		}catch(IOException ie){}

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
			int answer = JOptionPane.showConfirmDialog(
					null, 
					"게임을 끝낼까요?", 
					"Exit", 
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					new ImageIcon("img/char/Char7.png"));
			
			if(answer == JOptionPane.YES_OPTION){
				JOptionPane.showMessageDialog(null, "잘가요.(^-^)");
				System.exit(0);
				try{
					boolean serverOn2 = true;
					dos.writeBoolean(serverOn2);
				}catch(IOException ie){}
			}else{
				JOptionPane.showMessageDialog(null, "알겠어요.(^-^)");
			}		
			//new MainPage(ms.qp.mp.lp, s).init(); ＃랭킹에서 버튼(메인) 누르면 main 페이지로 이동
		}else{}
	}

}



