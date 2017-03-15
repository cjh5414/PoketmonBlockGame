package element;

import javax.swing.*;

import java.awt.*;
import java.awt.Event.*;

public class BTN extends JButton{
	public static final String PREVBTNIMAGE[] = {
		"images/button/prevButton1.png",
		"images/button/prevButton2.png"		
	};
	public static final String NEXTBTNIMAGE[] = {
		"images/button/nextButton1.png",
		"images/button/nextButton2.png"	
	};
	public static final String REMOVEBTNIMAGE[] = {
		"images/button/RemoveButton.png"		
	};
	public static final String RESETBTNIMAGE[] = {
		"images/button/ResetButton.png"	
	};
	public static final String SAVEBTNIMAGE[] = {
		"images/button/SaveButton.png"
	};
	public static final String ADDSTAGEBTNIMAGE[] ={
		"images/button/AddStageButton.png"
	};
	public static final String STARTBTNIMAGE[] ={
		"images/button/StartButton.png"
	};
	public static final String MAPEDITBTNIMAGE[] ={
		"images/button/MapEditButton.png"
	};
	public static final String INFOBTNIMAGE[] ={
		"images/button/InfoButton.png"
	};
	ImageIcon image;
	public BTN(int x,int y,int width, int height,ImageIcon inputImg){
		super(inputImg);
		image = inputImg;
		setBounds(x, y, width, height);		
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);		
	}
	public ImageIcon getImage(){ return image;	}
	
}
