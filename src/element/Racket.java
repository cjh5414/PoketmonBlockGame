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
	private int racketSpeed = 3; 		// ���� �����Ҽ��� ������.
	private int type = 4;					// ���� ����
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
		// ��� ���� ������ ����.
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
	
	// Ű�� released�̸� wait
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
				isMove();									//Ű�� �������� �˻�
				if(leftKeyPressed && getX()>0) {			//�Է��� Ű������ �̵�. ���� �浹�ϸ� �������� ����.
					setLocation(getX()-3,getY());
					if(!isStageStart) ball.leftMove();
				}
				else if(rightKeyPressed && getX()<MapPanel.MAPWIDTH-getWidth()) {
					setLocation(getX()+3,getY());
					if(!isStageStart) ball.rightMove();
				}
				Thread.sleep(racketSpeed);					//�����̴� �ӵ�
			}catch(InterruptedException e) { return; }
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
