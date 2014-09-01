package com.sywhofloats.tetris.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameBoard {

	public static final int BOARD_WIDTH = 10;
	public static final int BOARD_HEIGHT = 25;
	public static final int FIRST_VISIBLE_LINE = 3;


	public int[][] board;

	private Tetromino currentTet;

	private ArrayList<Tetromino> currentPot;
	
	public ArrayList<Integer> filledLines;

	public static final int[][] TITLE_LOGO = {
		{15,15,15,15,15,15,15,15,15,15},
		{9,9,9,9,9,9,9,9,9,9},
		{1,1,9,3,3,9,9,5,9,9},
		{9,1,2,2,3,4,4,5,6,6},
		{9,1,2,9,3,4,9,5,6,9},
		{9,1,2,2,3,4,9,5,6,6},
		{9,1,2,9,3,4,9,5,9,6},
		{9,9,2,2,9,4,9,9,6,6},
		{9,9,9,9,9,9,9,9,9,9},
		{15,15,15,15,15,15,15,15,15,15}
	};
	
	private Random random;

	public GameBoard() {}

	public void init() {
		board = new int[BOARD_HEIGHT][BOARD_WIDTH];
		random = new Random(System.currentTimeMillis());
		filledLines = new ArrayList<>();
		fillCurrentPot();
		updateCurrentTetWithNext();
	}

	public void showTitleScreen(){
		currentTet.setY(-3);
		int yOffset = -4;
		for(int y=FIRST_VISIBLE_LINE;y<BOARD_HEIGHT;y++){
			if(y+yOffset>=0&&y+yOffset<TITLE_LOGO.length){
				board[y]=TITLE_LOGO[y+yOffset];
			} else {
				board[y] = new int[BOARD_WIDTH];
			}
		}
	}
	
	public boolean checkAndMoveCurrentTet(int deltaX,int deltaY) {
		if (checkMatrixAllowedAtPosition(currentTet.getX()+deltaX, currentTet.getY()+deltaY, currentTet.getCurrentMatrix())){
			currentTet.setX(currentTet.getX()+deltaX);
			currentTet.setY(currentTet.getY()+deltaY);
			return true;
		}
		return false;
	}
	

	public boolean checkAndRotateCurrentTet(boolean isLeftRotation) {
		// implemented a much simpler wallkick than the original idea
		if(!checkMatrixAllowedAtPosition(currentTet.getX(), currentTet.getY(), isLeftRotation?currentTet.getLeftRotatedMatrix():currentTet.getRightRotatedMatrix())){
			if(checkMatrixAllowedAtPosition(currentTet.getX()-1, currentTet.getY(), isLeftRotation?currentTet.getLeftRotatedMatrix():currentTet.getRightRotatedMatrix())){
				currentTet.setX(currentTet.getX()-1);
			} else {
				if(checkMatrixAllowedAtPosition(currentTet.getX()+1, currentTet.getY(), isLeftRotation?currentTet.getLeftRotatedMatrix():currentTet.getRightRotatedMatrix())){
					currentTet.setX(currentTet.getX()+1);
				} else {
					// also check for the I and POW tetrominos, which might need shifting +2
					if(currentTet.isLongType() && checkMatrixAllowedAtPosition(currentTet.getX()+2, currentTet.getY(), isLeftRotation?currentTet.getLeftRotatedMatrix():currentTet.getRightRotatedMatrix())){
						currentTet.setX(currentTet.getX()+2);
					} else {
						return false;
					}
				}
			}
		}
		
		if(isLeftRotation){
			currentTet.rotateLeft();
		} else {
			currentTet.rotateRight();
			
		}		
		return true;
	}

	public void fillCurrentPot() {
		ArrayList<Integer> list = Tetromino.normalTetronimoTypeList();
		currentPot=new ArrayList<Tetromino>();
		while(list.size()>0){
			int index=random.nextInt(list.size());
			if(list.get(index)==Tetromino.I && random.nextInt(10)>8){
				list.set(index, Tetromino.X);
			}
			Tetromino tet = new Tetromino(list.get(index),random.nextInt(4));
			tet.setX(BOARD_WIDTH/2-2);
			currentPot.add(tet);
			list.remove(index);
		}
	}

	public void updateCurrentTetWithNext(){
		currentTet=currentPot.get(0);
		currentPot.remove(0);
		if(currentPot.size()==0){
			fillCurrentPot();
		}
	}

	public Tetromino getNextTet(){
		return currentPot.get(0);
	}

	public int getFilledLineCount(){
		return filledLines.size();
	}
	
	public boolean isCurrentTetBonus(){
		return currentTet.isBonus();
	}
	
	public void pasteCurrentTetromino(){
		TetrominoMatrix matrix=currentTet.getCurrentMatrix();
		// if you try to set something out of bounds, do nothing
		for(int y=0;y<4;y++){
			if(currentTet.getY()+y>0 && currentTet.getY()+y<BOARD_HEIGHT) {
				for(int x=0;x<4;x++){
					if(matrix.getMatrix()[y][x]!=0){
						if(currentTet.getX()+x>=0 && currentTet.getX()+x<BOARD_WIDTH) {
							board[currentTet.getY()+y][currentTet.getX()+x]=matrix.getMatrix()[y][x];
						}
					}
				}
			}
		}
	}

	private boolean checkMatrixAllowedAtPosition(int testX, int testY, TetrominoMatrix tetMatrix) {

		int[][] matrix=tetMatrix.getMatrix();
		for(int y=0;y<4;y++){
			for(int x=0;x<4;x++){
				if(matrix[y][x]!=0){
					if(testY+y<0 || testY+y >= BOARD_HEIGHT || (testX+x)<0 || (testX+x) >= BOARD_WIDTH) {
						// out of bounds
						return false;
					}

					if(board[testY+y][testX+x]!=0){
						// both the matrix and the board are non-zero == collision
						return false;
					}
				}


			}	
		}
		return true;
	}
	
	public ArrayList<int[]> getCurrentLinesForRendering(boolean flashFilledLines) {
		ArrayList<int[]> returnBoard = new ArrayList<>();
		
		int[][] currentTetMatrix=currentTet.getCurrentMatrix().getMatrix();

		for(int y=GameBoard.BOARD_HEIGHT-1;y>=GameBoard.FIRST_VISIBLE_LINE;y--){
			int[] lineClone = new int[board[0].length];
			if(filledLines.contains(y)&&flashFilledLines){
				Arrays.fill(lineClone, Tetromino.FILLED);
			} else {
				// have to do this, GWT won't compile with .clone() on simple arrays
				System.arraycopy(board[y], 0, lineClone, 0, board[y].length);
				if(y>=currentTet.getY() && y<currentTet.getY()+4){
					for(int x=currentTet.getX();x<currentTet.getX()+4;x++){
						if(x>=0&&x<lineClone.length&&currentTetMatrix[y-currentTet.getY()][x-currentTet.getX()]!=0){
							lineClone[x]=currentTetMatrix[y-currentTet.getY()][x-currentTet.getX()];
						}
					}
				}
			}
			returnBoard.add(lineClone);
		}
		return returnBoard;
	}
	
	public int calculateFilledLines()  {
		for(int i=0;i<board.length;i++){
			boolean add=true;
			for(int j=0;j<board[i].length;j++){
				if(board[i][j]==0){
					add=false;
					break;
				}
			}
			if(add) {
				filledLines.add(i);
			}
		}
		return filledLines.size();
	}

	public void destroyFilledLinesShiftBoard() {
		int c=board.length-1;
		for(int i=board.length-1;i>=0;i--){
			while(filledLines.contains(c)){
				c--;
			}
			board[i]=(c>=0)?board[c]:new int[BOARD_WIDTH];
			c--;
		}
		filledLines=new ArrayList<>();
	}
	
	public boolean fillNextLineWithFail(){
		if(currentTet.getY()!=-3){
			pasteCurrentTetromino();
			currentTet.setY(-3);
		}
		 for(int y=BOARD_HEIGHT-1;y>=FIRST_VISIBLE_LINE;y--){
			if(board[y][0]!=Tetromino.DEAD){
				Arrays.fill(board[y],Tetromino.DEAD);
				return true;
			}
		}
		return false;
		
	}


}
