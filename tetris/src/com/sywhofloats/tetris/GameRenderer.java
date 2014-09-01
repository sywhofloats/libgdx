package com.sywhofloats.tetris;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.sywhofloats.tetris.objects.Tetromino;

public class GameRenderer implements Disposable {
	

    private static final int VIRTUAL_WIDTH = 576;
    private static final int VIRTUAL_HEIGHT = 768;
    
    private float scalingFactor = 1f;
    
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private GameController gameController;
	private Stage stage = new Stage();
	public GameRenderer (GameController gameController) { 
		this.gameController = gameController;
		init();
	}
	
	private final String BLOCK = Character.toString((char)0x09);
	private final String BLOCK_X1 = Character.toString((char)0x0a);
	private final String BLOCK_X2 = Character.toString((char)0x0b);
	private final String BLOCK_X3 = Character.toString((char)0x0c);
	private final String BLOCK_X4 = Character.toString((char)0x0d);
	
	private final float[][] BLOCK_COLORS 
		= {{0f,0f,0f,0f},{1f,0f,0f,1f},{1f,0.5f,0f,1f},{1f,1f,0f,1f},
			{0f,1f,0f,1f},{0f,0f,1f,1f},{0.5f,0f,1f,1f},{1f,0f,1f,1f},
			{1f,0f,1f,1f},{0.1f,0.1f,0.1f,1f},{0f,0.6f,1f,0.5f},{0f,0.7f,1f,0.5f},
			{0f,0.8f,1f,0.5f},{0f,0.9f,1f,0.5f},{0.4f,0.4f,0.5f,1f},{0.3f,0.3f,0.4f,1f}};

	private void init () { 
		stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),false);
		
		batch = stage.getSpriteBatch();
		camera = (OrthographicCamera) stage.getCamera();
		
		if(Gdx.app.getType() == ApplicationType.Android) {
			scalingFactor = (float) Gdx.graphics.getWidth()/(float)VIRTUAL_WIDTH;
			Assets.instance.fonts.font.setScale(scalingFactor);
		};
	}
	
	public void render () { 
		batch.begin();

		int baseY=50 + gameController.getShakeYOffset();
		
		int y=0;
		
		ArrayList<int[]> lines = gameController.getLinesForRendering();
		
		for(int i = 0;i<=lines.get(0).length+1;i++){
			renderBlock(Tetromino.BORDER,i*32,baseY);
	
		}
		
		for(int[] line:lines){
			y+=32;
			int x=0;
			renderBlock(Tetromino.BORDER,0,baseY+y);
			for(int block:line){
				x+=32;
				if(block>0){
					renderBlock(block,x,baseY+y);
				}
			}
			renderBlock(Tetromino.BORDER,x+32,baseY+y);
		}
		
		
		
		if(gameController.getGameState()!=GameController.GAME_STATE_MENU){
			y=700+baseY;
			for(int[] line:gameController.getNextTetMatrix()){
				y-=32;
				int x=380;
				for(int block:line){
					x+=32;
					if(block>0){
						renderBlock(block,x,y);
					}
				}
			}
		}
		
		Assets.instance.fonts.font.setColor(0.8f,1f,0.8f,1f);
		
		renderText("NEXT", 400, baseY+700);
		
		renderText("SCORE", 400, baseY+482);
		String scoreString = "0000000"+gameController.getScore();
		renderText(scoreString.substring(scoreString.length()-8), 400, baseY+440);
		
		renderText( "LINES", 400, baseY+382);
		String lineString = "   "+gameController.getLines();
		renderText("    "+lineString.substring(lineString.length()-4), 400, baseY+340);
		
		renderText( "LEVEL", 400, baseY+282);
		String levelString = " "+gameController.getLevel();
		renderText("      "+levelString.substring(levelString.length()-2), 400, baseY+240);
		
		if(gameController.getGameState()==GameController.GAME_STATE_OVER){
			Assets.instance.fonts.font.setColor(1f,1f,1f,1f);
			renderText( "=GAME OVER=", 80, baseY+376);
		}
		
		if(gameController.getGameState()==GameController.GAME_STATE_PAUSED){
			Assets.instance.fonts.font.setColor(1f,1f,1f,1f);
			renderText( "=PAUSED=", 113, baseY+376);
		}
		
		if(gameController.getGameState()==GameController.GAME_STATE_MENU){
			Assets.instance.fonts.font.setColor(1f,1f,1f,1f);
			renderText( "Simon Fisher", 80, baseY+326);
			renderText( "Aug 2014", 110, baseY+226);
			renderText( "Start Level: "+gameController.getStartingLevel(), 50, baseY+176);
			
		}
		
		batch.end();

	}
	
	private void renderBlock(int block, int x, int y){
		Assets.instance.fonts.font.setColor(BLOCK_COLORS[block][0], BLOCK_COLORS[block][1], BLOCK_COLORS[block][2], BLOCK_COLORS[block][3]);
		switch(block){
		case Tetromino.X1:
			Assets.instance.fonts.font.draw(batch, BLOCK_X1, x*scalingFactor, y*scalingFactor);
			break;
		case Tetromino.X2:
			Assets.instance.fonts.font.draw(batch, BLOCK_X2, x*scalingFactor, y*scalingFactor);
			break;
		case Tetromino.X3:
			Assets.instance.fonts.font.draw(batch, BLOCK_X3, x*scalingFactor, y*scalingFactor);
			break;
		case Tetromino.X4:
			Assets.instance.fonts.font.draw(batch, BLOCK_X4, x*scalingFactor, y*scalingFactor);
			break;
		default:
			Assets.instance.fonts.font.draw(batch, BLOCK, x*scalingFactor, y*scalingFactor);
		}
	}
	
	private void renderText(String text, int x, int y) {
		Assets.instance.fonts.font.draw(batch,text,x*scalingFactor, y*scalingFactor);
	}
	
	public void resize (int width, int height) {
		

			Vector2 size = Scaling.fit.apply(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, width, height);
	        int viewportX = (int)(width - size.x) / 2;
	        int viewportY = (int)(height - size.y) / 2;
	        int viewportWidth = (int)size.x;
	        int viewportHeight = (int)size.y;
	        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);

			if(Gdx.app.getType() == ApplicationType.Android) {
				scalingFactor = (float) Gdx.graphics.getWidth()/(float)VIRTUAL_WIDTH;
				Assets.instance.fonts.font.setScale(scalingFactor);
			}

	}
	@Override public void dispose () { }
}
