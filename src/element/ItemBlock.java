package element;

import javax.swing.ImageIcon;

public class ItemBlock extends Block{
	public static final String ITEMBLOCKIMAGE[] = {
		"images/block/itemBlock1.png",
		"images/block/itemBlock2.png",
		"images/block/itemBlock3.png",
		"images/block/itemBlock4.png",
		"images/block/itemBlock5.png",
		"images/block/itemBlock6.png",
		"images/block/itemBlock7.png" };
	public ItemBlock(int x, int y, int width, int height, int type) {
		super(x,y,width,height,type);
	}
	protected void setImage() {
		image = new ImageIcon(ITEMBLOCKIMAGE[type]);
	}
	public void setLife() {
		life = 1;
	}
}
