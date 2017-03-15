package element;

import java.awt.*;
import javax.swing.*;

public class Item extends JLabel {
	public static final String ITEMIMAGE[] = {
		"images/item/item1.png",
		"images/item/item2.png",
		"images/item/item3.png",
		"images/item/item4.png",
		"images/item/item5.png" };
	public static final String ITEMUSE[] = {
		"images/item/itemUse1.png",		//ÇÇÄ«Ãò
		"images/item/itemUse2.png",		//²¿ºÎ±â
		"images/item/itemUse31.png",	//ÀÌ»óÇØ¾¾
		"images/item/itemUse4.jpg",		//ÆÄÀÌ¸®
		"images/item/itemUse5.jpg",		//ºÕº¼
		"images/item/itemUse32.png",
		"images/item/itemUse33.png",
		"images/item/itemUse34.png",
		"images/item/itemUse35.png" };
	private static final int ITEMWIDTH = 50;
	private static final int ITEMHEIGHT = 50;
	private ImageIcon image;
	private int type;
	public Item(int x, int y, int type) {
		setLocation(x, y);
		setSize(ITEMWIDTH, ITEMHEIGHT);
		this.type = type;
		image = new ImageIcon(ITEMIMAGE[type]);
	}
	public int getType() {
		return type;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
