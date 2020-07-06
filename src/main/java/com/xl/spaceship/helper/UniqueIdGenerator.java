/**
 * 
 */
package com.xl.spaceship.helper;

import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import com.xl.spaceship.vo.GameVO;

/**
 * 
 * Class to generate unique id
 * 
 * @author Ganapathy_N
 *
 */
@Configuration("uniqueIdGenerator")
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class UniqueIdGenerator {

	private static final String MATCH = "match-";

	private static int nextId = 0;
	
	private Map<String, GameVO> gameIdMap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xl.spaceship.helper.UniqueIdGenerator#getNext()
	 */
	public String getNext() {
		synchronized (this) {
			nextId += 1;

		}
		return MATCH + nextId;
	}
	

	public Map<String, GameVO> getGameIdMap() {
		return gameIdMap;
	}

	public void setGameIdMap(Map<String, GameVO> gameIdMap) {
		this.gameIdMap = gameIdMap;
	}


}
