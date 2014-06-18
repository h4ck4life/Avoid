package com.conner.avoid.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.conner.avoid.Application;

public class HUD {

	private Player player;
	
	private TextureRegion[] font;
	private TextureRegion score;
	private ShapeRenderer sr;
	
	public HUD(Player player) {
		this.player = player;
		sr = new ShapeRenderer();

		Texture tex = Application.res.getTexture("hud");
		
		font = new TextureRegion[12];
		for(int i = 0; i < 6; i++) {
			font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
		}
		for(int i = 0; i < 6; i++) {
			font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
		}
		score = new TextureRegion(tex, 32, 34, 54, 9);
	}
	
	public void render(SpriteBatch batch) {
		
		batch.begin();
		batch.draw(score, 10, 220);
		drawString(batch, player.getHealth() + "", 10, 10);
		drawScore(batch, ":" + player.getScore(), 55, 220);
		batch.end();
		
		if(player.getHealth() >= 0) {
			sr.begin(ShapeType.Filled);
			sr.setColor(1, 0, 0, 1);
			sr.rect(80, 20, 540 * player.getHealth()/100, 20);
			sr.end();
		}
		
		sr.begin(ShapeType.Line);
		sr.setColor(0, 0, 0, 1);
		sr.rect(80, 20, 540, 20);
		sr.end();
	}
	
	private void drawString(SpriteBatch batch, String s, float x, float y) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c >= '0' && c <= '9') c -= '0';
			else continue;
			batch.draw(font[c], x + i * 9, y);
		}
	}
	
	private void drawScore(SpriteBatch batch, String s, float x, float y) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c >= '0' && c <= '9') c -= '0';
			else if(c == ':') {
				batch.draw(font[11], x + i * 9, y);
				continue;
			} else continue;
			batch.draw(font[c], x + i * 9, y);
		}
	}
}
