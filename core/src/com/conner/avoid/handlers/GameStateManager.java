package com.conner.avoid.handlers;

import java.util.Stack;

import com.conner.avoid.Application;
import com.conner.avoid.states.GameState;
import com.conner.avoid.states.HighscoreList;
import com.conner.avoid.states.MainMenu;
import com.conner.avoid.states.Play;

public class GameStateManager {
	private Application app;
	
	private Stack<GameState> states;

	public static final int MAINMENU = 0;
	public static final int PLAY = 1;
	public static final int HIGHSCORE = 2;
	
	public GameStateManager(Application app) {
		this.app = app;
		states = new Stack<GameState>();
		pushState(MAINMENU);
	}
	
	public Application application() {
		return app;
	}
	
	public void update(float delta) {
		states.peek().update(delta);
	}
	
	public void render() {
		states.peek().render();
	}
	
	private GameState getState(int state) {
		if(state == MAINMENU) return new MainMenu(this);
		if(state == PLAY) return new Play(this);
		if(state == HIGHSCORE) return new HighscoreList(this);
		return null;
	}
	
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushState(int state) {
		states.push(getState(state));
	}
	public void popState() {
		GameState currentState = states.pop();
		currentState.dispose();
	}
}
