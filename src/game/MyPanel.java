package game;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MyPanel extends JPanel{
	private ImageIcon bgImage;
	protected MyPanel(String bgImage) {
		this.bgImage = new ImageIcon(bgImage);
		
		//esc key를 누르면 basePanel로 이동.
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					GameManager.changePanel("basePanel");
				}
			}
		});
		setLayout(null);
	}
	protected void setBgImage(String bgImage) {
		this.bgImage = new ImageIcon(bgImage);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
