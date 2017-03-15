package element;

import javax.swing.ImageIcon;

public class SpecialBlock extends Block{
	public static final String SPECIALBLOCKIMAGE[] = {
		"images/block/specialBlock1.jpg",
		"images/block/����.jpg" };	
	public static final String WARPBLOCKIMAGE[] = {
		"images/block/warpBlockA.jpg",
		"images/block/warpBlockB.jpg" };
	private SpecialBlock pairBlock = null;	// warp block�ΰ�� ¦ block�� ����Ŵ.		
	public SpecialBlock(int x, int y, int width, int height, int type) {
		super(x,y,width,height,type);
	}
	protected void setImage() {
		if(type>=1000) // warp block
			image = new ImageIcon(WARPBLOCKIMAGE[type%2]); // index�� 0�̸� ���� in �̹���, 1�̸� ���� out �̹���. 
		else
			image = new ImageIcon(SPECIALBLOCKIMAGE[type]);
	}
	public void setPairBlock(SpecialBlock spBlock) {
		this.pairBlock = spBlock;
	}
	public SpecialBlock getPairBlock() {
		return pairBlock;
	}
}


// type = 1  ->  �ȱ����� ��
// type = 1000 ~ 1300 (¦��)  ->  ���� A  
// type = 1001 ~ 1301 (Ȧ��)  ->  ���� B