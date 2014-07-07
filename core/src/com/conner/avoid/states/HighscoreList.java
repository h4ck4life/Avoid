package com.conner.avoid.states;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.conner.avoid.Application;
import com.conner.avoid.handlers.GameStateManager;

public class HighscoreList extends GameState {

	private Stage stage;
	private Table table;
	private Skin skin;
	private TextureAtlas atlas;
	private ScrollPane list_Highscores;
	private TextButton btn_Menu;
	private Label highscoreEntry, title;
	private BitmapFont font;
	
	private ArrayList<Integer> highscores;
	
	public HighscoreList(GameStateManager gsm) {
		super(gsm);
		initList();
	}

	private void initList() {
		
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
		tbs.font = Application.font;
		
		// Return to Main Menu button
		btn_Menu = new TextButton("Return", tbs);
		btn_Menu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.clear();
				gsm.setState(GameStateManager.MAINMENU);
			}
		});
		
		// Highscore list -- Max of 10 high scores
		Table contents = new Table(skin);
		getHighscores(contents);
		list_Highscores = new ScrollPane(contents);
		list_Highscores.setWidth(Gdx.graphics.getWidth());
		
		
		table.top();
		table.add(title);
		table.row();
		table.add(list_Highscores);
		table.row();
		table.add(btn_Menu);
	}
	
	private ArrayList<Integer> getHighscores(Table listToPopulate) {
		LabelStyle ls = new LabelStyle();
		ls.font = Application.font;
		title = new Label("Highscore List: ", ls);
		ArrayList<Integer> scores = new ArrayList<Integer>();
//		highscores = 
//		if(file.exists()) {
//			String[] fileList = file.readString().split("\n");
//			for(int i = 0; i < 10; i++) {
//				highscoreEntry = new Label(((i+1) + ". " + fileList[i]), ls);
//				listToPopulate.top();
//				listToPopulate.add(highscoreEntry).pad(20f);
//				listToPopulate.row();
//			}
//		} else {
//			for(int i = 0; i < 10; i++) {
//				file.writeString(0+"\n", true);
//				highscoreEntry = new Label(((1+1) + ". " + 0), ls);
//				listToPopulate.top();
//				listToPopulate.add(highscoreEntry).pad(20f);
//				listToPopulate.row();
//			}
//		}
		return scores;
	}
	
	public static ArrayList<Integer> getHighscores() {
		ArrayList<Integer> scores = new ArrayList<Integer>();
		FileHandle file = Gdx.files.local("data/highscores.txt");
		if(file.exists()) {
			String[] fileList = file.readString().split("\n");
			for(String s : fileList) {
				scores.add(Integer.parseInt(s));
			}
		} else {
			for(int i = 0; i < 10; i++) {
				file.writeString(0+"\n", true);
				scores.add(0);
			}
		}
		return scores;
	}
	
	public static void updateHighscores(int newScore) {
		int found = -1;
		ArrayList<Integer> scores = getHighscores();
		for(int i = scores.size()-1; i >= 0; i--) {
			if(scores.get(i) < newScore) {
				found = i;
			}
		}
		if(found != -1) {
			scores = getHighscores();
		}
	}
	
	@Override
	public void handleInput() {
	}

	@Override
	public void update(float delta) {
		stage.act(delta);
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
	
	@Override
	public void resize(int w, int h) {
		stage.getViewport().update(w, h, true);
	}
}
