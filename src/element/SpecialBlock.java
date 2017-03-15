package element;

import javax.swing.ImageIcon;

public class SpecialBlock extends Block{
	public static final String SPECIALBLOCKIMAGE[] = {
		"images/block/specialBlock1.jpg",
		"images/block/투명.jpg" };	
	public static final String WARPBLOCKIMAGE[] = {
		"images/block/warpBlockA.jpg",
		"images/block/warpBlockB.jpg" };
	private SpecialBlock pairBlock = null;	// warp block인경우 짝 block을 가리킴.		
	public SpecialBlock(int x, int y, int width, int height, int type) {
		super(x,y,width,height,type);
	}
	protected void setImage() {
		if(type>=1000) // warp block
			image = new ImageIcon(WARPBLOCKIMAGE[type%2]); // index가 0이면 워프 in 이미지, 1이면 워프 out 이미지. 
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


// type = 1  ->  안깨지는 블럭
// type = 1000 ~ 1300 (짝수)  ->  워프 A  
// type = 1001 ~ 1301 (홀수)  ->  워프 B