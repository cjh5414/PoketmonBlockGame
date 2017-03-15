package element;

import java.awt.*;
import javax.swing.*;
import game.*;

public class Racket extends JLabel implements Runnable {
	private static final String RACKETIMAGE[] = {
		"images/racket/racket1.jpg",
		"images/racket/racket2.jpg",
		"images/racket/racket3.jpg",
		"images/racket/racket4.jpg",
		"images/racket/racket5.jpg",
		"images/racket/racket6.jpg",
		"images/racket/racket7.png" };
	private static final int STARTX = 400;
	private static final int STARTY = 700;
	public static final int WIDTH = 120;
	public static final int HEIGHT = 30;
	private int racketSpeed = 3; 		// 값이 감소할수록 빨라짐.
	private int type = 4;					// 라켓 종류
	private ImageIcon image;
	
	private Ball ball;
	private boolean isStageStart = false;
	private boolean leftKeyPressed = false;
	public boolean rightKeyPressed = false;
	
	public Racket(int type, Ball ball) {
		this.ball = ball;
		changeRacket(type);
		initRacket();
	}
	public int getType() {
		return type;
	}
	public void changeRacket(int type) {
		this.type = type;
		image = new ImageIcon(RACKETIMAGE[type]);
		ball.setImage(type+1);
		// 사용 가능 아이템 세팅.
	}
	public void setImage(int type) {
		image = new ImageIcon(RACKETIMAGE[type]);
	}
	public void initRacket() {
		changeRacket(5);
		isStageStart = false;
		leftKeyPressed = false;
		rightKeyPressed = false;
		setBounds(STARTX,STARTY,WIDTH,HEIGHT);
	}
	public void stageStart() {
		this.isStageStart = true;
	}
	synchronized public void leftMove() {
		leftKeyPressed = true;
		notify();
	}
	synchronized public void rightMove() {
		rightKeyPressed = true;
		notify();
	}
	public void leftStop() {
		leftKeyPressed = false;	
	}
	public void	rightStop() {
		rightKeyPressed = false;
	}
	
	// 키가 released이면 wait
	synchronized public void isMove() {
		if(!leftKeyPressed && !rightKeyPressed) {
			try{
			wait();
			} catch(InterruptedException e) { return; }
		}
	}
	@Override
	public void run() {
		while(true) {
			try {				
				isMove();									//키가 떼졌는지 검사
				if(leftKeyPressed && getX()>0) {			//입력한 키방향대로 이동. 벽에 충돌하면 움직이지 않음.
					setLocation(getX()-3,getY());
					if(!isStageStart) ball.leftMove();
				}
				else if(rightKeyPressed && getX()<MapPanel.MAPWIDTH-getWidth()) {
					setLocation(getX()+3,getY());
					if(!isStageStart) ball.rightMove();
				}
				Thread.sleep(racketSpeed);					//움직이는 속도
			}catch(InterruptedException e) { return; }
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
