package element;

import java.util.*;

public class MapSet {
	private String name;	//작성자 이름.
	Vector<Stage> stageSet;
	
	public MapSet(String name) {
		this.name = name;
		stageSet = new Vector<Stage>();
	}
	
	public void addStage(Stage stage) {
		stageSet.add(stage);
	}
	
	public String getName() {
		return name;
	}
	
	public Vector<Stage> getStage() {
		return stageSet;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
