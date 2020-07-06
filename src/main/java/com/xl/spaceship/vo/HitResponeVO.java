/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Ganapathy_N
 *
 */
public class HitResponeVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, String> salvo;

	private Map<String, String> game;

	public Map<String, String> getSalvo() {
		return salvo;
	}

	public void setSalvo(Map<String, String> salvo) {
		this.salvo = salvo;
	}

	public Map<String, String> getGame() {
		return game;
	}

	public void setGame(Map<String, String> game) {
		this.game = game;
	}

	@Override
	public String toString() {
		return "HitResponeVO [salvo=" + salvo + ", game=" + game + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result + ((salvo == null) ? 0 : salvo.hashCode());
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
		HitResponeVO other = (HitResponeVO) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (salvo == null) {
			if (other.salvo != null)
				return false;
		} else if (!salvo.equals(other.salvo))
			return false;
		return true;
	}
	
	
}
