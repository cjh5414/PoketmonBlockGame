package mapEdit;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import org.w3c.dom.Node;

import xml.*;
import element.*;
import game.*;

public class MapEditPanel extends MyPanel {
	//MapEdit Element
	private MapPanel editMapPanel;
	private MapSet mapSet;
	private XMLEditer xml;
	public static Block selectBlock;
	private int stageIndex = 0;
	private int bgIndex = 0;
	private JLabel stageState;
	private JSlider timeControl;
	private Vector<MapSet> mapSetVector;
	
	// Block References
	private Block normalBlock[];
	private ItemBlock itemBlock[];
	private SpecialBlock specialBlock[];
	private SpecialBlock warpBlock[];
	
	// BTN References
	private BTN resetBTN;
	private BTN addStageBTN;
	private BTN saveMapSetBTN;
	private BTN prevBackgroundBTN;
	private BTN nextBackgroundBTN;
	private BTN prevStageBTN;
	private BTN nextStageBTN;
	private BTN removeStageBTN;
	

	// 생성자
	public MapEditPanel(String bgImage, XMLEditer inputXml, Vector<MapSet> InputMapSetVector) {
		super(bgImage);
		this.xml = inputXml;
		this.mapSetVector = InputMapSetVector;
		setSize(500, 850);
		setLocation(900, 0);
		
		// Create Element
		mapSet = new MapSet("권영재");
		editMapPanel = new MapPanel(stageIndex + 1, 60, 0);
		stageState = new JLabel("Stage " + (mapSet.getStage().size() + 1));
		timeControl = new JSlider(JSlider.HORIZONTAL, 30, 300, 60);
		
		// Create Block;
		normalBlock = new Block[Block.NORMALBLOCKIMAGE.length];
		itemBlock = new ItemBlock[ItemBlock.ITEMBLOCKIMAGE.length];
		specialBlock = new SpecialBlock[SpecialBlock.SPECIALBLOCKIMAGE.length];		
		warpBlock = new SpecialBlock[SpecialBlock.WARPBLOCKIMAGE.length];
		
		// Create BTN;
		prevStageBTN = new BTN(100, 650, 135, 135,new ImageIcon(BTN.PREVBTNIMAGE[0]));
		nextStageBTN = new BTN(250, 650, 135, 135,new ImageIcon(BTN.NEXTBTNIMAGE[0]));
		prevBackgroundBTN = new BTN(550, 650, 135, 135,new ImageIcon(BTN.PREVBTNIMAGE[1]));
		nextBackgroundBTN = new BTN(700, 650, 135, 135,new ImageIcon(BTN.NEXTBTNIMAGE[1]));
		resetBTN = new BTN(1000, 650, 135, 135,new ImageIcon(BTN.RESETBTNIMAGE[0]));
		addStageBTN = new BTN(1000, 500, 135, 135,new ImageIcon(BTN.ADDSTAGEBTNIMAGE[0]));
		saveMapSetBTN = new BTN(1150, 650, 135, 135,new ImageIcon(BTN.SAVEBTNIMAGE[0]));
		removeStageBTN = new BTN(1150, 500, 135, 135,new ImageIcon(BTN.REMOVEBTNIMAGE[0]));
	
		
		// SET ELEMENT
		stageState.setFont(new Font("Arial", Font.PLAIN, 25));
		stageState.setBounds(420, 575, 100, 100);
		timeControl.setBounds(1000, 380, 300, 100);
		timeControl.setPaintLabels(true);
		timeControl.setPaintTicks(true);
		timeControl.setPaintTrack(true);
		timeControl.setMajorTickSpacing(50);
		timeControl.setMinorTickSpacing(15);	
		
	
		// Element ActionListener
		this.removeKeyListener(this.getKeyListeners()[0]); // 기존에 있던
															// KeyListener제거
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					int check;
					// MapSetVector에 맨마지막에 저장되어있다면 다른 메세지 출력
					if (!mapSetVector.get(mapSetVector.size() - 1).getStage()
							.contains(editMapPanel.getStage())) {
						check = JOptionPane.showConfirmDialog(null, "저장 후 종료?",
								"저장확인", JOptionPane.YES_NO_CANCEL_OPTION);
						if (check == JOptionPane.YES_OPTION) { // 저장 후 종료
							saveMapSetBTN.doClick();
							mapSet = new MapSet("권영재");
							editMapPanel.setStage(new Stage(1, 60, 1));
						} else if (check != JOptionPane.CANCEL_OPTION) {
							mapSet.getStage().removeAllElements();
							resetBTN.doClick();
							stageIndex = 0;
							stageState.setText("Stage" + (stageIndex + 1));
							GameManager.changePanel("basePanel");
						}
					} else {
						check = JOptionPane.showConfirmDialog(null,
								"종료하시겠습니까?", "종료확인", JOptionPane.YES_NO_OPTION);
						if (check == JOptionPane.YES_OPTION) { // 종료
							mapSet = new MapSet("권영재");
							editMapPanel.setStage(new Stage(1, 60, 1));
							mapSet.getStage().removeAllElements();
							resetBTN.doClick();
							stageIndex = 0;
							stageState.setText("Stage" + (stageIndex + 1));
							GameManager.changePanel("basePanel");
						}
					}
				}
			}
		});
		timeControl.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				JSlider value = (JSlider)e.getSource();
				int time = value.getValue();
				editMapPanel.getStage().setTime(time);
				requestFocus();
			}
		});

		
		//BTN ActionListener
		prevStageBTN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				stageIndex = (stageIndex-1) >= 0 ? stageIndex-1 : mapSet.getStage().size()-1;
				if(stageIndex >= 0){
					if(!mapSet.getStage().contains(editMapPanel.getStage()))//mapSet에 stage가 없을경우 추가하고 이전Stage로 이동한다.
						mapSet.addStage(editMapPanel.getStage());
					
					stageState.setText("Stage " + (stageIndex+1));
					editMapPanel.resetEditMapPanel();				
					editMapPanel.setStage(mapSet.getStage().get(stageIndex));
					editMapPanel.drawStage(mapSet.getStage().get(stageIndex));
					editMapPanel.changeBackground(mapSet.getStage().get(stageIndex).getBackgroundType());
				}
			}
		});
		nextStageBTN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				stageIndex = (stageIndex+1) <  mapSet.getStage().size() ? stageIndex+1 : 0;
				if(stageIndex <mapSet.getStage().size()){
					if(!mapSet.getStage().contains(editMapPanel.getStage()))//mapSet에 stage가 없을경우 추가하고 다음Stage로 이동한다.
						mapSet.addStage(editMapPanel.getStage());
					stageState.setText("Stage " + (stageIndex+1));
					editMapPanel.resetEditMapPanel();
					editMapPanel.setStage(mapSet.getStage().get(stageIndex));
					editMapPanel.drawStage(mapSet.getStage().get(stageIndex));
					editMapPanel.changeBackground(mapSet.getStage().get(stageIndex).getBackgroundType());
				}
				requestFocus();
			}
		});
		prevBackgroundBTN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				bgIndex = (bgIndex-1) >= 0 ? bgIndex-1 : Stage.STAGEBACKGROUNDIMAGE.length-1 ;
				editMapPanel.changeBackground(bgIndex);
				requestFocus();
			}
		});
		nextBackgroundBTN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				bgIndex = (bgIndex+1) < Stage.STAGEBACKGROUNDIMAGE.length ? bgIndex+1 : 0 ;
				editMapPanel.changeBackground(bgIndex);
				requestFocus();
			}
		});
		resetBTN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				editMapPanel.resetStage();
				editMapPanel.resetEditMapPanel();
				requestFocus();
			}
		});
		addStageBTN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				bgIndex = 0;
				if(!mapSet.getStage().contains(editMapPanel.getStage()))//mapSet에 stage가 없을경우 추가
					mapSet.addStage(editMapPanel.getStage());
				stageState.setText("Stage " + (mapSet.getStage().size()+1));
				stageIndex = mapSet.getStage().size();
				Stage stage = new Stage(mapSet.getStage().size()+1,60,0);
				editMapPanel.setStage(stage);
				editMapPanel.resetEditMapPanel();
				editMapPanel.changeBackground(stage.getBackgroundType());
				requestFocus();
			}
		});
		saveMapSetBTN.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String name = JOptionPane.showInputDialog("작성자의 이름을 입력해주세요 .");
				if(name != null){		//이름을 입력한 경우에만 할것.
					mapSet.setName(name);
					//현재 작성중인 mapPanel의 Block존재 유무 확인후 있으면 저장 아니면 저장X
					if(!editMapPanel.getStage().getBlock().isEmpty())
						mapSet.addStage(editMapPanel.getStage());
					for(int i=0; i<mapSet.getStage().size(); i++) {
						Vector<SpecialBlock> warpBlockASet = new Vector<SpecialBlock>();
						Vector<SpecialBlock> warpBlockBSet = new Vector<SpecialBlock>();
						Vector<Block> bVector = mapSet.getStage().get(i).getBlock();
						for(int j=0; j<bVector.size(); j++) {
							Block b = bVector.get(j);
								if(b instanceof SpecialBlock) {
									if(b.getType()>=1000) { // warpBlock인 경우 벡터에 임시 저장한 후 나중에 짝 맞춰줌. 
										if(b.getType()%2==0) warpBlockASet.add((SpecialBlock)b);
											else warpBlockBSet.add((SpecialBlock)b);
									}
								}
							}
						
						for(int k=0; k<warpBlockASet.size(); k++) {
							SpecialBlock warpBlockA = warpBlockASet.get(k);
							for(int l=0; l<warpBlockBSet.size(); l++) {
								SpecialBlock warpBlockB = warpBlockBSet.get(l);
								if(warpBlockA.getType()+1==warpBlockB.getType()) {
									warpBlockA.setPairBlock(warpBlockB);
									warpBlockB.setPairBlock(warpBlockA);
									break;
								}
							}
						}
					mapSet.getStage().get(i).setLevel(i+1);
				}
				mapSetVector.add(mapSet);
					xml.write("PoketMonster.xml",mapSet);
				}
				requestFocus();
			}
		});
		removeStageBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!mapSet.getStage().contains(editMapPanel.getStage()))// mapSet에 stage가 없을경우 추가
					mapSet.addStage(editMapPanel.getStage());
				if (mapSet.getStage().size() > 1) {
					if (stageIndex == mapSet.getStage().size() - 1) {
						mapSet.getStage().remove(stageIndex);
						stageIndex = mapSet.getStage().size() - 1;
					} else {
						mapSet.getStage().removeElementAt(stageIndex);
					}
					Stage stage = mapSet.getStage().get(stageIndex);
					stageState.setText("Stage " + (stageIndex + 1));
					editMapPanel.setStage(stage);
					editMapPanel.resetEditMapPanel();
					editMapPanel.changeBackground(stage.getBackgroundType());
					editMapPanel.drawStage(stage);
					requestFocus();
				}
			}
		});
		// ADD NormalBlock
		for (int i = 0; i < Block.NORMALBLOCKIMAGE.length; i++) {
			normalBlock[i] = new Block(65 * i + 900, 0, 60, 30, i);
			normalBlock[i].addMouseListener(new MyListener());
			add(normalBlock[i]);
		}
		// ADD ItemBlock
		for (int i = 0; i < ItemBlock.ITEMBLOCKIMAGE.length; i++) {
			itemBlock[i] = new ItemBlock(65 * i + 900, 100, 60, 30, i);
			itemBlock[i].addMouseListener(new MyListener());
			add(itemBlock[i]);
		}
		// ADD SpecialBlock
		for (int i = 0; i < SpecialBlock.SPECIALBLOCKIMAGE.length; i++) {
			specialBlock[i] = new SpecialBlock(65 * i + 900, 200, 60, 30, i);
			specialBlock[i].addMouseListener(new MyListener());
			add(specialBlock[i]);
		}
		for (int i = 0; i < SpecialBlock.WARPBLOCKIMAGE.length; i++) {
			warpBlock[i] = new SpecialBlock(65 * i + 900, 300, 60, 30, 1000 + i);
			warpBlock[i].addMouseListener(new MyListener());
			add(warpBlock[i]);
		}
		
		// ADD Element
		add(editMapPanel);
		add(stageState);
		add(timeControl);
		
		// ADD BTN
		add(prevStageBTN);
		add(nextStageBTN);
		add(prevBackgroundBTN);
		add(nextBackgroundBTN);
		add(resetBTN);
		add(addStageBTN);
		add(saveMapSetBTN);
		add(removeStageBTN);
	}

	// Block Listener
	class MyListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(((Block) e.getSource()).getType() < 1000 || (((Block) e.getSource()).getType()%1000)%2==0){
				selectBlock = (Block) e.getSource();				
			}
		}
	}
}

