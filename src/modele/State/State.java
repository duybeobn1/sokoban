package modele.State;

import java.awt.Graphics;

import modele.Jeu;

public abstract class State {
	
	public static State currentState = null;
	
	protected Jeu jeu;
	
	public State(Jeu jeu){
		this.jeu = jeu;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);
	
}
