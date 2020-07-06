/**
 * 
 */
package com.xl.spaceship;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import com.xl.spaceship.controller.ProtocolController;
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
public class ProtocolControllerTest {

	private final Logger log = LoggerFactory.getLogger(ProtocolControllerTest.class);
	
	private GameService gameService;

	private GameVO game = null;

	private MockMvc webReqMockMvc;

	private GameRequestVO gameRequestVO;

	private RestTemplate restTemplate;

	@Autowired
	private UniqueIdGenerator uniqueIdGenerator;

	@Autowired
	private Environment env;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@PostConstruct
	public void setup() {
		MockitoAnnotations.initMocks(this);

		gameService = new GameServiceImpl(uniqueIdGenerator, env, restTemplate);

		RestTemplate restTemplate = SpaceShipTestHelper.getMockRestTemplate();

		ProtocolController protocolController = new ProtocolController(gameService, uniqueIdGenerator);

		ReflectionTestUtils.setField(gameService, "restTemplate", restTemplate);
		this.webReqMockMvc = MockMvcBuilders.standaloneSetup(protocolController).build();
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
	public void createNewGameTest() throws Exception {

		String content = webReqMockMvc
				.perform(post("/xl-spaceship/protocol/game/new").contentType(APPLICATION_JSON_UTF8)
						.content(SpaceShipTestHelper.convertObjectToJsonBytes(gameRequestVO)))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		log.info("Output --",content);
	}

	@Test
	public void receiveShots() throws Exception {

		SalvoShotsVO salvo = SpaceShipTestHelper.getMockSalvo(1, 5);

		String content = webReqMockMvc
				.perform(put("/xl-spaceship/protocol/game/" + game.getGameId()).contentType(APPLICATION_JSON_UTF8)
						.content(SpaceShipTestHelper.convertObjectToJsonBytes(salvo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		log.info("Output --",content);
	}

	@Test
	public void dontReceiveShots() throws Exception {

		SalvoShotsVO salvo = SpaceShipTestHelper.getMockSalvo(1, 5);		
		game.setWinner("xebialabs-1");
		String content = webReqMockMvc
				.perform(put("/xl-spaceship/protocol/game/" + game.getGameId()).contentType(APPLICATION_JSON_UTF8)
						.content(SpaceShipTestHelper.convertObjectToJsonBytes(salvo)))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

		log.info("Output --",content);
	}

	@Test
	public void shouldAcceptShotsBasedOnRules() throws Exception {
				game.setRules("9-Shots");
		SalvoShotsVO salvo = SpaceShipTestHelper.getMockSalvo(1, 9);

		webReqMockMvc.perform(put("/xl-spaceship/protocol/game/" + game.getGameId())
				.contentType(SpaceShipTestHelper.APPLICATION_JSON_UTF8)
				.content(SpaceShipTestHelper.convertObjectToJsonBytes(salvo))).andExpect(status().isOk());
	}

	@Test
	public void shouldNotAcceptShotsWithLessShots() throws Exception {
		SalvoShotsVO salvo = SpaceShipTestHelper.getMockSalvo(1, 3);

		String content = webReqMockMvc
				.perform(put("/xl-spaceship/protocol/game/" + game.getGameId())
						.contentType(SpaceShipTestHelper.APPLICATION_JSON_UTF8)
						.content(SpaceShipTestHelper.convertObjectToJsonBytes(salvo)))
				.andExpect(status().isNotAcceptable()).andReturn().getResponse().getContentAsString();
		
		log.info("Output --",content);
	}
}
