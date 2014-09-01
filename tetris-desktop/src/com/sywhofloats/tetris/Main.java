package com.sywhofloats.tetris;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "tetris";
		cfg.useGL20 = false;
		cfg.width = 576;
		cfg.height = 768;
		cfg.resizable = true;
		//cfg.fullscreen = true;
		
		new LwjglApplication(new Tetris(), cfg);
	}
}
