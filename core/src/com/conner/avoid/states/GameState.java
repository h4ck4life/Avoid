package com.conner.avoid.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.conner.avoid.Application;
import com.conner.avoid.handlers.GameStateManager;

public abstract class GameState {
	protected GameStateManager gsm;
	protected Application app;
	
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
	protected OrthographicCamera hudCamera;
	
	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		app = gsm.application();
		batch = app.getSpriteBatch();
		camera = app.getCamera();
		hudCamera = app.getHUD();
	}
	
	public abstract void handleInput();
	public abstract void update(float delta);
	public abstract void render();
	public abstract void dispose();
}
