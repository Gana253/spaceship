/**
 * 
 */
package com.xl.spaceship.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.xl.spaceship.constants.SpaceShipConstants;
import com.xl.spaceship.constants.Status;
import com.xl.spaceship.helper.SpaceShipHelperImpl;
import com.xl.spaceship.helper.UniqueIdGenerator;
import com.xl.spaceship.service.GameService;
import com.xl.spaceship.vo.BoardDetails;
import com.xl.spaceship.vo.GameRequestVO;
import com.xl.spaceship.vo.GameResponseVO;
import com.xl.spaceship.vo.GameVO;
import com.xl.spaceship.vo.HitDetails;
import com.xl.spaceship.vo.HitResponeVO;
import com.xl.spaceship.vo.PlayerVO;
import com.xl.spaceship.vo.SalvoShotsVO;
import com.xl.spaceship.vo.SpaceShipProtocol;
import com.xl.spaceship.vo.SpaceShipVO;

/**
 * @author Ganapathy_N
 *
 */
@Service
public class GameServiceImpl implements GameService {
	private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

	private static final String MISS = "MISS";

	private static final String HIT = "HIT";

	private static final String KILL = "KILL";

	private static final String GAME_OVER = "GAME OVER";

	private static final String PLAYER_TURN = "player_turn";

	private UniqueIdGenerator uniqueIdGenerator;

	private final Environment env;

	private final RestTemplate restTemplate;

