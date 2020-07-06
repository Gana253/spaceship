/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Ganapathy_N
 * 
 *         Response VO - to hold the board details
 *
 */
public class BoardResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private PlayerResponseVO self;

	private PlayerResponseVO opponent;

	private Map<String, String> game;

	public PlayerResponseVO getSelf() {
		return self;
	}

	public void setSelf(PlayerResponseVO self) {
		this.self = self;
	}

	public PlayerResponseVO getOpponent() {
		return opponent;
	}

	public void setOpponent(PlayerResponseVO opponent) {
		this.opponent = opponent;
	}

	public Map<String, String> getGame() {
		return game;
	}

	public void setGame(Map<String, String> game) {
		this.game = game;
	}

	@Override
	public String toString() {
		return "GameStatusResponseVO [self=" + self + ", opponent=" + opponent + ", game=" + game + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result + ((opponent == null) ? 0 : opponent.hashCode());
		result = prime * result + ((self == null) ? 0 : self.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardResponseVO other = (BoardResponseVO) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (opponent == null) {
			if (other.opponent != null)
				return false;
		} else if (!opponent.equals(other.opponent))
			return false;
		if (self == null) {
			if (other.self != null)
				return false;
		} else if (!self.equals(other.self))
			return false;
		return true;
	}

}
