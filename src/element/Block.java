package element;

import java.awt.*;

import javax.swing.*;
import game.*;

public class Block extends JLabel {
	public static final String NORMALBLOCKIMAGE[] = {
		"images/block/noItemBlock1.png",
		"images/block/noItemBlock2.png",
		"images/block/noItemBlock3.png",
		"images/block/noItemBlock4.png",
		"images/block/noItemBlock5.png",
		"images/block/noItemBlock6.png",
		"images/block/noItemBlock7.png" };
	protected ImageIcon image;
	protected int type;
	protected int life;
	public Block(int x, int y, int width, int height, int type) {
		setBounds(x,y,width,height);
		this.type = type;
		setLife();
		setImage();		
	}
	protected void setImage() {
		image = new ImageIcon(NORMALBLOCKIMAGE[type]);
	}
	public void setLife() {
		life = type+1;
	}
	public int getLife() {
		return life;
	}
	public void decreaseLife() {
		life--;
		if(life==0)
			this.getParent().remove(this);
	}
	public void removeBlock() {
		life=0;
		this.getParent().remove(this);
	}
	public int getType() {
		return type;
	}
	public ImageIcon getImage(){
		return image;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}

