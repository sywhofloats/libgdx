package com.sywhofloats.tetris;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.sywhofloats.tetris.objects.GameBoard;

public class GameController {
	
	private static final float FRAME_LENGTH=0.01f;

	private static final int MOVE_LEFT = 1;
	private static final int MOVE_RIGHT = 2;
	private static final int ROT_LEFT = 3;
	private static final int ROT_RIGHT = 4;	
	private static final int SOFT_DROP = 5;	
	private static final int HARD_DROP = 6;	
	private static final int CHEAT = 7;	
	private static final int PAUSE = 8;	
	
	
	public static final int GAME_STATE_MENU = 0;
	public static final int GAME_STATE_GAME = 1;
	public static final int GAME_STATE_LOST = 2;
	public static final int GAME_STATE_OVER = 3;
	public static final int GAME_STATE_PAUSED = 4;
	public static final int GAME_STATE_OPTIONS = 5;	
	
	public static final int FLASH_FREQUENCY = 26;
	
	private static final int[] SCORE_FACTORS = {100,300,500,800}; 
	
	public int getGameState() {
		return gameState;
	}

	private int gameState = GAME_STATE_MENU;
	
	private float frameCounter;
	private int turnCounter;
	private int turnSpeed;
	private int keyCounter;
	private int keyDelay;
	private int lastKey;
	private int filledCounter;
	private int flashCounter; 
	
	private int score;
	private int lines;	
	private int level;	
	
	private int startingLevel = 1;

	private int shakeYOffset = 0; 
	
	public int getScore() {
		return score;
	}

	public int getLines() {
		return lines;
	}

	public int getLevel() {
		return level;
	}

	private HashMap<Integer, Integer> inputKeys;

	private GameBoard board = new GameBoard();


	public GameController() {
		init();
	}

	private void init() { 
		
		Assets.instance.music.themeSong.setLooping(true);
		Assets.instance.music.themeSong.setVolume(0.5f);
		Assets.instance.music.themeSong.play();
		
		inputKeys = new HashMap<>();
		inputKeys.put(Keys.A,MOVE_LEFT);
		inputKeys.put(Keys.D,MOVE_RIGHT);
		inputKeys.put(Keys.W,ROT_RIGHT);
		inputKeys.put(Keys.Q,ROT_LEFT);
		inputKeys.put(Keys.S,SOFT_DROP);
		inputKeys.put(Keys.SPACE,HARD_DROP);
		//inputKeys.put(Keys.C,CHEAT);
		inputKeys.put(Keys.P,PAUSE);
		
		board.init();
	}
	
	private void newGame(){
		frameCounter = 0f;
		turnSpeed = 50;
		keyCounter = 0;
		keyDelay = 4;
		filledCounter=0;
		
		this.level = startingLevel;
		score = 0;
		lines = 0;
		
		board.init();

	}

	public int getShakeYOffset(){
		return shakeYOffset;
	}
	
	public ArrayList<int[]> getLinesForRendering(){
		return board.getCurrentLinesForRendering(flashCounter>FLASH_FREQUENCY/2);
	}
	
	public int[][] getNextTetMatrix(){
		return board.getNextTet().getCurrentMatrix().getMatrix();
	}

	public boolean flashFilledLines() {
		return (filledCounter&1)==1;
	}
	
	public void update (float deltaTime) {
		frameCounter+=deltaTime;
		while(frameCounter>FRAME_LENGTH){
			frameCounter-=FRAME_LENGTH;			
			flashCounter=(flashCounter+1)%FLASH_FREQUENCY;
			
			int input=handleInput();
			
			switch(gameState){
			case GAME_STATE_GAME:
				mainGame(input);
				break;
			case GAME_STATE_PAUSED:
				if(lastKey!=PAUSE && input==PAUSE) {
					gameState=GAME_STATE_GAME;
				}
				lastKey=input;
				break;
			case GAME_STATE_LOST:
				turnCounter++;
				if(turnCounter>(turnSpeed/16)) {
					turnCounter=0;
					if(!board.fillNextLineWithFail()){
						gameState = GAME_STATE_OVER;
					}
				}
				break;
			case GAME_STATE_OVER:
				if(input!=0){
					gameState = GAME_STATE_MENU;
					lastKey=input;
				}
				break;
			default:
				gameState = GAME_STATE_MENU;
				board.showTitleScreen();
				if(input!=0&&input!=lastKey){
					if((input==MOVE_LEFT||input==ROT_LEFT)&&startingLevel>1){
						startingLevel--;
					}
					if((input==MOVE_RIGHT||input==ROT_RIGHT)&&startingLevel<20){
						startingLevel++;
					}
					if(input==SOFT_DROP||input==HARD_DROP){
						gameState = GAME_STATE_GAME;
						newGame();
					}
				}
				lastKey=input;
				break;
			}
		}
	}

