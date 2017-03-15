/*
 * Game Manager
 * 2015.05.19
 * Written by JiHun Choi, YoungJae Kwon 
 */

package game;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.w3c.dom.*;

import xml.*;
import element.*;
import mapEdit.*;

public class GameManager extends JFrame {
	private static final int WIDTH = 1375;
	private static final int HEIGHT = 850;
	private static final String BASEBGIMAGE = "images/background/title2.gif";
	private static final String GAMEBGIMAGE = "images/background/background2.JPG";
	private static final String MPAEDITBGIMAGE = "images/background/background2.jpg";
	private static final String INFOBGIMAGE = "images/background/Title3.gif";
	private static BasePanel basePanel;						// 게임 실행시 기본 화면. 메뉴 선택.
	private static GamePanel gamePanel;				
	private static MapEditPanel mapEditPanel;			
	private static InfoPanel infoPanel;
	private static CardLayout layoutManager;
	private static Container contentPane;
	private Vector<MapSet> mapSetVector;							// 전체 맵세트에 대한 정보
	
	GameManager() {
		XMLEditer xml = new XMLEditer("PoketMonster.xml");		//XML 읽어오기
		mapSetVector = new Vector<MapSet>();
		mapSetInit(xml);									// XML의 맵 정보를 읽어서 Vector에 저장.
		contentPane = getContentPane();
		layoutManager = new CardLayout();					//LayoutManager 생성
		
		//Panel 생성
		basePanel = new BasePanel(BASEBGIMAGE);
		gamePanel = new GamePanel(GAMEBGIMAGE, mapSetVector);
		mapEditPanel = new MapEditPanel(MPAEDITBGIMAGE, xml,mapSetVector);
		infoPanel = new InfoPanel(INFOBGIMAGE);
		
		//CardLayout으로 지정 및 Panel추가.
		setLayout(layoutManager);
		add("basePanel",basePanel);
		add("gamePanel",gamePanel);
		add("mapEditPanel",mapEditPanel);
		add("infoPanel",infoPanel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	
		//BasePanel보여주기.
		changePanel("basePanel");
	}
	
	//panelName으로 패널 바꿈.
	public static void changePanel(String panelName) {		
		layoutManager.show(contentPane, panelName);
		switch(panelName) {
		case "basePanel":
			basePanel.requestFocus();
			break;
		case "gamePanel":
			gamePanel.requestFocus();
			gamePanel.changePanel("mapSelectPanel");
			break;
		case "mapEditPanel":
			mapEditPanel.requestFocus();
			break;
		case "infoPanel":
			infoPanel.requestFocus();
			break;
		}
	}
	
	// 읽어온 xml으로부터 mapSet 초기화. 
	private void mapSetInit(XMLEditer xml) {
		Node mapSetParentNode = xml.getAvengersGameElement();
		NodeList mapSetNodeList = mapSetParentNode.getChildNodes();
		for(int i=0; i<mapSetNodeList.getLength(); i++) {
			Node node = mapSetNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE) // not mapset node
				continue;
			if(node.getNodeName().equals(XMLEditer.E_MAPSET)) { // find mapset node
				String mapSetName = XMLEditer.getAttr(node, "name");
				MapSet mapSet = new MapSet(mapSetName); // mapSetName라는 이름의 mapSet 생성 
				stageInit(node, mapSet); // mapSet의 stage들을 읽어와서 초기화.
				(this.mapSetVector).add(mapSet); // 현재 읽어온 mapSet를 벡터에 추가. 
			}			
		}
	}

	// xml으로부터 읽어온 mapSet 노드와 저장하려는 mapSet벡터 레퍼런스를 받아서 mapSet벡터 초기화.
	private void stageInit(Node mapSetNode, MapSet mapSet) {
		NodeList stageList = mapSetNode.getChildNodes();
		for(int i=0; i<stageList.getLength(); i++) {
			Node node = stageList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE) // not stage node
				continue;
			if(node.getNodeName().equals(XMLEditer.E_STAGE)) { // find stage node
				int level = Integer.parseInt(XMLEditer.getAttr(node, "level"));
				int time = Integer.parseInt(XMLEditer.getAttr(node, "time")); 
				int background = Integer.parseInt(XMLEditer.getAttr(node, "backgroundType"));
				Stage stage = new Stage(level,time,background); // stage level, 제한시간 time 을 설정하여 stage 생성.	
				blockInit(node, stage); // stage가 갖고있는 블록들을 읽어와서 stage에 초기화.
				mapSet.addStage(stage);
			}
		}
	}

	// xml으로부터 읽어온 stage 노드와 저장하려는 stage벡터 레퍼런스를 받아서 stage벡터 초기화.
	private void blockInit(Node blockNode, Stage stage) {
		NodeList blockList = blockNode.getChildNodes();
		Vector<SpecialBlock> warpBlockASet = new Vector<SpecialBlock>();
		Vector<SpecialBlock> warpBlockBSet = new Vector<SpecialBlock>();
		for(int i=0; i<blockList.getLength(); i++) {
			Node node = blockList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE) // not stage node
				continue;
			if(node.getNodeName().equals(XMLEditer.E_BLOCK)) { // find stage node
				int x = Integer.parseInt(XMLEditer.getAttr(node, "x")); // block의 x좌표 읽어옴. 
				int y = Integer.parseInt(XMLEditer.getAttr(node, "y")); // block의 y좌표 읽어옴.
				int width = Integer.parseInt(XMLEditer.getAttr(node, "width")); // block의 width 읽어옴.
				int height = Integer.parseInt(XMLEditer.getAttr(node, "height")); // block의 height 읽어옴.
				int type = Integer.parseInt(XMLEditer.getAttr(node, "type")); // block의 type 읽어옴.
				String kind = XMLEditer.getAttr(node, "kind"); // block의 종류를 읽어옴.
				switch(kind) {
				case "normal":
					stage.addBlock(new Block(x,y,width,height,type));
					break;
				case "item":
					stage.addBlock(new ItemBlock(x,y,width,height,type));
					break;
				case "special":
					SpecialBlock spBlock = new SpecialBlock(x,y,width,height,type);
					if(type>=1000) { // warpBlock인 경우 벡터에 임시 저장한 후 나중에 짝 맞춰줌. 
						if(type%2==0) warpBlockASet.add(spBlock);
						else warpBlockBSet.add(spBlock);
					}
					stage.addBlock(spBlock);
					break;
				}
			}
		}
		
		for(int i=0; i<warpBlockASet.size(); i++) {
			SpecialBlock warpBlockA = warpBlockASet.get(i);
			for(int j=0; j<warpBlockBSet.size(); j++) {
				SpecialBlock warpBlockB = warpBlockBSet.get(j);
				if(warpBlockA.getType()+1==warpBlockB.getType()) {
					warpBlockA.setPairBlock(warpBlockB);
					warpBlockB.setPairBlock(warpBlockA);
					break;
				}
			}
		}
		this.setResizable(false);
	}
	
	public static void main(String[] args) {
		GameManager game = new GameManager();
	}
}

