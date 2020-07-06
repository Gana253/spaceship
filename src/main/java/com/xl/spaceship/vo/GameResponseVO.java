/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object - used as output for the endpoint
 * 
 * @author Ganapathy_N
 *
 */
public class GameResponseVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("full_name")
	private String fullName;

	@JsonProperty("game_id")
	private String gameId;

	@JsonProperty("starting")
	private String starting;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getStarting() {
		return starting;
	}

	public void setStarting(String starting) {
		this.starting = starting;
	}

	@Override
	public String toString() {
		return "GameResponseVO [userId=" + userId + ", fullName=" + fullName + ", gameId=" + gameId + ", starting="
				+ starting + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result + ((starting == null) ? 0 : starting.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		GameResponseVO other = (GameResponseVO) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (starting == null) {
			if (other.starting != null)
				return false;
		} else if (!starting.equals(other.starting))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
