package com.conner.avoid.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
	private TextButton buttonPlay, buttonExit;
	private TextureAtlas atlas;
	private BitmapFont black;
	
	public MainMenu(final GameStateManager gsm) {
		super(gsm);

		black = Application.font;
		
		atlas = new TextureAtlas("ui/button2.pack");
		skin = new Skin(atlas);
		
		stage = new Stage();

		Gdx.input.setInputProcessor(stage);
		
		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.up = skin.getDrawable("button.normal");
		tbs.down = skin.getDrawable("button.pressed");
		tbs.pressedOffsetX = 1;
		tbs.pressedOffsetY = -1;
		tbs.font = black;
		black.setScale(Application.SCALE);
		
		buttonPlay = new TextButton("Play", tbs);
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.clear();
				gsm.setState(GameStateManager.PLAY);
			}
		});
		buttonExit = new TextButton("Quit", tbs);
		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		table.add(buttonPlay).width(300).pad(30);
		table.row();
		table.add(buttonExit).width(300).pad(30);
		stage.addActor(table);
	}

	@Override
	public void handleInput() {
		
	}

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
	}
}
