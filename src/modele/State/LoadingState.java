package modele.State;

import java.awt.Color;
import java.awt.Graphics;

import modele.Assets;
import modele.UI.Text;
import modele.Jeu;

public class LoadingState extends State{
	
	private final String NAME = "Sokoban";
	private String text = "";
	private int index = 0;
	private long time, lastTime;
	public LoadingState(Jeu jeu){
		super(jeu);
		time = 0;
		lastTime = System.currentTimeMillis();
	}
	
	@Override
	public void update() {
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		if(time > 150){
			
			text = NAME.substring(0, index);
			if(index < NAME.length()){
				index ++;

			}else{
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				State.currentState = jeu.getMenuState();
			}
			time = 0;
		}
			
	}

	@Override
	public void render(Graphics g) {
		g.setFont(Assets.font22);
		Text.drawString(g, text, jeu.SIZE_X/2, jeu.SIZE_Y/2, true, Color.WHITE);
	}
	
}
