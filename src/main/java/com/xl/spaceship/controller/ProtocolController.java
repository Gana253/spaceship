/**
 * 
 */
package com.xl.spaceship.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xl.spaceship.helper.SpaceShipHelperImpl;
import com.xl.spaceship.helper.UniqueIdGenerator;
import com.xl.spaceship.service.GameService;
import com.xl.spaceship.vo.GameRequestVO;
import com.xl.spaceship.vo.GameResponseVO;
import com.xl.spaceship.vo.GameVO;
import com.xl.spaceship.vo.HitDetails;
import com.xl.spaceship.vo.HitResponeVO;
import com.xl.spaceship.vo.SalvoShotsVO;

/**
 * @author Ganapathy_N
 *
 *         Controller for xl spaceship communication
 */
@RestController
@RequestMapping("/xl-spaceship/protocol/game")
public class ProtocolController {
	private final Logger log = LoggerFactory.getLogger(ProtocolController.class);

	private final GameService gameService;

	private final UniqueIdGenerator uniqueIdGenerator;

	public ProtocolController(GameService gameService, UniqueIdGenerator uniqueIdGenerator) {
		this.gameService = gameService;
		this.uniqueIdGenerator = uniqueIdGenerator;
	}

	/**
	 * Method to Start the New Game. - Use Case 1
	 * 
	 * @param gameRequest
	 * @return
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	@ResponseBody
	private ResponseEntity<GameResponseVO> createGame(@RequestBody GameRequestVO gameRequest) {
		log.info("Create New Game Endpoint - Request Received");

		// Create New Game
		GameVO game = gameService.createNewGame(gameRequest);

		// Build the response
		GameResponseVO responseVO = reponseBuilderForNewGame(game);

		return new ResponseEntity<>(responseVO, HttpStatus.CREATED);
	}

	/**
	 * TO build the response which needs to be sent
	 * 
	 * @param game
	 * @return
	 */
	private GameResponseVO reponseBuilderForNewGame(GameVO game) {
		GameResponseVO responseVO = new GameResponseVO();
		responseVO.setUserId(game.getPlayer().getUserId());
		responseVO.setFullName(game.getPlayer().getUserName());
		responseVO.setGameId(game.getGameId());
		responseVO.setStarting(game.getNext());
		return responseVO;
	}

	/**
	 * Method to accept the shots from opponent - Use Case 4
	 * 
	 * @param salvo
	 * @param gameId
	 * @return
	 */
	@RequestMapping(value = "/{gameId}", method = RequestMethod.PUT, produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE })
	@ResponseBody
	public ResponseEntity<?> receiveShots(@RequestBody SalvoShotsVO salvo, @PathVariable String gameId) {
		log.info("Request for incoming shots from opponent {}", gameId);
		GameVO game = uniqueIdGenerator.getGameIdMap().get(gameId);

		if (null != game.getWinner()) {
			log.info("Game is won by {} , request cannot be proceed furthur ", game.getWinner());
			return new ResponseEntity<>(SpaceShipHelperImpl.buildResponseWithWinner(salvo, game), HttpStatus.NOT_FOUND);
		}

		if (gameService.isValidSalvo(game, salvo, true)) {
			log.error("The salvo count is mismatching");
			return new ResponseEntity<>(SpaceShipHelperImpl.buildResponseWithWinner(salvo, game),
					HttpStatus.NOT_ACCEPTABLE);
		}

		HitDetails hit = gameService.acceptShots(salvo, gameId);

		HitResponeVO reponse = SpaceShipHelperImpl.buildHitResponse(hit);
		return new ResponseEntity<>(reponse, HttpStatus.OK);
	}

}
