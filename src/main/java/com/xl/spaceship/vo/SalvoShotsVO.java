/**
 * 
 */
package com.xl.spaceship.vo;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Ganapathy_N
 *
 */
public class SalvoShotsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String[] salvo;

	public String[] getSalvo() {
		return salvo;
	}

	public void setSalvo(String[] salvo) {
		this.salvo = salvo;
	}

	@Override
	public String toString() {
		return "SalvoShotsVO [salvo=" + Arrays.toString(salvo) + "]";
	}

}
