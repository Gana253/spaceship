/**
 * 
 */
package com.xl.spaceship.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.xl.spaceship.service.GameService;
import com.xl.spaceship.vo.BoardResponseVO;
import com.xl.spaceship.vo.GameRequestVO;
import com.xl.spaceship.vo.GameVO;
import com.xl.spaceship.vo.PlayerResponseVO;
import com.xl.spaceship.vo.SalvoShotsVO;

/**
 * @author Ganapathy_N
 *
 */
@RestController
@RequestMapping("/xl-spaceship/user/game")
public class UserController {
	private final Logger log = LoggerFactory.getLogger(UserController.class);

	private final GameService gameService;

	public UserController(GameService gameService) {

		this.gameService = gameService;
	}

	/**
	 * To view the current status of the game - Use case 2
	 * 
	 * @param gameId
	 * @return
	 */
	@RequestMapping(value = "/{gameId}", method = RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<?> viewGameSatus(@PathVariable String gameId) {
		log.info("To view current games status - game id", gameId);
		GameVO game = gameService.viewGameSatus(gameId);

		if (game == null) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("XL-SpaceShip-error", "Game - " + gameId + " does not exists");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(null);
		}

		return new ResponseEntity<>(constructBoardResponse(game), HttpStatus.OK);
	}

	/**
	 * To convert array to string
	 * 
	 * @param board
	 * @return
	 */
	private String[] convertArrayToString(char[][] board) {
		String[] flattenData = new String[board.length];
		for (int i = 0; i < board.length; i++) {
			StringBuilder boardRowPosition = new StringBuilder();
			for (char boardPosition : board[i]) {
				boardRowPosition.append(boardPosition);
			}
			flattenData[i] = boardRowPosition.toString();
		}
		return flattenData;
	}

	/**
	 * To construct the board response for any status call
	 * 
	 * @param game
	 * @param self
	 * @param opponent
	 * @return
	 */
	private BoardResponseVO constructBoardResponse(GameVO game) {

		PlayerResponseVO self = new PlayerResponseVO();
		self.setUserId(game.getPlayer().getUserId());
		self.setBoard(convertArrayToString(game.getPlayer().getBoardDetails().getBoard()));

		PlayerResponseVO opponent = new PlayerResponseVO();
		opponent.setUserId(game.getOpponent().getUserId());
		opponent.setBoard(convertArrayToString(game.getOpponent().getBoardDetails().getBoard()));

		BoardResponseVO boardResponse = new BoardResponseVO();
		boardResponse.setSelf(self);
		boardResponse.setOpponent(opponent);
		Map<String, String> palyerTurn = new HashMap<String, String>();
		palyerTurn.put("player_turn", game.getNext());
		boardResponse.setGame(palyerTurn);
		return boardResponse;
	}

	/**
	 * To Fire shots on opponent. Use Case 5
	 * 
	 * @param salvo
	 * @param gameId
	 * @return
	 */
	@RequestMapping(value = "/{gameId}/fire", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<?> fireShots(@RequestBody SalvoShotsVO salvo, @PathVariable String gameId) {

		return new ResponseEntity<>(gameService.fireShotsOnOpponent(salvo, gameId), HttpStatus.OK);

	}

	/**
	 * Method to challenge player for another game -Use Case 6
	 * 
	 * @param gameRequest
	 * @return
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public ResponseEntity<String> challengeAnotherPlayer(@RequestBody GameRequestVO gameRequest)
			throws URISyntaxException {
		try {
			GameVO game = gameService.challengePlayer(gameRequest);
			String url = "/xl-spaceship/user/game/" + game.getGameId();
			String msg = "A new game has been created at " + url;
			return ResponseEntity.status(HttpStatus.SEE_OTHER).location(new URI(url)).body(msg);
		} catch (ResourceAccessException e) {
			log.error(" Error occured - the instance unreachable {}", e.getMessage());
			HttpHeaders headers = new HttpHeaders();
			headers.add("XL-SpaceShip-error", "Error occured - the instance is unreachable ");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(null);
		}
	}

	/**
	 * Method to enable auto play - Use Case 7
	 * 
	 * @param gameId
	 * @return
	 */
	@RequestMapping(value = "/{gameId}/auto", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> autoPlay(@PathVariable String gameId) {
		gameService.setAutoPlayTrue(gameId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
