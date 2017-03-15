package game;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import element.*;

public class MapPanel extends JPanel {
	public static final int MAPWIDTH = 900;
	public static final int MAPHEIGHT = 800;
	
	private ImageIcon stageBgImage;		
	
	MapPanel(Stage stage) {
		setSize(MAPWIDTH, MAPHEIGHT);
		setLayout(null);
		drawStage(stage, null, null);
	}
	void setStageBackground(ImageIcon stageBgImage) {
		this.stageBgImage = stageBgImage;
	}
	
	//mapPanel�� stage �׸�.
	void drawStage(Stage stage, Racket racket, Ball ball) {
		removeAll();								// ���� �����.
		setStageBackground(stage.getBackground());
		Vector<Block> blockSet = stage.getBlock();
		for (int i = 0; i < blockSet.size(); i++)
			add(blockSet.get(i));
		if(ball!=null) {	// �������� �ƴѰ�� ball, racket�� null
			add(ball);	
			add(racket);
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {	
		super.paintComponent(g);
		g.drawImage(stageBgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
