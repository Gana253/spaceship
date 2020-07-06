/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Ganapathy_N
 * 
 *         Game Request object to create new game
 *
 */
public class GameRequestVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("full_name")
	private String fullName;
	@JsonProperty("spaceship_protocol")
	private SpaceShipProtocol spaceShipProtocol;
	@JsonProperty("rules")
	private String rules;

	public GameRequestVO(String userId, String fullName, SpaceShipProtocol spaceShipProtocol) {

		this.userId = userId;
		this.fullName = fullName;
		this.spaceShipProtocol = spaceShipProtocol;
	}

	public GameRequestVO() {
		super();
	}

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

	public SpaceShipProtocol getSpaceShipProtocol() {
		return spaceShipProtocol;
	}

	public void setSpaceShipProtocol(SpaceShipProtocol spaceShipProtocol) {
		this.spaceShipProtocol = spaceShipProtocol;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		return "GameRequestVO [userId=" + userId + ", fullName=" + fullName + ", spaceShipProtocol=" + spaceShipProtocol
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((spaceShipProtocol == null) ? 0 : spaceShipProtocol.hashCode());
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
		GameRequestVO other = (GameRequestVO) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (spaceShipProtocol == null) {
			if (other.spaceShipProtocol != null)
				return false;
		} else if (!spaceShipProtocol.equals(other.spaceShipProtocol))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
