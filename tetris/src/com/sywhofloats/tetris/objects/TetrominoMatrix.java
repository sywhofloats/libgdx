package com.sywhofloats.tetris.objects;

public class TetrominoMatrix {

	private int[][] matrix;
	private int minX;
	private int maxX;
	
	public TetrominoMatrix(int[][] matrix,int minX, int maxX) {
		this.matrix = matrix;
		this.minX = minX;
		this.maxX = maxX;
	}
	
	public int[][] getMatrix() {
		return matrix;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}
}
