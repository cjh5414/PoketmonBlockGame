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
	private static BasePanel basePanel;						// ���� ����� �⺻ ȭ��. �޴� ����.
	private static GamePanel gamePanel;				
	private static MapEditPanel mapEditPanel;			
	private static InfoPanel infoPanel;
	private static CardLayout layoutManager;
	private static Container contentPane;
	private Vector<MapSet> mapSetVector;							// ��ü �ʼ�Ʈ�� ���� ����
	
	GameManager() {
		XMLEditer xml = new XMLEditer("PoketMonster.xml");		//XML �о����
		mapSetVector = new Vector<MapSet>();
		mapSetInit(xml);									// XML�� �� ������ �о Vector�� ����.
		contentPane = getContentPane();
		layoutManager = new CardLayout();					//LayoutManager ����
		
		//Panel ����
		basePanel = new BasePanel(BASEBGIMAGE);
		gamePanel = new GamePanel(GAMEBGIMAGE, mapSetVector);
		mapEditPanel = new MapEditPanel(MPAEDITBGIMAGE, xml,mapSetVector);
		infoPanel = new InfoPanel(INFOBGIMAGE);
		
		//CardLayout���� ���� �� Panel�߰�.
		setLayout(layoutManager);
		add("basePanel",basePanel);
		add("gamePanel",gamePanel);
		add("mapEditPanel",mapEditPanel);
		add("infoPanel",infoPanel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	
		//BasePanel�����ֱ�.
		changePanel("basePanel");
	}
	
	//panelName���� �г� �ٲ�.
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
	
	// �о�� xml���κ��� mapSet �ʱ�ȭ. 
	private void mapSetInit(XMLEditer xml) {
		Node mapSetParentNode = xml.getAvengersGameElement();
		NodeList mapSetNodeList = mapSetParentNode.getChildNodes();
		for(int i=0; i<mapSetNodeList.getLength(); i++) {
			Node node = mapSetNodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE) // not mapset node
				continue;
			if(node.getNodeName().equals(XMLEditer.E_MAPSET)) { // find mapset node
				String mapSetName = XMLEditer.getAttr(node, "name");
				MapSet mapSet = new MapSet(mapSetName); // mapSetName��� �̸��� mapSet ���� 
				stageInit(node, mapSet); // mapSet�� stage���� �о�ͼ� �ʱ�ȭ.
				(this.mapSetVector).add(mapSet); // ���� �о�� mapSet�� ���Ϳ� �߰�. 
			}			
		}
	}

	// xml���κ��� �о�� mapSet ���� �����Ϸ��� mapSet���� ���۷����� �޾Ƽ� mapSet���� �ʱ�ȭ.
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
				Stage stage = new Stage(level,time,background); // stage level, ���ѽð� time �� �����Ͽ� stage ����.	
				blockInit(node, stage); // stage�� �����ִ� ��ϵ��� �о�ͼ� stage�� �ʱ�ȭ.
				mapSet.addStage(stage);
			}
		}
	}

	// xml���κ��� �о�� stage ���� �����Ϸ��� stage���� ���۷����� �޾Ƽ� stage���� �ʱ�ȭ.
	private void blockInit(Node blockNode, Stage stage) {
		NodeList blockList = blockNode.getChildNodes();
		Vector<SpecialBlock> warpBlockASet = new Vector<SpecialBlock>();
		Vector<SpecialBlock> warpBlockBSet = new Vector<SpecialBlock>();
		for(int i=0; i<blockList.getLength(); i++) {
			Node node = blockList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE) // not stage node
				continue;
			if(node.getNodeName().equals(XMLEditer.E_BLOCK)) { // find stage node
				int x = Integer.parseInt(XMLEditer.getAttr(node, "x")); // block�� x��ǥ �о��. 
				int y = Integer.parseInt(XMLEditer.getAttr(node, "y")); // block�� y��ǥ �о��.
				int width = Integer.parseInt(XMLEditer.getAttr(node, "width")); // block�� width �о��.
				int height = Integer.parseInt(XMLEditer.getAttr(node, "height")); // block�� height �о��.
				int type = Integer.parseInt(XMLEditer.getAttr(node, "type")); // block�� type �о��.
				String kind = XMLEditer.getAttr(node, "kind"); // block�� ������ �о��.
				switch(kind) {
				case "normal":
					stage.addBlock(new Block(x,y,width,height,type));
					break;
				case "item":
					stage.addBlock(new ItemBlock(x,y,width,height,type));
					break;
				case "special":
					SpecialBlock spBlock = new SpecialBlock(x,y,width,height,type);
					if(type>=1000) { // warpBlock�� ��� ���Ϳ� �ӽ� ������ �� ���߿� ¦ ������. 
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

