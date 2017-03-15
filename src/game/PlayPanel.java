package game;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import element.*;

class PlayPanel extends MyPanel {
	private MapPanel mapPanel;
	private TimerPanel timerPanel;
	private LevelPanel levelPanel;
	private ItemPanel itemPanel;
	private MessageLabel messageLabel = new MessageLabel();
	
	private MapSet mapSet;							//현재 맵세트.
	private Vector<Stage> stageSet;					//현재 멥세트의 스테이지들.
	private int stageIndex;							//현재 스테이지 index
	private Vector<Block> blockSet;					//현재 스테이지의 블락Set
	private Vector<Item> moveItemVector = new Vector<Item>();
	private int haveItemNum = 0;
	
	private Ball ball;
	private Racket racket;
	private Thread racketThread;
	private GameThread gameThread;
	private ItemThread itemThread;
	
	private int ballFastSpeed = 5;		// 최고속도
	private int ballSlowSpeed = 1;		// 최저속도 (8)
	private int ballSpeed = ballFastSpeed;
	
	private boolean isStageStart;
	private boolean isItemUsing;
	private int stageLevel;
	private int stageTime;
	private int stageTimeCount;	
	
	PlayPanel(String bgImage, MapPanel mapPanel) {
		super(bgImage);
		this.mapPanel = mapPanel;
		
		setComponentZOrder(messageLabel, 0);
		
		
		levelPanel = new LevelPanel();
		timerPanel = new TimerPanel();
		itemPanel = new ItemPanel();
		
		levelPanel.setLocation(910,100);
		levelPanel.setSize(300,300);
		add(levelPanel);
		timerPanel.setLocation(980,300);
		timerPanel.setSize(120,200);
		add(timerPanel);
		itemPanel.setLocation(910,550);
		itemPanel.setSize(300,300);
		add(itemPanel);
		
		this.ball = new Ball(0);
		this.racket = new Racket(4, ball);
		
		gameThread = new GameThread();
		itemThread = new ItemThread();
		racketThread = new Thread(racket);
		
		racketThread.start();
		gameThread.start();
		itemThread.start();
		addKeyListener(new GameKeyListener());
	}
	
	void playPanelRun(MapSet mapSet) {
		this.mapSet = mapSet;
		this.stageSet = mapSet.getStage();
		isStageStart = false;		
		stageIndex = 0;
		mapPanel.setLocation(0,0);
		add(mapPanel);
		
		gameStart();
	}

	private void gameStart() {
		stageReady();		
		requestFocus();
	}
	
	private void timeCheck() {
		stageTimeCount++;
		if(stageTimeCount==100) {
			stageTime--;
			timerPanel.setTime(stageTime);
			stageTimeCount=0;
		}
		if(stageTime==0) gameOver();							
	}
	private void gameOver() {
		isStageStart = false;
		showMessage("Game Over!!");
		GameManager.changePanel("basePanel");
	}
	
	// 현재 멥세트의 모든 스테이지 완료.
	private void mapClear() {
		isStageStart = false;
		showMessage(mapSet.getName()+" Clear!!");
		GameManager.changePanel("basePanel");
	}
	
	// specailBlock이 아닌 모든 block의 life가 0이면 스테이지 완료.
	private void isStageClear() {
		for(int i=0; i<blockSet.size(); i++) {
			Block block = blockSet.get(i); 
			if(block instanceof SpecialBlock) {
				continue;
			}
			else {
				if(block.getLife() > 0)
					return;
			}
		}
		// 블럭이 모두 부셔지면
		if(stageIndex+1==stageSet.size()) {
			mapClear();
			return;
		}
		showMessage("Stage Clear!!");
		nextStage();
	}
	private void showMessage(String message) {
		messageLabel.setText(message);
		add(messageLabel);
		setComponentZOrder(messageLabel, 0);
		gameThread.messageWait();	// 2초 보여줌.
		remove(messageLabel);
	}
	// 다음 스테이지.
	private void nextStage() {
		isStageStart = false;
		stageIndex++;		

		stageReady();
	}
	
