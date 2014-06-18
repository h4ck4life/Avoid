package com.conner.avoid.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {

	
	public boolean mouseMoved(int x, int y) {
		InputHandler.x = x;
		InputHandler.y = y;
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer) {
		InputHandler.x = x;
		InputHandler.y = y;
		InputHandler.down = true;
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button) {
		InputHandler.x = x;
		InputHandler.y = y;
		InputHandler.down = true;
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button) {
		InputHandler.x = x;
		InputHandler.y = y;
		InputHandler.down = false;
		return true;
	}
	
	public boolean keyDown(int k) {
		if(k == Keys.Z) {
			InputHandler.setKey(InputHandler.BUTTON_Z, true);
		}
		if(k == Keys.X) {
			InputHandler.setKey(InputHandler.BUTTON_X, true);
		}
		if(k == Keys.LEFT) {
			InputHandler.setKey(InputHandler.BUTTON_LEFT, true);
		}
		if(k == Keys.UP) {
			InputHandler.setKey(InputHandler.BUTTON_UP, true);
		}
		if(k == Keys.RIGHT) {
			InputHandler.setKey(InputHandler.BUTTON_RIGHT, true);
		}
		if(k == Keys.DOWN) {
			InputHandler.setKey(InputHandler.BUTTON_DOWN, true);
		}
		return true;
	}
	
	public boolean keyUp(int k) {
		if(k == Keys.Z) {
			InputHandler.setKey(InputHandler.BUTTON_Z, false);
		}
		if(k == Keys.X) {
			InputHandler.setKey(InputHandler.BUTTON_X, false);
		}
		if(k == Keys.LEFT) {
			InputHandler.setKey(InputHandler.BUTTON_LEFT, false);
		}
		if(k == Keys.UP) {
			InputHandler.setKey(InputHandler.BUTTON_UP, false);
		}
		if(k == Keys.RIGHT) {
			InputHandler.setKey(InputHandler.BUTTON_RIGHT, false);
		}
		if(k == Keys.DOWN) {
			InputHandler.setKey(InputHandler.BUTTON_DOWN, false);
		}
		return true;
	}
}
