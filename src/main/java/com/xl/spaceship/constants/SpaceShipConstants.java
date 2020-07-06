/**
 * 
 */
package com.xl.spaceship.constants;

/**
 * @author Ganapathy_N
 *
 */
public class SpaceShipConstants {
	public static char[][] WINGER = new char[][] { { Status.ALIVE, Status.EMPTY, Status.ALIVE },
			{ Status.ALIVE, Status.EMPTY, Status.ALIVE }, { Status.EMPTY, Status.ALIVE, Status.EMPTY },
			{ Status.ALIVE, Status.EMPTY, Status.ALIVE }, { Status.ALIVE, Status.EMPTY, Status.ALIVE } };

	public static char[][] ANGLE = new char[][] { { Status.ALIVE, Status.EMPTY, Status.EMPTY },
			{ Status.ALIVE, Status.EMPTY, Status.EMPTY }, { Status.ALIVE, Status.EMPTY, Status.EMPTY },
			{ Status.ALIVE, Status.ALIVE, Status.ALIVE } };

	public static char[][] A_CLASS = new char[][] { { Status.EMPTY, Status.ALIVE, Status.EMPTY },
			{ Status.ALIVE, Status.EMPTY, Status.ALIVE }, { Status.ALIVE, Status.ALIVE, Status.ALIVE },
			{ Status.ALIVE, Status.EMPTY, Status.ALIVE } };

	public static char[][] B_CLASS = new char[][] { { Status.ALIVE, Status.ALIVE, Status.EMPTY },
			{ Status.ALIVE, Status.EMPTY, Status.ALIVE }, { Status.ALIVE, Status.ALIVE, Status.EMPTY },
			{ Status.ALIVE, Status.EMPTY, Status.ALIVE }, { Status.ALIVE, Status.ALIVE, Status.EMPTY } };

	public static char[][] S_CLASS = new char[][] { { Status.EMPTY, Status.ALIVE, Status.ALIVE, Status.EMPTY },
			{ Status.ALIVE, Status.EMPTY, Status.EMPTY, Status.EMPTY },
			{ Status.EMPTY, Status.ALIVE, Status.ALIVE, Status.EMPTY },
			{ Status.EMPTY, Status.EMPTY, Status.EMPTY, Status.ALIVE },
			{ Status.EMPTY, Status.ALIVE, Status.ALIVE, Status.EMPTY } };

	public static final String STANDARD_RULES = "standard";
	public static final String SUPER_CHARGE = "super-charge";
	public static final String DESPERATION = "desperation";
	public static final String X_SHOT = "-shot";
}
