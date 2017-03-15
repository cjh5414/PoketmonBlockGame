package element;

import java.awt.Graphics;
import java.util.*;

import javax.swing.*;

public class Stage {
	public static final String [] STAGEBACKGROUNDIMAGE = {
			"images/stage/stageBackground1.jpg",
			"images/stage/stageBackground2.jpg",
			"images/stage/stageBackground3.jpg",
			"images/stage/stageBackground4.jpg",
			"images/stage/stageBackground5.jpg",
			"images/stage/stageBackground6.jpg",
			"images/stage/stageBackground7.jpg",
			"images/stage/stageBackground8.jpg",
			"images/stage/stageBackground9.jpg",
			"images/stage/stageBackground10.jpg",
			"images/stage/stageBackground11.jpg",
			"images/stage/stageBackground12.jpg",
			"images/stage/stageBackground13.jpg",
			"images/stage/stageBackground14.jpg",
			"images/stage/stageBackground15.jpg",
			"images/stage/stageBackground16.jpg",
			"images/stage/stageBackground17.jpg",
			"images/stage/stageBackground18.jpg",
			"images/stage/stageBackground19.jpg",
			"images/stage/stageBackground20.jpg" };
	private int level;
	private int time;
	private int backgroundType;
	private int warpBlockIndex;
	private ImageIcon bgImage;
	private Vector<Block> blockSet;
	
	public Stage(int level, int time, int backgroundType) {
		this.level = level;
		this.time = time;
		this.backgroundType = backgroundType;
		bgImage = new ImageIcon(STAGEBACKGROUNDIMAGE[backgroundType]);
		blockSet = new Vector<Block>();
		warpBlockIndex = 0;
	}	
	public void setBackgroundType(int type) { this.backgroundType = type; }
	public void setTime(int time) { this.time = time; }
	public void setLevel(int level) { this.level = level; }
	public int getTime() { return time; }
	public int getLevel() { return level; }
	public void addBlock(Block block) { blockSet.add(block); }
	public Vector<Block> getBlock() { return blockSet; }
	public ImageIcon getBackground() { return bgImage; }
	public int getBackgroundType() { return backgroundType; }
	public int getWarpBlockIndex(){	return warpBlockIndex;	}
	public void addWarpBlockIndex(){ warpBlockIndex += 1; }
	public void minusWarpBlockIndex(){ warpBlockIndex -= 1; }
}