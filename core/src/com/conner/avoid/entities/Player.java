package com.conner.avoid.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.conner.avoid.Application;

public class Player extends B2DSprite {

	private int health;
	private float prevHealth;
	private int score, phase;
	private int[] phaseTimes;
	private boolean phaseUp = false;
	private Map<String, Float> ticks;
	
	private boolean alive;
	private boolean dying;
	private TextureRegion[] sprites;
	private TextureRegion[] deathSprites;
	
	public Player(Body body) {
		super(body);
		alive = true;
		dying = false;
		health = 100;
		prevHealth = 100;
		score = 0;
		phase = 0;
		ticks = new HashMap<String, Float>();
		ticks.put("phaseTick", 0f);
		ticks.put("scoreTick", 0f);
		phaseTimes = new int[] {40, 50, 70, 100, 200};
		phaseTimes = new int[] {2, 3, 4, 5, 6};
		
		// Image/Animation
		Texture tex = Application.res.getTexture("ball");
		Texture tex2 = Application.res.getTexture("ball-death");
		sprites = TextureRegion.split(tex, 16, 16)[0];
		deathSprites = TextureRegion.split(tex2, 16, 16)[0];
		setAnimation(sprites, 1 / 8f);
	}
	
	public void update(float dt) {
		super.update(dt);
		tick(dt);
		if(health <= 0 && alive) {
			alive = false;
			dying = true;
			setAnimation(deathSprites, 1 / 12f);
		}
		if(dying && animation.getTimesPlayed() >= 1) {
			dying = false;
		}
	}
	public void phaseUp() {
		phaseUp = false;
	}
	public boolean isPhaseUp() {
		return phaseUp;
	}
	public int getPhase() {
		return phase;
	}
	private void tick(float dt) {
		for(String key : ticks.keySet()) {
			ticks.put(key, ticks.get(key) + dt);
		}
		
		// Phase Level Up
		if(phase != 4 && ticks.get("phaseTick") > phaseTimes[phase]) {
			phase++;
			phaseUp = true;
			ticks.put("levelUp", 0f);
			ticks.put("phaseTick", 0f);
		}
	}
	public float getTick(String key) {
		if(ticks.containsKey(key)) {
			return ticks.get(key);
		}
		return 0;
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
	public float getPrevHealth() {
		return prevHealth;
	}
	public void addPrevHealth(float addition) {
		prevHealth += addition;
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
	public float getPhaseTick() {
		return ticks.get("phaseTick");
	}
	public int getPhaseUpTime() {
		return phaseTimes[phase];
	}
}
