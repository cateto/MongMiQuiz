package mongmi.client;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import javax.sound.sampled.*; 
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class MainPage extends JFrame {//

	JButton startB;//1.Start버튼
	JLabel background; //1.배경 패널
	JPanel cp, pMom;
	JTextField idTf;
	Font font = new Font("배달의민족 주아", Font.PLAIN, 40);
	
	String id;
	LoginPage lp;
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	Clip clip;
	
	MainPage(LoginPage lp, Socket s){
		this.lp = lp;
		this.s = s;
		try{
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		}catch(IOException ie){}

		playSound("bgm_quiz.wav");
	}
	
	void playSound(String filename){ 
        File file = new File("bgm/" + filename);
        if(file.exists()){ 
            try{
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
				clip.loop(8);
                if(filename.equals("bgm_quiz.wav")); //clip.loop(8);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{ 
            System.out.println("File Not Found!");
        }
    }


	void init(){
		
//가장 맨아래 깔린 패널(컨테이너) 설정//컨테이너로 만들지 않은 이유는 장면전환을 위해?
		cp = new JPanel();//ct
		cp.setBorder(null);
		setContentPane(cp);
		cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS)); //왼쪽->오른쪽 순서대로 배치
		cp.setOpaque(true); //투명하게
//큰패널들 깔아둘 패널 설정
		pMom = new JPanel();			
		pMom.setOpaque(true);		
		cp.add(pMom);
		pMom.setLayout(null);//앱솔루트
//배경화면 설정//컴포넌트
		background = new JLabel(new ImageIcon("img/background/Main.png"));
		background.setBounds(0, 0, 350, 550); //버튼 위치(0.0)및 크기(350,550)설정
		background.setOpaque(true);//불투명설정
		pMom.add(background);
//큰 패널들 배치
		idTf = new JTextField();		
		
		idTf.setBorder(BorderFactory.createEmptyBorder());//외곽선X
		idTf.setFont(font);
		idTf.setOpaque(false);
		idTf.setBounds(110, 367, 200,70);
		background.add(idTf);
		KeyListener keyListener = new MainKeyL(this);
		idTf.addKeyListener(keyListener);

		startB = new JButton(new ImageIcon("img/button/StartB.png"));
		startB.setPressedIcon(new ImageIcon("img/button/StartBC.png"));
		
		startB.setBorderPainted(false);//외곽선
        startB.setFocusPainted(false);
        startB.setContentAreaFilled(false);//버튼 배경 지우기?
		startB.setBounds(-5,440,360,100);//버튼 위치(-5,440)및 크기(360,100) 설정
		background.add(startB);
		ActionListener listener = new MainActL(this);
		startB.addActionListener(listener);//시작 버튼 누르면 액션(QuizPage로 전환)

		setUI();

	}
	void setUI(){
		setTitle("몽미 초성퀴즈");
		setIconImage(new ImageIcon("img/icon/ICon.png").getImage());
		setSize(360, 585);
		setVisible(true);
		setLocation(600,150);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class MainActL implements ActionListener{
	MainPage mp;

	MainActL(MainPage mp){
		this.mp = mp;
	}
	@Override
	public void actionPerformed(ActionEvent e){
		mp.id = mp.idTf.getText();
		if(mp.id.length() < 6){
			if(mp.id.equals("")) mp.id = "GUEST";
			try{
				mp.dos.writeUTF(mp.id);
			}
			catch (IOException ie){
			}
			mp.idTf.setEnabled(false);//id입력 후 비활성화
			mp.setVisible(false);
			mp.dispose();
			new QuizPage(mp, mp.s).init();
		}else{
			JOptionPane.showMessageDialog(null, "아이디 길이는 최대 5자까지 가능해요.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

class MainKeyL implements KeyListener{
	MainPage mp;

	MainKeyL(MainPage mp){
		this.mp = mp;
	}
	@Override
	public void keyTyped(KeyEvent e) { 
	}
	@Override
	public void keyPressed(KeyEvent e) {   
	}
	@Override
	public void keyReleased(KeyEvent e) {//키가 눌렸다가 떼어졌을때

		if (e.getKeyCode()==KeyEvent.VK_ENTER) {//각각의 키들이 가지고 있는 코드 값이 나타난다
												//VK_ENTER = 상수 , 엔터 키에 대한 키값을 의미한다
			mp.id = mp.idTf.getText();
			if(mp.id.length() < 6){
				if(mp.id.equals("")) mp.id = "GUEST";
				try{
					mp.dos.writeUTF(mp.id);
				}catch (IOException ie){}
				mp.idTf.setEnabled(false);//id입력 후 비활성화
				mp.setVisible(false);
				mp.dispose();
				new QuizPage(mp, mp.s).init();
			}else{
				JOptionPane.showMessageDialog(null, "아이디 길이는 최대 5자까지 가능해요.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}