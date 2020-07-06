/**
 * 
 */
package com.xl.spaceship.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.xl.spaceship.constants.SpaceShipConstants;
import com.xl.spaceship.constants.Status;
import com.xl.spaceship.vo.BoardDetails;
import com.xl.spaceship.vo.GameRequestVO;
import com.xl.spaceship.vo.GameVO;
import com.xl.spaceship.vo.HitDetails;
import com.xl.spaceship.vo.HitResponeVO;
import com.xl.spaceship.vo.PlayerVO;
import com.xl.spaceship.vo.SalvoShotsVO;
import com.xl.spaceship.vo.SpaceShipVO;

/**
 * @author Ganapathy_N
 *
 */
public class SpaceShipHelperImpl {

	/**
	 * API to get random user
	 * 
	 * @param player
	 * @param opponent
	 * @return
	 */
	public static String toGetRandomUserToStart(PlayerVO player, PlayerVO opponent) {
		// To get the random user to start
		String[] mystring = { player.getUserId(), opponent.getUserId() };

		int idx = new Random().nextInt(mystring.length);
		String starting = (mystring[idx]);
		return starting;
	}

	/**
	 * Create default board for start game - 16x16
	 * 
	 * @return
	 */
	public static char[][] createDefaultBoard() {
		char[][] defaultBoard = new char[16][16];
		for (int i = 0; i < 16; i++) /* Row */ {
			for (int j = 0; j < 16; j++) /* COlumn */ {
				defaultBoard[i][j] = Status.EMPTY;
			}
		}
		return defaultBoard;
	}

	/**
	 * To create the new game on request
	 * 
	 * @param spaceShips
	 * @return
	 */
	public static BoardDetails startGame(SpaceShipVO[] spaceShips) {
		BoardDetails board = new BoardDetails();
		board.setBoard(SpaceShipHelperImpl.createDefaultBoard());
		board.setSpaceShipsAvailable(spaceShips);
		board.setTotalShips(spaceShips.length);
		board.setCurrentShipsAvailable(spaceShips.length);
		for (int i = 0; i < spaceShips.length; i++) {
			SpaceShipVO ship = spaceShips[i];
			stationShip(ship.getShipArray(), board.getBoard(), ship.getRowLen(), ship.getColLen(), ship.getRowStart(),
					ship.getColStart());
		}

		return board;
	}

	public static void stationShip(char[][] ship, char[][] defaultBoard, int requiredRows, int requiredCols,
			int randomRow, int randomCol) {
		int shipRowIterator = 0;
		char[][] tmpBoard = defaultBoard.clone();
		for (int i = randomRow; i < (randomRow + requiredRows); i++) {
			int shipColIterator = 0;
			for (int j = randomCol; j < (randomCol + requiredCols); j++) {
				if (tmpBoard[i][j] == Status.EMPTY) {
					tmpBoard[i][j] = ship[shipRowIterator][shipColIterator];
					shipColIterator++;
				}
			}
			shipRowIterator++;
		}
		defaultBoard = tmpBoard.clone();

	}

	/**
	 * API to create opponent for the new game request
	 * 
	 * @param gameRequestVO
	 * @param spaceShips
	 * @return
	 */
	public static PlayerVO createOpponent(GameRequestVO gameRequestVO, SpaceShipVO[] spaceShips) {
		PlayerVO opponent = new PlayerVO(gameRequestVO.getUserId(), gameRequestVO.getFullName(),
				gameRequestVO.getSpaceShipProtocol());

		BoardDetails boardDetails = new BoardDetails();
		boardDetails.setCurrentShipsAvailable(spaceShips.length);
		boardDetails.setTotalShips(spaceShips.length);
		boardDetails.setBoard(SpaceShipHelperImpl.createDefaultBoard());// Empty Board
		opponent.setBoardDetails(boardDetails);

		return opponent;
	}

	/**
	 * To get the structure of the ship and allocate starting coords
	 * 
	 * @return
	 */
	public static SpaceShipVO[] createShipNAllocateCoords() {

		SpaceShipVO[] spaceShipList = createShipStructure();
		int length = spaceShipList.length;
		for (int i = 0; i < length; i++) {
			SpaceShipVO spaceShip = spaceShipList[i];
			allocateRangeForShips(i, spaceShip);

		}
		return spaceShipList;
	}

