package com.conner.avoid.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.conner.avoid.Application;
import com.conner.avoid.input.InputHandler;

public class GameButton {

	private float x, y;
	private float width = 80, height = 20;
	
	private TextureRegion reg;
	
	Vector3 vec;
	private OrthographicCamera camera;
	
	private boolean clicked;
	
	private String text;
	private TextureRegion[] font;
	
	public GameButton(TextureRegion reg, float x, float y, OrthographicCamera camera) {
		this.reg = reg;
		this.x = x;
		this.y = y;
		this.camera = camera;
		
		vec = new Vector3();
		
		Texture tex = Application.res.getTexture("hud");
		font = new TextureRegion[12];
		for(int i = 0; i < 6; i++) {
			font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
		}
		for(int i = 0; i < 6; i++) {
			font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
		}
	}
	
	public boolean isClicked() {
		return clicked;
	}
	public void setText(String s) {
		text = s;
	}
	
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(reg, x - width /2, y - height / 2);
		if(text != null) {
			drawText(batch, text, x, y);
		}
		batch.end();
	}
	
	public void update(float dt) {
		vec.set(InputHandler.x, InputHandler.y, 0);
		camera.unproject(vec);
		if(InputHandler.isPressed() &&
				vec.x > x - width / 2 && vec.x < x + width / 2 &&
				vec.y > y - height / 2 && vec.y < y + height / 2) {
			clicked = true;
		} else {
			clicked = false;
		}
	}
	
	private void drawText(SpriteBatch batch, String s, float x, float y) {
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