	// space bar 누르면 stage시작.
	private void stageReady() {
		isStageStart = false;
		ball.initBall();
		racket.initRacket();
		moveItemVector.clear();
		haveItemNum = 0;
		blockSet = stageSet.get(stageIndex).getBlock();
		stageTime = stageSet.get(stageIndex).getTime();
		stageLevel = stageSet.get(stageIndex).getLevel();
		mapPanel.drawStage(stageSet.get(stageIndex), racket, ball);
		stageTimeCount = 0;
		timerPanel.setTime(stageTime);
		levelPanel.setLevel(stageLevel);
		itemPanel.setItemNum(haveItemNum);
		blockSetInit();
		isItemUsing = false;
	}
	
	private void blockSetInit() {
		for(int i=0; i<blockSet.size(); i++) {
			Block block = blockSet.get(i); 
			if(block instanceof SpecialBlock) {
				continue;
			}
			else {
				block.setLife();
			}
		}
	}
	
	private void stageStart() {
		isStageStart = true;
		racket.stageStart();
	}
	
	private void collideBlock() {
		int collidePosition = Ball.NOTCOLLIDE;		// ball 가 충돌한 방향
		
		for(int i = 0; i < blockSet.size(); i++) {
			Block block = blockSet.get(i);
			if (block.getLife() != 0) {
				if (ball.getX() >= block.getX() - Ball.COLLIDEDIFF
					&& ball.getX()+ball.getWidth() <= block.getX() + block.getWidth() + Ball.COLLIDEDIFF) {
					// ball 이 벽돌의 아랫면과 부딪혔을 경우.
					if(ball.getY() - (block.getY() + block.getHeight()) == -1 ) {
						if(block instanceof SpecialBlock) {
							if(block.getType()>=1000) { //warp block 인 경우
								ball.setLocation(((SpecialBlock)block).getPairBlock().getX()+block.getWidth()/2-ball.getWidth()/2,
										((SpecialBlock)block).getPairBlock().getY()-ball.getHeight());
								break;
							}
							else {
								switch(block.getType()) {
								case 0: break;
								}	
							}
						}
						else if(block instanceof ItemBlock) {
							itemHit((ItemBlock)block);
						}
						else { 
							block.decreaseLife();
						}	
						collidePosition = Ball.UPCOLLIDE;
					}
					// ball 이 벽돌의 윗면과 부딪혔을 경우.
					else if((ball.getY()+ball.getHeight()) - block.getY() == 1) {
						if(block instanceof SpecialBlock) {
							if(block.getType()>=1000) { //warp block 인 경우
								ball.setLocation(((SpecialBlock)block).getPairBlock().getX()+block.getWidth()/2-ball.getWidth()/2,
										((SpecialBlock)block).getPairBlock().getY()+block.getHeight());
								break;
							}
							else {
								switch(block.getType()) {
								case 0: break;
								}	
							}							
						}
						else if(block instanceof ItemBlock) {
							itemHit((ItemBlock)block);
						}
						else { 
							block.decreaseLife();
						}
						collidePosition = Ball.DOWNCOLLIDE;
					}
				}
				else if(ball.getY() >= block.getY() - Ball.COLLIDEDIFF
					&& ball.getY() + ball.getHeight() <= block.getY() + block.getHeight() + Ball.COLLIDEDIFF) {
					// ball 이 벽돌의 왼쪽면과 부딪혔을 경우.
					if( (ball.getX() + ball.getWidth()) - block.getX() == -1) {
						if(block instanceof SpecialBlock) {
							switch(block.getType()) {
							case 0: break;
							}							
						}
						else if(block instanceof ItemBlock) {
							itemHit((ItemBlock)block);
						}
						else { 
							block.decreaseLife();
						}
						collidePosition = Ball.RIGHTCOLLIDE;
					}
					// ball 이 벽돌의 오른쪽면과 부딪혔을 경우.
					else if(ball.getX() - (block.getX() + block.getWidth()) == 1) {
						if(block instanceof SpecialBlock) {
							switch(block.getType()) {
							case 0: break;
							}				
						}
						else if(block instanceof ItemBlock) {
							itemHit((ItemBlock)block);
						}
						else { 
							block.decreaseLife();
						}
						collidePosition = Ball.LEFTCOLLIDE;
					}				
				}
			}
		}
		//충돌 검사 후 ball 방향 변경
		if(collidePosition!=Ball.NOTCOLLIDE)
			ball.changeDirection(collidePosition);
		repaint();
	}
	// 
	private void itemHit(ItemBlock block) {
		block.decreaseLife();
		Item item = new Item(block.getX()+20, block.getY(), block.getType());
		moveItemVector.add(item);
		mapPanel.add(item);
	}
	private void collideRacket() {
		// racket에 ball 이 튕긴 위치에 따라 ball의 각도 변경.
		if(Math.abs(ball.getY()+ball.getHeight()-racket.getY()) == 0) {
			int diffX = ball.getX() - racket.getX();  // ball 과 racket의 x좌표 차이
			int divSize = racket.getWidth()/10;
			
			if(diffX > -1*(Ball.COLLIDEDIFF) && diffX <= divSize) // 맨 왼쪽
				ball.changeAngle(0);
			else if(diffX > divSize*9 && diffX <= divSize*10+(Ball.COLLIDEDIFF)) // 맨 오른쪽
				ball.changeAngle(9);
			else { // 가운데 여덟개 부분.
				for(int i=1; i<9; i++) {
					if(diffX > divSize*i && diffX <= divSize*(i+1)) {
						ball.changeAngle(i);
						return;
					}
				}
			}
		}
		// racket 옆면에 ball이 부딛힌 경우
		else if(ball.getY()+ball.getHeight() > racket.getY() && ball.getY()+ball.getHeight() <= racket.getY()+racket.getHeight()) {
			if(Math.abs(ball.getX()+ball.getWidth() - racket.getX())==0)  
				ball.changeAngle(0);			
			else if(Math.abs(ball.getX() - (racket.getX()+racket.getWidth()))==0)
				ball.changeAngle(9);
		}
	}

