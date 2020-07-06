/**
 * 
 */
package com.xl.spaceship.service;

import com.xl.spaceship.vo.GameRequestVO;
import com.xl.spaceship.vo.GameVO;
import com.xl.spaceship.vo.HitDetails;
import com.xl.spaceship.vo.HitResponeVO;
import com.xl.spaceship.vo.SalvoShotsVO;

/**
 * @author Ganapathy_N Service class to create a new game
 */
public interface GameService {

	/**
	 * API to create new game.
	 * 
	 * @param gameRequestVO
	 * @return
	 */
	public GameVO createNewGame(GameRequestVO gameRequestVO);

	/**
	 * To retrieve the game status based on the game id(retrieved from Map)
	 * 
	 * @param gameId
	 * @return
	 */
	public GameVO viewGameSatus(String gameId);

	/**
	 * To receive/accept shots fired by the opponent
	 * 
	 * @param salvo
	 * @param gameId
	 * @return
	 */
	public HitDetails acceptShots(SalvoShotsVO salvo, String gameId);

	/**
	 * To fire shots on the opponent
	 * 
	 * @param salvo
	 * @param gameId
	 * @return
	 */
	public HitResponeVO fireShotsOnOpponent(SalvoShotsVO salvo, String gameId);

	/**
	 * API to challenge another player for game
	 * 
	 * @param gameRequestVO
	 * @return
	 */
	public GameVO challengePlayer(GameRequestVO gameRequestVO);

	/**
	 * API to set autoplay true for game
	 * 
	 * @param gameId
	 */
	public void setAutoPlayTrue(String gameId);
	
	/**
	 * Check whether the shot fired is valid
	 * 
	 * @param game
	 * @param salvo
	 * @param isOpponent
	 * @return
	 */
	public boolean isValidSalvo(GameVO game, SalvoShotsVO salvo, boolean isOpponent);
}
