/**
 * 
 */
package com.xl.spaceship;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.xl.spaceship.controller.UserController;
import com.xl.spaceship.helper.SpaceShipTestHelper;
import com.xl.spaceship.helper.UniqueIdGenerator;
import com.xl.spaceship.service.GameService;
import com.xl.spaceship.service.impl.GameServiceImpl;
import com.xl.spaceship.vo.GameRequestVO;
import com.xl.spaceship.vo.GameVO;
import com.xl.spaceship.vo.SalvoShotsVO;

/**
 * @author Ganapathy_N
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpaceShipApplication.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = UniqueIdGenerator.class, initializers = ConfigFileApplicationContextInitializer.class)
public class UserControllerTest {

	private final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

	private GameService gameService;

	@Autowired
	private UniqueIdGenerator uniqueIdGenerator;

	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	private GameVO game = null;

	private GameRequestVO gameRequestVO = null;

	private MockMvc webReqMockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	private Environment env;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@PostConstruct
	public void setup() {
		MockitoAnnotations.initMocks(this);

		gameService = new GameServiceImpl(uniqueIdGenerator, env, restTemplate);

		RestTemplate restTemplate = SpaceShipTestHelper.getMockRestTemplate();
		mockServer = MockRestServiceServer.createServer(restTemplate);

		UserController userResource = new UserController(gameService);
		ReflectionTestUtils.setField(gameService, "restTemplate", restTemplate);
		this.webReqMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
	}

	@Before
	public void before() {

		gameRequestVO = SpaceShipTestHelper.createGameRequest();
		game = SpaceShipTestHelper.createSpaceShipGame();
		gameRequestVO = SpaceShipTestHelper.createGameRequest();
		gameService.createNewGame(gameRequestVO);
		game = uniqueIdGenerator.getGameIdMap().get("match-" + uniqueIdGenerator.getGameIdMap().size());
	}

	@Test
	public void viewGameStatus() throws Exception {

		String response = webReqMockMvc
				.perform(get("/xl-spaceship/user/game/" + game.getGameId()).contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		log.info("Output Message ---- " + response);
	}

	@Test
	public void shouldNotGetBattleFieldForInvalidGame() throws Exception {
		webReqMockMvc.perform(get("/xl-spaceship/user/game/" + SpaceShipTestHelper.DUMMY_KEY)
				.contentType(SpaceShipTestHelper.APPLICATION_JSON_UTF8)).andExpect(status().isNotFound());
	}

	@Test
	public void shouldFireShots() throws Exception {

		SalvoShotsVO salvo = SpaceShipTestHelper.getMockSalvo(1, 5);

		SpaceShipTestHelper.attackOpponent(mockServer, game.getGameId());

		String content = webReqMockMvc
				.perform(put("/xl-spaceship/user/game/" + game.getGameId() + "/fire").contentType(APPLICATION_JSON_UTF8)
						.content(SpaceShipTestHelper.convertObjectToJsonBytes(salvo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		log.info("Output --", content);
	}

	@Test
	public void challengeNewGame() throws Exception {
		SpaceShipTestHelper.setChallengeEndpoint(mockServer);

		String content = webReqMockMvc
				.perform(post("/xl-spaceship/user/game/new").contentType(APPLICATION_JSON_UTF8)
						.content(SpaceShipTestHelper.convertObjectToJsonBytes(gameRequestVO)))
				.andExpect(status().isSeeOther()).andReturn().getResponse().getContentAsString();
		log.info("Output Message --- ", content.toString());
	}

	@Test
	public void setAutoPlay() throws Exception {

		webReqMockMvc.perform(post("/xl-spaceship/user/game/" + game.getGameId() + "/auto")).andExpect(status().isOk());

		assertThat(game.isAutoPlay()).isTrue();
	}
}
