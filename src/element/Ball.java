package element;

import game.*;

import java.awt.*;
import javax.swing.*;

public class Ball extends JLabel{
	public static final String BALLIMAGE[] = {
		"images/ball/ball1.png",
		"images/ball/ball2.png",
		"images/ball/ball3.png",
		"images/ball/ball4.png",
		"images/ball/ball5.png",
		"images/ball/ball6.jpg",
		"images/ball/ball7.png",
	};
	private static final int STARTX = 450;
	private static final int STARTY = 670;
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	public static final int COLLIDEDIFF = WIDTH;  // 충돌할 때 ball 이 충돌했는지 허용 여분길이
	
	private int type;
	ImageIcon image;
	
	private static int MOVESIZE_X = 2;		// MOVESIZE_X 크기 만큼 번 중에 1번 움직임(3번중에 1번 움직임)
	private static int MOVESIZE_Y = 2;
	private static int MOVECOUNT_X = 0;
	private static int MOVECOUNT_Y = 0;
	private static int ISMOVE_X = 1;
	private static int ISMOVE_Y = 1;
	
	public static final int RIGHTUP = 0;
	public static final int LEFTUP = 1;
	public static final int LEFTDOWN =2;
	public static final int RIGHTDOWN = 3;
	public static final int UPCOLLIDE = 4;
	public static final int LEFTCOLLIDE = 5;
	public static final int DOWNCOLLIDE = 6;
	public static final int RIGHTCOLLIDE = 7;
	public static final int NOTCOLLIDE = 999;
	
	int direction = Ball.RIGHTUP;
	
	public Ball(int type) {
		this.type = type;
		initBall();
		image = new ImageIcon(BALLIMAGE[type]);
	}
	public void setImage(int type) {
		image = new ImageIcon(BALLIMAGE[type]);
	}
	public void initBall() {
		setBounds(STARTX,STARTY,WIDTH,HEIGHT);
	}
	public void leftMove() {
		setLocation(getX()-3,getY());
	}
	public void rightMove() {
		setLocation(getX()+3,getY());
	}
	public int getDirection() {
		return direction;
	}
	public void move() {
		switch(direction) {
		case RIGHTUP:
			setLocation(getX()+ISMOVE_X,getY()-ISMOVE_Y);
			break;
		case LEFTUP:
			setLocation(getX()-ISMOVE_X,getY()-ISMOVE_Y);
			break;
		case LEFTDOWN:
			setLocation(getX()-ISMOVE_X,getY()+ISMOVE_Y);
			break;
		case RIGHTDOWN:
			setLocation(getX()+ISMOVE_X,getY()+ISMOVE_Y);
			break;
		}
		MOVECOUNT_X = (MOVECOUNT_X+1)%MOVESIZE_X;
		if(MOVECOUNT_X==MOVESIZE_X-1) ISMOVE_X=1;
		else ISMOVE_X=0;
		MOVECOUNT_Y = (MOVECOUNT_Y+1)%MOVESIZE_Y;
		if(MOVECOUNT_Y==MOVESIZE_Y-1) ISMOVE_Y=1;
		else ISMOVE_Y=0;
	}
	
	public void changeAngle(int boundPosition) {		// 공이 튕긴 라켓의 위치에 따라 각도 변경	
		switch(boundPosition) {
		case 0:
			MOVESIZE_X = 1;
			MOVESIZE_Y = 3;
			direction = Ball.LEFTUP;
			break;
		case 1:
			MOVESIZE_X = 2;
			MOVESIZE_Y = 3;
			direction = Ball.LEFTUP;
			break;
		case 2:
			MOVESIZE_X = 2;
			MOVESIZE_Y = 2;
			direction = Ball.LEFTUP;
			break;
		case 3:
			MOVESIZE_X = 3;
			MOVESIZE_Y = 2;
			direction = Ball.LEFTUP;
			break;
		case 4:
			MOVESIZE_X = 3;
			MOVESIZE_Y = 1;
			direction = Ball.LEFTUP;
			break;
		case 5:
			MOVESIZE_X = 3;
			MOVESIZE_Y = 1;
			direction = Ball.RIGHTUP;
			break;
		case 6:
			MOVESIZE_X = 3;
			MOVESIZE_Y = 2;
			direction = Ball.RIGHTUP;
			break;
		case 7:
			MOVESIZE_X = 2;
			MOVESIZE_Y = 2;
			direction = Ball.RIGHTUP;
			break;
		case 8:
			MOVESIZE_X = 2;
			MOVESIZE_Y = 3;
			direction = Ball.RIGHTUP;
			break;
		case 9:
			MOVESIZE_X = 1;
			MOVESIZE_Y = 3;
			direction = Ball.RIGHTUP;
			break;
		}
		ISMOVE_X=1;
		ISMOVE_Y=1;
	}
	
	public void changeDirection(int collidePosition) { // collidePosition은 충돌한 위치.
		switch(direction) {
		case RIGHTUP:
			if(collidePosition==Ball.RIGHTCOLLIDE) 		   // 공이 반시계방향으로 도는 경우
				direction = Ball.LEFTUP;
			else									   // 공이 시계방향으로 도는 경우
				direction = Ball.RIGHTDOWN;
			break;
		case LEFTUP:
			if(collidePosition==Ball.UPCOLLIDE)
				direction = Ball.LEFTDOWN;
			else
				direction = Ball.RIGHTUP;
			break;
		case LEFTDOWN:
			if(collidePosition==Ball.LEFTCOLLIDE)
				direction = Ball.RIGHTDOWN;
			else
				direction = Ball.LEFTUP;
			break;
		case RIGHTDOWN:
			if(collidePosition==Ball.DOWNCOLLIDE)
				direction = Ball.RIGHTUP;
			else
				direction = Ball.LEFTDOWN;
			break;
		}
		ISMOVE_X=1;
		ISMOVE_Y=1;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getImage(), 0, 0, WIDTH, HEIGHT, this);
	}

}
