package oauth.client.demo;

import java.util.Map;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = DemoApplication.class)
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

}
