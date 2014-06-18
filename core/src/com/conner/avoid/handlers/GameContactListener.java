package com.conner.avoid.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.conner.avoid.entities.Player;

public class GameContactListener implements ContactListener {
	
	private Array<Body> deflectsToRemove;
	
	public GameContactListener() {
		super();
		deflectsToRemove = new Array<Body>();
	}
	
	// Called when two fixtures start to collide
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if(fa == null || fb == null) return;
		if(fa.getUserData() == null || fb.getUserData() == null) return;
		
		if(fa.getUserData().equals("deflector-hit")) {
			if(fb.getUserData().equals("player")) {
				deflectsToRemove.add(fa.getBody());
			}
		}
		if(fb.getUserData().equals("deflector-hit")) {
			if(fa.getUserData().equals("player")) {
				deflectsToRemove.add(fb.getBody());
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
	}
	
	public Array<Body> getBodiesToRemove() {
		return deflectsToRemove;
	}
	
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
