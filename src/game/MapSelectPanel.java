package game;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import element.*;

class MapSelectPanel extends MyPanel {
	private Vector<MapSet> mapSetVector; 
	private MapPanel mapPanel;
	private int mapSetIndex = 0;								// �㼼Ʈ index 0���� ����.
	private int mapSetSize;										// �ʼ�Ʈ ���� ����.								
	private JLabel mapSetName;							
	private BTN leftButton;									// ���� �㼼Ʈ�� �̵�
	private BTN rightButton;									// ���� �ʼ�Ʈ�� �̵�
	MapSelectPanel(String bgImage, Vector<MapSet> mapSetVector, MapPanel mapPanel) {
		super(bgImage);
		this.mapSetVector = mapSetVector;
		this.mapPanel = mapPanel;
		mapSetSize = mapSetVector.size();	
		mapSetName = new JLabel(mapSetVector.get(mapSetIndex).getName()); // ù��° �㼼Ʈ�� �̸����� �ʱ�ȭ.
		mapSetName.setFont(new Font("Times", Font.ITALIC, 50));
		mapSetName.setBounds(600, 30, 500, 100);
		add(mapSetName);
		leftButton = new BTN(100, 400, 135, 135,new ImageIcon(BTN.PREVBTNIMAGE[0]));		
		add(leftButton);
		rightButton = new BTN(1133, 400, 135, 135,new ImageIcon(BTN.NEXTBTNIMAGE[0]));	
		add(rightButton);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
					mapChange(e.getKeyCode());		//�Է��� ����Ű��� �㼼Ʈ ����.
					break;
				case KeyEvent.VK_ENTER:
					GamePanel.changePanel("playPanel");
					break;
				}
			}
		});
	}

	void mapSelectRun() {
		for(int i=0; i<mapSetVector.size(); i++){
			System.out.println(i+"  " +mapSetVector.get(i).getName());
			System.out.println(i+"  " +mapSetVector.get(i).getStage().size());
		}
		mapSetSize = mapSetVector.size();
		System.out.println("mapSetSize " +mapSetSize);
		mapSetIndex = 0;
		mapPanel.drawStage(mapSetVector.get(0).getStage().get(0), null, null);
		mapPanel.setLocation(230,200);
		add(mapPanel);
	}
	//�㼼Ʈ�� �¿�� ����.
	private void mapChange(int direction) {
		if(direction==KeyEvent.VK_LEFT)
			mapSetIndex = (mapSetIndex - 1) >= 0 ? (mapSetIndex - 1) : mapSetSize-1;
		else
			mapSetIndex = (mapSetIndex + 1) % mapSetSize;
		System.out.println("MAPSETINDEX " + mapSetIndex);
		System.out.println("mapSetSize " +mapSetSize);
		for(int i=0; i<mapSetVector.size(); i++){
			System.out.println(i + " : " + mapSetVector.get(i).getName());
			System.out.println("Stage ���� : " + mapSetVector.get(i).getStage().size());
		}
		MapSet presentMapSet = mapSetVector.get(mapSetIndex);
		System.out.println("���������"+presentMapSet.getStage().get(0).getBlock().size());
		
		mapPanel.drawStage(presentMapSet.getStage().get(0), null, null);
		mapSetName.setText(presentMapSet.getName());
	}
	
	MapSet getSelectedMapSet() {
		return mapSetVector.get(mapSetIndex);
	}
}