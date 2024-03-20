package modele.State;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import modele.Assets;
import modele.UI.Text;
import modele.Jeu;
import modele.UI.Button;
import modele.UI.Click;

public class MenuState extends State{
	
	private Button button;
	
	private ArrayList<Button> buttons = new ArrayList<Button>();
	
	public MenuState(Jeu window){
		super(window);
		buttons.add(new Button("PLAY", Jeu.SIZE_X/2, Jeu.SIZE_Y/2 - 50, new Click(){

			@Override
			public void onClick() {
				State.currentState = window.getLevelSelectorState();
			}}, Assets.font48));
		buttons.add(new Button("CREDITS", Jeu.SIZE_X/2, Jeu.SIZE_Y/2 + 50, new Click(){

			@Override
			public void onClick() {
				State.currentState = window.getLevelSelectorState();
			}}, Assets.font48));
		buttons.add(new Button("EXIT", Jeu.SIZE_X/2, Jeu.SIZE_Y/2 + 150, new Click(){

			@Override
			public void onClick() {
				System.exit(1);
			}}, Assets.font48));
	}
	
	@Override
	public void update() {
		for(int i = 0; i < buttons.size(); i++)
			buttons.get(i).update();
	}

	@Override
	public void render(Graphics g) {
		g.setFont(Assets.font48);
		Text.drawString(g, "SOKOBAN", Jeu.SIZE_X/2, Jeu.SIZE_Y/2 - 200, true, Color.DARK_GRAY);
		for(int i = 0; i < buttons.size(); i++)
			buttons.get(i).render(g);
	}

}
