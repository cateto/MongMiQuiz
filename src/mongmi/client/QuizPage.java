package mongmi.client;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import javax.sound.sampled.*; 
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class QuizPage extends JFrame{

	JButton passB;
	JLabel background;
	JLabel timerL,quizL,hintL;
	JLabel markO,markX;
	JPanel cp, pMom;
	JTextField answerTf;
	Font font = new Font("배달의민족 주아", Font.PLAIN, 40);
	int score;
	ActionListener listener;
	KeyListener keyListener;

	MainPage mp;
	Socket s;
	DataInputStream dis;
	DataOutputStream dos;
	String id;
	Question qt;

	Clip clip;



	QuizPage(MainPage mp,Socket s){
		this.mp = mp;
		this.s = s;
		this.dis = mp.dis;
		this.dos = mp.dos;
		this.id = mp.id;
		this.clip = mp.clip;

		listener = new QuizActL(this);
		qt = new Question(this);
		keyListener = new QuizKeyL(this);
	}
	
	void playSound(String filename){ 
        File file = new File("bgm/" + filename);
        if(file.exists()){ 
            try{
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(stream);
                clip.start();
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
		cp.setOpaque(true);//투명하게
//큰패널들 깔아둘 패널 설정
		pMom = new JPanel();			
		pMom.setOpaque(true);		
		cp.add(pMom);
		pMom.setLayout(null);
//배경화면 설정
		background = new JLabel(new ImageIcon("img/background/Quiz.png"));
		background.setBounds(0, 0, 350, 550); //버튼 위치(0.0)및 크기(350,550)설정
		background.setOpaque(true);//불투명설정
		pMom.add(background);

//배경화면에 채점 gif 만들기위한 라벨 넣음
		markO = new JLabel(new ImageIcon("img/mark/Opa.png"));
		markO.setBounds(100, 320, 150, 150); //버튼 위치(100.320)및 크기(150,150)설정
		markO.setOpaque(false);//불투명설정
		background.add(markO);

		markX = new JLabel(new ImageIcon("img/mark/Opa.png"));
		markX.setBounds(185, 120, 150, 150); //버튼 위치(185.120)및 크기(150,150)설정
		markX.setOpaque(false);//불투명설정
		background.add(markX);

//배경화면에 버튼,라벨 넣음
		timerL = new JLabel("",JLabel.CENTER);
		timerL.setFont(font);
		timerL.setBounds(10, 15, 150, 45); //버튼 위치(10.15)및 크기(150,45)설정
		timerL.setOpaque(false);//불투명설정
		background.add(timerL);
		TimerModule tm = new TimerModule(this);//시간초 설정
		tm.start();//시간흘러가는 메소드 호출

		quizL = new JLabel("",JLabel.CENTER);
		quizL.setFont(font);
		quizL.setBounds(45, 85, 260, 50); //버튼 위치(45.85)및 크기(260,50)설정
		quizL.setOpaque(false);//불투명설정
		background.add(quizL);

		hintL = new JLabel("",JLabel.CENTER);
		//Font font = new Font("배달의민족 주아", Font.PLAIN, 20);
		hintL.setFont(new Font("배달의민족 주아", Font.PLAIN, 20));
		hintL.setBounds(35,  480, 270, 50); //버튼 위치(35,480)및 크기(270,50)설정
		hintL.setOpaque(false);//불투명설정
		background.add(hintL);
		qt.init();
		qt.show();//Question class에 있는 문제,힌트 출력하는 메소드
//ct가 입력할 답Field
		answerTf = new JTextField();		
		
		answerTf.setBorder(BorderFactory.createEmptyBorder());
		answerTf.setFont(new Font("배달의민족 주아", Font.PLAIN, 30));
		answerTf.setOpaque(false);
		answerTf.setBounds(40, 355, 280	,70);
		answerTf.setHorizontalAlignment(JTextField.CENTER);
		background.add(answerTf);
		/////////사용자가 답을 입력했을 때 event!!!!/////////
		answerTf.addKeyListener(keyListener);

		passB = new JButton(new ImageIcon("img/button/PassB.png"));
		passB.setPressedIcon(new ImageIcon("img/button/PassBC.png"));
		
		passB.setBorderPainted(false);//외곽선
        passB.setFocusPainted(false);
        passB.setContentAreaFilled(false);//버튼 배경 지우기?
		passB.setBounds(170,-10,200,100);//버튼 위치(170,-10)및 크기(200,100) 설정
		background.add(passB);
		/////////사용자가 pass를 클릭했을 때 event!!!!/////////
		passB.addActionListener(listener);

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

class QuizActL implements ActionListener{
	QuizPage qp;

	QuizActL(QuizPage qp){
		this.qp = qp;
	}
	@Override
	public void actionPerformed(ActionEvent e){
		qp.playSound("eff_pass.wav");
		qp.qt.removeItem();//패스 눌렀을 때 보여진 문제는 제거
		qp.qt.show();//다음 문제 보여짐
	}
}
class QuizKeyL implements KeyListener{
	QuizPage qp;
	
	QuizKeyL(QuizPage qp){
		this.qp = qp;
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
			String answer = "";
			
			answer = qp.answerTf.getText();//사용자가 입력한 답을 answer에 넣음
			qp.answerTf.setText("");//입력 후 입력창 리셋
			answer = answer.trim();//사용자가 입력한 답 공백 제거
			//System.out.println(qp.qt.vAnswer.get(qp.qt.random));답 확인
			if(answer.equals(qp.qt.vAnswer.get(qp.qt.random))){
				ImageIcon correct = new ImageIcon("img/mark/Correct.gif");
				correct.getImage().flush();
				qp.markO.setIcon(correct);
				qp.playSound("eff_star.wav");
				qp.score++;//맞추면 점수 획득
				qp.qt.removeItem();
				qp.qt.show();
			}else{
				ImageIcon inCorrect = new ImageIcon("img/mark/InCorrect.gif");
				qp.playSound("eff_wrong.wav");
				inCorrect.getImage().flush();
				qp.markX.setIcon(inCorrect);
			}
			//System.out.println("점수 : "+qp.score);//맞추면 점수
		}
	}
}

class TimerModule extends Thread{//퀴즈 시간이 끝나면 MyScore페이지로 넘어감

	QuizPage qp;

	int time = 10;
	int minute;
	int second;
	TimerModule(QuizPage qp){
		this.qp = qp;	
	}
	public void run(){

		while(time>0){
			minute = time/60;
			second = time%60;
			String m = Integer.toString(minute);
			String s = Integer.toString(second);
			qp.timerL.setText(m+" : "+s);
			time--;
			try{
				Thread.sleep(1000);//1초씩으로 설정!
			}catch(Exception e){}
		}
		try{
			qp.dos.writeByte(qp.score);
			System.out.println(qp.score);
		}catch(IOException ie){}
		qp.clip.stop();
		qp.setVisible(false);
        qp.dispose();
        new MyScore(qp, qp.s).init();
	}
	
}