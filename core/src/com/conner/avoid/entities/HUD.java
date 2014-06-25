package com.conner.avoid.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.conner.avoid.Application;

public class HUD {

	private Player player;
	
	private ShapeRenderer sr;
	private BitmapFont font;
	
	public HUD(Player player) {
		this.player = player;
		sr = new ShapeRenderer();
		font = Application.font;
		font.setScale(1f);
	}
	
	public void render(SpriteBatch sb) {
		renderScore(sb);
		renderPhaseBar(sb);
		renderHealthBar(sb);
	}
	
	private void renderScore(SpriteBatch sb) {
		sb.begin();
		font.draw(sb, "SCORE: " + player.getScore(), 10, 40);
		sb.end();
	}
	private void renderPhaseBar(SpriteBatch sb) {
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeType.Filled);
		sr.setColor(new Color(.2f, .2f, .45f, .75f));
		sr.rect(0, Gdx.graphics.getHeight() - 15 * Application.SCALE, Gdx.graphics.getWidth() * player.getPhaseTick() / (float)player.getPhaseUpTime(), Gdx.graphics.getHeight());
		sr.end();
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
		sr.begin(ShapeType.Line);
		sr.setColor(0, 0, 0, 1);
		sr.rect(0, Gdx.graphics.getHeight() - 15 * Application.SCALE, Gdx.graphics.getWidth(), 1);
		sr.end();
	}
	private void renderHealthBar(SpriteBatch sb) {
		// Draw Health Bar
		if(player.getPrevHealth() >= player.getHealth()) {
			player.addPrevHealth(-0.4f);
			Gdx.gl20.glEnable(GL20.GL_BLEND);
			Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			sr.begin(ShapeType.Filled);
			sr.setColor(new Color(1, 0, 0, .6f));
			sr.rect(0, 0, Gdx.graphics.getWidth() * player.getPrevHealth()/100, 15 * Application.SCALE);
			sr.end();
			Gdx.gl20.glDisable(GL20.GL_BLEND);
		}
		if(player.getHealth() >= 0) {
			sr.begin(ShapeType.Filled);
			sr.setColor(new Color(1, 0, 0, 1f));
			sr.rect(0, 0, Gdx.graphics.getWidth() * player.getHealth()/100, 15 * Application.SCALE);
			sr.end();
		}
		sr.begin(ShapeType.Line);
		sr.setColor(0, 0, 0, 1);
		sr.rect(0, 15 * Application.SCALE, Gdx.graphics.getWidth(), 1);
		sr.end();	
	}
}
