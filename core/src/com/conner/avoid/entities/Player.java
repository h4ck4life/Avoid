package com.conner.avoid.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.conner.avoid.Application;

public class Player extends B2DSprite {

	private int health;
	private int score;
	private boolean alive;
	private boolean dying;
	private TextureRegion[] sprites;
	private TextureRegion[] deathSprites;
	
	public Player(Body body) {
		super(body);
		alive = true;
		dying = false;
		health = 100;
		score = 0;
		
		// Image/Animation
		Texture tex = Application.res.getTexture("ball");
		Texture tex2 = Application.res.getTexture("ball-death");
		sprites = TextureRegion.split(tex, 16, 16)[0];
		deathSprites = TextureRegion.split(tex2, 16, 16)[0];
		setAnimation(sprites, 1 / 8f);
	}
	
	public void update(float dt) {
		super.update(dt);
		if(health <= 0 && alive) {
			alive = false;
			dying = true;
			setAnimation(deathSprites, 1 / 12f);
		}
		if(dying && animation.getTimesPlayed() >= 1) {
			dying = false;
		}
		
	}
	
	public boolean isAlive() {
		return alive;
	}
	public boolean isDying() {
		return dying;
	}
	public void render(SpriteBatch sb) {
		if(alive || dying)
			super.render(sb);
	}
	public int getHealth() {
		return health;
	}
	public void hurt(int damage) {
		if(health > 0) {
			health -= damage;
		}
	}
	public int getScore() {
		return score;
	}
	public void addPoints(int points) {
		score += points;
	}
}