	private void itemCollideRacket(Item item) {
		if(item.getX()+item.getWidth()>racket.getX() && item.getX() < racket.getX() + racket.getWidth() 
				&& item.getY() + item.getHeight() > racket.getY() && item.getY() < racket.getY() + racket.getHeight()) {
			racket.changeRacket(item.getType());
			mapPanel.remove(item);
			moveItemVector.remove(item);
			itemPanel.setItemNum(++haveItemNum);
		}
	}

	private void collideWall() {
		if(ball.getX() >= MapPanel.MAPWIDTH-ball.getWidth()) {
			if(ball.getX()==MapPanel.MAPWIDTH) ball.setLocation(ball.getX()-1, ball.getY()); // 워프벽돌이 벽에 붙어있는 경우.
			ball.changeDirection(Ball.RIGHTCOLLIDE);
		}
		else if(ball.getY() <= 0) {
			if(ball.getY()==-ball.getWidth()) ball.setLocation(ball.getX(), ball.getWidth()+1); // 워프벽돌이 벽에 붙어있는 경우.
			ball.changeDirection(Ball.UPCOLLIDE);
		}
		else if(ball.getX() <= 0) {
			if(ball.getX()==-ball.getWidth()) ball.setLocation(ball.getWidth()+1, ball.getY()); // 워프벽돌이 벽에 붙어있는 경우.
			ball.changeDirection(Ball.LEFTCOLLIDE);
		}
		else if(ball.getY() >= MapPanel.MAPHEIGHT-ball.getHeight()) {
			if(ball.getY()==MapPanel.MAPHEIGHT) ball.setLocation(ball.getX(), ball.getY()-1); // 워프벽돌이 벽에 붙어있는 경우.
			ball.changeDirection(Ball.DOWNCOLLIDE);
		}
	}


