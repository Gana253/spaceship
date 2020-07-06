/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;
import java.util.Set;

/**
 * GameVO will hold the game details .
 * 
 * @author Ganapathy_N
 *
 */
public class GameVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String gameId;

	private PlayerVO player;

	private PlayerVO opponent;

	private String next;

	private String winner;
	
	private boolean isAutoPlay;
	
	private Set<Integer> salvoValue;
	
	private String rules;
	
	public Set<Integer> getSalvoValue() {
		return salvoValue;
	}

	public void setSalvoValue(Set<Integer> salvoValue) {
		this.salvoValue = salvoValue;
	}

	public boolean isAutoPlay() {
		return isAutoPlay;
	}

	public void setAutoPlay(boolean isAutoPlay) {
		this.isAutoPlay = isAutoPlay;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public PlayerVO getPlayer() {
		return player;
	}

	public void setPlayer(PlayerVO player) {
		this.player = player;
	}

	public PlayerVO getOpponent() {
		return opponent;
	}

	public void setOpponent(PlayerVO opponent) {
		this.opponent = opponent;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	
}
