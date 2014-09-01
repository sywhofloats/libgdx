package com.sywhofloats.tetris.objects;

import java.util.ArrayList;
import java.util.Arrays;

public class Tetromino {

	public static final int I = 1;
	public static final int O = 2;
	public static final int T = 3;
	public static final int J = 4;
	public static final int L = 5;
	public static final int S = 6;
	public static final int Z = 7;
	public static final int X = 8;
	public static final int FILLED = 9;
	
	public static final int X1 = 10;
	public static final int X2 = 11;
	public static final int X3 = 12;
	public static final int X4 = 13;
	

	public static final int DEAD = 14;
	
	public static final int BORDER = 15;
	

	public static final TetrominoMatrix[][] LAYOUT_MATRICES = {
		null,
		{
			new TetrominoMatrix(new int[][] 
					{
					{0,0,I,0},
					{0,0,I,0},
					{0,0,I,0},
					{0,0,I,0}
					}
			, 2,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{I,I,I,I},
					{0,0,0,0},
					}
			, 0,3),	
			new TetrominoMatrix(new int[][] 
					{
					{0,0,I,0},
					{0,0,I,0},
					{0,0,I,0},
					{0,0,I,0},
					}
			,2,2),
			new TetrominoMatrix(new int[][] 
					{

					{0,0,0,0},
					{0,0,0,0},
					{I,I,I,I},
					{0,0,0,0},
					}
			,0,3)
		},
		{
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,O,O,0},
					{0,O,O,0},
					}
			, 1,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,O,O,0},
					{0,O,O,0},
					}
			, 1,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,O,O,0},
					{0,O,O,0},
					}
			, 1,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,O,O,0},
					{0,O,O,0},
					}
			, 1,2)
		},
		{
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,T,0},
					{0,T,T,0},
					{0,0,T,0},
					}
			, 1,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,T,0},
					{0,T,T,T},
					{0,0,0,0},
					}
			, 1,3),
			new TetrominoMatrix(new int[][] 
					{

					{0,0,0,0},
					{0,0,T,0},
					{0,0,T,T},
					{0,0,T,0},
					}
			, 2,3),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,T,T,T},
					{0,0,T,0},
					}
			, 1,2)
		},
		{
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,J,0},
					{0,0,J,0},
					{0,J,J,0},
					}
			, 1,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,J,0,0},
					{0,J,J,J},
					{0,0,0,0},
					}
			, 1,3),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,J,J},
					{0,0,J,0},
					{0,0,J,0},
					}
			, 2,3),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,J,J,J},
					{0,0,0,J},
					}
			, 1,3)
		},
		{
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,L,0},
					{0,0,L,0},
					{0,0,L,L},
					}
			, 2,3),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,L,L,L},
					{0,L,0,0},
					}
			, 1,3),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,L,L,0},
					{0,0,L,0},
					{0,0,L,0},
					}
			, 1,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,L},
					{0,L,L,L},
					{0,0,0,0},
					}
			, 1,3)
		},
		{
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,S,0,0},
					{0,S,S,0},
					{0,0,S,0},
					}
			, 1,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,S,S,0},
					{S,S,0,0},
					}
			, 0,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,S,0,0},
					{0,S,S,0},
					{0,0,S,0},
					}
			, 2,3),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,S,S,0},
					{S,S,0,0},
					}
			, 0,2)
		},
		{
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,Z,0},
					{0,Z,Z,0},
					{0,Z,0,0},
					}
			, 1,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,Z,Z,0},
					{0,0,Z,Z}
					}
			, 1,3),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,Z},
					{0,0,Z,Z},
					{0,0,Z,0},
					}
			, 2,3),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{0,Z,Z,0},
					{0,0,Z,Z},
					}
			, 1,3)
		},
		{
			new TetrominoMatrix(new int[][] 
					{
					{0,0,X1,0},
					{0,0,X2,0},
					{0,0,X3,0},
					{0,0,X4,0},
					}
			, 2,2),
			new TetrominoMatrix(new int[][] 
					{

					{0,0,0,0},
					{0,0,0,0},
					{X4,X3,X2,X1},
					{0,0,0,0},
					}
			, 0,3),
			new TetrominoMatrix(new int[][] 
					{

					{0,0,X4,0},
					{0,0,X3,0},
					{0,0,X2,0},
					{0,0,X1,0},
					}
			, 2,2),
			new TetrominoMatrix(new int[][] 
					{
					{0,0,0,0},
					{0,0,0,0},
					{X1,X2,X3,X4},
					{0,0,0,0},
					}
			, 0,3)
		}
	};

	public Tetromino (int type, int initialOrientation) {
		this.type = type;
		this.orientation = initialOrientation;
	}

	private int type;
	private int orientation;
	private int x;
	private int y;	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void rotateLeft() {
		orientation=(orientation+3)%4;
	}

	public void rotateRight() {
		orientation=(orientation+1)%4;
	}

	public TetrominoMatrix getCurrentMatrix() {
		return LAYOUT_MATRICES[type][orientation];
	}

	public TetrominoMatrix getLeftRotatedMatrix() {
		return LAYOUT_MATRICES[type][(orientation+3)%4];
	}

	public TetrominoMatrix getRightRotatedMatrix() {
		return LAYOUT_MATRICES[type][(orientation+1)%4];
	}

	public static ArrayList<Integer> normalTetronimoTypeList() {
		return new ArrayList<Integer>(Arrays.asList(I,O,T,J,L,S,Z));
	}

	public boolean isBonus() {
		return type==X;
	}

	public boolean isLongType() {
		return type==X||type==I;
	}
}
