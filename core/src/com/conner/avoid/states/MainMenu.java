package com.conner.avoid.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.conner.avoid.Application;
import com.conner.avoid.handlers.GameStateManager;


public class MainMenu extends GameState {

	private Stage stage;
	private Table table;
	private Skin skin;
	private Label title;
	private TextButton buttonPlay, buttonHS, buttonExit;
	private TextureAtlas atlas;
	private BitmapFont black;
	
	public MainMenu(final GameStateManager gsm) {
		super(gsm);

		black = Application.font;
		
		// Init Stage
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// Init ui graphics
		atlas = new TextureAtlas("ui/button2.pack");
		skin = new Skin(atlas);
		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(table);
		
		// Init button styles
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.up = skin.getDrawable("button.normal");
		tbs.down = skin.getDrawable("button.pressed");
		tbs.pressedOffsetX = 1;
		tbs.pressedOffsetY = -1;
		black.setScale(2f);
		tbs.font = black;
		
		// Title Label
		LabelStyle ls = new LabelStyle();
		ls.font = black;
		
		title = new Label("DEFLECTOR", ls);
		
		// Play Button
		buttonPlay = new TextButton("Play", tbs);
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.clear();
				gsm.setState(GameStateManager.PLAY);
			}
		});
		
		// Play Button
		buttonHS = new TextButton("Highscores", tbs);
		buttonHS.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.clear();
				gsm.setState(GameStateManager.HIGHSCORE);
			}
		});
		
		// Quit button
		buttonExit = new TextButton("Quit", tbs);
		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		// Add actors to stage
		table.top();
		table.add(title).pad(100);
		table.row();
		table.bottom();
		table.add(buttonPlay).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getHeight() / 8).padBottom(30 * Gdx.graphics.getDensity());
		table.row();
		table.add(buttonHS).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getHeight() / 8).padBottom(30 * Gdx.graphics.getDensity());
		table.row();
		table.add(buttonExit).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getHeight() / 8).padBottom(70 * Gdx.graphics.getDensity());
	}

	@Override
	public void resize(int w, int h) {
		stage.getViewport().update(w, h, true);
	}
	
	@Override
	public void handleInput() { }

	@Override
	public void update(float dt) {
		handleInput();
		stage.act(dt);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		
		stage.draw();
	}

	@Override
	public void dispose() { 
		stage.dispose();
	}
}
