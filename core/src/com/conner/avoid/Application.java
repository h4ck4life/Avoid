package com.conner.avoid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.conner.avoid.handlers.GameStateManager;
import com.conner.avoid.input.InputHandler;
import com.conner.avoid.input.InputProcessor;
import com.conner.avoid.utils.Cache;

public class Application extends ApplicationAdapter {
	
	// FPS counter
	FPSLogger logger;
	
	// Game Configuration
	public static final String TITLE = "Deflector";
	public static final int V_WIDTH = 640;
	public static final int V_HEIGHT = 360;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;
	
	// Game Handler
	private GameStateManager gsm;
	
	// Resource Cache
	public static Cache res;
	
	// Scene vars
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthographicCamera hudCamera;
	private BitmapFont font;
	
	public void create() {
		Gdx.input.setInputProcessor(new InputProcessor());
		batch = new SpriteBatch();
		
		// TODO: Load this into a loadstate for loading screen for resources
		res = new Cache();
		res.loadTexture("img/player.png", "ball");
		res.loadTexture("img/player_death.png", "ball-death");
		res.loadTexture("img/deflector.png", "deflector");
		res.loadTexture("img/hud.png", "hud");
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		gsm = new GameStateManager(this);
		font = new BitmapFont();	
	}
	
	public void render() {		
		gsm.update(STEP);
		gsm.render();
		InputHandler.update();
	}
	
	public void dispose() {
		batch.dispose();
		//res.disposeAll();
	}
	
	// Utility methods
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	public OrthographicCamera getCamera() {
		return camera;
	}
	public OrthographicCamera getHUD() {
		return hudCamera;
	}
	
	// Other methods
	public void resize(int w, int h) {
		
	}
	public void pause() {
		
	}
	public void resume() {
		
	}
}
