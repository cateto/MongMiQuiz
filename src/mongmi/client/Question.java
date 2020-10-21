package mongmi.client;

import java.io.*;
import java.util.*;

class Question{
	Vector<String> vQuest = new Vector<String>();
	Vector<String> vHint = new Vector<String>();
	Vector<String> vAnswer = new Vector<String>();
	BufferedReader brQ,brH,brA;
	String fNameQ = "quiz.txt";
	String fNameH = "hint.txt";
	String fNameA = "answer.txt";
	
	Random r = new Random();
	int random;
	Question(){}
	QuizPage qp;
	Question(QuizPage qp){
		this.qp = qp;
	}
	
	void init(){
		try{
			brQ = new BufferedReader(new FileReader(fNameQ));
			brH = new BufferedReader(new FileReader(fNameH));
			brA = new BufferedReader(new FileReader(fNameA));
		}catch(FileNotFoundException fe){}
		read();
	}
	void read(){
		String lineQ = "";
		String lineH = "";
		String lineA = "";
		try{
			while((lineQ = brQ.readLine()) != null){
				if(lineQ != null) lineQ.trim();
				if(lineQ.length()!=0) vQuest.add(lineQ);
			}
			while((lineH = brH.readLine()) != null){
				if(lineH != null) lineH.trim();
				if(lineH.length()!=0) vHint.add(lineH);
			}
			while((lineA = brA.readLine()) != null){
				if(lineA != null) lineA.trim();
				if(lineA.length()!=0) vAnswer.add(lineA);
			}
		}catch(IOException io){}
	}
	void show(){
		if(vQuest.size()>0){
			//vQuest에 들어잇는 데이터 개수가 1개 이상이면 1개를 제거한다.
			random = r.nextInt(vQuest.size());//random에 vQuest의 크기 안의 숫자를 랜덤으로 대입한다.
				qp.quizL.setText(vQuest.get(random));
				qp.hintL.setText(vHint.get(random));
		}
	}
	void removeItem(){
		vQuest.remove(random);
		vHint.remove(random);
		vAnswer.remove(random);
	}
	public static void main(String[] args)
	{
		new Question().init(); 
	}
}
