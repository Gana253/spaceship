/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;

/**
 * @author Ganapathy_N
 *
 */
public class BoardDetails implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private char[][] board;

	private SpaceShipVO[] spaceShipsAvailable;

	private int totalShips;

	private int currentShipsAvailable;

	public char[][] getBoard() {
		return board;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

	public SpaceShipVO[] getSpaceShipsAvailable() {
		return spaceShipsAvailable;
	}

	public void setSpaceShipsAvailable(SpaceShipVO[] spaceShipsAvailable) {
		this.spaceShipsAvailable = spaceShipsAvailable;
	}

	public int getTotalShips() {
		return totalShips;
	}

	public void setTotalShips(int totalShips) {
		this.totalShips = totalShips;
	}

	public int getCurrentShipsAvailable() {
		return currentShipsAvailable;
	}

	public void setCurrentShipsAvailable(int currentShipsAvailable) {
		this.currentShipsAvailable = currentShipsAvailable;
	}

}
