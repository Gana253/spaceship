/**
 * 
 */
package com.xl.spaceship.helper;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.xl.spaceship.vo.GameRequestVO;
import com.xl.spaceship.vo.GameVO;
import com.xl.spaceship.vo.PlayerVO;
import com.xl.spaceship.vo.SalvoShotsVO;
import com.xl.spaceship.vo.SpaceShipProtocol;

/**
 * @author Ganapathy_N
 *
 */
public class SpaceShipTestHelper {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	public static final String DEFAULT_OPPONENT_ID = "xebialabs-1";
	public static final String DEFAULT_OPPONENT_FULL_NAME = "XebiaLabs Opponent";
	public static final String DEFAULT_OPPONENT_HOST = "127.0.0.1";
	public static final String DEFAULT_OPPONENT_PORT = "9002";
	public static final String DEFAULT_GAME_ID = "match-1";

	public static final String DEFAULT_USER_ID = "player-1";
	public static final String DEFAULT_USER_FULL_NAME = "Assessment Player";
	public static final String DEFAULT_USER_HOST = "127.0.0.1";
	public static final String DEFAULT_USER_PORT = "9000";
	public static final String MATCH_1 = "match-1";
	public static final String DUMMY_KEY = "no-match";

	/**
	 * Convert an object to JSON byte array.
	 *
	 * @param object the object to convert
	 * @return the JSON byte array
	 * @throws IOException an IO exception
	 */
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		return mapper.writeValueAsBytes(object);
	}

	/**
	 * Creates Default Game for testing
	 * 
	 * @return the game
	 */
	public static GameVO createSpaceShipGame() {
		GameVO game = new GameVO();

		PlayerVO opponent = new PlayerVO();
		opponent.setUserId(DEFAULT_OPPONENT_ID);
		opponent.setUserName(DEFAULT_OPPONENT_FULL_NAME);
		SpaceShipProtocol opponentProtocol = new SpaceShipProtocol();
		opponentProtocol.setHostname(DEFAULT_OPPONENT_HOST);
		opponentProtocol.setPort(DEFAULT_OPPONENT_PORT);
		opponent.setSsProtocol(opponentProtocol);

		PlayerVO player = new PlayerVO();
		player.setUserId(DEFAULT_USER_ID);
		player.setUserName(DEFAULT_USER_FULL_NAME);
		SpaceShipProtocol playerProtocol = new SpaceShipProtocol();
		playerProtocol.setHostname(DEFAULT_USER_HOST);
		playerProtocol.setPort(DEFAULT_USER_PORT);
		player.setSsProtocol(playerProtocol);

		game.setOpponent(opponent);
		game.setPlayer(player);
		game.setGameId(MATCH_1);
		return game;
	}

	/**
	 * Creates a default game request view model
	 * 
	 * @return the game request view model
	 */
	public static GameRequestVO createGameRequest() {
		GameRequestVO gameVMObj = new GameRequestVO();
		gameVMObj.setUserId(DEFAULT_OPPONENT_ID);
		gameVMObj.setFullName(DEFAULT_OPPONENT_FULL_NAME);
		SpaceShipProtocol ssProtocol = new SpaceShipProtocol();
		ssProtocol.setHostname(DEFAULT_OPPONENT_HOST);
		ssProtocol.setPort(DEFAULT_OPPONENT_PORT);
		gameVMObj.setSpaceShipProtocol(ssProtocol);
		return gameVMObj;
	}

	/**
	 * Creates mock salvo
	 * 
	 * @param rows the number of rows needed
	 * @param cols the number of cols needed
	 * @return create a mock salvo for the same
	 */
	public static SalvoShotsVO getMockSalvo(int rows, int cols) {
		String[] salvoArr = new String[rows * cols];
		SalvoShotsVO salvo = new SalvoShotsVO();
		int index = 0;
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				salvoArr[index] = computeHex(x, y);
				index++;
			}
		}
		salvo.setSalvo(salvoArr);
		return salvo;
	}

	/**
	 * Compute hex from the 2 integers
	 * 
	 * @param x the x value
	 * @param y the y value
	 * @return the hexadecimal String
	 */
	private static String computeHex(Integer x, Integer y) {
		return String.format("%1X", x) + "x" + String.format("%1X", y);
	}

	/**
	 * Mock Rest Template
	 * 
	 * @return the mocked rest template
	 */
	public static RestTemplate getMockRestTemplate() {
		RestTemplate mockRestTemplate = new RestTemplate();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);
		mockRestTemplate.setMessageConverters(Collections.singletonList(converter));

		return mockRestTemplate;
	}

	/**
	 * Attack Opponent - mock
	 * 
	 * @param mockServer
	 * @param id
	 */
	public static void attackOpponent(MockRestServiceServer mockServer, String id) {
		attackOpponent(mockServer, id, DEFAULT_OPPONENT_HOST, DEFAULT_OPPONENT_PORT);
	}

	/**
	 * Ouput Mock for attach opponent
	 * 
	 * @param mockServer
	 * @param id
	 * @param host
	 * @param port
	 */
	private static void attackOpponent(MockRestServiceServer mockServer, String id, String host, String port) {
		mockServer.expect(requestTo("http://" + host + ":" + port + "/xl-spaceship/protocol/game/" + id))
				.andExpect(method(HttpMethod.PUT))
				.andRespond(withSuccess(
						"{\n" + "\"salvo\": {\n" + "\"5x0\": \"hit\",\n" + "\"6x1\": \"hit\",\n"
								+ "\"7x2\": \"kill\",\n" + "\"8x3\": \"miss\",\n" + "\"2x4\": \"miss\"\n" + "},\n"
								+ "\"game\": {\n" + "\"player_turn\": \"xebialabs-1\"\n" + "}\n" + "}\n",
						MediaType.APPLICATION_JSON));
	}
	


	public static void setChallengeEndpoint(MockRestServiceServer mockServer) {

		mockServer.expect(requestTo("http://127.0.0.1:9002/xl-spaceship/protocol/game/new"))
				.andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess("{\n" + "\"user_id\": \"" + DEFAULT_OPPONENT_ID + "\",\n" + "\"full_name\": \""
						+ DEFAULT_OPPONENT_FULL_NAME + "\",\n" + "\"game_id\": \"" + MATCH_1 + "\",\n" + "\"starting\": \""
						+ DEFAULT_OPPONENT_ID + "\"\n" + "}\n", MediaType.APPLICATION_JSON));
	}


}
