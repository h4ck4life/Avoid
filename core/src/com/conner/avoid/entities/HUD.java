package com.conner.avoid.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.conner.avoid.Application;

public class HUD {

	private Stage stage;
	private Player player;
	private ShapeRenderer sr;
	private BitmapFont font;
	
	public HUD(Stage stage, Player player) {
		this.player = player;
		this.stage = stage;
		
		sr = new ShapeRenderer();
		
		font = Application.font;
		font.setScale(1f);
	}
	
	public void render(SpriteBatch sb) {
		renderScore(sb);
		renderPhaseBar(sb);
		renderHealthBar(sb);
	}
	
	// Render Score -- Top Right
	private void renderScore(SpriteBatch sb) {
		sb.begin();
		font.draw(sb, "SCORE: " + player.getScore(), 10, 40);
		sb.end();
	}
	
	// Render Phase Level bar -- Top
	private void renderPhaseBar(SpriteBatch sb) {
		// Enable Alpha Blending
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		// Draw bar 
		sr.begin(ShapeType.Filled);
		sr.setColor(new Color(.2f, .2f, .45f, .75f));
		sr.rect(0, stage.getHeight() - 15, stage.getWidth() * player.getPhaseTick() / (float)player.getPhaseUpTime(), stage.getWidth());
		sr.end();
		
		// Draw container line
		sr.begin(ShapeType.Line);
		sr.setColor(0, 0, 0, 1);
		sr.line(0, stage.getHeight() - 15, stage.getWidth(), stage.getHeight() - 15);
		sr.end();

		// Disable Alpha Blending
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
	// Render Health Bar -- Bottom
	private void renderHealthBar(SpriteBatch sb) {
		
		// Draw fade bar
		if(player.getPrevHealth() >= player.getHealth()) {
			// Slow degrade of fade bar -- will meet up with actual player health
			player.addPrevHealth(-0.4f);
			
			// Enable Alpha Blending
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			// Draw bar
			sr.begin(ShapeType.Filled);
			sr.setColor(new Color(1, 0, 0, .6f));
			sr.rect(0, 0, stage.getWidth() * player.getPrevHealth()/100, 15);
			sr.end();
			
			// Disable Alpha Blending
			Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
		
		// Draw health bar
		if(player.getHealth() >= 0) {
			sr.begin(ShapeType.Filled);
			sr.setColor(new Color(1, 0, 0, 1f));
			sr.rect(0, 0, stage.getWidth() * player.getHealth()/100, 15);
			sr.end();
		}
		
		// Draw container line
		sr.begin(ShapeType.Line);
		sr.setColor(0, 0, 0, 1);
		sr.line(0, 15, stage.getWidth(), 15);
		sr.end();	
	}
}
