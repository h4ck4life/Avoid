package com.conner.avoid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.conner.avoid.Application;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Application.V_WIDTH * Application.SCALE;
		config.height = Application.V_HEIGHT * Application.SCALE;
		config.title = Application.TITLE;
		new LwjglApplication(new Application(), config);
	}
}
