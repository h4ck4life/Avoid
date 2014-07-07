package com.conner.avoid.states;

import static com.conner.avoid.utils.B2DVars.PPM;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
	private ScreenViewport worldView; // TODO: learn how to utilize this with .setPixelsPerUnit scale 1/PPM
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
	private Stage stage;
	private Table table;
	private Skin skin;
	private TextButton buttonPause, buttonResume, buttonRestart, buttonReturn, buttonOptions;
	private TextureAtlas atlas;
	private BitmapFont black;
	
	// Touchpad Controller
	private Touchpad touchpad;
    private TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private boolean inControl = false;
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		initPauseMenu();
		initTouchpad();
		
		world = new World(new Vector2(0, 0f), true);
		World.setVelocityThreshold(0f); // Fix: http://www.badlogicgames.com/wordpress/?p=2030
		world.setContactListener(cl = new GameContactListener());
		camera.zoom = 1f;
		
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
		hud = new HUD(stage, player);
	}
	
	private void initTouchpad() {
		// Create a touchpad skin    
        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("ui/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("ui/touchKnob.png"));
        
        // Touchpad Images
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        
        // Create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        
        //Create new TouchPad with style
        touchpad = new Touchpad(3, touchpadStyle);
        touchpad.setBounds(0, 0, 110, 110);
        touchpad.setVisible(false);
        
        // Add touchpad to stage8
        stage.addActor(touchpad);
	}
	
	private void initPauseMenu() {
		paused = false;
		
		black = Application.font;
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		atlas = new TextureAtlas("ui/button2.pack");
		skin = new Skin(atlas);
		table = new Table(skin);
		table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		
		// Initialize button styles
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.up = skin.getDrawable("button.normal");
		tbs.down = skin.getDrawable("button.pressed");
		tbs.pressedOffsetX = 1;
		tbs.pressedOffsetY = -1;
		tbs.font = black;
		black.setScale(1f);
		
		// In-game Pause Button
		buttonPause = new TextButton("ll", tbs);
		buttonPause.setWidth(50);
		buttonPause.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 80);
		buttonPause.setVisible(true);
		buttonPause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				buttonPause.setVisible(false);
				buttonResume.setVisible(true);
				buttonReturn.setVisible(true);
				buttonRestart.setVisible(true);
				paused = true;
			}
		});
		
		// In-pause Resume
		buttonResume = new TextButton("Resume", tbs);
		buttonResume.setVisible(false);
		buttonResume.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				buttonPause.setVisible(true);
				buttonResume.setVisible(false);
				buttonReturn.setVisible(false);
				buttonRestart.setVisible(false);
				paused = false;
			}
		});
		
		// In-pause Return to main menu
		buttonReturn = new TextButton("Main Menu", tbs);
		buttonReturn.setVisible(false);
		buttonReturn.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				paused = true;
				gsm.setState(GameStateManager.MAINMENU);
			}
		});
		
		// In-pause Restart Game
		buttonRestart = new TextButton("Retry", tbs);
		buttonRestart.setVisible(false);
		buttonRestart.addListener(new ClickListener() { 
			@Override
			public void clicked(InputEvent event, float x, float y) {
				paused = false;
				world.destroyBody(player.getBody());
				buttonResume.setVisible(false);
				buttonReturn.setVisible(false);
				buttonRestart.setVisible(false);
				buttonPause.setVisible(true);
				createPlayer();
			}
		});
		
		// Add everything to the stage
		table.add(buttonResume).width(200).pad(30);
		table.row();
		table.add(buttonRestart).width(200).pad(30);
		table.row();
		table.add(buttonReturn).width(200).pad(30);
		stage.addActor(buttonPause);
		stage.addActor(table);
	}
	
	@Override
	public void handleInput() {
		if(!paused) {
			// OLD Touch Based Control Scheme
			if(player.isAlive() && Gdx.input.isTouched()) {
				Vector3 test;
				camera.unproject(test = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
				player.getBody().applyForceToCenter(new Vector2((test.x / PPM - player.getBody().getPosition().x) * 20, (test.y / PPM - player.getBody().getPosition().y) * 20), true);
			}
			
			if(Gdx.input.isTouched() && false && player.isAlive()) {
				if(!inControl) {
					inControl = true;
					touchpad.setPosition(Gdx.input.getX() - touchpad.getWidth()/2,  -Gdx.input.getY() + Gdx.graphics.getHeight() - touchpad.getHeight()/2);
					touchpad.setVisible(true);
					InputEvent et = new InputEvent(); 
					et.setType(Type.touchDown);
					et.setStageX(Gdx.input.getX());
					et.setStageY(Gdx.input.getY());
					touchpad.fire(et);
				} else {
					player.getBody().setLinearVelocity(new Vector2(touchpad.getKnobPercentX() * 2.5f, touchpad.getKnobPercentY() * 2.5f));
				}
			} else {
				inControl = false;
				touchpad.setVisible(false);
			}
			
			if((Gdx.input.isTouched() && Gdx.input.isTouched(1)) || InputHandler.isDown(InputHandler.BUTTON_X)) {
				world.destroyBody(player.getBody());
				createPlayer();
			}

			if(Gdx.input.isKeyPressed(Keys.A)) camera.zoom += .1f;
			if(Gdx.input.isKeyPressed(Keys.S)) camera.zoom -= .1f;
		}
		
		
		
	}

	private float accum = 0;
	private float score_accum = 0;
	
	@Override
	public void update(float dt) {
		stage.act(dt);
		if(!buttonPause.isPressed()) {
			handleInput();
		}
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
			
			if(player.isAlive() || player.isDying()) 
				player.update(dt);
			
			for(Deflector d : deflects) {
				d.update(dt);
			}
			
			score_accum += dt;
			if(score_accum > 1) {
				score_accum = 0;
				if(player.isAlive()) 
					player.addPoints(5);
			}
			
			accum += dt;
			if(accum > 3) {
				accum = 0;
				if(player.isAlive()) {
					for(int i = 0; i < player.getPhase()+1; i++) {
						createDeflector(player.getPosition().x, player.getPosition().y);
					}
				}
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
		if(paused) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			ShapeRenderer sr = new ShapeRenderer();
			sr.setProjectionMatrix(hudCamera.combined);
			sr.begin(ShapeType.Filled);
			sr.setColor(new Color(.5f, .5f, .5f, 0.5f));
			sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			sr.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
		
		stage.draw();
		
		if(player.isPhaseUp()) {
			if(player.getTick("levelUp") < 1f) {
				Gdx.gl.glEnable(GL20.GL_BLEND);
				ShapeRenderer sr = new ShapeRenderer();
				sr.begin(ShapeType.Filled);
				sr.setColor(new Color(1f, 1f, 1f, 1f - player.getTick("levelUp")));
				sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				sr.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
			} else {
				player.phaseUp();
			}
		}
		
		// Draw box2d world
		//b2dr.render(world, b2dCam.combined);
	}

	@Override 
	public void resize(int w, int h) {
		stage.getViewport().update(w, h, true);
	}
	
	@Override
	public void dispose() { 
		world.dispose();
		stage.dispose();
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
		
		hud = new HUD(stage, player);
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
