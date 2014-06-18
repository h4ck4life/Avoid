package com.conner.avoid.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.conner.avoid.Application;
import com.conner.avoid.handlers.GameStateManager;
import com.conner.avoid.utils.GameButton;

public class MainMenu extends GameState {

	private GameButton playButton, highscoreButton, settingsButton, quitButton;
	
	public MainMenu(GameStateManager gsm) {
		super(gsm);
		
		camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);
		
		Texture tex = Application.res.getTexture("hud");
		playButton = new GameButton(new TextureRegion(tex, 0,0,0,0), 160, 100, camera);
		playButton.setText("0");
		highscoreButton = new GameButton(new TextureRegion(tex, 0,0,0,0), 160, 80, camera);
		highscoreButton.setText("1");
		settingsButton = new GameButton(new TextureRegion(tex, 0,0,0,0), 160, 60, camera);
		settingsButton.setText("2");
		quitButton = new GameButton(new TextureRegion(tex, 0,0,0,0), 160, 40, camera);
		quitButton.setText("3");
	}

	@Override
	public void handleInput() {
		if(playButton.isClicked()) {
			System.out.println("BUTTS");
			gsm.setState(GameStateManager.PLAY);
		}
		if(highscoreButton.isClicked()) {
			gsm.setState(GameStateManager.HIGHSCORE);
		}
		if(settingsButton.isClicked()) {
			
		}
		if(quitButton.isClicked()) {
			System.exit(0);
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
		
		playButton.update(dt);
		highscoreButton.update(dt);
		settingsButton.update(dt);
		quitButton.update(dt);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		
		batch.setProjectionMatrix(camera.combined);
		
		playButton.render(batch);
		highscoreButton.render(batch);
		settingsButton.render(batch);
		quitButton.render(batch);
	}

	@Override
	public void dispose() {
	}
}
