package oauth.client.demo;

import java.util.Map;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import oauth.client.demo.config.OauthClientConfig;
import oauth.client.demo.service.OauthConnectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("rawtypes")
@RestController
public class MyRestController {
	@Autowired
	@Qualifier("myRestTemplate")
	private OAuth2RestOperations restTemplate;
	@Autowired
	@Qualifier("myClientOnlyRestTemplate")
	private OAuth2RestOperations clientOnlyrestTemplate;
	@Autowired
	OauthConnectionService oauthConnectionService;

	@Value("${oauth.resource.greeting}")
	private String resourceUrl;

	/**
	 * In this method if token is not obtained exception is ***NOT*** thrown and
	 * access token is obtained by template. It bypasses requirement for
	 * associating request with authenticated user ? This uri does not require
	 * authentication on client side
	 */
	@RequestMapping(value = "/results-asynch")
	@ResponseBody
	public Map resultsAsynch(HttpServletResponse response) throws Exception {

		Future<Map> futureMap = oauthConnectionService.getAsynchronousResults(
				resourceUrl, Map.class, restTemplate);
		while (futureMap.isDone()) {
			Thread.sleep(10);
		}

		return futureMap.get();
	}

	/**
	 * InsufficientAuthenticationException is not thrown since we have supplied
	 * instance of{@link AccessTokenProvider} to the {@link OAuth2RestTemplate}
	 * This uri does not require authentication on client side
	 * 
	 * @see OauthClientConfig#accessTokenProvider()
	 */
	@RequestMapping(value = "/results-nonauthorized")
	@ResponseBody
	public Map nonAuthorizedResultsLoginNotRequired(HttpServletResponse response)
			throws Exception {
		return oauthConnectionService.getResults(resourceUrl, Map.class,
				restTemplate);
	}

	/**
	 * Template used has clientOnly() method returning true so user
	 * authorization is not necessary. Uses
	 * {@link ClientOnlyResourceOwnerPasswordResourceDetails} This uri does not
	 * require authentication on client side
	 */
	@RequestMapping(value = "/results")
	@ResponseBody
	public Map results() throws Exception {
		return oauthConnectionService.getClientOnlyResults(resourceUrl,
				Map.class, clientOnlyrestTemplate);
	}

	/**
	 * User is being redirected to login page for required authorization This
	 * uri REQUIRES authentication on client side
	 */
	@RequestMapping(value = "/authorized-results")
	@ResponseBody
	public Map authorized() throws Exception {
		return oauthConnectionService.getResults(resourceUrl, Map.class,
				restTemplate);
	}

}