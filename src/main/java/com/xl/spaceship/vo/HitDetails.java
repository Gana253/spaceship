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
public class HitDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, String> salvo;

	
	private String winner;

	private String next;

	public Map<String, String> getSalvo() {
		return salvo;
	}

	public void setSalvo(Map<String, String> salvo) {
		this.salvo = salvo;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "HitResponseVO [salvo=" + salvo + ", winner=" + winner + ", next=" + next + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		result = prime * result + ((salvo == null) ? 0 : salvo.hashCode());
		result = prime * result + ((winner == null) ? 0 : winner.hashCode());
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
		HitDetails other = (HitDetails) obj;
		if (next == null) {
			if (other.next != null)
				return false;
		} else if (!next.equals(other.next))
			return false;
		if (salvo == null) {
			if (other.salvo != null)
				return false;
		} else if (!salvo.equals(other.salvo))
			return false;
		if (winner == null) {
			if (other.winner != null)
				return false;
		} else if (!winner.equals(other.winner))
			return false;
		return true;
	}

}
