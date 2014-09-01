package com.sywhofloats.tetris;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;

public class Tetris implements ApplicationListener {
	 
	private GameController gameController;
	private GameRenderer gameRenderer;
	
	private boolean paused;
	
	@Override
	public void create() {		
		
		Gdx.app.setLogLevel(Application.LOG_NONE);
		
		// initialize assets
		Assets.instance.init(new AssetManager());
		
		// initialize game
		gameController = new GameController();
		gameRenderer = new GameRenderer(gameController);
		
		paused = false;
		
	}

	@Override
	public void dispose() {
		gameRenderer.dispose();
	}

	@Override
	public void render() {
		if(!paused) {	
			gameController.update(Gdx.graphics.getDeltaTime());
		}
		Gdx.gl.glClearColor(0f,0f,0f,1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gameRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		gameRenderer.resize(width, height);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;	
	}
}
