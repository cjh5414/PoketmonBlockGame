package mapEdit;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import element.Block;
import element.ItemBlock;
import element.SpecialBlock;
import element.Stage;
import game.MyPanel;

class MapPanel extends MyPanel {
	private Stage stage; // ���簡���� �ִ� Stage
	private boolean check; // �ִ��� ������ Check Flag
	private int direction;	

	MapPanel(int level, int time, int type) {
		super(Stage.STAGEBACKGROUNDIMAGE[type]);
		setSize(900, 600);
		setLocation(0, 0);
		stage = new Stage(level, time, type);
		// Ŭ�������� ���߰�.
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (MapEditPanel.selectBlock != null) {
					int x = (e.getX() / 60) * 60;
					int y = (e.getY() / 30) * 30;
					if (e.getButton() == e.BUTTON1) { // Left Clicked Event
						check = false;
						direction = 0;
						// Check BlOCK
						for (int i = 0; i < stage.getBlock().size(); i++) {
							if (x == stage.getBlock().get(i).getX()
									&& y == stage.getBlock().get(i).getY()) {
								check = true;
							}
						}
						if (check == false) {
							switch (MapEditPanel.selectBlock.getClass()
									.getName()) {
							case "element.Block":
								stage.addBlock(new Block(x, y, 60, 30,
										MapEditPanel.selectBlock.getType()));
								add(stage.getBlock().get(
										stage.getBlock().size() - 1));
								break;
							case "element.ItemBlock":
								stage.addBlock(new ItemBlock(x, y, 60, 30,
										MapEditPanel.selectBlock.getType()));								
								add(stage.getBlock().get(
										stage.getBlock().size() - 1));
								break;
							case "element.SpecialBlock":
								//SpecialBlock ADD
								if(MapEditPanel.selectBlock.getType()<1000){
									stage.addBlock(new SpecialBlock(x, y, 60, 30,MapEditPanel.selectBlock.getType()));							
									add(stage.getBlock().get(stage.getBlock().size() - 1));
								}
								//WarpBlock ADD
								else{
									stage.addBlock(new SpecialBlock(x, y, 60, 30,1000+stage.getWarpBlockIndex()));
									add(stage.getBlock().get(stage.getBlock().size() - 1));
									stage.addWarpBlockIndex();
									MapEditPanel.selectBlock = new SpecialBlock(x, y, 60, 30,1000+stage.getWarpBlockIndex());
								}
								break;
							}
						}
					} else { // Right Clicked Event
						direction = 1;
						for (int i = 0; i < stage.getBlock().size(); i++) {
							// IF(YES BLOCK) REMOVE BLOCK
							if (x == stage.getBlock().get(i).getX()
									&& y == stage.getBlock().get(i).getY()) {
								remove(stage.getBlock().get(i));
								stage.getBlock().remove(i);
							}
						}
					}
					repaint();
				}
			}
			@Override
			public void mouseExited(MouseEvent e){
				if(	MapEditPanel.selectBlock != null && MapEditPanel.selectBlock.getType() >= 1000 && (MapEditPanel.selectBlock.getType()%2 == 1)){
					
					stage.minusWarpBlockIndex();
					MapEditPanel.selectBlock = new SpecialBlock(0,0,60,30,1000+stage.getWarpBlockIndex());
					remove(stage.getBlock().get(stage.getBlock().size() - 1));
					stage.getBlock().remove(stage.getBlock().size()-1);
					repaint();
				}
			}
		});// MouseListener END
			// �ѹ��� ���� �� �ֱ�
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
					int x = (e.getX() / 60) * 60;
					int y = (e.getY() / 30) * 30;
				if (MapEditPanel.selectBlock != null && direction == 0) {					
						check = false;
						// Check BlOCK
						for (int i = 0; i < stage.getBlock().size(); i++) {
							if (x == stage.getBlock().get(i).getX()
									&& y == stage.getBlock().get(i).getY()) {
								check = true;
							}
						}
						if (check == false) {
							switch (MapEditPanel.selectBlock.getClass()
									.getName()) {
							case "element.Block":
								stage.addBlock(new Block(x, y, 60, 30,
										MapEditPanel.selectBlock.getType()));
								add(stage.getBlock().get(
										stage.getBlock().size() - 1));
								break;
							case "element.ItemBlock":
								stage.addBlock(new ItemBlock(x, y, 60, 30,
										MapEditPanel.selectBlock.getType()));								
								add(stage.getBlock().get(
										stage.getBlock().size() - 1));
								break;
							case "element.SpecialBlock":
								//SpecialBlock ADD
								if(MapEditPanel.selectBlock.getType()<1000){
									stage.addBlock(new SpecialBlock(x, y, 60, 30,MapEditPanel.selectBlock.getType()));							
									add(stage.getBlock().get(stage.getBlock().size() - 1));
								}
								//WarpBlock ADD
								else{
									stage.addBlock(new SpecialBlock(x, y, 60, 30,1000+stage.getWarpBlockIndex()));
									add(stage.getBlock().get(stage.getBlock().size() - 1));
									stage.addWarpBlockIndex();
									MapEditPanel.selectBlock = new SpecialBlock(x, y, 60, 30,1000+stage.getWarpBlockIndex());
								}
								break;
							}
						}
				}
				else if(MapEditPanel.selectBlock != null && direction == 1){ // Right Clicked Event						
							for (int i = 0; i < stage.getBlock().size(); i++) {
							// IF(YES BLOCK) REMOVE BLOCK
							if (x == stage.getBlock().get(i).getX()
									&& y == stage.getBlock().get(i).getY()) {
								remove(stage.getBlock().get(i));
								stage.getBlock().remove(i);
						}
					}
				}
					repaint();
			}
		});// MouseMotionListener End
	}

	// Stage���ȭ�� �ٲٴ� Method
	void changeBackground(int type) {
		super.setBgImage(Stage.STAGEBACKGROUNDIMAGE[type]);
		super.repaint();
		stage.setBackgroundType(type);
	}

	// Stage �����ϴ� Method
	void setStage(Stage stage) {
		this.stage = stage;
	}

	// Stage ������ Method
	Stage getStage() {
		return stage;
	}

	// EditMapPanel�� ������ �ִ� ���� �� ���� Method
	void resetEditMapPanel() {
		removeAll();
		repaint();
	}

	// Stage ������ Clear();
	void resetStage() {
		stage.getBlock().clear();
	}

	// �Ű����� Stage�� �������� MapPanel�� �ٽ� �׸��� Method
	void drawStage(Stage stage) {
		for (int i = 0; i < stage.getBlock().size(); i++) {
			Block temp = stage.getBlock().get(i);
			temp.setBounds(temp.getX(), temp.getY(), 60, 30);
			add(temp);
		}
	}
}