	public GameServiceImpl(UniqueIdGenerator uniqueIdGenerator, Environment env, RestTemplate restTemplate) {
		this.uniqueIdGenerator = uniqueIdGenerator;
		this.env = env;
		this.restTemplate = restTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xl.spaceship.service.GameService#createNewGame(com.xl.spaceship.vo.
	 * GameRequestVO)
	 */
	@Override
	public GameVO createNewGame(GameRequestVO gameRequestVO) {
		log.info("Create Space Ship", gameRequestVO.toString());
		// Create Ships - Predefined one
		SpaceShipVO[] shipInfoArr = SpaceShipHelperImpl.createShipNAllocateCoords();

		GameVO game = new GameVO();
		// Create Player
		PlayerVO player = createPlayer(shipInfoArr);
		// Create Opponent
		PlayerVO opponent = SpaceShipHelperImpl.createOpponent(gameRequestVO, shipInfoArr);
		log.info("After Creating Player{} opponent{} for the new game", player.getUserId(), opponent.getUserId());
		String starting = SpaceShipHelperImpl.toGetRandomUserToStart(player, opponent);
		// Set data in game object
		game.setPlayer(player);
		game.setOpponent(opponent);
		game.setNext(starting);
		game.setRules(getRules(gameRequestVO.getRules()));
		setGameId(game);

		log.info("Game Created Successfully", game.getGameId());
		return game;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xl.spaceship.service.GameService#viewGameSatus(java.lang.String)
	 */
	public GameVO viewGameSatus(String gameId) {
		log.info("To view the current game status", gameId);
		return uniqueIdGenerator.getGameIdMap().get(gameId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xl.spaceship.service.GameService#acceptShots(com.xl.spaceship.vo.
	 * SalvoShotsVO, java.lang.String)
	 */
	public HitDetails acceptShots(SalvoShotsVO salvo, String gameId) {
		log.info("Player accepting shots", gameId);
		HitDetails hit = new HitDetails();
		GameVO game = uniqueIdGenerator.getGameIdMap().get(gameId);
		BoardDetails board = game.getPlayer().getBoardDetails();
		if (null != board) {
			log.info("To check whether the shots had any impact", salvo);
			hit.setSalvo(shotsFiredOutCome(salvo, board));
			if (game.getPlayer().getBoardDetails().getCurrentShipsAvailable() <= 0) {
				hit.setWinner(game.getOpponent().getUserId());
				game.setWinner(game.getOpponent().getUserId());
				log.info("We have winner for the mathc", game.getWinner());
			} else {
				hit.setNext(determineNextPlayer(game));
				game.setNext(determineNextPlayer(game));
				log.info("Game not over next turn", game.getNext());
			}
			setGameDetails(game);
		}
		return hit;
	}

	/**
	 * Set the game details in gameMap
	 * 
	 * @param game
	 */
	private void setGameDetails(GameVO game) {
		log.info("updating the tracker object with {}", game);
		Map<String, GameVO> gameMap = uniqueIdGenerator.getGameIdMap();
		gameMap.put(game.getGameId(), game);
		uniqueIdGenerator.setGameIdMap(gameMap);
	}

	/**
	 * @param game
	 */
	private String determineNextPlayer(GameVO game) {
		if (game.getNext().equalsIgnoreCase(game.getPlayer().getUserId())) {
			return game.getOpponent().getUserId();
		} else {
			return game.getPlayer().getUserId();
		}
	}

	/**
	 * To Check whether the shots fired killed/missed the spaceship
	 * 
	 * @param salvo
	 * @param boardDetails
	 * @return
	 */
	private Map<String, String> shotsFiredOutCome(SalvoShotsVO salvo, BoardDetails boardDetails) {
		log.info("To Compute shots fired", salvo.toString());
		int length = salvo.getSalvo().length;
		Map<String, String> salvoMap = new LinkedHashMap<>();
		for (int i = 0; i < length; i++) {// Iterate the salvo shots
			String rowColumnStr = salvo.getSalvo()[i];
			String[] rowColumnArr = rowColumnStr.split("x");// To split the shots fored
			String out = getShotStatus(rowColumnArr[0], rowColumnArr[1], boardDetails);
			if (out.equals("game over")) {
				salvoMap.put(rowColumnStr, "kill");
			} else {
				salvoMap.put(rowColumnStr, out);
			}
		}
		return salvoMap;
	}

	/**
	 * Fire shots and mark the status in boar
	 * 
	 * @param row
	 * @param col
	 * @param boardDetails
	 * @return
	 */
	private String getShotStatus(String row, String col, BoardDetails boardDetails) {
		log.info("Status of the salvo shots fired ");
		int rowVal = SpaceShipHelperImpl.convertStringToInt(row);
		int colVal = SpaceShipHelperImpl.convertStringToInt(col);
		String outputShots;
		if (rowVal != -1 && colVal != -1 && rowVal < 16 && colVal < 16) {
			char val = boardDetails.getBoard()[rowVal][colVal];
			if (val == Status.ALIVE) {
				boardDetails.getBoard()[rowVal][colVal] = Status.HIT;
				boolean isKilled = SpaceShipHelperImpl.toCheckShipKilledorNot(boardDetails, rowVal, colVal);
				if (isKilled) {
					boardDetails.setCurrentShipsAvailable(boardDetails.getCurrentShipsAvailable() - 1);
					outputShots = KILL;
					if (boardDetails.getCurrentShipsAvailable() <= 0) {
						outputShots = GAME_OVER;
					}
				} else {
					outputShots = HIT;
				}
			} else {
				if (boardDetails.getBoard()[rowVal][colVal] != Status.HIT) {
					boardDetails.getBoard()[rowVal][colVal] = Status.MISS;
				}
				outputShots = MISS;
			}
		} else {
			outputShots = MISS;
		}

		log.info("output of the shots fired- ", outputShots);
		return outputShots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xl.spaceship.service.GameService#fireShotsOnOpponent(com.xl.spaceship.vo.
	 * SalvoShotsVO, java.lang.String)
	 */
	public HitResponeVO fireShotsOnOpponent(SalvoShotsVO salvo, String gameId) {
		log.info("fire shot to the opponent", gameId);
		GameVO game = uniqueIdGenerator.getGameIdMap().get(gameId);
		HitResponeVO hitResponse = attackOpponentEndpoint(salvo, game);
		if (null != hitResponse) {
			computeSalvoImpact(hitResponse, game.getGameId());
			if (null != hitResponse.getGame().get(PLAYER_TURN)) {
				game.setNext(hitResponse.getGame().get(PLAYER_TURN));
			}
			setGameDetails(game);
		}
		return hitResponse;
	}

	private void computeSalvoImpact(HitResponeVO hitVM, String gameId) {
		log.info("Salvo shots impact on enemy", gameId);
		GameVO game = uniqueIdGenerator.getGameIdMap().get(gameId);
		BoardDetails board = game.getOpponent().getBoardDetails();
		hitVM.getSalvo().forEach((key, value) -> {
			String[] salvoArr = key.split("x");
			String result = hitVM.getSalvo().get(key);
			enemyBoardUpdate(board.getBoard(), salvoArr[0], salvoArr[1], result);
			if (result.equals(KILL)) {
				if (board.getCurrentShipsAvailable() >= 0) {
					board.setCurrentShipsAvailable(board.getCurrentShipsAvailable() - 1);
				}
			}
		});
		if (board.getCurrentShipsAvailable() > 0) {
			game.setNext(determineNextPlayer(game));
		}
		setGameDetails(game);
	}

	private void enemyBoardUpdate(char[][] enemyField, String row, String col, String result) {
		int rowVal = SpaceShipHelperImpl.convertStringToInt(row);
		int colVal = SpaceShipHelperImpl.convertStringToInt(col);
		if (rowVal != -1 && colVal != -1 && rowVal < 16 && colVal < 16) {
			if (result.equals(HIT) || result.equals(KILL)) {
				enemyField[rowVal][colVal] = Status.HIT;
			} else {
				if (enemyField[rowVal][colVal] != Status.HIT) {
					enemyField[rowVal][colVal] = Status.MISS;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.xl.spaceship.service.GameService#challengePlayer(com.xl.spaceship.vo.
	 * GameRequestVO)
	 */
	@Override
	public GameVO challengePlayer(GameRequestVO gameRequestVO) {
		log.info("th new game for challenging player", gameRequestVO.getUserId());
		SpaceShipProtocol spaceShipProtocol = new SpaceShipProtocol();
		createSpaceShipProtocol(spaceShipProtocol);

		PlayerVO self = new PlayerVO("player", "Assessment Player", spaceShipProtocol);

		GameRequestVO requestVO = new GameRequestVO(self.getUserId(), self.getUserName(), self.getSsProtocol());

		GameResponseVO gameResponseVO = challengeEndPoint(requestVO, gameRequestVO.getSpaceShipProtocol());
		requestVO.setUserId(gameResponseVO.getUserId());
		requestVO.setFullName(gameResponseVO.getFullName());

		GameVO game = createNewGame(requestVO);
		game.setNext(gameResponseVO.getStarting());
		game.setGameId(gameResponseVO.getGameId());

		setGameDetails(game);

		return game;
	}

	/**
	 * Create player based on request
	 * 
	 * @param shipInfoArr
	 * @return
	 */
	private PlayerVO createPlayer(SpaceShipVO[] shipInfoArr) {
		log.info("Create player for new game");
		SpaceShipProtocol spaceShipProtocol = new SpaceShipProtocol();
		// To Setup Spaceship protocol
		createSpaceShipProtocol(spaceShipProtocol);
		PlayerVO self = new PlayerVO("player-1", "Assessment Player", spaceShipProtocol);
		BoardDetails boardDetails = SpaceShipHelperImpl.startGame(shipInfoArr);
		self.setBoardDetails(boardDetails);
		return self;
	}

	/**
	 * Setup Space Ship Protocol
	 * 
	 * @param spaceShipProtocol
	 */
	private void createSpaceShipProtocol(SpaceShipProtocol spaceShipProtocol) {
		try {
			spaceShipProtocol.setHostname(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			log.error("Unknown host exception --- ", e.getMessage());
			spaceShipProtocol.setHostname("127.0.0.1");// set local host if any exception occurs
		}
		String port = env.getProperty("server.port");
		spaceShipProtocol.setPort(port);
	}

	private GameResponseVO challengeEndPoint(GameRequestVO gameRequestVO, SpaceShipProtocol spaceShipProtocol) {
		HttpEntity<GameRequestVO> entity = new HttpEntity<>(gameRequestVO);
		String url = "http://" + spaceShipProtocol.getHostname() + ":" + spaceShipProtocol.getPort()
				+ "/xl-spaceship/protocol/game/new";
		log.info("challenge endpoint request", url);
		return restTemplate.exchange(url, HttpMethod.POST, entity, GameResponseVO.class).getBody();
	}

	private HitResponeVO attackOpponentEndpoint(SalvoShotsVO salvo, GameVO game) {
		HttpEntity<SalvoShotsVO> entity = new HttpEntity<>(salvo);
		String url = "http://" + game.getOpponent().getSsProtocol().getHostname() + ":"
				+ game.getOpponent().getSsProtocol().getPort() + "/xl-spaceship/protocol/game/" + game.getGameId();
		log.info("attack opponent endpoint request", url);
		return restTemplate.exchange(url, HttpMethod.PUT, entity, HitResponeVO.class, game.getGameId()).getBody();
	}

	/**
	 * Auto scheduled - Initial Delay will be 2 sec and the api will run every 5 sec
	 */
	@Scheduled(fixedRate = 5000, initialDelay = 2000)
	public void autoPlay() {
		if (null != uniqueIdGenerator.getGameIdMap() && uniqueIdGenerator.getGameIdMap().size() > 0) {

			uniqueIdGenerator.getGameIdMap().entrySet().forEach(entry -> {
				log.info("Key : " + entry.getKey() + " Value : " + entry.getValue().getGameId());
				GameVO game = entry.getValue();
				if (game.isAutoPlay() && null == game.getWinner()
						&& game.getNext().equalsIgnoreCase(game.getPlayer().getUserId())) {
					log.info("AutoPlay mode on for game id -" + game.getGameId());
					SalvoShotsVO salvo = buildSalvoShotsForAutoPlay(game);
					// Call the API to fire shots on the opponent - Use Case 5.
					fireShotsOnOpponent(salvo, game.getGameId());
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xl.spaceship.service.GameService#setAutoPlayTrue(java.lang.String)
	 */
	public void setAutoPlayTrue(String gameId) {
		log.info("set autoplay as true for game id", gameId);
		GameVO game = uniqueIdGenerator.getGameIdMap().get(gameId);
		game.setAutoPlay(true);
		setGameDetails(game);

	}

	/**
	 * build salvo of shots for autoplay
	 * 
	 * @param game
	 * @return
	 */
	private SalvoShotsVO buildSalvoShotsForAutoPlay(GameVO game) {
		log.info("Build salvo of shots for the autoplay enabled games", game.getGameId());
		SalvoShotsVO salvo = new SalvoShotsVO();
		Set<Integer> salvoValue = game.getSalvoValue();
		if (salvoValue == null) {
			salvoValue = new HashSet<>();
		}
		String[] salvoArr = new String[5];
		for (int i = 0; i < 5; i++) {
			int val = (int) Math.floor(Math.random() * (16 * 16));
			if (salvoValue.add(val)) {
				salvoArr[i] = SpaceShipHelperImpl.computeSalvoHexString(val);
			} else {
				if (salvoValue.size() < (16 * 16)) {
					i = i - 1;
				} else {
					salvoArr[i] = SpaceShipHelperImpl
							.computeSalvoHexString((int) Math.floor(Math.random() * (16 * 16)));
				}
			}
		}
		salvo.setSalvo(salvoArr);
		game.setSalvoValue(salvoValue);
		setGameDetails(game);
		return salvo;
	}

	/**
	 * To set the gameId
	 * 
	 * @param game
	 * @return
	 */
	private GameVO setGameId(GameVO game) {
		Map<String, GameVO> gameIdMap = fetchGameDetails();
		if (null == game.getGameId()) {
			String gameId = uniqueIdGenerator.getNext(); // To generate unique match id
			game.setGameId(gameId);
		}
		gameIdMap.put(game.getGameId(), game);
		uniqueIdGenerator.setGameIdMap(gameIdMap);
		return game;

	}

	/**
	 * Fetch game details from the gameidmap
	 * 
	 * @return
	 */
	private Map<String, GameVO> fetchGameDetails() {
		log.info("Getting Game Details");
		Map<String, GameVO> gameIdMap = uniqueIdGenerator.getGameIdMap();
		return null == gameIdMap ? new HashMap<>() : gameIdMap;
	}

	/**
	 * to set rules if present else set it to default
	 * 
	 * @param rules
	 * @return
	 */
	private String getRules(String rules) {
		if (null != rules) {
			if (StringUtils.isEmpty(rules)) {
				return SpaceShipConstants.STANDARD_RULES;
			} else {
				return rules;
			}
		} else {
			return SpaceShipConstants.STANDARD_RULES;
		}
	}

	/**
	 * To check whether the salvo of shots is valid as per the rules
	 */
	public boolean isValidSalvo(GameVO game, SalvoShotsVO salvo, boolean isOpponent) {
		return salvo.getSalvo().length != getSalvoLength(game, isOpponent);
	}

	private int getSalvoLength(GameVO game, boolean isOpponent) {
		if (isOpponent) {
			return getMaxSalvo(game.getOpponent(), game, isOpponent);
		} else {
			return getMaxSalvo(game.getPlayer(), game, isOpponent);
		}
	}

	/**
	 * Get maximum shots applicable based on rules defined
	 * 
	 * @param player
	 * @param game
	 * @param isOpponent
	 * @return
	 */
	private int getMaxSalvo(PlayerVO player, GameVO game, boolean isOpponent) {
		switch (game.getRules()) {
		case SpaceShipConstants.STANDARD_RULES:
			return player.getBoardDetails().getCurrentShipsAvailable();

		case SpaceShipConstants.DESPERATION:
			return (1
					+ (player.getBoardDetails().getTotalShips() - player.getBoardDetails().getCurrentShipsAvailable()));

		default:
			return xShot(game.getRules());
		}
	}

	/**
	 * 
	 * Xshot value retrieve
	 * 
	 * @param rules
	 * @return
	 */
	private int xShot(String rules) {
		int salvo = 0;
		try {
			salvo = Integer.parseInt(rules.split("-")[0]);
		} catch (NumberFormatException e) {
			log.error("Cannot parse the given rules {} due to {}", rules, e);
			return salvo;
		}
		return salvo;
	}

}