	/**
	 * Method to create the ship information
	 *
	 * @return
	 */
	public static SpaceShipVO[] createShipStructure() {
		int length = 5;
		SpaceShipVO[] ships = new SpaceShipVO[length];
		for (int i = 0; i < length; i++) {
			ships[i] = new SpaceShipVO();
			char[][] shipArr = getPredefinedShipPattern(i);
			ships[i].setShipArray(shipArr);
			ships[i].setRowLen(shipArr.length);
			ships[i].setColLen(shipArr[0].length);
		}
		return ships;
	}

	public static char[][] getPredefinedShipPattern(int shipNo) {
		switch (shipNo) {
		case 0:
			return SpaceShipConstants.WINGER;
		case 1:
			return SpaceShipConstants.ANGLE;
		case 2:
			return SpaceShipConstants.A_CLASS;
		case 3:
			return SpaceShipConstants.B_CLASS;
		default:
			return SpaceShipConstants.S_CLASS;
		}
	}

	public static void allocateRangeForShips(int shipNo, SpaceShipVO spaceShip) {
		switch (shipNo) {
		case 0:
			spaceShip.setRowStart(0);
			spaceShip.setColStart(0);
			return;
		case 1:
			spaceShip.setRowStart(0);
			spaceShip.setColStart(8);
			return;
		case 2:
			spaceShip.setRowStart(6);
			spaceShip.setColStart(4);
			return;
		case 3:
			spaceShip.setRowStart(11);
			spaceShip.setColStart(0);
			return;
		default:
			spaceShip.setRowStart(11);
			spaceShip.setColStart(9);
			return;
		}
	}

	/**
	 * convert string to int
	 * 
	 * @param value
	 * @return
	 */
	public static int convertStringToInt(String value) {
		try {
			return Integer.parseInt(value, 16);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Util method to check whether the ship is killed or not
	 * 
	 * @param boardDetails
	 * @param row
	 * @param col
	 * @return
	 */
	public static boolean toCheckShipKilledorNot(BoardDetails boardDetails, int row, int col) {
		SpaceShipVO ship = getShipBasedOnLocation(boardDetails.getSpaceShipsAvailable(), row, col);
		if (ship != null) {
			int rowStart = ship.getRowStart();
			int rowLen = ship.getRowLen();
			for (int i = rowStart; i < (rowStart + rowLen); i++) {
				int colStart = ship.getColStart();
				int colLen = ship.getColLen();
				for (int j = colStart; j < (colStart + colLen); j++) {
					if (boardDetails.getBoard()[i][j] == Status.ALIVE) {
						return false;
					}
				}
			}
		} else {
			return false;
		}
		ship.setKilled(true);
		return true;
	}

	/**
	 * Get ship on the location
	 * 
	 * @param ships
	 * @param row
	 * @param col
	 * @return
	 */
	public static SpaceShipVO getShipBasedOnLocation(SpaceShipVO[] ships, int row, int col) {
		for (SpaceShipVO ship : ships) {
			if (!ship.isKilled()) {
				int rowStart = ship.getRowStart();
				int rowLen = ship.getRowLen();
				if (row >= rowStart && row <= (rowStart + rowLen)) {
					int colStart = ship.getColStart();
					int colLen = ship.getColLen();
					if (col >= colStart && col <= (colStart + colLen)) {
						return ship;
					}
				}
			}
		}
		return null;
	}

	public static String computeSalvoHexString(int arrValue) {
		return String.format("%1X", (arrValue / 16)) + "x" + String.format("%1X", (arrValue % 16));
	}
	
	
	/**
	 * Build the hit response upon receiving the shots from opponent
	 * 
	 * @param hit
	 * @return
	 */
	public static HitResponeVO buildHitResponse(HitDetails hit) {
		HitResponeVO reponse = new HitResponeVO();
		reponse.setSalvo(hit.getSalvo());
		Map<String, String> game = new HashMap<>();
		if (hit.getWinner() != null) {
			game.put("won", hit.getWinner());
		} else {
			game.put("player_turn", hit.getNext());
		}
		reponse.setGame(game);
		return reponse;
	}

	public static HitResponeVO buildResponseWithWinner(SalvoShotsVO salvo, GameVO game) {
		int length = salvo.getSalvo().length;
		Map<String, String> salvoShots = new HashMap<>();
		HitResponeVO hitResponse = new HitResponeVO();
		for (int i = 0; i < length; i++) {
			salvoShots.put(salvo.getSalvo()[i], "MISS");
		}
		hitResponse.setSalvo(salvoShots);
		Map<String, String> gameMap = new HashMap<>();
		gameMap.put("won", game.getWinner());
		hitResponse.setGame(gameMap);
		return hitResponse;
	}

}