	private void itemUse() {
		if(haveItemNum==0 || isItemUsing==true) return;
		switch(racket.getType()) {
		case 0:		// 피카츄
			isItemUsing = true;
			itemThread.itemPica();
			break;
		case 1:		// 꼬부기
			isItemUsing = true;
			itemThread.itemGgobook();
			break;
		case 2:		// 이상해씨
			isItemUsing = true;
			itemThread.itemEsang();
			break;
		case 3:		// 파이리
			isItemUsing = true;
			itemThread.itemPie();
			break;
		case 4:		// 붐볼
			isItemUsing = true;
			itemThread.itemBoomBall();
			break;
		}
		itemPanel.setItemNum(--haveItemNum);
	}
	class ItemThread extends Thread {
		private ItemLabel picaLabel = new ItemLabel(80, 130, Item.ITEMUSE[0]);
		private ItemLabel ggoBookLabel = new ItemLabel(110, 110, Item.ITEMUSE[1]);
		private ItemLabel [] esangLabel = { new ItemLabel(30, 30, Item.ITEMUSE[2]),
				new ItemLabel(30, 30, Item.ITEMUSE[5]),
				new ItemLabel(30, 30, Item.ITEMUSE[6]),
				new ItemLabel(30, 30, Item.ITEMUSE[7]),
				new ItemLabel(30, 30, Item.ITEMUSE[8]), };
		private ItemLabel boomLabel = new ItemLabel(110, 110, Item.ITEMUSE[4]);
		private int stopWatch = 0;
		private int endTime = 0;
		private boolean [] isEsangCollide = { false, false, false, false, false };
		private boolean [] isDrawing = { false, false, false, false, false };
		@Override
		public void run() {
			while(true) {
				System.out.println("");
				try {
					sleep(1);
					stopWatch++;
					if(isDrawing[0]==true && stopWatch==endTime) {  // 피카츄
						mapPanel.remove(picaLabel);
						isDrawing[0] = false;
						isItemUsing = false;
					}
					if(isDrawing[1]==true) {						// 꼬부기
						ggoBookLabel.setLocation(ggoBookLabel.getX(), ggoBookLabel.getY()-1);
						if(isGgoBookCollideBlock()) {
							mapPanel.remove(ggoBookLabel);
							isDrawing[1] = false;
							isItemUsing = false;
						}
					}
					if(isDrawing[2]==true) {  // 이상해씨
						isEsangCollideBlock();
						int i=0;
						for(i=0; i<esangLabel.length; i++) {
							esangLabel[i].setLocation(esangLabel[i].getX(), esangLabel[i].getY()-1);
							if(esangLabel[i].getY()+esangLabel[i].getHeight() <= 0) {
								isEsangCollide[i] = true;
								mapPanel.remove(esangLabel[i]);
							}
						}
						for(i=0; i<isEsangCollide.length; i++)
							if(!isEsangCollide[i]) break;
						if(i==isEsangCollide.length) {
							for(i=0; i<isEsangCollide.length; i++)
								isEsangCollide[i] = false;
							isDrawing[2] = false;
							isItemUsing = false;
						}
					}					
					if(isDrawing[3]==true && stopWatch==endTime) {  // 파이리
						racket.setSize(racket.WIDTH, racket.HEIGHT);
						racket.setImage(3);
						isDrawing[3] = false;
						isItemUsing = false;
					}
					if(isDrawing[4]==true && stopWatch==endTime) {  // 붐볼
						mapPanel.remove(boomLabel);
						isDrawing[4] = false;
						isItemUsing = false;
					}
					
				} catch(InterruptedException e) { return; }
			}
		}
		void itemTimeSet(int type, int time) {  // time 밀리초동안 사용한 아이템 이미지 그리기.
			isDrawing[type] = true;
			stopWatch=0;
			this.endTime = time;
		}
		boolean isGgoBookCollideBlock() {
			boolean result = false;
			if(ggoBookLabel.getY()+ggoBookLabel.getHeight() <= 0)
				return true;
			for(int i=0; i<blockSet.size(); i++) {
				Block block = blockSet.get(i);
				if(block.getLife()!=0) {
					if(block instanceof SpecialBlock) continue;
					else {
						int bLeftX = block.getX();
						int bRightX = block.getX()+block.getWidth();
						int gLeftX = ggoBookLabel.getX();
						int gRightX = ggoBookLabel.getX()+ggoBookLabel.getWidth();
						int bRightY = block.getY()+block.getHeight();
						int gLeftY = ggoBookLabel.getY();
						if( (Math.abs(bRightY-gLeftY)==0 || Math.abs(bRightY+block.getHeight()-gLeftY)<=30 ) &&
							((bRightX>gLeftX&&bRightX<gRightX) || (bLeftX>gLeftX&&bLeftX<gRightX)) ) {
								if(block instanceof ItemBlock) itemHit((ItemBlock)block);
								else block.removeBlock();
								result = true;
						}
					}			
				}
			}
			return result;
		}
		void isEsangCollideBlock() {
			for(int j=0; j<esangLabel.length; j++) {
				if(!isEsangCollide[j]) {
					for(int i=0; i<blockSet.size(); i++) {
						Block block = blockSet.get(i);
						if(block.getLife()!=0) {
							if(block instanceof SpecialBlock) continue;
							else {
								int bLeftX = block.getX();
								int bRightX = block.getX()+block.getWidth();
								int eLeftX = esangLabel[j].getX();
								int eRightX = esangLabel[j].getX()+esangLabel[j].getWidth();
								int bRightY = block.getY()+block.getHeight();
								int eLeftY = esangLabel[j].getY();
								if( (Math.abs(bRightY-eLeftY)==0 ) &&
										(eLeftX>=bLeftX && eLeftX<bRightX) ) {
										if(block instanceof ItemBlock) itemHit((ItemBlock)block);
										else block.removeBlock();
										isEsangCollide[j] = true;
										mapPanel.remove(esangLabel[j]);
								}
							}
						}
					}			
				}
			}
		}		
		void itemPica() {
			int x = (int)(Math.random()*(MapPanel.MAPWIDTH-picaLabel.getWidth()));
			int y = (int)(Math.random()*(MapPanel.MAPHEIGHT-picaLabel.getHeight()-200));
			picaLabel.setLocation(x,y);
			mapPanel.add(picaLabel);
			mapPanel.setComponentZOrder(picaLabel, 0);
			itemTimeSet(0, 1000);
			for(int i=0; i<blockSet.size(); i++) {
				Block block = blockSet.get(i);
				if(block.getLife()!=0) {
					if(block instanceof SpecialBlock) continue;
					else {
						int bLeftX = block.getX();
						int bRightX = block.getX()+block.getWidth();
						int pLeftX = picaLabel.getX();
						int pRightX = picaLabel.getX()+picaLabel.getWidth();
						int bLeftY = block.getY();
						int bRightY = block.getY()+block.getHeight();
						int pLeftY = picaLabel.getY();
						int pRightY = picaLabel.getY()+picaLabel.getHeight();
						if( ((bRightX>pLeftX&&bRightX<pRightX) || (bLeftX>pLeftX&&bLeftX<pRightX))
								&& ((bRightY>pLeftY&&bRightY<pRightY) || (bLeftY>pLeftY&&bLeftY<pRightY)) )
							if(block instanceof ItemBlock) itemHit((ItemBlock)block);
							else block.removeBlock();
					}			
				}
			}
		}
		void itemGgobook() {
			ggoBookLabel.setLocation(racket.getX()+29, racket.getY()-ggoBookLabel.getHeight());
			mapPanel.add(ggoBookLabel);
			mapPanel.setComponentZOrder(ggoBookLabel, 0);
			isDrawing[1] = true;
		}
		void itemEsang() {
			for(int i=0; i<esangLabel.length; i++) {
				int x = (int)(Math.random()*(MapPanel.MAPWIDTH-esangLabel[i].getWidth()));
				esangLabel[i].setLocation(x, racket.getY()-esangLabel[i].getHeight());
				mapPanel.add(esangLabel[i]);
				mapPanel.setComponentZOrder(esangLabel[i], 0);
			}
			isDrawing[2] = true;
		}
		void itemPie() { 
			racket.setSize(racket.WIDTH*3, racket.HEIGHT);
			racket.setImage(6);
			itemTimeSet(3,3000);
		}
		void itemBoomBall() {
			int x = ball.getX()-boomLabel.getWidth()/2+ball.getWidth()/2;
			int y = ball.getY()-boomLabel.getHeight()/2+ball.getHeight()/2;
			boomLabel.setLocation(x,y);
			mapPanel.add(boomLabel);
			mapPanel.setComponentZOrder(boomLabel, 0);
			
			itemTimeSet(4, 500);
			for(int i=0; i<blockSet.size(); i++) {
				Block block = blockSet.get(i);
				if(block.getLife()!=0) {
					if(block instanceof SpecialBlock) continue;
					else {
						int bLeftX = block.getX();
						int bRightX = block.getX()+block.getWidth();
						int gLeftX = boomLabel.getX();
						int gRightX = boomLabel.getX()+boomLabel.getWidth();
						int bLeftY = block.getY();
						int bRightY = block.getY()+block.getHeight();
						int gLeftY = boomLabel.getY();
						int gRightY = boomLabel.getY()+boomLabel.getHeight();
						if( ((bRightX>gLeftX&&bRightX<gRightX) || (bLeftX>gLeftX&&bLeftX<gRightX))
								&& ((bRightY>gLeftY&&bRightY<gRightY) || (bLeftY>gLeftY&&bLeftY<gRightY)) )
							if(block instanceof ItemBlock) itemHit((ItemBlock)block);
							else block.decreaseLife();
					}			
				}
			}
		}
	}
	class GameThread extends Thread {
		@Override
		public void run() {
			while(true) {
				System.out.println("");
				if(isStageStart) {
					try {
						sleep(10);
						for(int i=0; i<ballSpeed; i++)
							ballMove();	
						timeCheck();		// 시간 증가, 시간 오버하면 종료.
						isStageClear();		// 스테이지 완료하면 다음 스테이지로 이동.
						itemMove();
					} catch (InterruptedException e) { return; }
				}
			}
		}
		public void messageWait() {
			try {
				sleep(2000);
			} catch (InterruptedException e) { return; }
		}
		public void ballMove() {
			collideWall();
			collideBlock();
			collideRacket();
			ball.move();
			if(Math.abs(ball.getY()+ball.getHeight() - MapPanel.MAPHEIGHT) == 0)
	            gameOver();
		}
		public void itemMove() {
			for(int i=0; i<moveItemVector.size(); i++) {
				Item item = moveItemVector.get(i);
				item.setLocation(item.getX(), item.getY()+1);
				itemCollideRacket(item);
				if(item.getY() > MapPanel.MAPHEIGHT) {
					mapPanel.remove(item);
					moveItemVector.remove(item);
				}					
			}
		}
	}

