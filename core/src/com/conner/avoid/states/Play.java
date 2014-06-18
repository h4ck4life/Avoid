package com.conner.avoid.states;

import static com.conner.avoid.utils.B2DVars.PPM;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.conner.avoid.Application;
import com.conner.avoid.entities.Deflector;
import com.conner.avoid.entities.HUD;
import com.conner.avoid.entities.Player;
import com.conner.avoid.handlers.GameContactListener;
import com.conner.avoid.handlers.GameStateManager;
import com.conner.avoid.input.InputHandler;
import com.conner.avoid.map.LevelMap;
import com.conner.avoid.utils.B2DVars;

public class Play extends GameState {

	private World world;
	private GameContactListener cl;
	private Box2DDebugRenderer b2dr;
	
	private OrthographicCamera b2dCam;
	
	// HUD
	private HUD hud;
	
	// Map
	private LevelMap map;
	
	// Player vars
	private Player player;
	
	// Deflectors
	private Array<Deflector> deflects;
	
	// Game Vars
	private boolean paused;
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		paused = false;
		
		world = new World(new Vector2(0, 0f), true);
		World.setVelocityThreshold(0f);
		world.setContactListener(cl = new GameContactListener());
		
		b2dr = new Box2DDebugRenderer();
		
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Application.V_WIDTH / PPM, Application.V_HEIGHT / PPM);
		
		// Create the player
		createPlayer();
		
		// Deflectors
		deflects = new Array<Deflector>();
		
		// Create the map
		map = new LevelMap(world, "test_map");
		
		// Create the hud
		hud = new HUD(player);
	}

	@Override
	public void handleInput() {
		if((Gdx.input.isTouched() && Gdx.input.isTouched(2)) || InputHandler.isPressed(InputHandler.BUTTON_Z)) {
			paused = !paused;
		}
		if(!paused) {
			if(player.isAlive() && Gdx.input.isTouched()) {
				Vector3 test;
				camera.unproject(test = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
				player.getBody().applyForceToCenter(new Vector2((test.x / PPM - player.getBody().getPosition().x) * 50, (test.y / PPM - player.getBody().getPosition().y) * 50), true);
			}
			if((Gdx.input.isTouched() && Gdx.input.isTouched(1)) || InputHandler.isDown(InputHandler.BUTTON_X)) {
				world.destroyBody(player.getBody());
				createPlayer();
			}
		}
	}

	private float accum = 0;
	private float score_accum = 0;
	
	@Override
	public void update(float dt) {
		handleInput();
		
		if(!paused) {
			world.step(dt, 6, 2);
			
			// remove bullets
			Array<Body> bodies = cl.getBodiesToRemove();
			for(Body b : bodies) {
				if(player.isAlive()) {
					deflects.removeValue((Deflector) b.getUserData(), true);
					world.destroyBody(b);
					player.hurt(5);
				}
			}
			bodies.clear();
			
			//if(!player.isAlive() && !player.isDying()) world.destroyBody(player.getBody());
			
			if(player.isAlive() || player.isDying()) 
				player.update(dt);
			
			for(Deflector d : deflects) {
				d.update(dt);
			}
			
			score_accum += dt;
			if(score_accum > 1) {
				score_accum = 0;
				if(player.isAlive()) 
					player.addPoints(1);
			}
			
			accum += dt;
			if(accum > 3) {
				accum = 0;
				if(player.isAlive())
					createDeflector(player.getPosition().x, player.getPosition().y);
			}
		}
	}

	@Override
	public void render() {
		// Clear Screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		
		camera.position.set(
				player.getPosition().x * PPM,
				player.getPosition().y * PPM,
				0
		);
		camera.update();
		
		// Draw map
		map.render(camera);
		
		// Draw player/deflectors
		batch.setProjectionMatrix(camera.combined);
		player.render(batch);
		for(Deflector d : deflects) {
			d.render(batch);
		}
		
		// Draw hud
		batch.setProjectionMatrix(hudCamera.combined);
		hud.render(batch);
		
		batch.begin();
		if(paused) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			ShapeRenderer sr = new ShapeRenderer();
			sr.setProjectionMatrix(hudCamera.combined);
			sr.begin(ShapeType.Filled);
			sr.setColor(new Color(.5f, .5f, .5f, 0.5f));
			sr.rect(0, 0, app.V_WIDTH, app.V_HEIGHT);
			sr.end();
		}
		batch.end();
		// Draw box2d world
		//b2dr.render(world, b2dCam.combined);
	}

	@Override
	public void dispose() {
		batch.dispose();
		Application.res.disposeAll();
	}
	
	private void createPlayer() {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		
		// Create Player
		bdef.position.set(320 / PPM, 190 / PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		shape.setRadius(6 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_DEFLECT;
		body.setFixedRotation(true);
		body.setLinearDamping(10f);
		body.createFixture(fdef).setUserData("player");
		shape.dispose();
		
		player = new Player(body);
		body.setUserData(player);
		
		hud = new HUD(player);
	}
	
	private void createDeflector(float x, float y) {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		// Create Player
		bdef.position.set(x, y);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		
		shape.setAsBox(3 / PPM, 3 / PPM);
		fdef.shape = shape;
		fdef.restitution = 1f;
		fdef.filter.categoryBits = B2DVars.BIT_DEFLECT;
		fdef.filter.maskBits = B2DVars.BIT_GROUND;
		body.setFixedRotation(true);
		Random r = new Random();
		int xVel = r.nextInt(4) - 2;
		int yVel = r.nextInt(4) - 2;
		if(xVel == 0) xVel = 1;
		if(yVel == 0) yVel = 1;
		body.setLinearVelocity(xVel, yVel);
		body.createFixture(fdef).setUserData("deflector");
		
		// Player hit sensor (sleeps for .25s)
		shape.setAsBox(3 / PPM, 3 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_DEFLECT;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("deflector-hit-sleep");
		shape.dispose();
		
		deflects.add(new Deflector(body));
		body.setUserData(deflects.get(deflects.size-1));
	}
}
