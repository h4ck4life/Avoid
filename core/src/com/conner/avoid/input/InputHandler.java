package com.conner.avoid.input;

public class InputHandler {
	
	public static int x, y;
	public static boolean down, pdown;
	
	public static boolean[] keys;
	public static boolean[] pkeys;
	
	public static final int NUM_KEYS = 6;
	public static final int BUTTON_X = 0;
	public static final int BUTTON_Z = 1;
	public static final int BUTTON_UP = 2;
	public static final int BUTTON_LEFT = 3;
	public static final int BUTTON_DOWN = 4;
	public static final int BUTTON_RIGHT = 5;
	
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			pkeys[i] = keys[i];
		}
	}

	public static boolean isDown() { return down; }
	public static boolean isPressed() { return down && !pdown; }
	public static boolean isReleased() { return !down && pdown; }
	
	public static void setKey(int i, boolean b) { 
		keys[i] = b; 
	}
	public static boolean isDown(int i) { 
		return keys[i]; 
	}
	public static boolean isPressed(int i) { 
		return keys[i] && !pkeys[i]; 
	}
}