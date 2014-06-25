package com.conner.avoid.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.conner.avoid.Application;
import com.conner.avoid.utils.B2DVars;

public class Deflector extends B2DSprite{

	private float cooldown;
	private boolean alive = false;
	private Vector2[] tail;
	private Vector2 prev;
	
	public Deflector(Body body) {
		super(body);

		Texture tex = Application.res.getTexture("deflector");
		TextureRegion[] sprites = TextureRegion.split(tex, 6, 6)[0];
		setAnimation(sprites, 1 / 12f);
		
		tail = new Vector2[10];
		cooldown = 2f;
		alive = false;
	}
	
	public void update(float dt) {
		super.update(dt);
		if(cooldown > 0 && !alive) {
			cooldown -= dt;
		} else if(!alive) {
			alive = true;
			body.getFixtureList().get(1).setUserData("deflector-hit");
		}

//		tail[0] = getPosition();
//		for(int i = tail.length - 1; i > 0; i--) {
//			if(tail[i] == null)
//				tail[i] = tail[i-1];
//		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		batch.begin();
		for(int i = 0; i < tail.length; i++) {
			if(tail[i] != null)
				batch.draw(animation.getFrame(), 
						tail[i].x * B2DVars.PPM - width / 2,
						tail[i].y * B2DVars.PPM - height / 2
				);
		}
		batch.end();
	}
	public boolean isAlive() {
		return alive;
	}
}
