/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Ganapathy_N
 *
 */
public class PlayerVO  implements Serializable{
	private static final long serialVersionUID = 1L;

	private String userId;

	private String userName;

	@JsonIgnore
	private BoardDetails boardDetails;

	private SpaceShipProtocol ssProtocol;

	public PlayerVO() {
	}

	public PlayerVO(String userId, String userName, SpaceShipProtocol ssProtocol) {
		this.userId = userId;
		this.userName = userName;
		this.ssProtocol = ssProtocol;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BoardDetails getBoardDetails() {
		return boardDetails;
	}

	public void setBoardDetails(BoardDetails boardDetails) {
		this.boardDetails = boardDetails;
	}

	public SpaceShipProtocol getSsProtocol() {
		return ssProtocol;
	}

	public void setSsProtocol(SpaceShipProtocol ssProtocol) {
		this.ssProtocol = ssProtocol;
	}
}
