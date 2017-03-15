package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import element.*;

public class GamePanel extends JPanel {
	private MapPanel mapPanel;
	private static MapSelectPanel mapSelectPanel;
	private static PlayPanel playPanel;
	private static CardLayout layoutManager;
	private static Container thisPanel;
	
	GamePanel(String bgImage, Vector<MapSet> mapSet) {
		thisPanel = this;
		mapPanel = new MapPanel(mapSet.get(0).getStage().get(0)); // 첫번째 mapSet의 첫번째 stage로 초기화.
		mapSelectPanel = new MapSelectPanel(bgImage, mapSet, mapPanel);
		playPanel = new PlayPanel(bgImage, mapPanel);
		layoutManager = new CardLayout();
	
		setLayout(layoutManager);
		add("playPanel", playPanel);
		add("mapSelectPanel", mapSelectPanel);
	}
	
	static void changePanel(String panelName) {
		layoutManager.show(thisPanel, panelName);
		switch (panelName) {
		case "mapSelectPanel":
			mapSelectPanel.requestFocus();
			mapSelectPanel.mapSelectRun();
			break;
		case "playPanel":
			playPanel.playPanelRun(mapSelectPanel.getSelectedMapSet());  // 선택한 멤세트로 게임 실행.
			break;
		}
	}
}