	class ItemLabel extends JLabel {
		private ImageIcon img;
		ItemLabel(int width, int height, String img) {
			this.img = new ImageIcon(img);
			setSize(width, height);
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), this);
		}
	}
	class GameKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) { 
			case KeyEvent.VK_LEFT:
				racket.leftMove();
				break;
			case KeyEvent.VK_RIGHT:
				racket.rightMove();
				break;
			case KeyEvent.VK_SPACE:
				if(!isStageStart) stageStart();
				else itemUse();
				break;
			case KeyEvent.VK_Z:
				if(ballSpeed==ballSlowSpeed) ballSpeed = ballFastSpeed;
				else ballSpeed = ballSlowSpeed;
				break;
			case KeyEvent.VK_ESCAPE:
				break;
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			switch(e.getKeyCode()) { 
			case KeyEvent.VK_LEFT:
				racket.leftStop();
				break;
			case KeyEvent.VK_RIGHT:
				racket.rightStop();
				break;
			}
		}
	}
	class TimerPanel extends JPanel {
		JLabel label;
		JLabel time;
		TimerPanel() {
			setLayout(new FlowLayout());
			this.setOpaque(false);
			label = new JLabel("Time");
			label.setFont(new Font("Times", Font.ITALIC, 50));
			add(label);
			time = new JLabel("");
			time.setFont(new Font("Times", Font.ITALIC, 50));
			add(time);
		}
		void setTime(int time) {
			this.time.setText(Integer.toString(time));
		}
	}
	class LevelPanel extends JPanel {
		JLabel label;
		JLabel level;
		LevelPanel(){
			setLayout(new FlowLayout());
			this.setOpaque(false);
			label = new JLabel("Level ");
			label.setFont(new Font("Times", Font.ITALIC, 50));
			add(label);
			level = new JLabel("");
			level.setFont(new Font("Times", Font.ITALIC, 50));
			add(level);
		}
		void setLevel(int level) {
			this.level.setText(Integer.toString(level));
		}
	}
	class ItemPanel extends JPanel {
		JLabel label;
		JLabel itemNum;
		ItemPanel(){
			this.setOpaque(false);
			setLayout(new FlowLayout());
			label = new JLabel("Item ");
			label.setFont(new Font("Times", Font.ITALIC, 45));
			add(label);
			itemNum = new JLabel("");
			itemNum.setFont(new Font("Times", Font.ITALIC, 50));
			add(itemNum);
		}
		void setItemNum(int itemNum) {
			this.itemNum.setText(Integer.toString(itemNum));
		}
	}
	class MessageLabel extends JLabel {
		MessageLabel() {
			setSize(400,200);
			setLocation(300,300);
			setFont(new Font("Times", Font.ITALIC, 50));
		}
	}
}

	
	