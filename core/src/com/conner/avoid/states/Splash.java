package com.conner.avoid.states;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.conner.avoid.Application;
import com.conner.avoid.handlers.GameStateManager;
import com.conner.avoid.tween.SpriteAccessor;

public class Splash extends GameState {

	private Texture splashTex;
	private Sprite splash;
	
	private TweenManager tweenMgr;
	
	public Splash(final GameStateManager gsm) {
		super(gsm);
		
		// Splash Texture
		splashTex = Application.res.getTexture("splash");
		splash = new Sprite(splashTex);
		splash.setPosition(Application.V_WIDTH/2 - splash.getWidth()/2, Application.V_HEIGHT/2 - splash.getHeight()/2);
		
		// Fade in/out of splash -> go to main menu, afterwards
		tweenMgr = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		// Establish splash sequence
		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenMgr);
		Tween.to(splash, SpriteAccessor.ALPHA, .5f).target(1)
			.repeatYoyo(1, 1)
			.setCallback(new TweenCallback() {
				@Override
				public void onEvent(int arg0, BaseTween<?> arg1) {
					gsm.setState(GameStateManager.MAINMENU);
				}
			})
			.start(tweenMgr);
	}

	@Override
	public void handleInput() {
	}

	@Override
	public void update(float delta) {
		tweenMgr.update(delta);
	}

	@Override
	public void render() {
		// Clear Screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		splash.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
	}
}
