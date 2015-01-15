package oauth.client.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import oauth.client.demo.config.OauthClientConfig;
import oauth.client.demo.config.WebSecurityConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = { DemoApplication.class,
		WebSecurityConfig.class, OauthClientConfig.class })
public class MyRestControllerTest {
	@Autowired
	WebApplicationContext context;
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	@InjectMocks
	MyRestController controller;

	private MockMvc mvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilter(springSecurityFilterChain).build();
	}

	@Test
	public void resultsAsynch() throws Exception {

		mvc.perform(get("/results-asynch").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(
						content()
								.contentTypeCompatibleWith(
										MediaType
												.parseMediaType(MediaType.APPLICATION_JSON_VALUE)));
	}

	@Test
	public void resultsClientOnly() throws Exception {

		mvc.perform(get("/results").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(
						content()
								.contentTypeCompatibleWith(
										MediaType
												.parseMediaType(MediaType.APPLICATION_JSON_VALUE)));
	}

	@Test
	public void resultsRedirectToLogin() throws Exception {
		mvc.perform(get("/authorized-results")).andExpect(
				status().is(HttpStatus.FOUND.value()));

	}

	@Test
	public void resultsNonauthorizedExceptionForNoLoginRequiredLocaly() throws Exception {
		mvc.perform(get("/results-nonauthorized").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(
						content()
								.contentTypeCompatibleWith(
										MediaType
												.parseMediaType(MediaType.APPLICATION_JSON_VALUE)));

	}
}
