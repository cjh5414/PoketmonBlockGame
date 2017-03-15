package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import element.*;

class BasePanel extends JPanel {
	private ImageIcon bgImage;
	private BTN gameStart;
	private BTN mapEdit;
	private BTN info;
	private MenuButtonListener menuButtonListener;
	private JComponent thisPanel = this;
	
	BasePanel(String bgImage) {
		setLayout(null);
		this.bgImage = new ImageIcon(bgImage);
		gameStart = new BTN(100, 200, 225, 100,new ImageIcon(BTN.STARTBTNIMAGE[0]));
		mapEdit = new BTN(100, 400, 300, 100,new ImageIcon(BTN.MAPEDITBTNIMAGE[0]));
		info = new BTN(100, 600, 345, 100,new ImageIcon(BTN.INFOBTNIMAGE[0]));
		menuButtonListener = new MenuButtonListener();		
		gameStart.addActionListener(menuButtonListener);
		mapEdit.addActionListener(menuButtonListener);
		info.addActionListener(menuButtonListener);
		
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					int result = JOptionPane.showConfirmDialog(thisPanel, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
					switch(result) {
					case JOptionPane.CLOSED_OPTION:
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.YES_OPTION:
						System.exit(0);						
					}
				}
			}
		});
		add(gameStart);
		add(mapEdit);
		add(info);
	}
	
	class MenuButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			BTN button = (BTN)e.getSource();
			if(button == gameStart)
				GameManager.changePanel("gamePanel");
			else if(button == mapEdit)
				GameManager.changePanel("mapEditPanel");
			else if(button == info)
				GameManager.changePanel("infoPanel");
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImage.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