	private int handleInput() {
		if (Gdx.app.getType() == ApplicationType.Desktop || Gdx.app.getType() == ApplicationType.WebGL) {
			for(Integer key:inputKeys.keySet()){
				if (Gdx.input.isKeyPressed(key)) {
					return inputKeys.get(key);
				}
			}
		}
		if (Gdx.app.getType() == ApplicationType.Android) {
			if(Gdx.input.isTouched()){
				
				float propX = (float)Gdx.input.getX()/(float)Gdx.graphics.getWidth();
				float propY = (float)Gdx.input.getY()/(float)Gdx.graphics.getHeight();
				
				if(propY > 0.666f) {
					if(propX < 0.333f){
						return MOVE_LEFT;
					} else if(propX > 0.666f){
						return MOVE_RIGHT;
					} else {
						return SOFT_DROP;
					}
				} else {
					if(propX < 0.333f){
						return ROT_LEFT;
					}
					if(propX > 0.666f){
						return ROT_RIGHT;
					}
				}
			}
		}
		return 0;
	}
	
	private boolean nextBlockWithLosingCheck() {
		board.updateCurrentTetWithNext();
		if(!board.checkAndMoveCurrentTet(0,0)){
			Assets.instance.sounds.lost.play();
			return true;
		}
		return false;
	}
	
	private void mainGame(int input) {
		shakeYOffset=-shakeYOffset;
		if(shakeYOffset>0){
			shakeYOffset--;
		}
		turnCounter++;
		
		// filled counter>0 means we are flashing the filled blocks 
		if(filledCounter>0){
			if(turnCounter>(turnSpeed-level*2)) {
				turnCounter=0;
				filledCounter--;
				// stop flashing, clear the filled lines
				if(filledCounter==0){
					score+= (SCORE_FACTORS[board.getFilledLineCount()-1])*level*(board.isCurrentTetBonus()?2:1);
					lines+= board.getFilledLineCount();
					switch(board.getFilledLineCount()){
					case 2:
						Assets.instance.sounds.lines2.play();
						break;
					case 3:
						Assets.instance.sounds.lines3.play();
						shakeYOffset = 10;
						break;					
					case 4:
						Assets.instance.sounds.lines4.play();
						shakeYOffset = 25;
						break;		
					default:
						Assets.instance.sounds.lines1.play();
						break;					
					}
					board.destroyFilledLinesShiftBoard();
					
					if(nextBlockWithLosingCheck()){
						gameState = GAME_STATE_LOST;
					}
					if(level < 20 && level<(lines/10)+1){
						level++;
					}
				}
			}
		} else {
			// turns get quicker depending on the level
			if(turnCounter>(turnSpeed-level*2)) {
				turnCounter=0;
				boolean fallPermitted = board.checkAndMoveCurrentTet(0,1);
				if(!fallPermitted){
					board.pasteCurrentTetromino();
					Assets.instance.sounds.blockLand.play();
					board.calculateFilledLines();
					if(board.getFilledLineCount()>0){
						switch(board.getFilledLineCount()){
						case 3:
							Assets.instance.sounds.lineSignal.play();
							break;
						case 4:
							Assets.instance.sounds.lineSignal4.play();
							break;					
						default:
							break;					
						}
						filledCounter=4;
					} else {
						if(nextBlockWithLosingCheck()){
							gameState = GAME_STATE_LOST;
						}
					}
				}
			} else {

				if(keyCounter == 0){
					boolean noKey = false;
					switch(input) {
					case MOVE_LEFT:
						board.checkAndMoveCurrentTet(-1,0);
						break;
					case MOVE_RIGHT:
						board.checkAndMoveCurrentTet(1,0);
						break;
					case ROT_RIGHT:
						if(lastKey!=ROT_RIGHT) {
							if(board.checkAndRotateCurrentTet(false)){
								Assets.instance.sounds.rotate.play();
							}
						}
						break;
					case ROT_LEFT:
						if(lastKey!=ROT_LEFT) {
							if(board.checkAndRotateCurrentTet(true)){
								Assets.instance.sounds.rotate.play();
							}
						}
						break;
					case SOFT_DROP:
						if(board.checkAndMoveCurrentTet(0,1)){
							score++;
						}
						break;
					case HARD_DROP:
						if(lastKey!=HARD_DROP) {
							if(board.checkAndMoveCurrentTet(0,1)){
								score+=2;
								while(board.checkAndMoveCurrentTet(0,1)){
									score+=2;
								};
							}
						}
						break;
					case CHEAT:
						board.init();
						break;
					case PAUSE:
						if(lastKey!=PAUSE) {
							gameState=GAME_STATE_PAUSED;
						}
						break;
					default:
						noKey = true;
						break;
					}
					if(!noKey){
						keyCounter=keyDelay;
					}
					lastKey=input;
				} else {
					keyCounter--;
				}
			}
		}
	}
	
	public int getStartingLevel() {
		return startingLevel;
	}
	

}
