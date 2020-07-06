/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * To Hold Space Ship Information
 * 
 * @author Ganapathy_N
 *
 */
public class SpaceShipVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private int rowStart;

	private int rowLen;

	private int colStart;

	private int colLen;

	private char[][] shipArray;

	private boolean killed;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRowStart() {
		return rowStart;
	}

	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	public int getRowLen() {
		return rowLen;
	}

	public void setRowLen(int rowLen) {
		this.rowLen = rowLen;
	}

	public int getColStart() {
		return colStart;
	}

	public void setColStart(int colStart) {
		this.colStart = colStart;
	}

	public int getColLen() {
		return colLen;
	}

	public void setColLen(int colLen) {
		this.colLen = colLen;
	}

	public char[][] getShipArray() {
		return shipArray;
	}

	public void setShipArray(char[][] shipArray) {
		this.shipArray = shipArray;
	}

	public boolean isKilled() {
		return killed;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
	}

	@Override
	public String toString() {
		return "SpaceShipVO [name=" + name + ", rowStart=" + rowStart + ", rowLen=" + rowLen + ", colStart=" + colStart
				+ ", colLen=" + colLen + ", shipArray=" + Arrays.toString(shipArray) + ", killed=" + killed + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colLen;
		result = prime * result + colStart;
		result = prime * result + (killed ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + rowLen;
		result = prime * result + rowStart;
		result = prime * result + Arrays.deepHashCode(shipArray);
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
		SpaceShipVO other = (SpaceShipVO) obj;
		if (colLen != other.colLen)
			return false;
		if (colStart != other.colStart)
			return false;
		if (killed != other.killed)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rowLen != other.rowLen)
			return false;
		if (rowStart != other.rowStart)
			return false;
		if (!Arrays.deepEquals(shipArray, other.shipArray))
			return false;
		return true;
	}

}
